package sample.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * SampleTableModel
 */
public class SampleTableModel extends AbstractTableModel {

	private final String[] names = {"2007", "2008", "2009"};

	private Object[][] data = {
			{ 567d, 956d, 1154d },
			{ 1292d, 1665d, 1927d },
			{ 1292d, 2559d, 2774d }
	};

	public double getTickUnit() {
		return 1000;
	}
	public List getColumnNames() {
		return Arrays.asList(names);
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return names.length;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data[row][column];
	}

	@Override
	public String getColumnName(int column) {
		return names[column];
	}

	@Override
	public Class getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		if (value instanceof Double) {
			data[row][column] = value;
		}

		fireTableCellUpdated(row, column);
	}

	private ObservableList<BarChart.Series> bcData;

	@SuppressWarnings("unchecked")
	public ObservableList<BarChart.Series> getBarChartData() {
		if (bcData == null) {
			bcData = FXCollections.<BarChart.Series>observableArrayList();
			for (int row = 0; row < getRowCount(); row++) {
				ObservableList<BarChart.Data> series =
						FXCollections.<BarChart.Data>observableArrayList();
				for (int column = 0; column < getColumnCount(); column++) {
					series.add(new BarChart.Data(getColumnName(column),
							getValueAt(row, column)));
				}
				bcData.add(new BarChart.Series(series));
			}
		}
		return bcData;
	}
}
