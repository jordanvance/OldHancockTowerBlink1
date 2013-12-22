package net.jvance.blinkWeather;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import dme.forecastiolib.ForecastIO;
import processing.core.*;
import thingm.blink1.*;

public class BlinkWeather extends PApplet {
	private Blink1 blink1;
	
	public static void main(String args[]) {
		PApplet.main("net.jvance.blinkWeather.BlinkWeather", new String[] {"--present", "Old Hancock"});
		PApplet.useQuartz = true;
	}
	
	public void setup() {
		blink1 = new Blink1();
		int rc = blink1.open();
		if(rc != 0) {
			return;
		}
		ForecastIO fio = new ForecastIO("8a378214366053ca60d93c5c09c3d773");
		fio.setUnits(ForecastIO.UNITS_US);
		fio.getForecast("42.3581", "71.0636");
		size(200, 200);
		background(128, 128, 128);
	}
	
	public void draw() {
		try {
			Stopwatch watch = Stopwatch.createStarted();
			while(watch.elapsed(TimeUnit.SECONDS) < 20) {
				cloudy();
			}
			watch.reset();
			while(watch.elapsed(TimeUnit.SECONDS) < 20) {
				snow();
			}
		} catch (InterruptedException e) {
			blink1.setRGB(Color.BLACK);
		}
	}
	
	private void changeColor(Color color) {
		blink1.setRGB(color);
		setBackground(color);
	}
	
	private void clearSkies() {
		changeColor(Color.BLUE);
	}
	
	private void cloudy() throws InterruptedException {
		blinkingColors(Color.BLUE, Color.BLACK);
	}
	
	private void rain() throws InterruptedException {
		changeColor(Color.RED);
	}
	
	private void snow() throws InterruptedException {
		blinkingColors(Color.RED, Color.BLACK);
	}
	
	private void redSoxWinTheWorldSeries() throws InterruptedException {
		blinkingColors(Color.RED, Color.BLUE);
	}
	
	private void blinkingColors(Color color1, Color color2) throws InterruptedException {
		changeColor(color1);
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		changeColor(color2);
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));	
	}
	
}
