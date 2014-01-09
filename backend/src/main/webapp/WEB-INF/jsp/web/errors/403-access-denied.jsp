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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />
<spring:eval expression="@applicationProps['application.revision']" var="applicationRevision" />
<spring:eval expression="@applicationProps['google.analytics.account.web']" var="googleAnalyticsAccount" />
<spring:eval expression="@applicationProps['google.analytics.account.web']" var="googleAnalyticsAccount" />
<!doctype html>
<html lang="en">
	<!-- Application version: ${applicationVersion} -->
	<!-- Application revision: ${applicationRevision} -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="author" content="">
		<title>403 - ShareTa.sk</title>
		<link rel="shortcut icon" href="<c:url value="/resources-web-${applicationVersion}/favicon.ico" />" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/font-awesome.min.css" />">
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/font-awesome-ie7.min.css" />">
		<![endif]-->
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/sharetask.css" />">
	</head>
	<body>
		<!-- application menu -->
		<div id="app-menu">
			<div class="container">
				<div class="navbar navbar-inverse">
					<div class="navbar-inner" style="padding-left:0;">
						<div class="container" style="width:auto;">
							<img class="pull-left" src="<c:url value="/resources-web-${applicationVersion}/img/icon-white-small.png" />" style="padding:8px 15px 0 8px;" />
							<a href="<c:url value="/" />"><span class="brand">ShareTa.sk</span></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- slide header -->
		<%@include file="../inc/slider.jsp" %>
		<!-- page body -->
		<div class="panel-body">
			<div class="container">
				<h2><spring:message code="403.title" /></h2>
				<spring:message code="403.msg" />
			</div>
		</div>
		<!-- page footer -->
		<%@include file="../inc/footer.jsp" %>
		
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/jquery/jquery-1.9.1.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/bootstrap/bootstrap.min.js" />"></script>
		<script type="text/javascript">
			$('.carousel').carousel();
		</script>
		<script type="text/javascript">
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments);},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m);
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', '${googleAnalyticsAccount}', 'shareta.sk');
			ga('send', 'pageview');
		</script>
	</body>
</html>
 