<!--
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
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Properties"%>

<!doctype html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>ShareTa.sk app - info</title>
	</head>
	<body>
		<h1>Environment information:</h1>
		<p>
		<% 
		Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            out.println(String.format("%s=%s%n<br/>",
                              envName,
                              env.get(envName)));
        }
		%>	
		</p>
		<h1>System properties:</h1>
		<p>
		<% 
		Properties systemProperties = System.getProperties();
		Enumeration enuProp = systemProperties.propertyNames();
		while (enuProp.hasMoreElements()) {
			String propertyName = (String) enuProp.nextElement();
			String propertyValue = systemProperties.getProperty(propertyName);
            out.println(String.format("%s=%s%n<br/>",
            		propertyName,
            		propertyValue));
		}
		%>
		</p>
	</body>
</html>
 