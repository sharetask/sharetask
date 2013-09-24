#!/usr/bin/env groovy
def pid = args[0]
def sharetaskLogging = new javax.management.ObjectName("sharetask:type=logging,name=config")
def loggers = JmxServer.retrieveServerConnection(pid).getAttribute(sharetaskLogging, "Loggers")
loggers.each {
	println "${it}"
}

