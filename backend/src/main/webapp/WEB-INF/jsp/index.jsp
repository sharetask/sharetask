<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion"/>

<!doctype html>
<html lang="en" ng-app="shareTaskApp">
	<head>
		<meta charset="utf-8">
		<title>ShareTa.sk</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/bootswatch.min.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/datepicker.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/bootstrap-timepicker.css" />">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources-${applicationVersion}/css/sharetask.css" />">
	</head>
	<body>
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
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/services/services.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/controllers/controllers.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/filters/filters.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources-${applicationVersion}/scripts/directives/directives.js" />"></script>
		<script type="text/javascript">
			// Declare app level module which depends on filters, and services
			angular.module('shareTaskApp', ['shareTaskApp.filters', 'shareTaskApp.services', 'shareTaskApp.directives', 'shareTaskApp.controllers']).
				config(['$routeProvider', function($routeProvider) {
					$routeProvider.when('/', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/index.html" />'/*, controller: 'AppCtrl'*/});
					$routeProvider.when('/tasks', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/tasks.html" />'/*, controller: 'AppCtrl'*/});
					$routeProvider.when('/admin', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/admin.html" />'/*, controller: 'AdminCtrl'*/});
					$routeProvider.when('/user', {templateUrl: '<c:url value="/resources-${applicationVersion}/views/user.html" />'/*, controller: 'UserCtrl'*/});
					$routeProvider.otherwise({redirectTo: '/'});
				}]);
		</script>
	</body>
</html>
