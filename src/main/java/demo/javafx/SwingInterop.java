package demo.javafx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class SwingInterop extends JApplet {

	private static final int PANEL_WIDTH_INT = 600;
	private static final int PANEL_HEIGHT_INT = 400;
	private static final int TABLE_PANEL_HEIGHT_INT = 100;
	private JFXPanel chartFxPanel;
	private SampleTableModel tableModel;

	@Override
	public void init() {
		tableModel = new SampleTableModel();

		// create javafx panel for charts
		chartFxPanel = new JFXPanel();
		chartFxPanel.setPreferredSize(new Dimension(PANEL_WIDTH_INT, PANEL_HEIGHT_INT));


		//JTable
		JTable table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		table.setGridColor(Color.DARK_GRAY);
		SwingInterop.DecimalFormatRenderer renderer = new SwingInterop.DecimalFormatRenderer();
		renderer.setHorizontalAlignment(JLabel.RIGHT);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		JScrollPane tablePanel = new JScrollPane(table);
		tablePanel.setPreferredSize(new Dimension(PANEL_WIDTH_INT, TABLE_PANEL_HEIGHT_INT));

		JPanel chartTablePanel = new JPanel();
		chartTablePanel.setLayout(new BorderLayout());

		//Create split pane that holds both the bar chart and table
		JSplitPane jsplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jsplitPane.setTopComponent(chartTablePanel);
		jsplitPane.setBottomComponent(tablePanel);
		jsplitPane.setDividerLocation(410);
		chartTablePanel.add(chartFxPanel, BorderLayout.CENTER);

		//Add the split pane to the content pane of the application
		add(jsplitPane, BorderLayout.CENTER);

		Platform.runLater( this::createScene );
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater( () -> {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) { e.printStackTrace(); }

			JFrame frame = new JFrame("Swing JTable");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			JApplet applet = new SwingInterop();
			applet.init();

			frame.setContentPane(applet.getContentPane());

			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

			applet.start();
		} );
	}

	private void createScene() {
		BarChart chart = createBarChart();
		chartFxPanel.setScene(new Scene(chart));
	}

	@SuppressWarnings("unchecked")
	private BarChart createBarChart() {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories( FXCollections.<String>observableArrayList( tableModel.
				getColumnNames() ));
		xAxis.setLabel("Year");
		double tickUnit = tableModel.getTickUnit();

		NumberAxis yAxis = new NumberAxis();
		yAxis.setTickUnit(tickUnit);
		yAxis.setLabel("Units Sold");

		final BarChart chart = new BarChart(xAxis, yAxis, tableModel.getBarChartData());
		tableModel.addTableModelListener( e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				final int row = e.getFirstRow();
				final int column = e.getColumn();
				final Object value =
						((SampleTableModel) e.getSource()).getValueAt(row, column);

				Platform.runLater( () -> {
					XYChart.Series< String, Number > s =
							( XYChart.Series< String, Number > ) chart.getData().get( row );
					BarChart.Data data = s.getData().get( column );
					data.setYValue( value );
				} );
			}
		} );
		return chart;
	}

	public static class DecimalFormatRenderer extends DefaultTableCellRenderer {
		private static final DecimalFormat formatter = new DecimalFormat("#.0");

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			value = formatter.format(value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
}
