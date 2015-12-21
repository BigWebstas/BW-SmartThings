/**
 *  Alarm Control Center
 *
 *  Author: JTT <aesystems@gmail.com>
 *  Original Code By: Rob Fisher <robfish@att.net> and Carlos Santiago <carloss66@gmail.com> 
 *  Date: 2015-11-26
 */
 // for the UI

preferences {
    input("ip", "text", title: "IP", description: "The IP of your alarmserver")
    input("port", "text", title: "Port", description: "The port")
    input("pincode", "text", title: "Pincode", description: "Alarm Pincode")
	} 
 
metadata {
	// Automatically generated. Make future change here.
	definition (name: "Alarm Control Center", namespace: "Alarm", author: "JTT <aesystems@gmail.com>") {
		capability "Switch"
		capability "polling"
		
		command "stayarm"
		command "arm"
		command "disarm"
		}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles(scale: 2) {
		multiAttributeTile(name:"status", type: "generic", width: 6, height: 4){
     	   tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "disarm", label:'Disarmed - Ready', icon:"st.security.alarm.off", backgroundColor:"#79b821"
				attributeState "arm", label:'Armed - Away', icon:"st.security.alarm.on", backgroundColor:"#800000"
				attributeState "stayarm", label:'Armed - Stay', icon:"st.security.alarm.on", backgroundColor:"#008CC1"
			}
		}		
		standardTile("disarm", "capability.momentary", width: 2, height: 2, title: "Disarm", required: true, multiple: false){
			state "disarm", label: 'Disarm', action: "disarm", icon: "st.Home.home4", backgroundColor: "#79b821"
		}

		standardTile("arm", "capability.momentary", width: 2, height: 2, title: "Arm", required: true, multiple: false){
			state "arm", label: 'Arm', action: "arm", icon: "st.Home.home4", backgroundColor: "#800000"
       }
		standardTile("stayarm", "capability.momentary", width: 2, height: 2, title: "Armed Stay", required: true, multiple: false){
			state "stayarm", label: 'Arm - Stay', action: "stayarm", icon: "st.Home.home4", backgroundColor: "#008CC1"
       }

		main (["status", "arm", "stayarm", "staybutton", "disarm"])	
		details(["status", "arm", "stayarm", "staybutton", "disarm"])	

	}
}

def parse(String description) {
}

def stayarm() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/stayarm",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
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
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to arm received"
    //log.debug "arm"
    sendEvent (name: "switch", value: "arm")
    return result
}


def on() {
}

def off() {
}



def disarm() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/disarm",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
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

def api(method, args = [], success = {}) {

}
