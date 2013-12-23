package net.jvance.blinkWeather;

import java.awt.Color;

public enum WeatherType {
	RAIN(Color.RED, Color.RED),
	SNOW(Color.RED, Color.BLACK),
	CLOUDY(Color.BLUE, Color.BLACK),
	CLEAR(Color.BLUE, Color.BLUE),
	RED_SOX_WIN_WS(Color.BLUE, Color.RED),
	RED_SOX_CANCELLED(Color.RED, Color.BLACK);
	
	private Color color1;
	private Color color2;

	private WeatherType(Color color1, Color color2) {
		this.color1 = color1;
		this.color2 = color2;
	}
	
	public boolean blinks() {
		return color1 != color2;
	}
	
	public Color getColor1() {
		return color1;
	}
	
	public Color getColor2() {
		return color2;
	}
	
	public String whatsTheForecast() {
		switch(this) {
			case RAIN:
				return "Get your umbrella!";
			case SNOW:
				return "It's going to snow!";
			case CLOUDY:
				return "It's at least 50% cloudy.";
			case CLEAR:
				return "Clear skies ahead.";
			case RED_SOX_CANCELLED:
				return "The Red Sox game has been cancelled.";
			case RED_SOX_WIN_WS:
				return "The Red Sox WIN THE WORLD SERIES!";
			default:
				return "I have no idea. Are the Red Sox even playing?";
		}
	}
}
