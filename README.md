OldHancockTowerBlink1
=====================
This mimics the Old Hancock Buildings weather beacon (http://en.wikipedia.org/wiki/Berkeley_Building). It pings Forecast.io through the forecast.io Java API (https://github.com/dvdme/forecastio-lib-java), fetches the upcoming weather for Boston (hour 0) and plays that back over a thingM Blink(1) (http://blink1.thingm.com). Every 30 minutes, it checks for an update. Ideally I'll separate these out to limit API calls (I get 1,000 per day, so if you do check this out, please go get your own forecast.io API key and put that in the code) from the display, but I wanted to get this up as quickly as possible.
It's an eclipse package right now, not bundled into a JAR, but we'll get there.
