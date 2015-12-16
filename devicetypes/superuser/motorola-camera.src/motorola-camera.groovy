preferences {
    input("ip", "text", title: "IP", description: "The IP Camera")
    } 
// for the UI
metadata {
  // Automatically generated. Make future change here.
  definition (name: "Motorola Camera", author: "Jwebstas") {
    // Change or define capabilities here as needed
    capability "Switch"
    capability "Refresh"
    capability "Polling"

    // Add commands as needed
    command "up"
    command "down"
	command "left"
	command "right"
  }

  simulator {
    // Nothing here, you could put some testing stuff here if you like
  }

  tiles (scale: 2){
    carouselTile("cameraDetails", "device.image", width: 3, height: 2) { }
    standardTile("camera", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
			state "default", label: "", action: "Image Capture.take", icon: "st.camera.dropcam-centered", backgroundColor: "#FFFFFF"
		}
    standardTile("left", "capability.momentary", width: 3, height: 2, title: "Move left", required: true, multiple: false){
			state "move", label: 'Left', action: "left", backgroundColor: "#79b821"
            def cmd = "move_left"
		}
    standardTile("right", "capability.momentary", width: 3, height: 2, title: "Move right", required: true, multiple: false){
			state "right", label: 'right', action: "right", backgroundColor: "#79b821"
		}
    standardTile("up", "capability.momentary", width: 3, height: 2, title: "Move up", required: true, multiple: false){
			state "up", label: 'up', action: "up", backgroundColor: "#79b821"
		}
    standardTile("down", "capability.momentary", width: 3, height: 2, title: "Move down", required: true, multiple: false){
			state "down", label: 'down', action: "down", backgroundColor: "#79b821"
		}
    standardTile("take", "device.image", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false, decoration: "flat") {
			state "take", label: "", action: "Image Capture.take", icon: "st.secondary.take", nextState:"taking"
		} 
    standardTile("main", "main", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false, decoration: "flat") {
			state "Main", label: "", icon: "st.camera.camera"
		} 
		main (["main"])	
		details(["left", "right", "up", "down"])
  }
}

def refresh() {
  log.debug "Executing 'refresh' which is actually poll()"
  poll()
}

def left() {
	log.debug "Executing 'left'"
    cmd("move_left")
}
def right() {
	log.debug "Executing 'right'"
    cmd("move_right")
}
def up() {
	log.debug "Executing 'up'"
    cmd("move_backward")
}
def down() {
	log.debug "Executing 'down'"
    cmd("move_forward")
}
def cmd(vars){
    log.debug "command recieved $vars" 
        new physicalgraph.device.HubAction(
        method: "GET",
        path: "/?action=command&command=$vars",
        headers: [
            HOST: "$ip:80"
        ]
    )
}


def poll() {
	log.debug "Executing 'poll'"
	api('status' []) {
	}
}