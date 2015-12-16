preferences {
    input("ip", "text", title: "IP", description: "Camera IP address", required: true, displayDuringSetup: true)
 
    } 
// for the UI
metadata {
  // Automatically generated. Make future change here.
  definition (name: "Motorola Camera", author: "Jwebstas") {
    // Change or define capabilities here as needed
    capability "Image Capture"
    capability "Sensor"
    capability "Actuator"
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

    standardTile("blank", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "blank", label: "", action: "", icon: "", backgroundColor: "#FFFFFF"
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
    standardTile("take", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
      state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
      state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
      state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
    }
 
    standardTile("main", "main", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false, decoration: "flat") {
			state "Main", label: "", icon: "http://a2.mzstatic.com/us/r30/Purple69/v4/f2/33/c6/f233c68e-c4c5-85d1-0e10-8d7acf9664ea/icon175x175.png"
		} 
		main (["main"])	
		details(["cameraDetails", "blank", "left", "right", "up", "down"])
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
def take() {
  
      def hubAction = new physicalgraph.device.HubAction(
      method: "GET",
      path: "/cgi-bin/jpg/image.cgi",
      headers: [
          HOST: "$ip:80"
        )
          
    hubAction.options = [outputMsgToS3:true]
    log.debug hubAction
    hubAction
    }
    catch (Exception e) {
      log.debug "Hit Exception $e on $hubAction"
    }
    
}
def putImageInS3(map) {
  log.debug "firing s3"
    def s3ObjectContent
    try {
        def imageBytes = getS3Object(map.bucket, map.key + ".jpg")
        if(imageBytes)
        {
            s3ObjectContent = imageBytes.getObjectContent()
            def bytes = new ByteArrayInputStream(s3ObjectContent.bytes)
            storeImage(getPictureName(), bytes)
        }
    }
    catch(Exception e) {
        log.error e
    }
  finally {
    //Explicitly close the stream
    if (s3ObjectContent) { s3ObjectContent.close() }
  }
}

def parseDescriptionAsMap(description) {
description.split(",").inject([:]) { map, param ->
def nameAndValue = param.split(":")
map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
}
}

private getPictureName() {
  def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
    log.debug pictureUuid
    def picName = device.deviceNetworkId.replaceAll(':', '') + "_$pictureUuid" + ".jpg"
  return picName
}


def poll() {
	log.debug "Executing 'poll'"
	api('status' []) {
	}
}