'use strict';

/* Services */
var shareTaskApp = angular.module('shareTaskApp.services', ['ngResource']);

shareTaskApp.service('Workspace', function($resource) {
	
	this.findAll = function(callback) {
		console.log("Getting all workspaces from server");
		return $resource("/sharetask/resources-1.0.0/scripts/data/workspaces.json", {}, {query: {method: "GET", isArray: true}}).query(callback);
	};
	
	this.getTasks = function(input, callback) {
		console.log("Getting tasks for workspace (id: %s) from server", input.workspaceId);
		console.log("Get JSON file js/data/workspace-"+input.workspaceId+"-tasks.json");
		return $resource("/sharetask/resources-1.0.0/scripts/data/workspace-"+input.workspaceId+"-tasks.json", {}, {query: {method: "GET", isArray: true}}).query(callback);
	};
});

shareTaskApp.service('Task', function($resource) {
	
	this.findById = function(input, callback) {
		console.log("Getting task (id: %s) from server", input.id);
		console.log("Get JSON file js/data/task-"+input.id+".json");
		return $resource("/sharetask/resources-1.0.0/scripts/data/task-"+input.id+".json", {}, {query: {method: "GET", isArray: false}}).query(callback);
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
	
	var storageIdPrefix = 'sharetask-storage-';
	
	this.load = function(workspaceId) {
		return JSON.parse(localStorage.getItem(storageIdPrefix + workspaceId) || '[]');
	};
	
	this.store = function(workspaceId, data) {
		console.log("Store data (data: %o) for workspace (id: %s) to local storage (id: %s)", data, workspaceId, storageIdPrefix + workspaceId);
		localStorage.setItem(storageIdPrefix + workspaceId, JSON.stringify(data));
	};
});
