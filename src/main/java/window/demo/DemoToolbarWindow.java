package window.demo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import model.node.HalcyonNode;
import model.list.HalcyonNodeRepository;
import model.node.HalcyonNodeType;
import view.ViewManager;
import window.control.ControlWindowBase;
import window.toolbar.ToolbarInterface;

/**
 * Control type Toolbar window
 */
public class DemoToolbarWindow extends ControlWindowBase implements
		ToolbarInterface
{
	final private HalcyonNodeRepository nodes;

	public DemoToolbarWindow( final ViewManager manager )
	{
		super( "ToolbarDockable" );

		nodes = manager.getNodes();

		setTitleText( "Demo Toolbar" );
		setCloseable( false );
		setMinimizable( false );
		setMaximizable( false );

		final JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.PAGE_AXIS ) );

		JButton btn = new JButton( "Add Camera-1" );
		btn.addActionListener( e -> {
			final HalcyonNode n = new HalcyonNode( "Camera-1", HalcyonNodeType.Camera );
			final JPanel cameraPanel = new JPanel( new FlowLayout() );

			cameraPanel.add( new JButton( "Test Button 1" ) );
			cameraPanel.add( new JButton( "Test Button 2" ) );
			cameraPanel.add( new JButton( "Test Button 3" ) );
			cameraPanel.add( new JButton( "Test Button 4" ) );

			n.setPanel( cameraPanel );

			nodes.add( n );
		} );

		panel.add( btn );

		btn = new JButton( "Add Laser-1" );
		btn.addActionListener( e -> {
			final HalcyonNode n = new HalcyonNode( "Laser-1", HalcyonNodeType.Laser );
			final JPanel laserPanel = new JPanel( new FlowLayout() );

			laserPanel.add( new JLabel( "Label1" ) );
			laserPanel.add( new JTextField( "TextField1" ) );
			laserPanel.add( new JLabel( "Label2" ) );
			laserPanel.add( new JTextField( "TextField2" ) );

			n.setPanel( laserPanel );

			nodes.add( n );
		} );

		panel.add( btn );

		btn = new JButton( "Add Laser-2" );
		btn.addActionListener( e -> {
			final HalcyonNode n = HalcyonNode.wrap( "Laser-2", HalcyonNodeType.Laser, new JPanel() );
			nodes.add( n );
		} );

		panel.add( btn );

		btn = new JButton( "Test Std Out" );
		btn.addActionListener( e -> {

			for(int i = 0; i < 2000; i++)
			{
				System.out.println("" + i + " " + "Console Test");
			}

		} );

		panel.add( btn );

		btn = new JButton( "Test Std Err" );
		btn.addActionListener( e -> {

			for(int i = 0; i < 2000; i++)
			{
				System.err.println("" + i + " " + "Console Test");
			}

		} );

		panel.add( btn );

		setLayout( new BorderLayout() );
		add( new JScrollPane( panel ), BorderLayout.CENTER );
	}
}
