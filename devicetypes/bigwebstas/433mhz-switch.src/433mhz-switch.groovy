/**
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
    input("ip", "text", title: "IP", description: "IPv4 Address")
    input("port", "text", title: "Port", description: "Port")
    input("user", "text", title: "user", description: "User Name")
    input("pass", "text", title: "pass", description: "Password")
   	} 

metadata {

  	definition (name: "433mhz switch", namespace: "BigWebstas", author: "BigWebstas") {

    capability "Switch"
   // capability "Refresh"
   // capability "Polling"
    capability "Momentary"

	command "push"
    command "off"   
    command "on"
    
  }

  	tiles {
    		 
		standardTile("down", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'Down', action:"on", icon:"https://github.com/BigWebstas/BW-SmartThings/blob/master/assets/down.png?raw=true"
		}  
        standardTile("up", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'Up', action:"off", icon:"https://github.com/BigWebstas/BW-SmartThings/blob/master/assets/up.png?raw=true"
		} 
        standardTile("stop", "device.button", inactiveLabel: false, decoration: "flat") {
			state "default", label:'stop', action:"momentary.push", icon:"https://github.com/BigWebstas/BW-SmartThings/blob/master/assets/stop.png?raw=true"
		}
        valueTile("status", "device.switch", ) {
            state "val", label:'${currentValue}', defaultState: true, icon:"st.Electronics.electronics18"
        }
        main "status"
		details(["down", "up", "stop"])
	}
}

// parse events into attributes
def parse(String description) {
  log.debug("Parsediddlyarsing")

  def map = stringToMap(description)
    def header = new String(map.headers.decodeBase64())
    def body = new String(map.body.decodeBase64())
}

def on() {
	log.debug "Executing 'on or down'"
        def userpassascii = "${user}:${pass}"
        def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
        def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/code/send/3266340",
        headers: [
            HOST: "$ip:$port", Authorization:userpass
            
        ]
    )
    sendEvent (name: "switch", value: "on")
    return result    
}
def off() {
	log.debug "Executing 'off or up'"
        def userpassascii = "${user}:${pass}"
        def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
        def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/code/send/3266337",
        headers: [
            HOST: "$ip:$port", Authorization:userpass
            
        ]
    )
    sendEvent (name: "switch", value: "off")
    return result
}

def push() {
	log.debug "Executing 'stop'"
        def userpassascii = "${user}:${pass}"
        def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
        def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/code/send/3266338",
        headers: [
            HOST: "$ip:$port", Authorization:userpass
            
        ]
    )
    return result    
}
