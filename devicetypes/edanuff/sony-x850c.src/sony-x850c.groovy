/**
 *  Sony X850C
 *
 *  Copyright 2016 Ed Anuff
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
 *  Based on Jamie Yates's example:
 *   https://gist.github.com/jamieyates79/fd49d23c1dac1add951ec8ba5f0ff8ae
 *
 *  Note: Device Network ID for Device instance must be hex of IP address and port
 *  in the form of 00000000.0000 (i.e. 10.0.1.220 is 0A0001DC:0050)
 *
 *  JSON-RPC Methods From:
 *
 *  curl -X "POST" "http://10.0.1.220/sony/system" \
 *  -H "X-Auth-PSK: 1111" \
 *  -H "Content-Type: application/json" \
 *  -d $'{
 *  "id": 4649,
 *  "method": "getMethodTypes",
 *  "version": "1.0",
 *  "params": [
 *    "1.0"
 *  ]
 *  }'
 *
 */ 
metadata {
  definition (name: "Sony X850C", namespace: "edanuff", author: "Ed Anuff") {
    capability "Switch"
    capability "Polling"
    capability "Refresh"
  }
  
  simulator {
    status "on": "on/off: 1"
    status "off": "on/off: 0"
  }

  tiles(scale: 2) {
    standardTile("switch", "device.switch", width: 6, height: 4, canChangeIcon: true) {
      state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
      state "on", label: 'ON', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
    }

    standardTile("refresh", "device.switch", inactiveLabel: false, height: 2, width: 2, decoration: "flat") {
      state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
    }  

    main "switch"
    details(["switch", "refresh"])
  }
}

def parse(description) {
  log.debug "Parsing '${description}'"
  def msg = parseLanMessage(description)

  if (msg.json?.id == 2) {
    def status = (msg.json.result[0]?.status == "active") ? "on" : "off"
    sendEvent(name: "switch", value: status)
    log.debug "TV is '${status}'"
  }
}

private sendJsonRpcCommand(json) {

  // TV IP and Pre-Shared Key
  def tv_ip = "10.2.0.35"
  def tv_psk = "1111"

  def headers = [:]
  headers.put("HOST", "${tv_ip}:80")
  headers.put("Content-Type", "application/json")
  headers.put("X-Auth-PSK", tv_psk)

  def result = new physicalgraph.device.HubAction(
    method: 'POST',
    path: '/sony/system',
    body: json,
    headers: headers
  )

  result
}

def installed() {
  log.debug "Executing 'installed'"

  poll()
}

def on() {
  log.debug "Executing 'on'"

  def json = "{\"method\":\"setPowerStatus\",\"version\":\"1.0\",\"params\":[{\"status\":true}],\"id\":102}"
  def result = sendJsonRpcCommand(json)
}

def off() {
  log.debug "Executing 'off'"

  def json = "{\"method\":\"setPowerStatus\",\"version\":\"1.0\",\"params\":[{\"status\":false}],\"id\":102}"
  def result = sendJsonRpcCommand(json)
}


def refresh() {
  log.debug "Executing 'refresh'"

  poll()
}

def poll() {
  log.debug "Executing 'poll'"

  def json = "{\"id\":2,\"method\":\"getPowerStatus\",\"version\":\"1.0\",\"params\":[]}"
  def result = sendJsonRpcCommand(json)
}
