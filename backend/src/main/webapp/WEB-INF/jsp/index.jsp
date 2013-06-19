<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion"/>
<spring:eval expression="@applicationProps['application.revision']" var="applicationRevision"/>

<!doctype html>
<html lang="en" ng-app="shareTaskApp">
	<!-- ================================================================= -->
	<!-- Application version: ${applicationVersion}                        -->
	<!-- Application revision: ${applicationRevision}                      -->
	<!-- ================================================================= -->
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<title>ShareTa.sk</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/jquery.ui.datepicker.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/sharetask.css" />">
		<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css">
	</head>
	<body ng-cloak>
		<div ng-view></div>
		<!-- In production use:
		<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.6/angular.min.js"></script>
		-->
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/jquery/jquery-1.9.1.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/jquery/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/bootstrap/bootstrap.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular-resource.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/angular-dragdrop.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/angular/ui-bootstrap-tpls-0.3.0.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/vendor/localize/localize.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/services/services.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/controllers/controllers.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/filters/filters.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/directives/directives.js" />"></script>
		<script type="text/javascript">
			// Declare app level module which depends on filters, and services
			angular.module('shareTaskApp', ['shareTaskApp.filters', 'shareTaskApp.services', 'shareTaskApp.directives', 'shareTaskApp.controllers']).
				config(['$routeProvider', function($routeProvider) {
					$routeProvider.when('/', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/index.html" />'});
					$routeProvider.when('/register', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/register.html" />'});
					$routeProvider.when('/tasks', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/tasks.html" />'});
					$routeProvider.when('/admin', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/admin.html" />'});
					$routeProvider.when('/user', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/user.html" />'});
					$routeProvider.otherwise({redirectTo: '/'});
				}]);
		</script>
	</body>
</html>
