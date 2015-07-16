package window.demo;

import org.junit.Test;
import window.StdOutputCaptureConsole;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.EventQueue;

/**
 * StdOutputCaptureConsole Demo
 */
public class StdOutputCaptureConsoleDemo
{
	@Test
	public void ConsoleWindowTest()
	{
		EventQueue.invokeLater( () -> {
			try
			{
				UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			} catch (ClassNotFoundException ex)
			{
			} catch (InstantiationException ex)
			{
			} catch (IllegalAccessException ex)
			{
			} catch (UnsupportedLookAndFeelException ex)
			{
			}
			StdOutputCaptureConsole consoleWindow = new StdOutputCaptureConsole();

			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			frame.setLayout( new BorderLayout() );
			frame.add( consoleWindow.getJPanel() );
			frame.setSize( 200, 200 );
			frame.setLocationRelativeTo( null );
			frame.setVisible( true );

			System.out.println( "Hi, this is a console test" );
			System.out.println( "Test 1" );
			System.err.println( "Error Test 1" );
		} );
	}

	public static void main(String[] args) {
		new StdOutputCaptureConsoleDemo().ConsoleWindowTest();
	}
}
