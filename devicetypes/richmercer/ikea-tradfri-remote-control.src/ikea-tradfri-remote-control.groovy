import physicalgraph.zigbee.zcl.DataType

metadata {
    definition (name: "IKEA TRADFRI Remote control", namespace: "richmercer", author: "Richard Mercer") {
        capability "Battery"
        capability "Configuration"
        capability "Refresh"
                
        command "enrollResponse"

        fingerprint profileId: "0104", inClusters: "0000, 0001, 0003, 0009, 0B05, 1000", outClusters: "0003, 0004, 0005, 0006, 0008, 0019, 1000", manufacturer: "IKEA of Sweden", model: "TRADFRI remote control", deviceJoinName: "TRADFRI remote control"
        
    }

    preferences {
        section {
            
        }
    }

    tiles {
        valueTile("battery", "device.battery", decoration: "flat", inactiveLabel: false) {
            state "battery", label:'${currentValue}% battery', unit:""
        }

        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat") {
            state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
        }
        main (["battery"])
        details(["battery", "refresh"])
    }
}

def parse(String description) {
    log.debug "description is $description"
    def event = zigbee.getEvent(description)
    if (event) {
        sendEvent(event)
    }
    else {
        if ((description?.startsWith("catchall:")) || (description?.startsWith("read attr -"))) {
            def descMap = zigbee.parseDescriptionAsMap(description)
            if (descMap.clusterInt == 0x0001 && descMap.attrInt == 0x0020) {
                event = getBatteryResult(zigbee.convertHexToInt(descMap.value))
            }
            else if (descMap.clusterInt == 0x0006 || descMap.clusterInt == 0x0008) {
                event = parseNonIasButtonMessage(descMap)
            }
        }
        else if (description?.startsWith('zone status')) {
            event = parseIasButtonMessage(description)
        }

        log.debug "Parse returned $event"
        def result = event ? createEvent(event) : []

        if (description?.startsWith('enroll request')) {
            List cmds = zigbee.enrollResponse()
            result = cmds?.collect { new physicalgraph.device.HubAction(it) }
        }
        return result
    }
}

private Map parseIasButtonMessage(String description) {
    def zs = zigbee.parseZoneStatus(description)
    return zs.isAlarm2Set() ? getButtonResult("press") : getButtonResult("release")
}

private Map getBatteryResult(rawValue) {
    log.debug 'Battery'
    def volts = rawValue / 10
    if (volts > 3.0 || volts == 0 || rawValue == 0xFF) {
        return [:]
    }
    else {
        def result = [
                name: 'battery'
        ]
        def minVolts = 2.1
        def maxVolts = 3.0
        def pct = (volts - minVolts) / (maxVolts - minVolts)
        result.value = Math.min(100, (int) pct * 100)
        def linkText = getLinkText(device)
        result.descriptionText = "${linkText} battery was ${result.value}%"
        return result
    }
}

private Map parseNonIasButtonMessage(Map descMap){
    def buttonState = ""
    def buttonNumber = 0
    if ((device.getDataValue("model") == "3460-L") &&(descMap.clusterInt == 0x0006)) {
        if (descMap.commandInt == 1) {
            getButtonResult("press")
        }
        else if (descMap.commandInt == 0) {
            getButtonResult("release")
        }
    }
    else if ((device.getDataValue("model") == "3450-L") && (descMap.clusterInt == 0x0006)) {
        if (descMap.commandInt == 1) {
            getButtonResult("press")
        }
        else if (descMap.commandInt == 0) {
            def button = 1
            switch(descMap.sourceEndpoint) {
                case "01":
                    button = 4
                    break
                case "02":
                    button = 3
                    break
                case "03":
                    button = 1
                    break
                case "04":
                    button = 2
                    break
            }
        
            getButtonResult("release", button)
        }
    }
    else if (descMap.clusterInt == 0x0006) {
        buttonState = "pushed"
        if (descMap.command == "01") {
            buttonNumber = 1
        }
        else if (descMap.command == "00") {
            buttonNumber = 2
        }
        if (buttonNumber !=0) {
            def descriptionText = "$device.displayName button $buttonNumber was $buttonState"
            return createEvent(name: "button", value: buttonState, data: [buttonNumber: buttonNumber], descriptionText: descriptionText, isStateChange: true)
        }
        else {
            return [:]
        }
    }
    else if (descMap.clusterInt == 0x0008) {
        if (descMap.command == "05") {
            state.buttonNumber = 1
            getButtonResult("press", 1)
        }
        else if (descMap.command == "01") {
            state.buttonNumber = 2
            getButtonResult("press", 2)
        }
        else if (descMap.command == "03") {
            getButtonResult("release", state.buttonNumber)
        }
    }
}

def refresh() {
    log.debug "Refreshing Battery"

    return zigbee.readAttribute(zigbee.POWER_CONFIGURATION_CLUSTER, 0x20) +
            zigbee.enrollResponse()
}

def configure() {
    log.debug "Configuring Reporting, IAS CIE, and Bindings."
    def cmds = []
    if (device.getDataValue("model") == "3450-L") {
        cmds << [
                "zdo bind 0x${device.deviceNetworkId} 1 1 6 {${device.zigbeeId}} {}", "delay 300",
                "zdo bind 0x${device.deviceNetworkId} 2 1 6 {${device.zigbeeId}} {}", "delay 300",
                "zdo bind 0x${device.deviceNetworkId} 3 1 6 {${device.zigbeeId}} {}", "delay 300",
                "zdo bind 0x${device.deviceNetworkId} 4 1 6 {${device.zigbeeId}} {}", "delay 300"
        ]
    }
    
    return zigbee.onOffConfig() +
            zigbee.levelConfig() +
            zigbee.configureReporting(zigbee.POWER_CONFIGURATION_CLUSTER, 0x20, DataType.UINT8, 30, 21600, 0x01) +
            zigbee.enrollResponse() +
            zigbee.readAttribute(zigbee.POWER_CONFIGURATION_CLUSTER, 0x20) +
            cmds

}

private Map getButtonResult(buttonState, buttonNumber = 1) {
    if (buttonState == 'release') {
        log.debug "Button was value : $buttonState"
        def timeDiff = now() - state.pressTime
        log.info "timeDiff: $timeDiff"
        def holdPreference = holdTime ?: 1
        log.info "holdp1 : $holdPreference"
        holdPreference = (holdPreference as int) * 1000
        log.info "holdp2 : $holdPreference"
        if (timeDiff > 10000) {         //timeDiff>10sec check for refresh sending release value causing actions to be executed
            return [:]
        }
        else {
            if (timeDiff < holdPreference) {
                buttonState = "pushed"
            }
            else {
                buttonState = "held"
            }
            def descriptionText = "$device.displayName button $buttonNumber was $buttonState"
            return createEvent(name: "button", value: buttonState, data: [buttonNumber: buttonNumber], descriptionText: descriptionText, isStateChange: true)
        }
    }
    else if (buttonState == 'press') {
        log.debug "Button was value : $buttonState"
        state.pressTime = now()
        log.info "presstime: ${state.pressTime}"
        return [:]
    }
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
    sendEvent(name: "numberOfButtons", value: 5)
}