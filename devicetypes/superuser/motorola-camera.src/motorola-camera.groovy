preferences {
    input("ip", "text", title: "IP", description: "Camera IP address", required: true, displayDuringSetup: true)
    } 
    
metadata {
  definition (name: "Motorola Camera", author: "Jwebstas") {
    capability "Image Capture"
    capability "Sensor"
    capability "Actuator"
    capability "Polling"

    command "up"
    command "down"
    command "left"
    command "right"
    command "take"

  }

  simulator {
  }

  tiles (scale: 2){
    carouselTile("cameraDetails", "device.image", width: 6, height: 4) { }

    standardTile("blank", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
      state "blank", label: "", action: "", icon: "", backgroundColor: "#FFFFFF"
    }
    standardTile("left", "capability.momentary", width: 1, height: 1, title: "Move left", required: true, multiple: false, decoration: "flat"){
      state "move", label: '', action: "left", icon: "http://dangerzone.biz/wp-content/uploads/2015/12/Left_2.png", backgroundColor: "#ffffff"
    }
    standardTile("right", "capability.momentary", width: 1, height: 1, title: "Move right", required: true, multiple: false, decoration: "flat"){
      state "right", label: '', action: "right", icon: "http://dangerzone.biz/wp-content/uploads/2015/12/Right_2.png", backgroundColor: "#ffffff"
    }
    standardTile("up", "capability.momentary", width: 1, height: 1, title: "Move up", required: true, multiple: false, decoration: "flat"){
      state "up", label: '', action: "up", icon: "http://dangerzone.biz/wp-content/uploads/2015/12/Top_2.png", backgroundColor: "#ffffff"
    }
    standardTile("down", "capability.momentary", width: 1, height: 1, title: "Move down", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: '', action: "down", icon: "http://dangerzone.biz/wp-content/uploads/2015/12/Bottom_2.png", backgroundColor: "#ffffff" 
    }
    valueTile("temp", "device.temperature", width: 2, height: 2) {
            state "temp", label:'${currentValue}°', unit:"F", icon: "st.Weather.weather2",
                backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
        }
    standardTile("take", "device.image", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
      state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
      state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
      state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
    }
 
    standardTile("main", "main", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false, decoration: "flat") {
      state "Main", label: "", icon: "http://a2.mzstatic.com/us/r30/Purple69/v4/f2/33/c6/f233c68e-c4c5-85d1-0e10-8d7acf9664ea/icon175x175.png"
    } 
    main (["main"]) 
    details(["cameraDetails", "take", "temp", "left", "right", "up", "down" ])
  }
}

def left() {
  log.debug "Executing 'left'"
    cmd("move_left")
}
def right() {
  log.debug "Executing 'right'"
  	runIn(6, take)
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
//camera command interpreter
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

def parse(String description) {
    log.debug "Parsing '${description}'"
    def map = [:]
  def retResult = []
  def descMap = parseDescriptionAsMap(description)
  //Image
  if (descMap["bucket"] && descMap["key"]) {
    putImageInS3(descMap)
  } 
}
//get bits from camera and proceed 
def take() {
  
    def hubAction = new physicalgraph.device.HubAction(
      method: "GET",
      path: "/cgi-bin/jpg/image.cgi",
      headers: [
          HOST: "$ip:80"
          ]
        )
          
    hubAction.options = [outputMsgToS3:true]
    log.debug hubAction
    hubAction
}
//send image to AMZ S3 bucket     
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
//create a name for the image
private getPictureName() {
  def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
    log.debug pictureUuid
    def picName = ip + "_$pictureUuid" + ".jpg"
  return picName
}

def poll() {
  log.debug "Executing 'poll'"
  take()
}