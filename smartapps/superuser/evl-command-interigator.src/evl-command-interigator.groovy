/**
 *  DSC Command Center
 *
 *  Copyright 2015
 *  Author: David Cauthron
 *  Also Attributed:  JTT-AE <aesystems@gmail.com>
 *                    Rob Fisher <robfish@att.net>
 *					  Carlos Santiago <carloss66@gmail.com> 
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
    name: "EVL Command Interigator",
    author: "David Cauthron",
    description: "Command Center SmartApp for DSC Alarms",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	preferences {
    	section("Alarm Server Settings") {
            input("ip", "text", title: "IP", description: "The IP of your AlarmServer", required: true)
            input("port", "text", title: "Port", description: "The port", required: true)
            input("alarmCodePanel", "text", title: "Alarm Code", description: "The code for your alarm panel.", required: false)
            input "smartMonitorInt", "enum", title: "Integrate w/ Smart Monitor?", options: ["Yes", "No"], required: true
        }
        section("Button for Alarm") {
            input "thecommand", "capability.Switch", required: false
        }
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
	log.debug "Version 1.3"
    log.debug smartMonitorInt.value[0]
    if(smartMonitorInt.value[0] != "N")
    {
    	subscribe(location, "alarmSystemStatus", alarmStatusUpdate)
    }
	subscribe(thecommand, "switch", switchUpdate)
}

def switchUpdate(evt) {
	def eventMap = [
        'stayarm':"/api/alarm/stayarm",
        'disarm':"/api/alarm/disarm",
        'arm':"/api/alarm/armwithcode"
    ]
	
    def securityMonitorMap = [
        'stayarm':"stay",
        'disarm':"off",
        'arm':"away"
    ]
    
    if(smartMonitorInt.value[0] != "N")
    {
    	sendLocationEvent(name: "alarmSystemStatus", value: securityMonitorMap."${evt.value}")
    }
	callAlarmServer(evt, eventMap)
}

def alarmStatusUpdate(evt) {
	def eventMap = [
        'stay':"/api/alarm/stayarm",
        'off':"/api/alarm/disarm",
        'away':"/api/alarm/armwithcode"
    ]
	
    def securityMonitorMap = [
        'stay':"stayarm",
        'off':"disarm",
        'away':"arm"
    ]
    
    def command = securityMonitorMap."${evt.value}";
    thecommand."$command"()
	callAlarmServer(evt, eventMap)
}

private callAlarmServer(evt, eventMap) {
	try {
        def path = eventMap."${evt.value}"
        
        sendHubCommand(new physicalgraph.device.HubAction(
            method: "GET",
            path: path,
            headers: [
                HOST: "${ip}:${port}"
            ],
    		query: [alarmcode: "${alarmCodePanel.value}"]
        ))
    } catch (e) {
        log.error "something went wrong: $e"
    }
}