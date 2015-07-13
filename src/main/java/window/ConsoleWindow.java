package window;

import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

/**
 * Control type Console Window
 */
public class ConsoleWindow extends ControlType
{
	public ConsoleWindow()
	{
		super("ConsoleDockable");

		setTitleText( "Console" );
		setCloseable( true );
		setMinimizable( true );
		setMaximizable( true );

		setLayout( new BorderLayout() );

		JList<String> list = new JList<>(  );
		list.setListData( new String[] { "Console output", "All the logs are redirected here." } );
		add( new JScrollPane( list ), BorderLayout.CENTER );
	}
}
