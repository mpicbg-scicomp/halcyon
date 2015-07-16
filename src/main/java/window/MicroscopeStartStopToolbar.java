package window;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

/**
 * Microscope Start/Stop toolbar
 */
public class MicroscopeStartStopToolbar extends ToolbarBase
{
	public MicroscopeStartStopToolbar()
	{
		super( "StartStopToolbar" );
		setTitleText( "Start/Stop" );

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.PAGE_AXIS ) );

		JButton btn = new JButton( "Start" );

		panel.add( btn );

		btn = new JButton( "Stop" );

		panel.add( btn );

		setLayout( new BorderLayout() );
		add( new JScrollPane( panel ), BorderLayout.CENTER );
	}
}
