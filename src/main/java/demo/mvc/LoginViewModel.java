package demo.mvc;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel
{

	private final StringProperty userName = new SimpleStringProperty();
	private final StringProperty password = new SimpleStringProperty();
	private final ReadOnlyBooleanWrapper loginPossible = new ReadOnlyBooleanWrapper();
	private final DoubleProperty sliderValue = new SimpleDoubleProperty();

	public LoginViewModel()
	{

		//Logic to check, whether the login is possible or not
		loginPossible.bind( userName.isNotEmpty().and( password.isNotEmpty() ) );

	}

	public StringProperty userNameProperty()
	{
		return userName;
	}

	public StringProperty passwordProperty()
	{
		return password;
	}

	public ReadOnlyBooleanProperty isLoginPossibleProperty()
	{
		return loginPossible.getReadOnlyProperty();
	}

	public DoubleProperty sliderValueProperty() { return sliderValue; }
}