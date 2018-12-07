package halcyon.view;

import halcyon.view.console.TextAppender;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

/**
 * ConsolePane consists of ConsolePanel and a button for clearing the contents
 * of console
 */
public class ConsolePane extends BorderPane implements TextAppender
{
	private static final long cCheckPeriod = 1_000_000_000;
	private volatile long mLastCheck = System.nanoTime();

	// It remains more than 1024 lines at some point
	private static final int maxItems = 4096;
	private final ConcurrentLinkedQueue< String > queue = new ConcurrentLinkedQueue<>();
	private final List< ConsoleOutputEntry > entries = new ArrayList< ConsoleOutputEntry >( 1024 );

	private WebEngine engine;

	public ConsolePane()
	{
		WebView web = new WebView();
		engine = web.getEngine();
		engine.setUserStyleSheetLocation( getClass().getResource("ConsoleOutput_WebView.css").toString() );
		engine.getLoadWorker().stateProperty().addListener( new ChangeListener< Worker.State >()
		{
			@Override public void changed( ObservableValue< ? extends Worker.State > observable, Worker.State oldValue, Worker.State newValue )
			{
				if( newValue == Worker.State.SUCCEEDED )
				{
					Task<Void> task = new Task<Void>() {
						@Override protected Void call() throws Exception {
							while ( !isCancelled() )
							{
								boolean shouldExecuteScript = !queue.isEmpty();

								while ( !queue.isEmpty() )
								{
									String str = queue.poll();

									Platform.runLater( () -> {
										controlSize();

										if ( str.startsWith( "[INFO]" ) )
											addTextEntry( str.substring( 6 ) );
										else if ( str.startsWith( "[WARN]" ) )
											addErrorEntry( str.substring( 6 ) );
										else
											addTextEntry( str );
									} );
								}

								if ( shouldExecuteScript )
								{
									Platform.runLater( () -> {
										engine.executeScript( "window.scrollTo(0, document.body.scrollHeight);" );
									} );
								}

								Thread.sleep( 500 );
							}
							return null;
						}
					};

					Thread th = new Thread(task);
					th.setDaemon(true);
					th.start();
				}
			}
		} );
		engine.loadContent( "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"></head><body><pre></pre></body></html>" );

		web.prefWidthProperty().bind(super.widthProperty());
		web.maxHeightProperty().bind(super.heightProperty());
		web.setFocusTraversable( false );

		Button lClearButton = new Button( "Clear" );
		lClearButton.setOnAction( event -> {
			this.clear();
		} );

		setTop( lClearButton );
		setCenter( web );
	}

	private ConsoleOutputEntry createEntry() {
		ConsoleOutputEntry entry = new ConsoleOutputEntry();
		entries.add(entry);
		return entry;
	}

	private ConsoleOutputEntry createTextEntry(String text, String styleClass, BiConsumer<Element, Text> nodeModifier, Integer colorHex) {
		ConsoleOutputEntry entry = createEntry();

		Document doc = engine.getDocument();
		Node body = doc.getElementsByTagName("pre").item(0);
		Element entryNode = doc.createElement("div");

		Text textNode = doc.createTextNode(text);

		entryNode.appendChild(textNode);
		body.appendChild(entryNode);

		entry.entryNode = entryNode;
		entry.textNode = textNode;

		String className = "entry-text";
		if (styleClass != null) {
			className += ' ' + styleClass;
		}
		entryNode.setAttribute("class", className);

		if (colorHex != null)
		{
			entryNode.setAttribute("style", "color: #" + Integer.toHexString(colorHex) );
		}

		if (nodeModifier != null) {
			nodeModifier.accept(entryNode, textNode);
		}

		return entry;
	}

	private ConsoleOutputEntry createTextEntry(String text, String styleClass) {
		return createTextEntry(text, styleClass, null, null);
	}


	public ConsoleOutputEntry addTextEntry(String text, int colorHex) {
		return createTextEntry(text, null, null, colorHex);
	}

	public ConsoleOutputEntry addTextEntry(String text) {
		return createTextEntry(text, null);
	}

	public ConsoleOutputEntry addErrorEntry(String text) {
		return createTextEntry(text, "entry-error");
	}

	public ConsoleOutputEntry addInputEntry(String text) {
		return createTextEntry("> " + text, "entry-input");
	}

	public void clear()
	{
		List<ConsoleOutputEntry> toRemoved = new ArrayList<>( entries.subList( 0, entries.size() ) );
		entries.removeAll( toRemoved );

		runDeleteTask( toRemoved );
	}

	private void runDeleteTask( Collection<ConsoleOutputEntry> toRemoved )
	{
		Document doc = engine.getDocument();
		Node body = doc.getElementsByTagName( "pre" ).item( 0 );

		for ( ConsoleOutputEntry entry: toRemoved )
		{
			body.removeChild( entry.entryNode );
			entry.dispose();
		}
	}

	public void appendText( final String str )
	{
		queue.add( str );
	}

	private void controlSize()
	{
		final long lTimeNow = System.nanoTime();

		if (lTimeNow > mLastCheck + cCheckPeriod)
		{
			if( entries.size() > maxItems )
			{
				List<ConsoleOutputEntry> toRemoved = new ArrayList<>( entries.subList( 0, entries.size() - maxItems ) );
				entries.removeAll( toRemoved );

				runDeleteTask( toRemoved );
			}

			mLastCheck = lTimeNow;
		}
	}


	private class ConsoleOutputEntry
	{
		public Element entryNode;
		public Text textNode;

		public void dispose()
		{
			entryNode = null;
			textNode = null;
		}
	}
}
