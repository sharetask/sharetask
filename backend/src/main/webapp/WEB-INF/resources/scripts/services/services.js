'use strict';

/* Services */
var shareTaskApp = angular.module('shareTaskApp.services', ['ngResource']);

shareTaskApp.service('User', function($resource, $http) {
	
	this.authenticate = function(user, success, error) {
		console.log("Login user (user: %o)", user);
		//return $http.post('/sharetask/api/user/login', {username: 'dev1@shareta.sk', password: 'password'}).success(success).error(error);
		return $http.post('/sharetask/api/user/login', {username: user.username, password: user.password}).success(success).error(error);
	};
});

shareTaskApp.service('Workspace', function($resource) {
	
	this.findAll = function(success, error) {
		console.log("Getting all workspaces from server");
		//return $resource("/sharetask/resources-1.0.0/scripts/data/workspaces.json", {}, {query: {method: "GET", isArray: true}}).query(callback);
		var workspace = $resource("/sharetask/api/workspace", {}, {query: {method: "GET", isArray: true}});
		workspace.query(function(response){return success(response);}, function(response){return error(response);});
	};
	
	this.getActiveTasks = function(input, success, error) {
		console.log("Getting active tasks for workspace (id: %s) from server", input.workspaceId);
		//return $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'ALL'}, {query: {method: "GET", isArray: true}}).query(callback);
		var task = $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'ALL'}, {query: {method: "GET", isArray: true}});
		task.query(function(response){return success(response);}, function(response){return error(response);});
	};
	
	this.getCompletedTasks = function(input, callback) {
		console.log("Getting completed tasks for workspace (id: %s) from server", input.workspaceId);
		return $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'FINISHED'}, {query: {method: "GET", isArray: true}}).query(callback);
	};
});

shareTaskApp.service('Task', function($resource, $http) {
	
	this.findById = function(input, callback) {
		console.log("Getting task (id: %s) from server", input.id);
		//console.log("Get JSON file js/data/task-"+input.id+".json");
		return $resource("/sharetask/resources-1.0.0/scripts/data/task-"+input.id+".json", {}, {query: {method: "GET", isArray: false}}).query(callback);
	};
	
	this.update = function(input, success, error) {
		console.log("Update task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.put("/api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
});

shareTaskApp.service('Logger', function($log) {
	
	this.debug = function() {
		
	};
});

shareTaskApp.service('Utils', function($resource) {
	
	this.isCookieEnabled = function() {
		console.log("isCookieEnabled called");
		var cookieEnabled = (navigator.cookieEnabled) ? true : false;
		if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled) {
			document.cookie="testcookie";
			cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
		}
		return (cookieEnabled);
	};
});

shareTaskApp.service('LocalStorage', function($resource) {
	
	var keyPrefix = 'sharetask-storage-';
	
	this.get = function(key) {
		return JSON.parse(localStorage.getItem(keyPrefix + key) || '[]');
	};
	
	this.store = function(key, data) {
		console.log("Store data (data: %o) to local storage (key: %s)", data, keyPrefix + key);
		localStorage.setItem(keyPrefix + key, JSON.stringify(data));
	};
	
	this.remove = function(key) {
		console.log("Remove from local storage (key: %s)", keyPrefix + key);
		localStorage.removeItem(keyPrefix + key);
	};
});
