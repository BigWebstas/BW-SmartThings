/*
 *  Honeywell alarm panel callbacks via honeyalarm server python script
 *	uses restful api
 */

definition(
    name: "Honeywell Panel Integration V2",
    namespace: "Webstas",
    author: "BigWebstas (JWebstas@gmail.com)",
    description: "Honeywell Panel Integration V2",
    category: "My Apps",
    iconUrl: "https://dl.dropboxusercontent.com/u/2760581/dscpanel_small.png",
    iconX2Url: "https://dl.dropboxusercontent.com/u/2760581/dscpanel_large.png",
    oauth: true
)
import groovy.json.JsonBuilder

preferences {

  section("Alarm Panel:") {
    input "paneldevices", "capability.polling", title: "Partition Devies", multiple: false, required: false
  }
  section("Zone Devices:") {
    input "zonedevices", "capability.polling", title: "Honeywell Zone Devices", multiple: true, required: false
  }
  	section("Alarm Server Settings") {
            input("ip", "text", title: "IP", description: "The IP of your AlarmServer", required: true)
            input("port", "text", title: "Port", description: "The Port AlarmServer is running on", required: true)
        }
}

mappings {
  path("/panel/:eventcode/:zoneorpart") {
    action: [
      GET: "updateZoneOrPartition"
    ]
  }
}

def installed() {
	initialize()
}

def updated() {
  log.debug "Updated!"
  	unsubscribe()
    unschedule()
	initialize()
}
def initialize() {
    	subscribe(location, "alarmSystemStatus", alarmStatusUpdate)
        subscribe(paneldevices, "switch", switchUpdate)
}

void updateZoneOrPartition() {
  update()
}

private update() {
    def zoneorpartition = params.zoneorpart

    //map event codes to zones status and/or partition status's
    def eventMap = [
      '601':"zone alarm",
      '602':"zone closed",
      '609':"zone open",
      '610':"zone closed",
      '631':"zone smoke",
      '632':"zone clear",
      '650':"partition ready",
      '651':"partition notready",
      '652':"partition armed-stay",
      '654':"partition alarm",
      '703':"partition armed-max",
      '656':"partition exit/entry-delay",
      '701':"partition armed-away",
      '702':"partition ready-bypass"
    ]

    def eventCode = params.eventcode
    if (eventCode)
    {
      //lookup event and send SHM command if needed
      def opts = eventMap."${eventCode}"?.tokenize()
      log.debug eventCode
    if (eventCode == '650') {
        setSmartHomeMonitor("off")
      }
    if(eventCode == '701') {
        setSmartHomeMonitor("away")
      }
    if(eventCode == '652') {
        setSmartHomeMonitor("stay")
      }
    if (opts[0])
      {
        if ("${opts[0]}" == 'zone') {
           updateZoneDevices(zonedevices,"$zoneorpartition","${opts[1]}")
        }
        if ("${opts[0]}" == 'partition') {
           updatePartitions(paneldevices, "$zoneorpartition","${opts[1]}")
        }
      }
    }
}
//update zone child devices
private updateZoneDevices(zonedevices,zonenum,zonestatus) {
  log.debug "zonedevices: $zonedevices - ${zonenum} is ${zonestatus}"
  def zonedevice = zonedevices.find { it.deviceNetworkId == "zone${zonenum}" }
  if (zonedevice) {
      zonedevice.zone("${zonestatus}")
    }
}
//update partition child devices
private updatePartitions(paneldevices, partitionnum, partitionstatus) {
  log.debug "paneldevices: $paneldevices - ${partitionnum} is ${partitionstatus}"
  def paneldevice = paneldevices.find { it.deviceNetworkId == "partition${partitionnum}" }
  if (paneldevice) {
    paneldevice.partition("${partitionstatus}", "${partitionnum}")
  }
}
//Honeywell panel to SHM
def switchUpdate(evt) {
    def securityMonitorMap = [
        'stayarm':"stay",
        'disarm':"off",
        'arm':"away"
    ]
    setSmartHomeMonitor(securityMonitorMap."${evt.value}")
}
//SHM to Honeywell panel
def alarmStatusUpdate(evt) {
	def eventMap = [
        'stay':"/api/alarm/stayarm",
        'off':"/api/alarm/disarm",
        'away':"/api/alarm/arm"
    ]
    def path = eventMap."${evt.value}"
	callAlarmServer(path)
    
}
//Send commands to EVL3/4 
private callAlarmServer(path) {
		log.debug "${ip}:${port}${path}"
        sendHubCommand(new physicalgraph.device.HubAction(
            method: "GET",
            path: path,
            headers: [
                HOST: "${ip}:${port}"
            ],
        ))
}
//Set the SHM
private setSmartHomeMonitor(status)
{
	if(location.currentState("alarmSystemStatus").value != status && status != null ) {
    	log.debug "Set Smart Home Monitor to $status"
    	sendLocationEvent(name: "alarmSystemStatus", value: status)
        }
}