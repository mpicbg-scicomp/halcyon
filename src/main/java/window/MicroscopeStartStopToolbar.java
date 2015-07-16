package window;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Microscope Start/Stop toolbar
 */
public class MicroscopeStartStopToolbar extends ToolbarBase
{
	public MicroscopeStartStopToolbar()
	{
		super("StartStopToolbar");
		setTitleText("Start/Stop");

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JButton btn = new JButton("Start");
		btn.addActionListener((e) -> {
			System.out.println("START");
		});

		panel.add(btn);

		btn = new JButton("Stop");
		btn.addActionListener((e) -> {
			System.out.println("START");
		});

		panel.add(btn);

		setLayout(new BorderLayout());
		add(new JScrollPane(panel), BorderLayout.CENTER);
	}
}
