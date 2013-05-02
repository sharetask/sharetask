'use strict';

// Declare app level module which depends on filters, and services
angular.module('shareTaskApp', ['shareTaskApp.filters', 'shareTaskApp.services', 'shareTaskApp.directives', 'shareTaskApp.controllers']).
	config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/tasks', {templateUrl: 'partials/tasks.html', controller: 'AppCtrl'});
		$routeProvider.when('/admin', {templateUrl: 'partials/admin.html', controller: 'AdminCtrl'});
		$routeProvider.when('/user', {templateUrl: 'partials/user.html', controller: 'UserCtrl'});
		$routeProvider.otherwise({redirectTo: '/'});
	}]);
