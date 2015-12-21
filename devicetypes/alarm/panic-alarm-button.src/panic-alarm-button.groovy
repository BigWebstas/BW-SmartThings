/**
 *  DSC Panic Alarm Button
 *
 *  Author: Carlos Santiago <carloss66@gmail.com>
 *  Original Code By: Rob Fisher <robfish@att.net>
 *  Date: 06/29/2015
 */
 // for the UI

preferences {
    input("ip", "text", title: "IP", description: "The IP of your AlarmServer")
    input("port", "text", title: "Port", description: "The port")
} 
 
metadata {
	// Automatically generated. Make future change here.
	definition (name: "Panic Alarm Button", namespace: "Alarm", author: "Carlos Santiago <carloss66@gmail.com>") {
		capability "Switch"
/*        capability "Refresh" */
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 1, height: 1, canChangeIcon: true) {
			state "off", label: 'Panic', action: "switch.on", icon: "st.alarm.beep.beep", backgroundColor: "#79b821"
			state "on", label: 'Active', action: "switch.off", icon: "st.alarm.beep.beep", backgroundColor: "#800000"
    }
/*        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
    
    } */
		main "button"
        details(["button"])
        /*details(["button", "refresh"]) */
	}
}

def parse(String description) {
}

def on() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/alarm/panic",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to trigger panic alarm received"
    //log.debug "panic alarm"
    sendEvent (name: "switch", value: "on")
    return result
}

def off() {
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
    sendEvent (name: "switch", value: "off")
    return result
}

/*def refresh() {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/api/refresh",
        headers: [
            HOST: "$ip:$port"
            //HOST: getHostAddress()
        ]
    )
    log.debug "response" : "Request to refresh received"
    //log.debug "refresh"
    sendEvent (name: "switch", value: "refresh")
    return result
}
*/