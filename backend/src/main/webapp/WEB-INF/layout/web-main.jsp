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
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.sharetask.utility.RequestUltil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%
	Map<String, String> data = RequestUltil.getRequestData(request);
	pageContext.setAttribute("locale", data.get(RequestUltil.LOCALE));
	pageContext.setAttribute("country", data.get(RequestUltil.COUNTRY));
	pageContext.setAttribute("language", data.get(RequestUltil.LANGUAGE));
%>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />
<spring:eval expression="@applicationProps['application.revision']" var="applicationRevision" />
<spring:eval expression="@applicationProps['google.analytics.web.account']" var="googleAnalyticsAccount" />

<!doctype html>
<html lang="en" data-ng-app="shareTaskWeb">
	<!-- Application version: ${applicationVersion} -->
	<!-- Application revision: ${applicationRevision} -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="author" content="">
		<title><sitemesh:write property="title"/></title>
		<link rel="shortcut icon" href="<c:url value="/resources-${applicationVersion}/favicon.ico" />" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/font-awesome.min.css" />">
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/font-awesome-ie7.min.css" />">
		<![endif]-->
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/sharetask-web.css" />">

		<script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments);},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m);
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			ga('create', '${googleAnalyticsAccount}', 'shareta.sk');
			ga('send', 'pageview');
		</script>
	</head>
	<body data-ng-controller="IndexCtrl" data-ng-cloak>
		<!-- application menu -->
		<div id="app-menu">
			<div class="container">
				<div class="navbar navbar-inverse">
					<div class="navbar-inner" style="padding-left:0;">
						<div class="container" style="width:auto;">
							<img class="pull-left" src="<c:url value="/resources-${applicationVersion}/img/icon-white-small.png" />" style="padding:8px 15px 0 8px;" alt="" />
							<span class="brand">ShareTa.sk <small>[beta ${applicationVersion}]</small></span>
							<ul class="nav">
								<li data-ng-class="{'active':rootScope.currentPage == 'home'}"><a href="<c:url value="/" />"><i class="icon-home icon-white"></i> <spring:message code="menu.home" /></a></li>
								<li data-ng-class="{'active':rootScope.currentPage == 'features'}"><a href="<c:url value="/features" />"><i class="icon-check icon-white"></i> <spring:message code="menu.features" /></a></li>
							</ul>
							<ul class="nav pull-right">
								<li id="fat-menu" class="dropdown" data-ng-show="loggedUser != null">
									<a id="dropUser" role="button" class="dropdown-toggle cursor-pointer" data-toggle="dropdown"><i class="icon-user icon-white"></i> {{loggedUser.name}} {{loggedUser.surName}} <b class="caret"></b></a>
									<ul class="dropdown-menu" role="menu" aria-labelledby="dropUser">
										<li><a role="menuitem" tabindex="-1" href="<c:url value="/app" />"><spring:message code="app.button.open" /></a></li>
										<li class="divider"></li>
										<li><a class="cursor-pointer" role="menuitem" tabindex="-1" data-ng-click="logout()"><i class="icon-signout"></i><spring:message code="app.button.logout" /></a></li>
									</ul>
								</li>
								<sec:authorize access="!hasRole('ROLE_USER')">
									<li data-ng-class="{'active':rootScope.currentPage == 'signin'}"><a href="<c:url value="/signin" />"><i class="icon-signin icon-white"></i> <spring:message code="menu.signin" /></a></li>
									<li data-ng-class="{'active':rootScope.currentPage == 'register'}"><a href="<c:url value="/register" />"><spring:message code="menu.signup" /></a></li>
								</sec:authorize>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- page body -->
		<sitemesh:write property="body"/>
		<!-- page footer -->
		<%@include file="/WEB-INF/layout/includes/footer.jsp" %>
		
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/jquery/jquery-1.9.1.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/jquery/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/bootstrap/bootstrap.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular-resource.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular-route.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/localize/localize.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/services/services.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/filters/filters.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/directives/directives.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/controllers/controllers.js" />"></script>
		<script type="text/javascript">
			angular.module('shareTaskWeb', ['ngRoute', 'shareTaskWeb.filters', 'shareTaskWeb.services', 'shareTaskWeb.directives', 'shareTaskWeb.controllers'])
				.run(['$rootScope', function ($rootScope) {
					$('.carousel').carousel();
					$rootScope.appBaseUrl = '<c:url value="/" />';
					$rootScope.appVersion = '${applicationVersion}';
					$rootScope.appLocale = {language: '<c:out value="${language}" />', country: '<c:out value="${locale}" />'};
				}]);
		</script>
	</body>
</html>