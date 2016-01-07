/**
 *  Copyright 2016 Big Webstas
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "WUNder Weather", namespace: "Webstas", author: "JWebstas@gmail.com") {
		capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Sensor"
        capability "Illuminance Measurement"

		attribute "localSunrise", "string"
		attribute "localSunset", "string"
		attribute "wind", "string"
		attribute "weatherIcon", "string"
		attribute "alert", "string"
		attribute "alertKeys", "string"
		attribute "sunriseDate", "string"
		attribute "sunsetDate", "string"
        attribute "windStr", "string"
        attribute "precipToday", "string"    
        attribute "observationTime", "string"         
        attribute "observationLocation", "string"
		attribute "fcP0", "string"
		attribute "fcP1", "string"
		attribute "fcP2", "string"
		attribute "fcP3", "string" 
        attribute "windDir", "string"
        attribute "light", "string"

		command "refresh"
	}

	preferences {
		input "zipCode", "text", title: "Zip Code (optional)", required: false
	}

	tiles (scale: 2) {
		valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state "default", label:'${currentValue}°', unit:"F",
				backgroundColors:[
					[value: 31, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 95, color: "#d04e00"],
					[value: 96, color: "#bc2323"]
				]
		}

		valueTile("humidity", "device.humidity", decoration: "flat", width: 2, height: 2) {
			state "default", label:'${currentValue}% humidity'
		}

		standardTile("weatherIcon", "device.weatherIcon", decoration: "flat", width: 2, height: 2) {
			state "chanceflurries", icon:"st.custom.wu1.chanceflurries", label: ""
			state "chancerain", icon:"st.custom.wu1.chancerain", label: ""
			state "chancesleet", icon:"st.custom.wu1.chancesleet", label: ""
			state "chancesnow", icon:"st.custom.wu1.chancesnow", label: ""
			state "chancetstorms", icon:"st.custom.wu1.chancetstorms", label: ""
			state "clear", icon:"st.custom.wu1.clear", label: ""
			state "cloudy", icon:"st.custom.wu1.cloudy", label: ""
			state "flurries", icon:"st.custom.wu1.flurries", label: ""
			state "fog", icon:"st.custom.wu1.fog", label: ""
			state "hazy", icon:"st.custom.wu1.hazy", label: ""
			state "mostlycloudy", icon:"st.custom.wu1.mostlycloudy", label: ""
			state "mostlysunny", icon:"st.custom.wu1.mostlysunny", label: ""
			state "partlycloudy", icon:"st.custom.wu1.partlycloudy", label: ""
			state "partlysunny", icon:"st.custom.wu1.partlysunny", label: ""
			state "rain", icon:"st.custom.wu1.rain", label: ""
			state "sleet", icon:"st.custom.wu1.sleet", label: ""
			state "snow", icon:"st.custom.wu1.snow", label: ""
			state "sunny", icon:"st.custom.wu1.sunny", label: ""
			state "tstorms", icon:"st.custom.wu1.tstorms", label: ""
			state "cloudy", icon:"st.custom.wu1.cloudy", label: ""
			state "partlycloudy", icon:"st.custom.wu1.partlycloudy", label: ""
			state "nt_chanceflurries", icon:"st.custom.wu1.nt_chanceflurries", label: ""
			state "nt_chancerain", icon:"st.custom.wu1.nt_chancerain", label: ""
			state "nt_chancesleet", icon:"st.custom.wu1.nt_chancesleet", label: ""
			state "nt_chancesnow", icon:"st.custom.wu1.nt_chancesnow", label: ""
			state "nt_chancetstorms", icon:"st.custom.wu1.nt_chancetstorms", label: ""
			state "nt_clear", icon:"st.custom.wu1.nt_clear", label: ""
			state "nt_cloudy", icon:"st.custom.wu1.nt_cloudy", label: ""
			state "nt_flurries", icon:"st.custom.wu1.nt_flurries", label: ""
			state "nt_fog", icon:"st.custom.wu1.nt_fog", label: ""
			state "nt_hazy", icon:"st.custom.wu1.nt_hazy", label: ""
			state "nt_mostlycloudy", icon:"st.custom.wu1.nt_mostlycloudy", label: ""
			state "nt_mostlysunny", icon:"st.custom.wu1.nt_mostlysunny", label: ""
			state "nt_partlycloudy", icon:"st.custom.wu1.nt_partlycloudy", label: ""
			state "nt_partlysunny", icon:"st.custom.wu1.nt_partlysunny", label: ""
			state "nt_sleet", icon:"st.custom.wu1.nt_sleet", label: ""
			state "nt_rain", icon:"st.custom.wu1.nt_rain", label: ""
			state "nt_sleet", icon:"st.custom.wu1.nt_sleet", label: ""
			state "nt_snow", icon:"st.custom.wu1.nt_snow", label: ""
			state "nt_sunny", icon:"st.custom.wu1.nt_sunny", label: ""
			state "nt_tstorms", icon:"st.custom.wu1.nt_tstorms", label: ""
			state "nt_cloudy", icon:"st.custom.wu1.nt_cloudy", label: ""
			state "nt_partlycloudy", icon:"st.custom.wu1.nt_partlycloudy", label: ""
		}
        standardTile("main", "device.weatherIcon", decoration: "flat") {
			state "chanceflurries", icon:"st.custom.wu1.chanceflurries", label: "Chance Flurries"
			state "chancerain", icon:"st.custom.wu1.chancerain", label: "Chance Rain"
			state "chancesleet", icon:"st.custom.wu1.chancesleet", label: "Chance Sleet"
			state "chancesnow", icon:"st.custom.wu1.chancesnow", label: "Chance Snow"
			state "chancetstorms", icon:"st.custom.wu1.chancetstorms", label: "Chance T Storms"
			state "clear", icon:"st.custom.wu1.clear", label: "Clear"
			state "cloudy", icon:"st.custom.wu1.cloudy", label: "Cloudy"
			state "flurries", icon:"st.custom.wu1.flurries", label: "Flurries"
			state "fog", icon:"st.custom.wu1.fog", label: "Fog"
			state "hazy", icon:"st.custom.wu1.hazy", label: "Hazy"
			state "mostlycloudy", icon:"st.custom.wu1.mostlycloudy", label: "Mostly Cloudy"
			state "mostlysunny", icon:"st.custom.wu1.mostlysunny", label: "Mostly Sunny"
			state "partlycloudy", icon:"st.custom.wu1.partlycloudy", label: "Partly Cloudy"
			state "partlysunny", icon:"st.custom.wu1.partlysunny", label: "Partly Sunny"
			state "rain", icon:"st.custom.wu1.rain", label: "Rain"
			state "sleet", icon:"st.custom.wu1.sleet", label: "Sleet"
			state "snow", icon:"st.custom.wu1.snow", label: "Snow"
			state "sunny", icon:"st.custom.wu1.sunny", label: "Sunny"
			state "tstorms", icon:"st.custom.wu1.tstorms", label: "Thunder Storms"
			state "cloudy", icon:"st.custom.wu1.cloudy", label: "Cloudy"
			state "partlycloudy", icon:"st.custom.wu1.partlycloudy", label: "Partly Cloudy"
			state "nt_chanceflurries", icon:"st.custom.wu1.nt_chanceflurries", label: "Chance Flurries"
			state "nt_chancerain", icon:"st.custom.wu1.nt_chancerain", label: "Chance Rain"
			state "nt_chancesleet", icon:"st.custom.wu1.nt_chancesleet", label: "Chance Sleet"
			state "nt_chancesnow", icon:"st.custom.wu1.nt_chancesnow", label: "Chance Snow"
			state "nt_chancetstorms", icon:"st.custom.wu1.nt_chancetstorms", label: "Chance T Storms"
			state "nt_clear", icon:"st.custom.wu1.nt_clear", label: "Clear"
			state "nt_cloudy", icon:"st.custom.wu1.nt_cloudy", label: "Cloudy"
			state "nt_flurries", icon:"st.custom.wu1.nt_flurries", label: "Flurries"
			state "nt_fog", icon:"st.custom.wu1.nt_fog", label: "Fog"
			state "nt_hazy", icon:"st.custom.wu1.nt_hazy", label: "Hazy"
			state "nt_mostlycloudy", icon:"st.custom.wu1.nt_mostlycloudy", label: "Mostly Cloudy"
			state "nt_mostlysunny", icon:"st.custom.wu1.nt_mostlysunny", label: "Mostly Sunny"
			state "nt_partlycloudy", icon:"st.custom.wu1.nt_partlycloudy", label: "Partly Cloudy"
			state "nt_partlysunny", icon:"st.custom.wu1.nt_partlysunny", label: "Partly Sunny"
			state "nt_sleet", icon:"st.custom.wu1.nt_sleet", label: "Sleet"
			state "nt_rain", icon:"st.custom.wu1.nt_rain", label: "Rain"
			state "nt_sleet", icon:"st.custom.wu1.nt_sleet", label: "Sleet"
			state "nt_snow", icon:"st.custom.wu1.nt_snow", label: "Snow"
			state "nt_sunny", icon:"st.custom.wu1.nt_sunny", label: "Sunny"
			state "nt_tstorms", icon:"st.custom.wu1.nt_tstorms", label: "Thunder Storms"
			state "nt_cloudy", icon:"st.custom.wu1.nt_cloudy", label: "Cloudy"
			state "nt_partlycloudy", icon:"st.custom.wu1.nt_partlycloudy", label: "Partly Sunny"
		}
		valueTile("UV", "device.UV",  inactiveLabel: false, width: 2, height: 2) {
			state "default", label:'${currentValue} ${unit}', unit:"UV", backgroundColors:[
				[value: 0, color: "#44b621"],
				[value: 2.9, color: "#ffff00"],
				[value: 5.9, color: "#ff9900"],
				[value: 7.9, color: "#ff3300"],
				[value: 10, color: "#cc66ff"],
				[value: 11, color: "#f7e5ff"]
			]
		}
		standardTile("refresh", "device.weather", decoration: "flat") {
			state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
		}

		valueTile("alert", "device.alert", width: 3, height: 1, decoration: "flat") {
			state "default", label:'${currentValue}'
		}

		valueTile("rise", "device.localSunrise", decoration: "flat", width: 2, height: 1) {
			state "default", label:'${currentValue}'
		}

		valueTile("set", "device.localSunset", decoration: "flat", width: 2, height: 1) {
			state "default", label:'${currentValue}'
		}

		valueTile("txtSun", "device.illuminance", decoration: "flat", width: 2, height: 1) {
			state "default", label:'Sunrise Sunset'
		}
        
		valueTile("windDir", "device.windStr", decoration: "flat", width: 5, height: 1) {
			state "default", label:'Wind ${currentValue}'
		}     
        
		valueTile("precipToday", "device.precipToday", decoration: "flat", width: 2, height: 2) {
			state "default", label:'${currentValue}" rain'
		}   
        
		valueTile("fcastP0", "device.fcP0", decoration: "flat", width: 6, height: 2) {
			state "default", label:'${currentValue}'
		}         
        
		valueTile("fcastP1", "device.fcP1", decoration: "flat", width: 6, height: 2) {
			state "default", label:'${currentValue}'
		}       
        
		valueTile("fcastP2", "device.fcP2", decoration: "flat", width: 6, height: 2) {
			state "default", label:'${currentValue}'
		}       
        
		valueTile("fcastP3", "device.fcP3", decoration: "flat", width: 6, height: 2) {
			state "default", label:'${currentValue}'
		}               
        
		valueTile("obTime", "device.observationTime", decoration: "flat", width: 3, height: 1) {
			state "default", label:'${currentValue}'
		}      
        
		valueTile("obLocation", "device.observationLocation", decoration: "flat", width: 6, height: 2) {
			state "default", label:'Weather Data from \n${currentValue}'
		} 
		valueTile("light","device.illuminance",inactiveLabel: false, width: 2, height: 2) {
			state "luminosity",label:'${currentValue} ${unit}', unit:"lux", backgroundColors:[
				[value: 0, color: "#000000"],
				[value: 200, color: "#060053"],
				[value: 1000, color: "#3E3900"],
				[value: 2500, color: "#8E8400"],
				[value: 7500, color: "#C5C08B"],
				[value: 10000, color: "#DAD7B6"],
				[value: 128, color: "#F3F2E9"],
				[value: 2000, color: "#FFFFFF"]
		]
  }  
		main(["main"])
		details(["temperature", "humidity", "precipToday", "windDir", "refresh", "txtSun", "rise", "set", "UV", "light", "weatherIcon", "alert", "fcastP0", "fcastP1", "fcastP2", "fcastP3", "obTime", "obLocation"])}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def installed() {
	runPeriodically(60, poll)		// in seconds
}

def uninstalled() {
	unschedule()
}

def updated() {
	runPeriodically(60, poll)
}

// handle commands
def poll() {
	log.debug "WUSTATION: Executing 'poll', location: ${location.name}"

	// Current conditions
	def obs = get("conditions")?.current_observation
	if (obs) {
    	//log.info"current_observation: ${obs.observation_location}"
        
		def weatherIcon = obs.icon_url.split("/")[-1].split("\\.")[0]

		if(getTemperatureScale() == "C") {
			send(name: "temperature", value: Math.round(obs.temp_c), unit: "C")
		} else {
			send(name: "temperature", value: Math.round(obs.temp_f), unit: "F")
		} 
        
        Map lcation = obs.observation_location 
		send(name: "observationLocation", value: lcation.city)
        
        send(name: "observationTime", value: obs.observation_time)
        send(name: "precipToday", value: obs.precip_today_in)
		send(name: "windStr", value: obs.wind_string)
		send(name: "humidity", value: obs.relative_humidity[0..-2] as Integer, unit: "%")
		send(name: "weatherIcon", value: weatherIcon, displayed: false)
		send(name: "wind", value: Math.round(obs.wind_mph) as String, unit: "MPH") // as String because of bug in determining state change of 0 numbers
        send(name: "windDir", value: obs.wind_degrees, displayed: false) 
        send(name: "UV", value: obs.UV, displayed: false) 

		// Sunrise / sunset
		def a = get("astronomy")?.moon_phase
		def today = localDate("GMT${obs.local_tz_offset}")
		def ltf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
		ltf.setTimeZone(TimeZone.getTimeZone("GMT${obs.local_tz_offset}"))
		def utf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		utf.setTimeZone(TimeZone.getTimeZone("GMT"))

		def sunriseDate = ltf.parse("${today} ${a.sunrise.hour}:${a.sunrise.minute}")
		def sunsetDate = ltf.parse("${today} ${a.sunset.hour}:${a.sunset.minute}")

        def tf = new java.text.SimpleDateFormat("h:mm a")
        tf.setTimeZone(TimeZone.getTimeZone("GMT${obs.local_tz_offset}"))
        def localSunrise = "${tf.format(sunriseDate)}"
        def localSunset = "${tf.format(sunsetDate)}"
        send(name: "localSunrise", value: localSunrise, descriptionText: "Sunrise today is at $localSunrise")
        send(name: "localSunset", value: localSunset, descriptionText: "Sunset today at is $localSunset")
		send(name: "illuminance", value: estimateLux(sunriseDate, sunsetDate, weatherIcon))
		// Forecast
		def f = get("forecast")        
        //log.debug "f.forecast.txt_forecast.forecastday[0] = ${f.forecast.txt_forecast.forecastday[0]}"
        
        Map fcstMap = f.forecast.txt_forecast.forecastday[0]    
        def fcTxt = "${fcstMap.title}: ${fcstMap.fcttext}"
        send(name: "fcP0", value: fcTxt)
        //log.info "${fcTxt}"

        fcstMap = f.forecast.txt_forecast.forecastday[1]  
        fcTxt = "${fcstMap.title}: ${fcstMap.fcttext}"
        send(name: "fcP1", value: fcTxt)
        //log.info "${fcTxt}"        
        
        fcstMap = f.forecast.txt_forecast.forecastday[2]  
        fcTxt = "${fcstMap.title}: ${fcstMap.fcttext}"
        send(name: "fcP2", value: fcTxt)
        //log.info "${fcTxt}"    
 
        fcstMap = f.forecast.txt_forecast.forecastday[3]  
        fcTxt = "${fcstMap.title}: ${fcstMap.fcttext}"
        send(name: "fcP3", value: fcTxt)
        //log.info "${fcTxt}"     

		// Alerts
		def alerts = get("alerts")?.alerts
		def newKeys = alerts?.collect{it.type + it.date_epoch} ?: []
		log.debug "WUSTATION: newKeys = $newKeys"
		log.trace device.currentState("alertKeys")
		def oldKeys = device.currentState("alertKeys")?.jsonValue
		log.debug "WUSTATION: oldKeys = $oldKeys"

		def noneString = "no current weather alerts"
		if (!newKeys && oldKeys == null) {
			send(name: "alertKeys", value: newKeys.encodeAsJSON(), displayed: false)
			send(name: "alert", value: noneString, descriptionText: "${device.displayName} has no current weather alerts", isStateChange: true)
		}
		else if (newKeys != oldKeys) {
			if (oldKeys == null) {
				oldKeys = []
			}
			send(name: "alertKeys", value: newKeys.encodeAsJSON(), displayed: false)

			def newAlerts = false
			alerts.each {alert ->
				if (!oldKeys.contains(alert.type + alert.date_epoch)) {
					def msg = "${alert.description} from ${alert.date} until ${alert.expires}"
					send(name: "alert", value: pad(alert.description), descriptionText: msg, isStateChange: true)
					newAlerts = true
				}
			}

			if (!newAlerts && device.currentValue("alert") != noneString) {
				send(name: "alert", value: noneString, descriptionText: "${device.displayName} has no current weather alerts", isStateChange: true)
			}
		}
	}
	else {
		log.warn "No response from Weather Underground API"
	}
}

def refresh() {
	poll()
}

def configure() {
	poll()
}

private pad(String s, size = 25) {
	def n = (size - s.size()) / 2
	if (n > 0) {
		def sb = ""
		n.times {sb += " "}
		sb += s
		n.times {sb += " "}
		return sb
	}
	else {
		return s
	}
}


private get(feature) {
	getWeatherFeature(feature, zipCode)
}

private localDate(timeZone) {
	def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
	df.setTimeZone(TimeZone.getTimeZone(timeZone))
	df.format(new Date())
}

private estimateLux(sunriseDate, sunsetDate, weatherIcon) {
	def lux = 0
	def now = new Date().time
	if (now > sunriseDate.time && now < sunsetDate.time) {
		//day
		switch(weatherIcon) {
			case 'tstorms':
				lux = 200
				break
			case ['cloudy', 'fog', 'rain', 'sleet', 'snow', 'flurries',
				'chanceflurries', 'chancerain', 'chancesleet',
				'chancesnow', 'chancetstorms']:
				lux = 1000
				break
			case 'mostlycloudy':
				lux = 2500
				break
			case ['partlysunny', 'partlycloudy', 'hazy']:
				lux = 7500
				break
			default:
				//sunny, clear
				lux = 10000
		}

		//adjust for dusk/dawn
		def afterSunrise = now - sunriseDate.time
		def beforeSunset = sunsetDate.time - now
		def oneHour = 1000 * 60 * 60

		if(afterSunrise < oneHour) {
			//dawn
			lux = (long)(lux * (afterSunrise/oneHour))
		} else if (beforeSunset < oneHour) {
			//dusk
			lux = (long)(lux * (beforeSunset/oneHour))
		}
	}
	else {
		//night - always set to 10 for now
		//could do calculations for dusk/dawn too
		lux = 10
	}

	lux
}

private send(map) {
	log.debug "event: $map"
	sendEvent(map)
}
