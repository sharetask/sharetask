'use strict';

// Declare app level module which depends on filters, and services
angular.module('shareTaskApp', ['shareTaskApp.filters', 'shareTaskApp.services', 'shareTaskApp.directives', 'shareTaskApp.controllers']).
	config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/tasks', {templateUrl: 'views/tasks.html', controller: 'AppCtrl'});
		$routeProvider.when('/admin', {templateUrl: 'views/admin.html', controller: 'AdminCtrl'});
		$routeProvider.when('/user', {templateUrl: 'views/user.html', controller: 'UserCtrl'});
		$routeProvider.otherwise({redirectTo: '/'});
	}]);
