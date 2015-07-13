package demo.binding;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by moon on 6/24/15.
 */
public class BidirectionalBinding
{
	public static void main(String[] args) {
		StringProperty prop1 = new SimpleStringProperty("");
		StringProperty prop2 = new SimpleStringProperty("");

		prop2.bindBidirectional( prop1 );

		System.out.println("prop1.isBound() = " + prop1.isBound());
		System.out.println("prop2.isBound() = " + prop2.isBound());

		prop1.set("asdf");
		System.out.println(prop2.get());

		prop2.set("twotwo" );
		System.out.println(prop1.get());
	}
}
