metadata {
	definition (name: "XBee Reporter", namespace:	"BigWebstas", author: "BigWebstas") 
	{
		capability "Refresh"
        capability "Health Check"
        
	}

	preferences {

	}
	
    simulator {

	}

	tiles {
		standardTile("refresh", "device.status", inactiveLabel: false, decoration: "flat") {
			state "default", label:'Check', action:"refresh.refresh", icon:"st.secondary.refresh"
		}        
		main "refresh"
		details(["refresh"])
	}
}

def parse(String description) 
{
	def value = zigbee.parse(description)?.text
	log.debug value
}

def refresh() {
	try{
    zigbee.readAttribute(0x0B04, 0x050B)
  } catch (e) {
        log.error("caught exception", e)
        }
}
def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
	log.info "Command ${cmd}"} 
        


def zwaveEvent(physicalgraph.zwave.Command cmd) {
    log.debug "Unhandled event ${cmd}"
	[:]
}

def healthPoll() {
	sendHubCommand(new physicalgraph.device.HubAction(zwave.basicV1.basicGet().format()))
}

def installed() {
	refresh()
}

def updated() {
	unschedule()
	runEvery5Minutes(healthPoll)
	refresh()
}

def initialize() {
	refresh()
}


	