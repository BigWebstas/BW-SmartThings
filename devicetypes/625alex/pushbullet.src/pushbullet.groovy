/**
 *  Pushbullet (Standalone)
 *
 *  Copyright 2014 Alex Malikov
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

def getApiKey() {settings.apiKey}
def getIden() {settings.iden}

metadata {
	definition (name: "Pushbullet", namespace: "625alex", author: "Alex Malikov") {
    	command "push", ["string", "string"]
        command "push", ["string"]
        command "test"
		capability "Polling"
		capability "Refresh"
        capability "Notification"
	}

	preferences {
		input "apiKey", "text", title: "API Key", required: true
        input "iden", "text", title: "Device Id (leave empty to push to all device)", required: false
	}
    
	tiles {
    	standardTile("name", "device.name", width: 2, height: 2) {
        	state "name", label: "test", action: "test", icon: "https://baldeagle072.github.io/pushbullet_notifier/images/PushbulletLogo@1x.png", backgroundColor: "#59ab46"
        }
        standardTile("test", "device.name") {
			state "test", label: "Test", action: "test"
		}
	}
    
    main(["name"])
    details(["name", "test"])
}

def parse(String description) {
	log.debug "Parsing '${description}'"
}

def push(message) {
	push(null, message)
}

def push(title, message) {
	log.debug "push title: $title message: $message, key: ${getApiKey()}, iden: ${getIden()}" 
    def url = "https://${getApiKey()}@api.pushbullet.com/v2/pushes"
	    
	def successClosure = { response ->
      log.debug "Push request was successful, $response.data"
      sendEvent(name:"push", value:[title: title, message: message], isStateChange:true)
    }
    
    def postBody = [
        u: getApiKey(),
        type: "note",
        title: title ?: "SmartThings",
      	body: message
    ]
    
    getIden() ? postBody << [device_iden: getIden()] : false
    
    def params = [
      uri: url,
      success: successClosure,
      body: postBody
    ]
    
    httpPost(params)
}

def poll() {}

def refresh() {
	log.debug "Executing 'refresh'"
    poll()
}

def test() {
	log.debug "Executing 'test'"
    push("Hello $location")
}
