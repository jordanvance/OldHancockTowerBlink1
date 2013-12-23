package net.jvance.blinkWeather;

import net.jvance.blinkWeather.WeatherType;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import dme.forecastiolib.FIODataPoint;
import dme.forecastiolib.FIOHourly;
import dme.forecastiolib.ForecastIO;
import processing.core.*;
import thingm.blink1.*;

public class BlinkWeather extends PApplet {
	private static final long serialVersionUID = 1L;
	
	private Blink1 blink1;
	private ForecastIO fio;
	private WeatherType weather = WeatherType.CLEAR;
	private static final long usualSleep = 30;
	private Stopwatch watch;
	
	
	//Leaving Processing in here to add a GUI display of the old hancock tower at some point
	public static void main(String args[]) {
		PApplet.main("net.jvance.blinkWeather.BlinkWeather");
	}
	
	public void setup() {
		blink1 = new Blink1();
		int rc = blink1.open();
		if(rc != 0) {
			return;
		}
		fio = new ForecastIO("8a378214366053ca60d93c5c09c3d773");
		fio.setUnits(ForecastIO.UNITS_US);
		size(200, 200);
		background(0,255,0);
		setBackground(weather.getColor1());
	}
	
	public void draw() {
		try {
			watch = Stopwatch.createUnstarted();
			while(true) {
				System.out.println("Getting forecast...");
				fio.getForecast("42.3581", "71.0636");
				FIOHourly hourly = new FIOHourly(fio);
				if(hourly.hours() >= 0) {
					FIODataPoint fdp = hourly.getHour(1);
					if(fdp == null) {
						//Just continue with the current weather.
					} else if(fdp.precipIntensity() > 0 && fdp.precipProbability() > .15) {
						//Rain or hail == rain, snow or sleet == snow
						if(fdp.precipType().equals("rain") || fdp.precipType().equals("hail")) {
							this.weather = WeatherType.RAIN;
						} else {
							this.weather = WeatherType.SNOW;
						}
					} else {
						//If it's mostly cloudy... it's cloudy.
						if(fdp.cloudCover() > .5) {
							this.weather = WeatherType.CLOUDY;
						} else {
							this.weather = WeatherType.CLEAR;
						}
					}
					System.out.println(weather.whatsTheForecast());
					watch.reset();
					watch.start();
					setColors();				
				}
			}
			
		} catch (InterruptedException e) {
			blink1.setRGB(Color.BLACK);
		}
	}
	
	private void changeColor(Color color) {
		blink1.fadeToRGB(500, color);
		setBackground(color);
	}
	
	private void setColors() throws InterruptedException {
		changeColor(weather.getColor1());
		while(watch.elapsed(TimeUnit.MINUTES) < usualSleep) {
			long seconds = (30*60-watch.elapsed(TimeUnit.SECONDS))%60;
			long minutes = (30*60-watch.elapsed(TimeUnit.SECONDS))/60;
			String time = String.format("%d:%02d", minutes, seconds);
			System.out.println("Current weather (" + weather.whatsTheForecast() +") will be updated in " + time);			
			if(weather.blinks()) {			
				changeColor(weather.getColor1());
				Thread.sleep(TimeUnit.SECONDS.toMillis(2));
				changeColor(weather.getColor2());
				Thread.sleep(TimeUnit.SECONDS.toMillis(2));	
			}  else {
				Thread.sleep(TimeUnit.MINUTES.toMillis(5));	
			}
		}
	}
	
}
