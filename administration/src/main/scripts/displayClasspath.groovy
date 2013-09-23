#!/usr/bin/env groovy
def pid = args[0]
def javaRuntime = new javax.management.ObjectName("java.lang:type=Runtime")
def classpath = JmxServer.retrieveServerConnection(pid).getAttribute(javaRuntime, "ClassPath")
println "\nClasspath for Java pid (${pid}):\n${classpath}\n"

