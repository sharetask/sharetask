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
<%@ page import="java.util.Map" %>
<%@ page import="org.sharetask.utility.RequestUltil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
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
<spring:eval expression="@applicationProps['application.postfix']" var="applicationPostfix" />
<spring:eval expression="@applicationProps['application.mail.support']" var="mailSupport" />
<spring:eval expression="@applicationProps['log.level']" var="logLevel" />
<spring:eval expression="@applicationProps['google.analytics.webapp.account']" var="googleAnalyticsAccount" />
<spring:eval expression="@applicationProps['google.analytics.webapp.track.pages']" var="googleAnalyticsTrackPages" />
<spring:eval expression="@applicationProps['google.analytics.webapp.track.events']" var="googleAnalyticsTrackEvents" />
<spring:eval expression="false" var="adminRole" />
<sec:authorize access="hasRole('ROLE_ADMINISTRATOR')">
	<spring:eval expression="true" var="adminRole" />
</sec:authorize>

<!doctype html>
<html lang="en" data-ng-app="shareTaskApp">
	<!-- Application version: ${applicationVersion} -->
	<!-- Application revision: ${applicationRevision} -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="author" content="">
		<title>ShareTa.sk</title>
		<link rel="shortcut icon" href="<c:url value="/resources-webapp-${applicationVersion}/favicon.ico" />" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/font-awesome.min.css" />">
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/font-awesome-ie7.min.css" />">
		<![endif]-->
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/jquery.ui.datepicker.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-webapp-${applicationVersion}/css/sharetask.css" />">
		
		<script>
		  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		
		  ga('create', '${googleAnalyticsAccount}', 'auto');
		  ga('send', 'pageview');
		</script>		
	</head>
	<body data-ng-cloak>
		<div id="app-menu" data-ng-controller="HeaderController" data-ng-init="checkLogin();" data-ng-show="isLoggedIn">
			<div class="navbar navbar-inverse">
				<div class="navbar-inner" style="padding-left:0;">
					<div class="container" style="width:auto;">
						<img class="pull-left" data-ng-src="{{'resources-webapp-'+appVersion+'/img/icon-white-small.png'}}" style="padding:8px 15px 0 8px;" />
						<span class="brand">ShareTa.sk <small>[beta {{appVersion}}]</small></span>
						<ul class="nav">
							<li data-ng-class="{'active':currentPage == 'tasks'}"><a href="#/tasks"><i class="icon-list icon-white"></i> {{'_Tasks_' | i18n}}</a></li>
							<li data-ng-class="{'active':currentPage == 'workspaces'}"><a href="#/workspaces"><i class="icon-folder-close icon-white"></i> {{'_Workspaces_' | i18n}}</a></li>
						</ul>
						<ul class="nav pull-right">
							<li data-ng-show="currentPage == 'tasks'">
								<i class="icon-search"></i>
								<input type="search" placeholder="{{'_PlaceholderSearch_' | i18n}}" data-ng-model="filter.searchString" id="inputSearch" />
							</li>
							<li id="fat-menu" class="dropdown" data-ng-class="{'active':currentPage == 'user'}">
								<a id="dropUser" role="button" class="dropdown-toggle cursor-pointer" data-toggle="dropdown"><i class="icon-user icon-white"></i> {{loggedUser.name}} {{loggedUser.surName}} <b class="caret"></b></a>
								<ul class="dropdown-menu" role="menu" aria-labelledby="dropUser">
									<li><a role="menuitem" tabindex="-1" href="#/user"><i class="icon-cog"></i> {{'_MySettings_' | i18n}}</a></li>
									<li data-ng-show="adminRole"><a role="menuitem" tabindex="-1" href="#/statistics"><i class="icon-cog"></i> {{'_Statistics_' | i18n}}</a></li>
									<li class="divider"></li>
									<li><a class="cursor-pointer" role="menuitem" tabindex="-1" data-ng-click="logout()"><i class="icon-off"></i> {{'_Logout_' | i18n}}</a></li>
								</ul>
							</li>
							<!--<li><a data-ng-click="toggleHelp()"><i class="icon-question"></i></a></li>-->
						</ul>
					</div>
				</div>
			</div>
		</div>	
		<div data-ng-view></div>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/jquery/jquery-1.9.1.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/jquery/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/bootstrap/bootstrap.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/angular.${applicationPostfix}" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/angular-resource.${applicationPostfix}" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/angular-route.${applicationPostfix}" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/angular-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/angular-dragdrop.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/ui-bootstrap-tpls-0.3.0.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/i18n/angular-locale_${locale}.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/angular/angular-google-analytics.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/localize/localize.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/md5-min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/vendor/localize/jquery.ui.datepicker-${language}.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/services/services.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/controllers/controllers.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/filters/filters.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-webapp-${applicationVersion}/scripts/directives/directives.js" />"></script>
		<script type="text/javascript">
			// Declare app level module which depends on filters, and services
			angular.module('shareTaskApp', ['ngRoute', 'angular-google-analytics', 'shareTaskApp.filters', 'shareTaskApp.services', 'shareTaskApp.directives', 'shareTaskApp.controllers'])
				.config(['$routeProvider', 'AnalyticsProvider', '$locationProvider', function($routeProvider, AnalyticsProvider, $locationProvider) {
					// google analytics
					AnalyticsProvider.setAccount('${googleAnalyticsAccount}');
					AnalyticsProvider.setTracking('${googleAnalyticsTrackPages}', '${googleAnalyticsTrackEvents}');
					// routing
					$routeProvider.when('/', {redirectTo: '/tasks'});
					$routeProvider.when('/tasks', {templateUrl: '<c:url value="/resources-webapp-${applicationVersion}/views/tasks.html" />'});
					$routeProvider.when('/workspaces', {templateUrl: '<c:url value="/resources-webapp-${applicationVersion}/views/workspaces.html" />'});
					$routeProvider.when('/user', {templateUrl: '<c:url value="/resources-webapp-${applicationVersion}/views/user.html" />'});
					$routeProvider.when('/statistics', {templateUrl: '<c:url value="/resources-webapp-${applicationVersion}/views/statistics.html" />'});
					$routeProvider.otherwise({redirectTo: '/'});
				}])
				.factory('authHttpResponseInterceptor',['$q','$location', '$log', '$window', function($q, $location, $log, $window) {
				    return {
				        response: function(response){
				            if (response.status === 401) {
				            	alert("response: 401");
				            	$log.log("Response 401");
				            }
				            return response || $q.when(response);
				        },
				        responseError: function(rejection) {
				            if (rejection.status === 401) {
				            	$log.log("Response Error 401", rejection, $location.path());
				            	$window.location.replace('<c:url value="/signin" />');
				            }
				            return $q.reject(rejection);
				        }
				    };
				}])
				.config(['$httpProvider',function($httpProvider) {
				    //Http Intercpetor to check auth failures for xhr requests
				    $httpProvider.interceptors.push('authHttpResponseInterceptor');
				}])
				.run(['$rootScope', 'Logger', function ($rootScope, Logger) {
					$rootScope.appBaseUrl = '<c:url value="/" />';
					$rootScope.appVersion = '${applicationVersion}';
					$rootScope.mailSupport = '${mailSupport}';
					$rootScope.adminRole = ${adminRole};
					$rootScope.appLocale = {language: '<c:out value="${language}" />', country: '<c:out value="${locale}" />'};
					Logger.init('${logLevel}');
				}]);
		</script>fire
	</body>
</html>