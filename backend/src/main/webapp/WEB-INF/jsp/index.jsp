<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion"/>
<c:url value="/resources-${applicationVersion}" var="resourceUrl" />

<!doctype html>
<html lang="en" ng-app="shareTaskApp">
	<head>
		<meta charset="utf-8">
		<title>ShareTa.sk</title>
		<link rel="stylesheet" type="text/css" href="${resourceUrl}/css/bootswatch.min.css">
		<link rel="stylesheet" type="text/css" href="${resourceUrl}/css/datepicker.css">
		<link rel="stylesheet" type="text/css" href="${resourceUrl}/css/bootstrap-timepicker.css">
		<link rel="stylesheet" type="text/css" href="${resourceUrl}/css/sharetask.css">
	</head>
	<body>
		<!-----------------------------------
		  -- APPLICATION MENU
		  ----------------------------------->
		<div id="app-menu">
			<div class="navbar navbar-inverse">
				<div class="navbar-inner">
					<div class="container" style="width:auto;">
						<span class="brand">ShareTa.sk</span>
						<ul class="nav">
							<li class="active"><a id="menuItemTasks" href="#/tasks"><i class="icon-tasks icon-white"></i> <span id="textTasks">Tasks</span></a></li>
						</ul>
						<ul class="nav pull-right">
							<li><a id="menuItemAdministration" href="#/admin"><i class="icon-wrench icon-white"></i> <span id="textAdministration">Administration</span></a></li>
							<li id="fat-menu" class="dropdown">
								<a href="#" id="dropUser" role="button" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-user icon-white"></i> <span id="userName"></span> <b class="caret"></b></a>
								<ul class="dropdown-menu" role="menu" aria-labelledby="dropUser">
									<li><a id="menuItemUserSettings" role="menuitem" tabindex="-1" href="#/user"><span id="textMySettings">My settings</span></a></li>
									<li class="divider"></li>
									<li><a id="menuItemUserLogout" role="menuitem" tabindex="-1" href="#"><i class="icon-off"></i> <span id="textLogout">Logout</span></a></li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		
		<div ng-view></div>
		<!-- In production use:
		<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.6/angular.min.js"></script>
		-->
		<script type="text/javascript" src="${resourceUrl}/scripts/vendor/jquery/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/vendor/jquery/jquery-ui.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/vendor/bootstrap/bootstrap.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/vendor/angular/angular.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/vendor/angular/angular-resource.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/vendor/angular/angular-ui.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/app.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/services/services.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/controllers/controllers.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/filters/filters.js"></script>
		<script type="text/javascript" src="${resourceUrl}/scripts/directives/directives.js"></script>
	</body>
</html>
