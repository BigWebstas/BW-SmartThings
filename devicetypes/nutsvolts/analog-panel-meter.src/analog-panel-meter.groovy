/**
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
	definition (name: "Analog Panel Meter", namespace: "NutsVolts", author: "John.Rucker@Solar-Current.com") {
	
    capability "Actuator"
    capability "Configuration"
    capability "Refresh"
	capability "Sensor"
	capability "Switch Level"
    
    attribute "info","string"
        
	fingerprint profileId: "0104", inClusters: "0000", outClusters: "0008"
	}

	// simulator metadata
	simulator {
	}
    
	preferences {
		input "zipCode", "text", title: "Zip Code (optional)", required: false
	}    

	// UI tile definitions
	tiles (scale: 2){

        valueTile("level1Text", "device.level", decoration: "flat",  inactiveLabel: false, width: 6, height: 2) {
        state defaultState: true, label:'Use slider to change value displayed on panel meter'
        }   

        controlTile("level1", "device.level", "slider", height: 2, width: 4, inactiveLabel: false, range:"(1..50)") {
        state "setLevel", label:"Meter Setting", action:"setLevel", backgroundColor:"#d04e00"
        }     
        
        valueTile("level1Value", "device.level", decoration: "flat",  inactiveLabel: false, width: 2, height: 2) {
        state defaultState: true, label:'${currentValue}'
        }      
        
		standardTile("refresh", "device.weather", decoration: "flat", width: 2, height: 2) {
			state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
		}        
        
		main(["level1Value"])
		details(["level1Text", "level1", "level1Value", "refresh"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
	//log.debug "parse called with --> $description"
	Map map = [:]
	if (description?.startsWith('catchall:')) {
		map = parseCatchAllMessage(description)
	}
	else if (description?.startsWith('read attr -')) {
		map = parseReportAttributeMessage(description)
	}  
    else {
    	log.debug "No parse method for: $description"
    }
    
    if (map.value == "Device Boot"){
    	def result = []
    	List cmds = bootResponse()
    	log.trace "Sending current state to device ${cmds}"
        result = cmds?.collect { new physicalgraph.device.HubAction(it) }  
        return result 
    }else{
    	log.trace map
		return map ? createEvent(map) : null
    }
}

private Map parseReportAttributeMessage(String description) {
	//log.debug "Map parseReportAttributeMessage called with --> $description"
    Map resultMap = [:]
    def descMap = parseDescriptionAsMap(description)
   
    if (descMap.cluster == "0008" && descMap.attrId == "0000") { 
        resultMap.name = "level"
        resultMap.value = (Integer.parseInt(descMap.value, 16))      
        resultMap.displayed = true  
    } 
    
    else {
    	log.debug "Attribute match not found for --> $descMap"
    }
    return resultMap
}

private Map parseCatchAllMessage(String description) {
	//log.debug "Map parseCatchAllMessage called with --> $description"
    Map resultMap = [:]    
    def cluster = zigbee.parse(description)
    
	if (cluster.clusterId == 0x0008 && cluster.command == 0x0B) {				// command 0x0B = default response to command sent
    	//log.trace "level Cluster default response = $cluster.data"  
        switch(cluster.data) {
        
        case "[0, 0]":															// Level command acknowledged  
        resultMap.name = "info"
        resultMap.value = "set level cmd ack"         
        resultMap.displayed = false        
        break                  
        }
   }    
    else {
    	log.debug "CatchAll match not found for --> $description"
        log.debug "cluster.data = $cluster"
    }        
    
    return resultMap
}

// Zigbee Commands to device
def setLevel(value) {
    if (value < 1){
        value = 1
    }else if (value > 50){
        value = 50
    }
    sendEvent(name: "level", value: value, displayed: false)    
    def level = hexString(Math.round(value))
    log.trace "New level 0x${level} sent"
	def cmds = []
	cmds << "st cmd 0x${device.deviceNetworkId} 0x38 8 0 {${level} 0000}"
	cmds
}

def configure() {
    log.debug "Binding SEP 0x38 DEP 0x01 Cluster 0x0008 Level cluster to hub"      
    
    def cmd = []
    cmd << "zdo bind 0x${device.deviceNetworkId} 0x38 0x01 0x0008 {${device.zigbeeId}} {}"   // Bind to end point 0x38 and the Level Cluster
    cmd << "delay 1500"       
    
    return cmd + refresh() // send refresh cmds as part of config
}

// Utility methods
def refresh() {    
	log.info "read attributes request sent"
    def cmd = []
    cmd << "st rattr 0x${device.deviceNetworkId} 0x38 0x0008 0x0000"						// Read Level attribute 
    return cmd      
}

def parseDescriptionAsMap(description) {
    (description - "read attr - ").split(",").inject([:]) { map, param ->
        def nameAndValue = param.split(":")
        map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
    }
}