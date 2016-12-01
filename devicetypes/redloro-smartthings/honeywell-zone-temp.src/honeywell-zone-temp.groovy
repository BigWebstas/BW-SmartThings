/**
 *  SmartThings Device Handler: Honeywell Zone water
 *
 *  Author: redloro@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
  definition (name: "Honeywell Zone Temp", namespace: "redloro-smartthings", author: "redloro@gmail.com") {
    capability "Temperature Measurement"
    capability "Sensor"
        
    command "zone"
  }

  tiles(scale: 2) {
    multiAttributeTile(name:"zone", type: "generic", width: 6, height: 4){
      tileAttribute ("device.water", key: "PRIMARY_CONTROL") {
        attributeState "cold", label:"COLD", icon:"st.thermostat.cool", backgroundColor:"#ffffff"
        attributeState "hot", label:"Hot", icon:"st.thermostat.heat", backgroundColor:"#ff0000"
        attributeState "tested", label:"TEST", icon:"st.alarm.water.wet", backgroundColor:"#ffa81e"
      }
    }

    main "zone"
        
    details(["zone"])
  }
}

def zone(String state) {
  // need to convert open to detected and closed to clear
  def eventMap = [
    'closed':"cold",
    'open':"hot",
    'alarm':"detected",
    'tested':"tested"
  ]
  def newState = eventMap."${state}"
  
  def descMap = [
    'closed':"Was Cold",
    'open':"Was Hot",
    'alarm':"Was Hot",
    'tested':"Was Tested"
  ]
  def desc = descMap."${state}"

  sendEvent (name: "water", value: "${newState}", descriptionText: "${desc}")
}