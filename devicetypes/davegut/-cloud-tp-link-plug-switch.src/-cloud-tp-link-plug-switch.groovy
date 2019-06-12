/*
TP-Link Plug and Switch Device Handler, 2018, Version 3
	Copyright 2018 Dave Gutheinz and Anthony Ramirez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this  file except in compliance with the
License. You may obtain a copy of the License at:

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific 
language governing permissions and limitations under the 
License.

Discalimer:  This Service Manager and the associated Device 
Handlers are in no way sanctioned or supported by TP-Link.  
All  development is based upon open-source data on the 
TP-Link devices; primarily various users on GitHub.com.

	===== History ============================================
2018-10-14	Update to Version 3.  Initial compatibility with
			the Classic and new SmartThings Mobile App.  No
            update to Service Manager.  Service Manager must
            be installed via the SmartThings Classic App.
            Thanks to Anthony Ramirez for providing the
            technical information for this update.

	===== Plug/Switch Type.  DO NOT EDIT ====================*/
	def deviceType = "Plug-Switch"			//	Plug/Switch
//	def deviceType = "Dimming Switch"		//	HS220 Only
//	===== Hub or Cloud Installation =========================*/
	def installType = "Cloud"
//	def installType = "Hub"
//	===========================================================

metadata {
	definition (name: "(${installType}) TP-Link ${deviceType}",
				namespace: "davegut",
				author: "Dave Gutheinz and Anthony Ramirez",
				deviceType: "${deviceType}",
				energyMonitor: "Standard",
				ocfDeviceType: "oic.d.smartplug",
				mnmn: "SmartThings",
				vid: "generic-switch-power",
				installType: "${installType}") {
		capability "Switch"
		capability "refresh"
		capability "polling"
		capability "Sensor"
		capability "Actuator"
		capability "Health Check"
		if (deviceType == "Dimming Switch") {
			capability "Switch Level"
		}
	}
	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#00a0dc",
				nextState:"waiting"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff",
				nextState:"waiting"
				attributeState "waiting", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#15EE10",
				nextState:"waiting"
				attributeState "commsError", label:'Comms Error', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#e86d13",
				nextState:"waiting"
            }
			if (deviceType == "Dimming Switch") {
				tileAttribute ("device.level", key: "SLIDER_CONTROL") {
					attributeState "level", label: "Brightness: ${currentValue}", action:"switch level.setLevel", range: "(1..100)"
				}
			}
 			tileAttribute ("deviceError", key: "SECONDARY_CONTROL") {
				attributeState "deviceError", label: '${currentValue}'
			}
		}
        
		standardTile("refresh", "capability.refresh", width: 2, height: 1,  decoration: "flat") {
			state "default", label:"Refresh", action:"refresh.refresh"
		}
        
		main("switch")
		details("switch", "refresh")
	}

	def rates = [:]
	rates << ["1" : "Refresh every minutes (Not Recommended)"]
	rates << ["5" : "Refresh every 5 minutes"]
	rates << ["10" : "Refresh every 10 minutes"]
	rates << ["15" : "Refresh every 15 minutes"]

	preferences {
		if (installType == "Hub") {
			input("deviceIP", "text", title: "Device IP", required: true, displayDuringSetup: true)
			input("gatewayIP", "text", title: "Gateway IP", required: true, displayDuringSetup: true)
		}
		input name: "refreshRate", type: "enum", title: "Refresh Rate", options: rates, description: "Select Refresh Rate", required: false
	}
}

//	===== Update when installed or setting changed =====
/*	Health Check Implementation
	1.	Each time a command is sent, the DeviceWatch-Status
		is set to on- or off-line.
	2.	Refresh is run every 15 minutes to provide a min
		cueing of this.
	3.	Is valid for either hub or cloud based device.*/
def initialize() {
	log.trace "Initialized..."
	sendEvent(name: "DeviceWatch-Enroll", value: groovy.json.JsonOutput.toJson(["protocol":"cloud", "scheme":"untracked"]), displayed: false)
}

def ping() {
	refresh()
}

def installed() {
	update()
}

def updated() {
	runIn(2, update)
}

def update() {
	state.deviceType = metadata.definition.deviceType
	state.installType = metadata.definition.installType
	unschedule()
	switch(refreshRate) {
		case "1":
			runEvery1Minute(refresh)
			log.info "Refresh Scheduled for every minute"
			break
		case "5":
			runEvery5Minutes(refresh)
			log.info "Refresh Scheduled for every 5 minutes"
			break
		case "10":
			runEvery10Minutes(refresh)
			log.info "Refresh Scheduled for every 10 minutes"
			break
		default:
			runEvery15Minutes(refresh)
			log.info "Refresh Scheduled for every 15 minutes"	}
	runIn(5, refresh)
}

void uninstalled() {
	if (state.installType == "Cloud") {
		def alias = device.label
		log.debug "Removing device ${alias} with DNI = ${device.deviceNetworkId}"
		parent.removeChildDevice(alias, device.deviceNetworkId)
	}
}

//	===== Basic Plug Control/Status =====
def on() {
	sendCmdtoServer('{"system":{"set_relay_state":{"state": 1}}}', "deviceCommand", "commandResponse")
}

def off() {
	sendCmdtoServer('{"system":{"set_relay_state":{"state": 0}}}', "deviceCommand", "commandResponse")
}

def setLevel(percentage) {
	percentage = percentage as int
    if (percentage == 0) {
    	percentage = 1
    }
	sendCmdtoServer("""{"smartlife.iot.dimmer":{"set_brightness":{"brightness":${percentage}}}}""", "deviceCommand", "commandResponse")
}

def poll() {
	sendCmdtoServer('{"system":{"get_sysinfo":{}}}', "deviceCommand", "refreshResponse")
}

def refresh(){
	sendCmdtoServer('{"system":{"get_sysinfo":{}}}', "deviceCommand", "refreshResponse")
}

def commandResponse(cmdResponse) {
	refresh()
}

def refreshResponse(cmdResponse){
	def onOff = cmdResponse.system.get_sysinfo.relay_state
	if (onOff == 1) {
		onOff = "on"
	} else {
		onOff = "off"
	}
	sendEvent(name: "switch", value: onOff)
	def level = "0"
	if (state.deviceType == "Dimming Switch") {
		level = cmdResponse.system.get_sysinfo.brightness
	 	sendEvent(name: "level", value: level)
	}
	log.info "${device.name} ${device.label}: Power: ${onOff} / Dimmer Level: ${level}%"
}

//	----- SEND COMMAND TO CLOUD VIA SM -----
private sendCmdtoServer(command, hubCommand, action) {
	if (state.installType == "Hub") {
		sendCmdtoHub(command, hubCommand, action)
	} else {
		sendCmdtoCloud(command, hubCommand, action)
	}
}

private sendCmdtoCloud(command, hubCommand, action){
	def appServerUrl = getDataValue("appServerUrl")
	def deviceId = getDataValue("deviceId")
	def cmdResponse = parent.sendDeviceCmd(appServerUrl, deviceId, command)
	String cmdResp = cmdResponse.toString()
	if (cmdResp.substring(0,5) == "ERROR"){
		def errMsg = cmdResp.substring(7,cmdResp.length())
		log.error "${device.name} ${device.label}: ${errMsg}"
		sendEvent(name: "switch", value: "commsError", descriptionText: errMsg)
		sendEvent(name: "deviceError", value: errMsg)
		sendEvent(name: "DeviceWatch-DeviceStatus", value: "offline", displayed: false, isStateChange: true)
		action = ""
	} else {
		sendEvent(name: "DeviceWatch-DeviceStatus", value: "online", displayed: false, isStateChange: true)
		sendEvent(name: "deviceError", value: "OK")
	}
		actionDirector(action, cmdResponse)
}

private sendCmdtoHub(command, hubCommand, action){
	def headers = [:] 
	headers.put("HOST", "$gatewayIP:8082")	//	Same as on Hub.
	headers.put("tplink-iot-ip", deviceIP)
	headers.put("tplink-command", command)
	headers.put("action", action)
	headers.put("command", hubCommand)
	sendHubCommand(new physicalgraph.device.HubAction([
		headers: headers],
		device.deviceNetworkId,
		[callback: hubResponseParse]
	))
}

def hubResponseParse(response) {
	def action = response.headers["action"]
	def cmdResponse = parseJson(response.headers["cmd-response"])
	if (cmdResponse == "TcpTimeout") {
		log.error "$device.name $device.label: Communications Error"
		sendEvent(name: "switch", value: "offline", descriptionText: "ERROR at hubResponseParse TCP Timeout")
		sendEvent(name: "deviceError", value: "TCP Timeout in Hub")
		sendEvent(name: "DeviceWatch-DeviceStatus", value: "offline", displayed: false, isStateChange: true)
	} else {
		sendEvent(name: "deviceError", value: "OK")
		sendEvent(name: "DeviceWatch-DeviceStatus", value: "online", displayed: false, isStateChange: true)
		actionDirector(action, cmdResponse)
	}
}

def actionDirector(action, cmdResponse) {
	switch(action) {
		case "commandResponse":
			commandResponse(cmdResponse)
			break

		case "refreshResponse":
			refreshResponse(cmdResponse)
			break

		default:
			log.debug "Interface Error.  See SmartApp and Device error message."
	}
}

//	----- CHILD / PARENT INTERCHANGE TASKS -----
def syncAppServerUrl(newAppServerUrl) {
	updateDataValue("appServerUrl", newAppServerUrl)
		log.info "Updated appServerUrl for ${device.name} ${device.label}"
}