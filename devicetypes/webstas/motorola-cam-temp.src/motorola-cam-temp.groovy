
preferences {
    input("ip", "string", title:"Cam IP Address",
        required:true, displayDuringSetup: true)
    input("port", "number", title:"Cam Port",
        required:true, displayDuringSetup:true)
}

metadata {
    definition (name:"Motorola Cam Temp", namespace:"Webstas", author:"BigWebstas") {


        capability "Temperature Measurement"
        //capability "Refresh"
        
        command "updateDNI"
    }
        tiles {
		standardTile("updateDNI", "DNI", inactiveLabel: false, decoration: "flat") {
        	state "default", action:"updateDNI", icon: "st.secondary.refresh"
        }
        main(["updateDNI"])

        details(["updateDNI"])
    }
}

def updated(){
  	def iphex = convertIPtoHex(settings.ip)
  	def porthex = convertPortToHex(settings.port)
  	device.DeviceNetworkId = "$iphex:$porthex"
  	log.info "Device Network Id set to ${iphex}:${porthex}"
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    return hex

}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
    return hexport
}