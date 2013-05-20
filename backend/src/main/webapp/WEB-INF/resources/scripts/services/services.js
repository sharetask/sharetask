/*
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
 */
'use strict';

/* Services */
var shareTaskApp = angular.module('shareTaskApp.services', ['ngResource']);


shareTaskApp.service('User', ['$rootScope', '$location', 'LocalStorage', '$resource', '$http', function($rootScope, $location, LocalStorage, $resource, $http) {
	
	this.authenticate = function(user, success, error) {
		console.log("Login user (user: %o)", user);
		//return $http.post('/sharetask/api/user/login', {username: 'dev1@shareta.sk', password: 'password'}).success(success).error(error);
		return $http.post('/sharetask/api/user/login', {username: user.username, password: user.password}).success(success).error(error);
	};
	
	this.logout = function() {
		console.log("Logout user: %s", $rootScope.loggedUser.username);
		$rootScope.loggedUser = {};
		LocalStorage.remove('logged-user');
		$location.path("/");
	};
}]);


shareTaskApp.service('Workspace', function($resource, $http) {
	
	this.find = function(input, success, error) {
		console.log("Getting workspaces from server for type (type: %s)", input.type);
		//var workspace = $resource("/sharetask/api/workspace", {type: input.type}, {query: {method: "GET", isArray: true}});
		//workspace.query(function(response){return success(response);}, function(response){return error(response);});
		return $http.get("/sharetask/api/workspace/", {params: {type: input.type}}).success(success).error(error);
	};
	
	this.getActiveTasks = function(input, success, error) {
		console.log("Getting active tasks for workspace (id: %s) from server", input.workspaceId);
		//var task = $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'ALL'}, {query: {method: "GET", isArray: true}});
		//task.query(function(response){return success(response);}, function(response){return error(response);});
		return $http.get("/sharetask/api/workspace/"+input.workspaceId+"/task", {params: {taskQueue: 'ALL'}}).success(success).error(error);
	};
	
	this.getCompletedTasks = function(input, success, error) {
		console.log("Getting completed tasks for workspace (id: %s) from server", input.workspaceId);
		//return $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'FINISHED'}, {query: {method: "GET", isArray: true}}).query(callback);
		return $http.get("/sharetask/api/workspace/"+input.workspaceId+"/task", {params: {taskQueue: 'FINISHED'}}).success(success).error(error);
	};
	
	this.create = function(input, success, error) {
		console.log("Create workspace (workspace: %o)", input.workspace);
		return $http.post("/sharetask/api/workspace/", input.workspace).success(success).error(error);
	};
	
	this.update = function(input, success, error) {
		console.log("Update workspace (workspace: %o)", input.workspace);
		return $http.put("/sharetask/api/workspace", input.workspace).success(success).error(error);
	};
	
	this.inviteMember = function(input, success, error) {
		// FIXME Change call after implementation of back-end operation.
		console.log("invite new member (user: %o) to workspace (id: %o)", input.user, input.workspaceId);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/member", {username: input.user.username}).success(success).error(error);
	};
	
	this.removeMember = function(input, success, error) {
		console.log("Delete member (username: %o) from workspace (id: %o)", input.username, input.workspaceId);
		return $http.delete("/sharetask/api/workspace/"+input.workspaceId+"/member/"+input.username, input.user).success(success).error(error);
	};
});


shareTaskApp.service('Task', function($resource, $http) {
	
	this.create = function(input, success, error) {
		console.log("Create task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
	
	this.update = function(input, success, error) {
		console.log("Update task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.put("/sharetask/api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
	
	this.complete = function(input, success, error) {
		console.log("Complete task (id: %s) on workspace (id: %s)", input.taskId, input.workspaceId);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/complete", {}).success(success).error(error);
	};
	
	this.forward = function(input, success, error) {
		console.log("Forward task (id: %s) on workspace (id: %s) to user (id: %s)", input.taskId, input.workspaceId, input.username);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/forward", {assignee: input.username}).success(success).error(error);
	};
	
	this.getComments = function(input, success, error) {
		console.log("Getting task (id: %s) comments from server", input.taskId);
		//return $resource("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/comment", {}, {query: {method: "GET", isArray: true}}).query(callback);
		return $http.get("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/comment", {}).success(success).error(error);
	};
	
	this.getEvents = function(input, callback) {
		console.log("Getting task (id: %s) events from server", input.taskId);
		//return $resource("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/event", {}, {query: {method: "GET", isArray: true}}).query(callback);
		return $http.get("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/event", {}).success(success).error(error);
	};
});


shareTaskApp.service('Logger', function($log) {
	
	this.debug = function() {
		
	};
});


shareTaskApp.service('ErrorHandling', ['User', function(User) {
	
	this.handle = function(data, status) {
		console.log("Handle error! data: %o, status: %o", data, status);
		if (status == 403) {
			// logout user
			User.logout();
		}
	};
}]);


shareTaskApp.service('Utils', function($resource, $rootScope) {
	
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
