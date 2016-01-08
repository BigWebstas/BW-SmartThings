/**
 *  Panel Meter Management
 *
 *  Copyright 2015 John Rucker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Config Panel Meter",
    namespace: "NutsVolts",
    author: "John Rucker",
    description: "Manage and send updates to Analog Panel Meter",
    category: "My Apps",
    iconUrl: "http://coopboss.com/images/SmartThingsIcons/coopbossLogo.png",
    iconX2Url: "http://coopboss.com/images/SmartThingsIcons/coopbossLogo2x.png",
    iconX3Url: "http://coopboss.com/images/SmartThingsIcons/coopbossLogo3x.png")


preferences {
        section(hideable: true, "Overview"){
        	paragraph "This SmartApp allows you to manage your Analog Panel Meter"  
        }
        section("SmartApp Settings") {    
			input(name: "weatherStation", type: "capability.temperatureMeasurement", title: "Select a source for your weather data", required: true, multiple: false)
            input(name: "alogMeter", type: "capability.switchLevel", title: "Select the analog panel meter to manage and send updates", required: true)    
        }
}


def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(weatherStation, "wind", updateWind)
    log.debug "subscribed to wind"
}

def updateWind(evt){
	log.debug "New event ${evt.name}:${evt.value}"
    def windSpeed =(int)(evt.value as float)
    log.debug "calling setLevel(${windSpeed})"
	alogMeter.setLevel(windSpeed)
}