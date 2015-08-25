package window.util;

import java.awt.Color;

/**
 * WavelengthColors provides Wavelength to Color map
 */
public class WavelengthColors
{
	public static Color getWavelengthColor( String wavelength )
	{
		return Color.decode( Resources.getString( wavelength ) );
	}
}
