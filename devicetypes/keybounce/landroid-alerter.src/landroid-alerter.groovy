/**
 *  LANdroid Alerter
 *
 *  Requires the Android TTSService (aka LANdroid in the Play Store; https://play.google.com/store/apps/details?id=com.keybounce.ttsservice )
 *
 *  Copyright 2015 Tony McNamara
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

metadata {
    definition (name: "LANdroid Alerter", namespace: "KeyBounce", author: "Tony McNamara") {
        capability "Alarm"
        capability "Speech Synthesis"
        capability "Notification"
        capability "Tone"
        attribute  "LANdroidSMS","string"
    }
    preferences {
        input("DeviceLocalLan", "string", title:"Android IP Address", description:"Please enter your tablet's I.P. address", defaultValue:"" , required: false, displayDuringSetup: true)
        input("DevicePort", "string", title:"Android Port", description:"Port the Android device listens on", defaultValue:"1035", required: false, displayDuringSetup: true)
        input("ReplyOnEmpty", "bool", title:"Say Nothing", description:"When no speech is found, announce LANdroid?  (Needed for the speech and notify tiles to work)", defaultValue: true, displayDuringSetup: true)
    }

    simulator {
        
    }

    tiles {
        standardTile("alarm", "device.alarm", width: 2, height: 2) {
            state "off", label:'off', action:'alarm.both', icon:"st.alarm.alarm.alarm", backgroundColor:"#ffffff"
            state "strobe", label:'strobe!', action:'alarm.off', icon:"st.Lighting.light11", backgroundColor:"#e86d13"
            state "siren", label:'siren!', action:'alarm.off', icon:"st.alarm.alarm.alarm", backgroundColor:"#e86d13"
            state "both", label:'alarm!', action:'alarm.off', icon:"st.alarm.alarm.alarm", backgroundColor:"#e86d13"
        }

        standardTile("strobe", "device.alarm", inactiveLabel: false, decoration: "flat") {
            state "default", label:'', action:"alarm.strobe", icon:"st.secondary.strobe"
        }
        standardTile("siren", "device.alarm", inactiveLabel: false, decoration: "flat") {
            state "default", label:'', action:"alarm.siren", icon:"st.secondary.siren"
        }       

        standardTile("speak", "device.speech", inactiveLabel: false, decoration: "flat") {
            state "default", label:'Speak', action:"Speech Synthesis.speak", icon:"st.Electronics.electronics13"
        }
        standardTile("toast", "device.notification", inactiveLabel: false, decoration: "flat") {
            state "default", label:'Notify', action:"notification.deviceNotification", icon:"st.Kids.kids1"
        }
        standardTile("beep", "device.tone", inactiveLabel: false, decoration: "flat") {
            state "default", label:'Tone', action:"tone.beep", icon:"st.Entertainment.entertainment2"
        }       

        main "alarm"
        details(["alarm","strobe","siren","speak","toast","beep"])
    }
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
    // TODO: handle 'alarm' attribute

}

// handle commands
def off() {
    log.debug "Executing 'off'"
    // TODO: handle 'off' command
}

def strobe() {
    log.debug "Executing 'strobe'"
    // TODO: handle 'strobe' command
    def command="&FLASH=STROBE&"+getDoneString()
    sendCommands(command)

}

def siren() {
    log.debug "Executing 'siren'"
    // TODO: handle 'siren' command
    def command="&ALARM=SIREN&"+getDoneString()
    sendCommands(command)
}

def beep() {
    log.debug "Executing 'beep'"
    // TODO: handle 'siren' command
    def command="&ALARM=CHIME&"+getDoneString()
    sendCommands(command)
}

def both() {
    log.debug "Executing 'both'"
    // TODO: handle 'both' command
    def command="&ALARM=ON&FLASH=ON&"+getDoneString()
    sendCommands(command)
}


def speak(toSay) {
    log.debug "Executing 'speak'"
    if (!toSay?.trim()) {
        if (ReplyOnEmpty) {
            toSay = "Landroid Speech Synthesizer"
        }
    }

    if (toSay?.trim()) {
        def command="&SPEAK="+toSay+"&"+getDoneString()
        sendCommands(command)
    }
}

def deviceNotification(toToast) {
    log.debug "Executing notification with "+toToast
    if (!toToast?.trim()) {
        if (ReplyOnEmpty) {
            toToast = "Landroid Speech Synthesizer"
        }
    }
    if (toToast?.trim()) {
        def command="&TOAST="+toToast+"&"+getDoneString()
        sendCommands(command)
    }
}    

/* Send to IP and to SMS as appropriate */
private sendCommands(command) {
    log.debug "Command request: "+command
    sendSMSCommand(command)
    sendIPCommand(command)
}

private sendIPCommand(commandString ) {
    log.debug "Sending command to "+DeviceLocalLan
    if (DeviceLocalLan?.trim()) {
        def host = DeviceLocalLan 
        def hosthex = convertIPtoHex(host)
        def porthex = convertPortToHex(DevicePort)
        device.deviceNetworkId = "$hosthex:$porthex" 

        def headers = [:] 
        headers.put("HOST", "$host:$DevicePort")

        def method = "GET"

        def hubAction = new physicalgraph.device.HubAction([
            method: method,
            path: "/"+commandString,
            headers: headers],
            device.deviceNetworkId
            )
        hubAction
    }
}

private sendSMSCommand(commandString) {
    def preface = "+@TTSSMS@+"
    def smsValue = preface+"&"+commandString
    state.lastsmscommand = smsValue
    sendEvent(name: "LANdroidSMS", value: smsValue, isStateChange: true)
    /*
    if (SMSPhone?.trim()) {
        sendSmsMessage(SMSPhone, preface+"&"+commandString)
    }
    */
}

private String getDoneString() {
    return "@DONE@"
}


private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
    return hex

}

private String convertPortToHex(port) {
    String hexport = port.toString().format( '%04x', port.toInteger() )
    log.debug hexport
    return hexport
}
