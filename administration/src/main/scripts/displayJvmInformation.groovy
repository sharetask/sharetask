#!/usr/bin/env groovy  
import javax.management.ObjectName

def pid = args[0]  
def server = JmxServer.retrieveServerConnection(pid)  


// Java Information
def javaRuntime = new javax.management.ObjectName("java.lang:type=Runtime")  
def startTime = server.getAttribute(javaRuntime, "StartTime")
def uptime = server.getAttribute(javaRuntime, "Uptime")

def days =  (uptime / (3600 * 24 * 1000)).longValue()
def uptimeSec = ((uptime / 1000) - (days * 24 * 60 * 60)).longValue()
timeDuration = String.format("%d %02d:%02d:%02d", days, (uptimeSec / 3600).longValue(), ((uptimeSec % 3600) / 60).longValue(), (uptimeSec % 60).longValue())

println "-------------------------------------------------------------------------------"
println "Java process pid: (${pid})"
println "Started at: ${new Date(startTime)}"
println "Up for: ${timeDuration}" 
println "Virtual machine: ${server.getAttribute(javaRuntime, "VmName")} - ${server.getAttribute(javaRuntime, "SpecVersion")} (${server.getAttribute(javaRuntime, "VmVendor")} - ${server.getAttribute(javaRuntime, "VmVersion")})"


// OS information
def osRuntime = new javax.management.ObjectName("java.lang:type=OperatingSystem")  
def freeMemory = server.getAttribute(osRuntime, "FreePhysicalMemorySize")
def commitedMemory = server.getAttribute(osRuntime, "CommittedVirtualMemorySize")
def memory = server.getAttribute(osRuntime, "TotalPhysicalMemorySize")
def freeSwap = server.getAttribute(osRuntime, "FreeSwapSpaceSize")
def swap = server.getAttribute(osRuntime, "TotalSwapSpaceSize")

println "-------------------------------------------------------------------------------"
println "OS: ${server.getAttribute(osRuntime, "Name")} - ${server.getAttribute(osRuntime, "Version")} (${server.getAttribute(osRuntime, "Arch")})"
println "Physical memory [free/commited/total]: ${(freeMemory/(1024*1024)).longValue()}/${(commitedMemory/(1024*1024)).longValue()}/${(memory/(1024*1024)).longValue()} [MB]"
println "Swap [free/total]: ${(freeSwap/(1024*1024)).longValue()}/${(swap/(1024*1024)).longValue()} [MB]"


// Garbege collector information
println "-------------------------------------------------------------------------------"
println "Garbage collectors information"
server.queryNames(new ObjectName("java.lang:type=GarbageCollector,*"), null).each {
	println "Name: " + server.getAttribute(it, "Name")
	println "Collection count: " + server.getAttribute(it, "CollectionCount")
	println "Collection time: " + server.getAttribute(it, "CollectionTime") / 1000 + "[s]"
}

