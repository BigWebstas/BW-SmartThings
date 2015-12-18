/**
 *  Copyright 2015 BigWebstas
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
 * The "Take" functionality borrowed from patrick@patrickstuart.com  -  Generic Camera Device v1.0.07102014
 *  
 */
 
preferences {
    input("ip", "text", title: "IP", description: "Camera IP address", required: true, displayDuringSetup: true)
    } 
    
metadata {
  definition (name: "Motorola Camera", author: "Jwebstas") {
    capability "Image Capture"
    capability "Sensor"
    capability "Actuator"
    capability "Polling"
    capability "Temperature Measurement"

    command "up"
    command "down"
    command "left"
    command "right"
    command "take"
    command "mel1"
    command "mel2"
    command "mel3"
    command "mel4"
    command "mel5"
    command "meloff"
    command "reboot"
    command "beep"
    command "beepoff"
    command "bup"
    command "bdown"
    command "cup"
    command "cdown"
   
  }

  simulator {
  }

  tiles (scale: 2){
    carouselTile("cameraDetails", "device.image", width: 6, height: 4) { }
    
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
    standardTile("mel1", "capability.momentary", width: 1, height: 1, title: "mel1", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'mel1', action: "mel1", icon: "st.Electronics.electronics13", backgroundColor: "#ffffff" 
    }
    standardTile("mel2", "capability.momentary", width: 1, height: 1, title: "mel2", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'mel2', action: "mel2", icon: "st.Electronics.electronics13", backgroundColor: "#ffffff" 
    }
    standardTile("mel3", "capability.momentary", width: 1, height: 1, title: "mel3", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'mel3', action: "mel3", icon: "st.Electronics.electronics13", backgroundColor: "#ffffff" 
    }
    standardTile("mel4", "capability.momentary", width: 1, height: 1, title: "mel4", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'mel4', action: "mel4", icon: "st.Electronics.electronics13", backgroundColor: "#ffffff" 
    }
    standardTile("mel5", "capability.momentary", width: 1, height: 1, title: "mel5", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'mel5', action: "mel5", icon: "st.Electronics.electronics13", backgroundColor: "#ffffff" 
    }
    standardTile("meloff", "capability.momentary", width: 1, height: 1, title: "meloff", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'meloff', action: "meloff", icon: "st.Electronics.electronics13", backgroundColor: "#ffffff" 
    }
    standardTile("reboot", "capability.momentary", width: 2, height: 1, title: "reboot", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'reboot', action: "", icon: "", backgroundColor: "#ffffff" 
    }
    standardTile("beep", "capability.momentary", width: 2, height: 1, title: "beep", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'beeep', action: "beep", icon: "", backgroundColor: "#ffffff" 
    }
    standardTile("beepoff", "capability.momentary", width: 2, height: 1, title: "beepoff", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'beep off', action: "beepoff", icon: "", backgroundColor: "#ffffff" 
    }    
	standardTile("bup", "capability.momentary", width: 3, height: 1, title: "bup", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'Brightness -', action: "bup", icon: "", backgroundColor: "#ffffff" 
    }
	standardTile("bdown", "capability.momentary", width: 3, height: 1, title: "bdown", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'Brightness +', action: "bdown", icon: "", backgroundColor: "#ffffff" 
    }    
	standardTile("cdown", "capability.momentary", width: 3, height: 1, title: "cdown", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'contrast -', action: "cdown", icon: "", backgroundColor: "#ffffff" 
    }
	standardTile("cup", "capability.momentary", width: 3, height: 1, title: "cup", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'Contrast +', action: "cup", icon: "", backgroundColor: "#ffffff" 
    }
    valueTile("wifi",  "device.wifi", width: 3, height: 1, decoration: "flat"){
    	state "default", label: 'Wifi ${currentValue}'
    }
    valueTile("temperature", "device.temperature", width: 2, height: 2, decoration: "flat") {
            state "default", label:'temp ${currentValue}°', unit:"F", icon: "st.Weather.weather2",
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
    details(["cameraDetails", "take", "temperature", "wifi", "left", "right", "up", "down", "mel1", "mel2", "mel3", "mel4", "mel5", "meloff", "bup", "bdown", "cdown", "cup", "reboot", "beep", "beepoff"])
  }
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
def mel1() {
  log.debug "Executing 'melody 1'"
    cmd("melody1")
}
def mel2() {
  log.debug "Executing 'melody2'"
    cmd("melody2")
}
def mel3() {
  log.debug "Executing 'melody3'"
    cmd("melody3")
}
def mel4() {
  log.debug "Executing 'melody4'"
    cmd("melody4")
}
def mel5() {
  log.debug "Executing 'melody5'"
    cmd("melody5")
}
def meloff() {
  log.debug "Executing 'melody stop'"
    cmd("melodystop")
}
def reboot() {
  log.debug "Executing 'reboot'"
    cmd("restart_system")
}
def beep() {
  log.debug "Executing 'beep on'"
    cmd("beeper_en&setup=1000100000000000")
}
def beepoff() {
  log.debug "Executing 'Beep off'"
    cmd("beeper_dis")
}
def bup() {
  log.debug "Executing 'B+'"
    cmd("plus_brightness")
}
def bdown() {
  log.debug "Executing 'B-'"
    cmd("minus_brightness")
}
def cdown() {
  log.debug "Executing 'C-'"
    cmd("minus_contrast")
}
def cup() {
  log.debug "Executing 'C+'"
    cmd("plus_contrast")
}

//parser
def parse(String description) {
    log.debug "Parsing '${description}'"
    def map = [:]
  def retResult = []
  def descMap = parseDescriptionAsMap(description)
  //Image
  if (descMap["bucket"] && descMap["key"]) {
    putImageInS3(descMap)
  }
  if (descMap["get_wifi_strength"]) {
	updateWifiTile(descMap)
  }
  if (descMap["value_temperature"]) {
	updateTempTile(descMap)
  } 
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

def updateWifiTile(map) {
def value = map.get_Wifi_Strength?.isInteger() ? map.get_Wifi_Strength.toInteger() : 0
if (value > 0) {
sendEvent(name: "wifi", value: value)
}
}

def updateTempTile(map) {
def value = map.value_temperature?.isDouble() ? map.value_temperature.toDouble() : 0
if (value > 0) {
sendEvent(name: "temperature", value: value)
}
}

//Camera functionality provided by patrick@patrickstuart.com Thanks!
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

def refresh() {
	log.debug "Refreshing"
	poll()
}

def poll() {
  log.debug "Executing 'poll'"
	def cmds = []
	cmds << take()
	cmds << cmd("value_temperature")
	cmds << cmd("get_wifi_strength")
	cmds
}
