/**
 *  Honeywell Panel Device
 *
 *  Copyright 2016 BigWebstas
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
preferences {
    input("ip", "text", title: "IP", description: "The IP of your alarmserver")
    input("port", "text", title: "Port", description: "The port")
    //input("pincode", "text", title: "Pincode", description: "Alarm Pincode")
	} 
// for the UI
metadata {
  // Automatically generated. Make future change here.
  	definition (name: "Honeywell Panel Device", namespace: "Webstas", author: "BigWebstas") {
    // Change or define capabilities here as needed
    capability "Switch"
    capability "Refresh"
    capability "Polling"
    capability "Alarm"

    // Add commands as needed
    command "partition"
    command "stayarm"
	command "arm"
	command "disarm"
	command "chime"   
    command "both"
    command "off"   
    command "on"
    
  }

  tiles (scale: 2){
    standardTile("dscpartition", "device.dscpartition", width: 6, height: 4, canChangeBackground: true, canChangeIcon: true) {
      state "armed-stay",     label: 'Armed Stay',      backgroundColor: "#008CC1", icon:"st.Transportation.transportation13"
      state "armed-away",     label: 'Armed Away',      backgroundColor: "#800000", icon:"st.Transportation.transportation13"
      state "armed-max",     label: 'Armed MAX',      backgroundColor: "#800000", icon:"st.Transportation.transportation13"
      state "ready-bypass",     label: 'Ready Bypass',      backgroundColor: "#800000", icon:"st.Transportation.transportation12"
      state "exit/entry-delay", label: 'Entry/Exit Delay', backgroundColor: "#ff9900", icon:"st.Home.home3"
      state "notready",  label: 'Not Ready',  backgroundColor: "#ffcc00", icon:"st.Home.home2"
      state "ready",     label: 'Ready',      backgroundColor: "#79b821", icon:"st.Transportation.transportation12"
      state "alarm",     label: '!!Alarm!!',      backgroundColor: "#ff0000", icon:"st.Home.home3"
    }

		standardTile("disarm", "capability.momentary", width: 2, height: 2, title: "Disarm", required: true, multiple: false){
			state "disarm", label: 'Disarm', action: "disarm", icon: "st.Home.home3", backgroundColor: "#79b821"
		}

		standardTile("arm", "capability.momentary", width: 2, height: 2, title: "Arm", required: true, multiple: false){
			state "arm", label: 'Arm - Away', action: "arm", icon: "st.Home.home2", backgroundColor: "#800000"
       }
		standardTile("stayarm", "capability.momentary", width: 2, height: 2, title: "Armed Stay", required: true, multiple: false){
			state "stayarm", label: 'Arm - Stay', action: "stayarm", icon: "st.Home.home4", backgroundColor: "#008CC1"
       }
       standardTile("chime", "capability.momentary", width: 6, height: 2, title: "Chime", required: true, multiple: false, decoration: "flat"){
			state "chime", label: 'Chime Toggle', action: "chime", icon: "st.Electronics.electronics12"
       }
       
    standardTile("main", "device.dscpartition", width: 6, height: 4, canChangeBackground: true, canChangeIcon: true) {
      state "armed-stay",     label: 'Armed Stay',      backgroundColor: "#800000", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
  	  state "armed-away",     label: 'Armed Away',      backgroundColor: "#800000", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
  	  state "armed-max",     label: 'Armed MAX',      backgroundColor: "#800000", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
	  state "ready-bypass",     label: 'Ready Bypass',      backgroundColor: "#800000", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
      state "exit/entry-delay",	label: 'Entry/Exit Delay', backgroundColor: "#ff9900", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
      state "notready",  label: 'Not Ready',  backgroundColor: "#ffcc00", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
      state "ready",     label: 'Ready',      backgroundColor: "#79b821", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
      state "alarm",     label: '!!Alarm!!',      backgroundColor: "#ff0000", icon:"http://d1unzhqf5a606m.cloudfront.net/images/large/honeywell-6150-fixed-english-alarm-keypad-with-function-buttons.jpg?1340151882"
    }
		main (["main"])	
		details(["dscpartition", "arm", "stayarm", "staybutton", "disarm", "chime"])
  }
}

// parse events into attributes
def parse(String description) {
  def myValues = description.tokenize()

  log.debug "Event Parse function: ${description}"
  sendEvent (name: "${myValues[0]}", value: "${myValues[1]}")
}

def partition(String state, String partition) {
    log.debug "Partition: ${state} for partition: ${partition}"
    sendEvent (name: "dscpartition", value: "${state}")
    if (state != "ready") {
		sendEvent (name: "switch", value: "on")
        log.debug "switch on"
    } else { 
    	sendEvent (name: "switch", value: "off")
    	log.debug "switch off" }
}

def refresh() {
  poll()
}
def stayarm() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/stayarm",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request to stay arm received"
    //log.debug "stayarm"
    sendEvent (name: "dscpartition", value: "stayarm")
    return result
}

def arm() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/arm",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request to arm received"
    //log.debug "arm"
    sendEvent (name: "dscpartition", value: "arm")
    return result
}

def disarm() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/disarm",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request to disarm received"
    //log.debug "disarm"
    sendEvent (name: "dscpartition", value: "ready")
    return result
}
def both() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/panic",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request set alarm in panic"
    return result
} 
def on() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/stayarm",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request to stayarm received"
    //log.debug "disarm"
    sendEvent (name: "dscpartition", value: "stayarm")
    return result    
}
def off() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/disarm",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request to disarm received"
    //log.debug "disarm"
    sendEvent (name: "dscpartition", value: "disarm")
    return result    
}
def chime() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/chime",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    log.debug "response" : "Request to toggle chime received"
    return result
}
def poll() {
	log.debug "Executing 'poll'"
        def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api",
        headers: [
            HOST: "$ip:$port"
        ]
    )
    return result
}