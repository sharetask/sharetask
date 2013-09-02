/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.sharetask.utility.jmx;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.sharetask.utility.jmx.api.Log4jConfiguratorMXBean;

/**
 * Log4j mxbean implementation.
 * @author Michal Bocek
 * @since 1.0.0
 */
public class Log4jConfigurator implements Log4jConfiguratorMXBean {

    @Override
	public List<String> getLoggers() {
        final List<String> list = new ArrayList<String>();

        for (@SuppressWarnings("rawtypes") final Enumeration e = LogManager.getCurrentLoggers();
             e.hasMoreElements(); ) {

            final Logger log = (Logger) e.nextElement();
            if (log.getLevel() != null) {
                list.add(log.getName() + " = " + log.getLevel().toString());
            }
        }
        return list;
    }

    @Override
	public String getLogLevel(final String logger) {
        String level = "unavailable";

        if (StringUtils.isNotBlank(logger)) {
            final Logger log = Logger.getLogger(logger);

            if (log != null) {
                level = log.getLevel().toString();
            }
        }
        return level;
    }
    @Override
	public void setLogLevel(final String logger, final String level) {
        if (StringUtils.isNotBlank(logger)  &&  StringUtils.isNotBlank(level)) {
            final Logger log = Logger.getLogger(logger);

            if (log != null) {
                log.setLevel(Level.toLevel(level.toUpperCase()));
            }
        }
    }
}
