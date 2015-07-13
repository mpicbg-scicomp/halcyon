package demo.mvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class LoginView
{
	@FXML
	private TextField userNameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private Button loginButton;
	@FXML
	private Slider slider;
	@FXML
	private TextField sliderValueTextField;

	@FXML void initialize()
	{

		//Create the ViewModel - In production this should be done by dependency injection
		LoginViewModel loginViewModel = new LoginViewModel();

		//Connect the ViewModel
		userNameTextField.textProperty().bindBidirectional( loginViewModel.userNameProperty() );
		passwordTextField.textProperty().bindBidirectional( loginViewModel.passwordProperty() );
		slider.valueProperty().bindBidirectional( loginViewModel.sliderValueProperty() );
		//sliderValueTextField.textProperty().bind( loginViewModel.sliderValueProperty().asString() );
		sliderValueTextField.textProperty().bindBidirectional( loginViewModel.sliderValueProperty(), new StringConverter<Number>()
		{
			@Override
			public String toString(Number t)
			{
				return t.toString();
			}

			@Override
			public Number fromString(String string)
			{
				if(string.isEmpty() || !isNumeric( string ))
					return 0d;
				return Double.parseDouble(string);
			}

		} );

		loginButton.disableProperty().bind( loginViewModel.isLoginPossibleProperty().not() );
	}

	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
}