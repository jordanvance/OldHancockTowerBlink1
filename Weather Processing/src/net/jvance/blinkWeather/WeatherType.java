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
}
