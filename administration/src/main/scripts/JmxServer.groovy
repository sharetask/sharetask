/*
 * JmxServer.groovy
 *
 * Functions meant to be called by other scripts or tools that need to use
 * the Attach API to access an MBeanServerConnection to use to manage and
 * monitor a particular JVM (and possibly the application hosted in that JVM)
 * identified by the provided Process ID (pid)
 */

import javax.management.MBeanServerConnection
import javax.management.ObjectName
import javax.management.remote.JMXConnector
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL

import com.sun.tools.attach.VirtualMachine

/**
 * Provide an MBeanServerConnection based on the provided process ID (pid).
 *
 * @param pid Process ID of Java process for which MBeanServerConnection is
 *    desired.
 * @return MBeanServerConnection connecting to Java process identified by pid.
 */
def static MBeanServerConnection retrieveServerConnection(String pid)
{
   def connectorAddressStr = "com.sun.management.jmxremote.localConnectorAddress"
   def jmxUrl = retrieveUrlForPid(pid, connectorAddressStr)
   def jmxConnector = JMXConnectorFactory.connect(jmxUrl)
   return jmxConnector.getMBeanServerConnection()
}

/**
 * Provide JMX URL for attaching to the provided process ID (pid).
 *
 * @param @pid Process ID for which JMX URL is needed to connect.
 * @param @connectorAddressStr String for connecting.
 * @return JMX URL to communicating with Java process identified by pid.
 */
def static JMXServiceURL retrieveUrlForPid(String pid, String connectorAddressStr)
{
   // Attach to the target application's virtual machine
   def vm = VirtualMachine.attach(pid)

   // Obtain Connector Address
   def connectorAddress =
      vm.getAgentProperties().getProperty(connectorAddressStr)

   // Load Agent if no connector address is available
   if (connectorAddress == null)
   {
      def agent = vm.getSystemProperties().getProperty("java.home") +
          File.separator + "lib" + File.separator + "management-agent.jar"
      vm.loadAgent(agent)

      // agent is started, get the connector address
      connectorAddress =
         vm.getAgentProperties().getProperty(connectorAddressStr)
   }

   return new JMXServiceURL(connectorAddress);
}

