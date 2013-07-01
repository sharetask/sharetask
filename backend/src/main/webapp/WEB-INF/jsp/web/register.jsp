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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />
<spring:eval expression="@applicationProps['application.revision']" var="applicationRevision" />
<spring:eval expression="@applicationProps['google.analytics.account.web']" var="googleAnalyticsAccount" />

<!doctype html>
<html lang="en" ng-app="shareTaskWeb">
	<!-- Application version: ${applicationVersion} -->
	<!-- Application revision: ${applicationRevision} -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="author" content="">
		<title>ShareTa.sk</title>
		<link rel="shortcut icon" href="<c:url value="/resources-web-${applicationVersion}/favicon.ico" />" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-web-${applicationVersion}/css/sharetask.css" />">
		<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css">
	</head>
	<body>
		<!-- application menu -->
		<div id="app-menu">
			<div class="navbar navbar-inverse">
				<div class="navbar-inner" style="padding-left:0;">
					<div class="container" style="width:auto;">
						<img class="pull-left" src="<c:url value="/resources-web-${applicationVersion}/img/icon-white-small.png" />" width="40" height="40" hspace="5" />
						<span class="brand">ShareTa.sk</span>
						<ul class="nav">
							<li><a href="<c:url value="/" />"><i class="icon-home icon-white"></i> <spring:message code="menu.home" /></a></li>
							<li><a href="<c:url value="/register" />"><i class="icon-user icon-white"></i> <spring:message code="menu.registration" /></a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- registration form -->
		<div class="panel-full" ng-controller="RegisterCtrl" ng-cloak>
			<div class="panel-full-inside">
				<div style="padding:20px 20px 50px 20px;">
					<form name="formRegistration" novalidate class="css-form">
						<legend><spring:message code="register.title" /></legend>
						<div class="alert alert-success" ng-show="newAccountData.result == 1">
							<a class="close" ng-click="newAccountData.result = 0">&times;</a>
							<spring:message code="register.msg.success" />
						</div>
						<div class="alert alert-error" ng-class="{'hidden':newAccountData.result == -2 || newAccountData.result == 1 || newAccountData.result == 0}">
							<a class="close" ng-click="newAccountData.result = 0">&times;</a>
							<spring:message code="register.msg.error" />
						</div>
						<div class="alert alert-error" ng-class="{'hidden':newAccountData.result == -1 || newAccountData.result == 1 || newAccountData.result == 0}">
							<a class="close" ng-click="newAccountData.result = 0">&times;</a>
							<spring:message code="register.msg.error.exists" />
						</div>
						<label><spring:message code="register.name" /></label>
						<input class="span3" type="text" name="firstname" placeholder="<spring:message code="register.firstname.placeholder" />" ng-model="newAccount.name" /> <input class="span3" type="text" name="lastname" placeholder="<spring:message code="register.lastname.placeholder" />" ng-model="newAccount.surName" /><br />
						<label><spring:message code="register.email" /></label>
						<input class="span3" type="text" name="mail" placeholder="<spring:message code="register.email.placeholder" />" ng-model="newAccount.username" required /><br />
						<label><spring:message code="register.password" /></label>
						<input class="span3" type="password" name="password" placeholder="<spring:message code="register.password.placeholder" />" ng-model="newAccount.password1" required /><br />
						<label><spring:message code="register.password.check" /></label>
						<input class="span3" type="password" name="password" placeholder="<spring:message code="register.password.check.placeholder" />" ng-model="newAccount.password2" required /><br />
						<button class="btn btn-inverse" ng-click="register()" ng-disabled="formRegistration.$invalid || newAccount.password1 != newAccount.password2 || newAccountData.processing"><spring:message code="register.button.submit" /><span ng-show="newAccountData.processing"> processing...</span></button>
					</form>
				</div>
			</div>
		</div>
		<div id="footer">
			<spring:message code="msg.changeLanguage" />: <a href="<c:url value="/?lang=en" />">english</a>, <a href="<c:url value="/?lang=cs" />">česky</a>
		</div>
		<!-- In production use:
		<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.6/angular.min.js"></script>
		-->
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