/*
 *  DSC Panel
 *
 *  Author: Kent Holloway <drizit@gmail.com>
 *  Date: 2014-03-20
 *  Modified by BigWebstas
 *	
 *	Version
 *	1.0 - added switch state code to help with the dup disarms
 *	
 */
preferences {
    input("ip", "text", title: "IP", description: "The IP of your alarmserver")
    input("port", "text", title: "Port", description: "The port")
    input("pincode", "text", title: "Pincode", description: "Alarm Pincode")
	} 
// for the UI
metadata {
  // Automatically generated. Make future change here.
  definition (name: "DSC Single Panel", namespace: "Alarm", author: "Kent Holloway <drizit@gmail.com>") {
    // Change or define capabilities here as needed
    capability "Switch"
    capability "Refresh"
    capability "Polling"

    // Add commands as needed
    command "partition"
    command "stayarm"
	command "arm"
	command "disarm"
  }

  simulator {
    // Nothing here, you could put some testing stuff here if you like
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
		details(["dscpartition", "arm", "stayarm", "staybutton", "disarm"])
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
    if (state != "notready") { //attempt at fixing the duplicate disarm when using the physical panel
		sendEvent (name: "switch", value: "${state}")
    } else { sendEvent (name: "switch", value: "ready") }
}

def refresh() {
  log.debug "Executing 'refresh' which is actually poll()"
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
    sendEvent (name: "switch", value: "stayarm")
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
    sendEvent (name: "switch", value: "arm")
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
    sendEvent (name: "switch", value: "disarm")
    return result
}

def poll() {
	log.debug "Executing 'poll'"
	api('status' []) {
	}
}