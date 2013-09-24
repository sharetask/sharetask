#!/usr/bin/env groovy
import groovy.jmx.builder.*

def pid = args[0]
def logger = args[1]
def level = args[2]
def server = JmxServer.retrieveServerConnection(pid)  
def mbean = new GroovyMBean(server, "sharetask:type=logging,name=config")

println "Log levels befor change:" 
mbean.Loggers.each {
	println "${it}"
}

println "Setting logger: ${logger} with level: ${level}"
mbean.setLogLevel(logger, level)

println "Log levels after change:" 
mbean.Loggers.each {
	println "${it}"
}

