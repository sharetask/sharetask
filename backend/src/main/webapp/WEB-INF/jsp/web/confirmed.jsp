<%--
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
--%>
<%@page import="org.pac4j.oauth.client.Google2Client"%>
<%@page import="org.pac4j.oauth.client.FacebookClient"%>
<%@page import="org.pac4j.core.context.J2EContext"%>
<%@page import="org.pac4j.core.context.WebContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />
<spring:eval expression="@applicationProps['application.revision']" var="applicationRevision" />
<spring:eval expression="@applicationProps['google.analytics.account.web']" var="googleAnalyticsAccount" />
<spring:eval expression="@applicationProps['google.analytics.account.web']" var="googleAnalyticsAccount" />
<%
    WebContext context = new J2EContext(request, response); 
	Google2Client gClient= (Google2Client) application.getAttribute("GoogleClient");
    String redirectionGoogleUrl = gClient.getRedirectionUrl(context);
%>
<!doctype html>
<html lang="en" ng-app="shareTaskWeb">
	<!-- Application version: ${applicationVersion} -->
	<!-- Application revision: ${applicationRevision} -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="author" content="">
		<title>invitation - ShareTa.sk</title>
		<link rel="shortcut icon" href="<c:url value="/resources-web-${applicationVersion}/favicon.ico" />" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/font-awesome.min.css" />">
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/font-awesome-ie7.min.css" />">
		<![endif]-->
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/sharetask.css" />">
	</head>
	<body ng-controller="IndexCtrl" ng-cloak>
		<!-- application menu -->
		<div id="app-menu">
			<div class="container">
				<div class="navbar navbar-inverse">
					<div class="navbar-inner" style="padding-left:0;">
						<div class="container" style="width:auto;">
							<img class="pull-left" src="<c:url value="/resources-web-${applicationVersion}/img/icon-white-small.png" />" style="padding:8px 15px 0 8px;" />
							<span class="brand">ShareTa.sk</span>
							<ul class="nav">
								<li><a href="<c:url value="/" />"><i class="icon-home icon-white"></i> <spring:message code="menu.home" /></a></li>
								<li></li>
							</ul>
							<ul class="nav pull-right">
								<li id="fat-menu" class="dropdown" ng-show="loggedUser != null">
									<a id="dropUser" role="button" class="dropdown-toggle cursor-pointer" data-toggle="dropdown"><i class="icon-user icon-white"></i> {{loggedUser.name}} {{loggedUser.surName}} <b class="caret"></b></a>
									<ul class="dropdown-menu" role="menu" aria-labelledby="dropUser">
										<li><a role="menuitem" tabindex="-1" href="<c:url value="/webapp" />"><spring:message code="app.button.open" /></a></li>
										<li class="divider"></li>
										<li><a class="cursor-pointer" role="menuitem" tabindex="-1" ng-click="logout()"><i class="icon-signout"></i><spring:message code="app.button.logout" /></a></li>
									</ul>
								</li>
								<li class="active"><a href="<c:url value="/signin" />"><spring:message code="menu.signin" /></a></li>
								<li><a href="<c:url value="/register" />"><spring:message code="menu.signup" /></a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- slide header -->
		<div class="panel-head">
			<div class="container">
				<%@include file="inc/signin.jsp" %>
			</div>
		</div>
		<!-- page body -->
		<div class="panel-body">
			<div class="container">
				<h2><spring:message code="invitation.title" /></h2>
				<c:choose>
					<c:when test="${param.error.equals('notexists')}">
						<spring:message code="invitation.result.notexists" />
					</c:when>
					<c:when test="${param.error.equals('userNotExists')}">
						<spring:message code="invitation.result.userNotExists" />
					</c:when>
					<c:otherwise>
						<spring:message code="invitation.result.ok" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<!-- page footer -->
		<%@include file="inc/footer.jsp" %>
		
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/jquery/jquery-1.9.1.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/jquery/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/bootstrap/bootstrap.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/angular/angular.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/vendor/angular/angular-resource.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/services/services.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/filters/filters.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/directives/directives.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-web-${applicationVersion}/scripts/controllers/controllers.js" />"></script>
		<script type="text/javascript">
			angular.module('shareTaskWeb', ['shareTaskWeb.filters', 'shareTaskWeb.services', 'shareTaskWeb.directives', 'shareTaskWeb.controllers'])
				.run(['$rootScope', function ($rootScope) {
					$('.carousel').carousel();
					$rootScope.appBaseUrl = '<c:url value="/" />';
					$rootScope.appVersion = '${applicationVersion}';
					$rootScope.appLocale = {language: '<c:out value="${pageContext.request.locale.language}" />', country: '<c:out value="${pageContext.request.locale.country}" />'};
				}]);
		</script>
		<script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments);},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m);
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', '${googleAnalyticsAccount}', 'shareta.sk');
			ga('send', 'pageview');
		</script>
	</body>
</html>