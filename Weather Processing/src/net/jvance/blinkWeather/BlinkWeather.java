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
	private static final long noAnswerSleepDefault = 20;
	private Stopwatch watch;
	
	
	//Leaving Processing in here to add a GUI display of the old hancock tower at some point
	public static void main(String args[]) {
		PApplet.main("net.jvance.blinkWeather.BlinkWeather", new String[] {"--present"});
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
		background(0,0,0);
		setBackground(weather.getColor1());
	}
	
	public void draw() {
		try {
			watch = Stopwatch.createStarted();
			long noAnswerSleepTime = noAnswerSleepDefault;
			while(true) {
				System.out.println("Getting forecast...");
				fio.getForecast("42.3581", "71.0636");
				FIOHourly hourly = new FIOHourly(fio);
				if(hourly.hours() < 0) {
					Thread.sleep(TimeUnit.MINUTES.toMillis(noAnswerSleepTime));
					noAnswerSleepTime += 5;
					continue;
				} else {
					FIODataPoint fdp = hourly.getHour(1);
					if(fdp == null) {
						Thread.sleep(TimeUnit.MINUTES.toMillis(noAnswerSleepTime));
						continue;
					}
					if(fdp.precipIntensity() > 0 && fdp.precipProbability() > .15) {
						if(fdp.precipType().equals("rain") || fdp.precipType().equals("hail")) {
							this.weather = WeatherType.RAIN;
						} else {
							this.weather = WeatherType.SNOW;
						}
					} else {
						if(fdp.cloudCover() > .5) {
							this.weather = WeatherType.CLOUDY;
						} else {
							this.weather = WeatherType.CLEAR;
						}
					}
					setColors();
					noAnswerSleepTime = noAnswerSleepDefault;					
				}
			}
			
		} catch (InterruptedException e) {
			blink1.setRGB(Color.BLACK);
		}
	}
	
	private void changeColor(Color color) {
		blink1.setRGB(color);
		setBackground(color);
	}
	
	private void setColors() throws InterruptedException {
		changeColor(weather.getColor1());
		while(watch.elapsed(TimeUnit.MINUTES) < usualSleep) {
			long minutes = watch.elapsed(TimeUnit.MINUTES);
			long seconds = watch.elapsed(TimeUnit.SECONDS) - minutes*60;
			String time = String.format("%d:%02d", minutes, seconds);
			System.out.println("Current weather has been around for " + time);			
			if(weather.blinks()) {			
				changeColor(weather.getColor1());
				Thread.sleep(TimeUnit.SECONDS.toMillis(2));
				changeColor(weather.getColor2());
				Thread.sleep(TimeUnit.SECONDS.toMillis(2));	
			}  else {
				Thread.sleep(TimeUnit.MINUTES.toMillis(5));	
			}
		}
		watch.reset();
	}
	
}
