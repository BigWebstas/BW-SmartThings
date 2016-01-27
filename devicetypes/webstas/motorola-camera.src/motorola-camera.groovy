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
  definition (name: "Motorola Camera", namespace: "Webstas", author: "Jwebstas") {
    capability "Image Capture"
    capability "Sensor"
    capability "Actuator"
    capability "Polling"
    capability "Temperature Measurement"
    capability "Signal Strength"
    capability "Refresh"

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
      state "default", label: 'reboot', action: "reboot", icon: "", backgroundColor: "#ffffff" 
    }
    standardTile("beep", "capability.momentary", width: 2, height: 1, title: "beep", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'beeep', action: "beep", icon: "", backgroundColor: "#ffffff" 
    }
    standardTile("beepoff", "capability.momentary", width: 2, height: 1, title: "beepoff", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'beep off', action: "beepoff", icon: "", backgroundColor: "#ffffff" 
    }    
	standardTile("bup", "capability.momentary", width: 2, height: 1, title: "bup", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'Brightness -', action: "bup", icon: "", backgroundColor: "#ffffff" 
    }
	standardTile("bdown", "capability.momentary", width: 2, height: 1, title: "bdown", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'Brightness +', action: "bdown", icon: "", backgroundColor: "#ffffff" 
    }    
	standardTile("cdown", "capability.momentary", width: 3, height: 1, title: "cdown", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'contrast -', action: "cdown", icon: "", backgroundColor: "#ffffff" 
    }
	standardTile("cup", "capability.momentary", width: 3, height: 1, title: "cup", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'Contrast +', action: "cup", icon: "", backgroundColor: "#ffffff" 
    }
    standardTile("refresh", "refresh", width: 2, height: 1, title: "refresh", inactiveLabel: true, canChangeBackground: false, decoration: "flat"){
      state "default", label: 'refresh', action: "refresh", icon: "", backgroundColor: "#ffffff" 
    }
    valueTile("lqi", "device.lqi", width: 2, height: 1, decoration: "flat", inactiveLabel: false) {
			state "lqi", label:'Wifi ${currentValue}%', unit:""
        }
    valueTile("temperature", "device.temperature", width: 2, height: 1, decoration: "flat") {
            state "temp", label:'Temp ${currentValue}°', unit:"F", icon: "", backgroundColor: "#ffffff"
        }
    standardTile("take", "device.image", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
      state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
      state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
      state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
    } 
    valueTile("main", "device.temperature", width: 2, height: 2, decoration: "flat") {
            state "temp", label:'${currentValue}°', unit:"F", icon: "st.camera.camera", backgroundColor: "#ffffff"
        }
    main (["main"]) 
    details(["cameraDetails", "take", "temperature", "lqi", "left", "right", "up", "down", "mel1", "mel2", "mel3", "mel4", "mel5", "meloff", "bup", "refresh", "bdown", "cdown", "cup", "reboot", "beep", "beepoff"])
  }
}
//commands to camera
def left() { cmd("move_left") }
def right() { cmd("move_right") }
def up() { cmd("move_backward") }
def down() { cmd("move_forward") }
def mel1() { cmd("melody1") }
def mel2() { cmd("melody2") }
def mel3() { cmd("melody3") }
def mel4() { cmd("melody4") }
def mel5() { cmd("melody5") }
def meloff() { cmd("melodystop") }
def reboot() { cmd("restart_system") }
def beep() { cmd("beeper_en&setup=1000100000000000") }
def beepoff() { cmd("beeper_dis") }
def bup() { cmd("plus_brightness") }
def bdown() { cmd("minus_brightness") }
def cdown() { cmd("minus_contrast") }
def cup() { cmd("plus_contrast") }

//parser
def parse(String description) {
  log.debug("Parsediddlyarsing '${description}'")

  def map = stringToMap(description)

  if (map.bucket && map.key) { //got a s3 pointer
    putImageInS3(map)
  }
  else{

    def header = new String(map.headers.decodeBase64())
    def body = new String(map.body.decodeBase64())
        
    if( body.contains('value_temperature')) {	 
    	body = body.replaceAll("[^\\d.]", "")
        converttemp(body)
    }
    if( body.contains('get_wifi_strength')) {
    	body = body.replaceAll("[^\\d.]", "")
        sendEvent(name: "lqi", value: body)
    }
  }
  
}

//camera command interpreter
def cmd(vars){
    log.debug "Executing command $vars"
        new physicalgraph.device.HubAction(
        method: "GET",
        path: "/?action=command&command=$vars",
        headers: [
            HOST: "$ip:80"
        ]
    )
}

//convert C to F
def converttemp(cinput) {
	cinput = celsiusToFahrenheit(cinput.toDouble())
    sendEvent(name: "temperature", value: String.format("%.1f", cinput))
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
    delayBetween([
        cmd("value_temperature"),
        cmd("get_wifi_strength")
    ], 500)
}

def poll() {
  log.debug "Executing 'poll'"
    delayBetween([
        cmd("value_temperature"),
        cmd("get_wifi_strength"),
        take()
    ], 200)
}
