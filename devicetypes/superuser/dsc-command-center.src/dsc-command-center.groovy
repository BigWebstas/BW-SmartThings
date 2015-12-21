/**
 *  DSC Command Center
 *
 *  Author: David Cauthron
 *  Also Attributed:  JTT-AE <aesystems@gmail.com>
 *                    Rob Fisher <robfish@att.net>
 *					  Carlos Santiago <carloss66@gmail.com> 
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
	definition (name: "DSC Command Center", namespace: "Alarm", author: "David Cauthron") {
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

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def arm() {
	log.debug "Executing 'arm'"
    sendEvent (name: "switch", value: "arm")
}

def stayarm() {
	log.debug "Executing 'stayarm'"
    sendEvent (name: "switch", value: "stayarm")
}

def disarm() {
	log.debug "Executing 'disarm'"
    sendEvent (name: "switch", value: "disarm")
}

