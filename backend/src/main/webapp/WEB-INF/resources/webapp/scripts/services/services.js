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


shareTaskApp.service('User', ['$rootScope', '$location', '$window', 'LocalStorage', '$resource', '$http', function($rootScope, $location, $window, LocalStorage, $resource, $http) {
	
	this.authenticate = function(user, success, error) {
		Log.debug("Login user (user: %o)", user);
		return $http.post($rootScope.appBaseUrl+"api/user/login", {username: user.username, password: user.password}).success(success).error(error);
	};
	
	this.getCurrentUser = function(success, error) {
		Log.debug("Getting current loged in user from server");
		return $http.get($rootScope.appBaseUrl+"api/user").success(success).error(error);
	};
	
	this.create = function(input, success, error) {
		Log.debug("Create user (user: %o)", input.user);
		return $http.post($rootScope.appBaseUrl+"api/user", input.user).success(success).error(error);
	};
	
	this.update = function(input, success, error) {
		Log.debug("Update user (user: %o)", input.user);
		return $http.put($rootScope.appBaseUrl+"api/user", input.user).success(success).error(error);
	};
	
	this.logout = function(success, error) {
		Log.debug("Logout user");
		return $http.get($rootScope.appBaseUrl+"api/user/logout").success(success).error(error);
	};
}]);


shareTaskApp.service('Workspace', ['$rootScope', '$resource', '$http', function($rootScope, $resource, $http) {
	
	this.find = function(input, success, error) {
		Log.debug("Getting workspaces from server for type (type: %s)", input.type);
		return $http.get($rootScope.appBaseUrl+"api/workspace/", {params: {type: input.type}}).success(success).error(error);
	};
	
	this.getActiveTasks = function(input, success, error) {
		Log.debug("Getting tasks for workspace (id: %s) from server", input.workspaceId);
		return $http.get($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task", {params: {taskQueue: 'ALL'}}).success(success).error(error);
	};
	
	this.getCompletedTasks = function(input, success, error) {
		Log.debug("Getting completed tasks for workspace (id: %s) from server", input.workspaceId);
		return $http.get($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task", {params: {taskQueue: 'FINISHED'}}).success(success).error(error);
	};
	
	this.create = function(input, success, error) {
		Log.debug("Create workspace (workspace: %o)", input.workspace);
		return $http.post($rootScope.appBaseUrl+"api/workspace", input.workspace).success(success).error(error);
	};
	
	this.update = function(input, success, error) {
		Log.debug("Update workspace (workspace: %o)", input.workspace);
		return $http.put($rootScope.appBaseUrl+"api/workspace", input.workspace).success(success).error(error);
	};
	
	this.remove = function(input, success, error) {
		Log.debug("Delete workspace (id: %s)", input.workspace.id);
		return $http.delete($rootScope.appBaseUrl+"api/workspace/"+input.workspace.id, {}).success(success).error(error);
	};
	
	this.inviteMember = function(input, success, error) {
		Log.debug("Invite new member (user: %o) to workspace (id: %o)", input.user, input.workspaceId);
		return $http.post($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/invite", {username: input.user.username}).success(success).error(error);
	};
	
	this.removeMember = function(input, success, error) {
		Log.debug("Delete member (username: %o) from workspace (id: %o)", input.username, input.workspaceId);
		return $http.post($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/member/delete", {username: input.username}).success(success).error(error);
	};
}]);


shareTaskApp.service('Task', ['$rootScope', '$resource', '$http', function($rootScope, $resource, $http) {
	
	this.create = function(input, success, error) {
		Log.debug("Create task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.post($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
	
	this.update = function(input, success, error) {
		Log.debug("Update task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.put($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
	
	this.remove = function(input, success, error) {
		Log.debug("Delete task (id: %s) on workspace (id: %s)", input.taskId, input.workspaceId);
		return $http.delete($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task/"+input.taskId, {}).success(success).error(error);
	};
	
	this.complete = function(input, success, error) {
		Log.debug("Complete task (id: %s) on workspace (id: %s)", input.taskId, input.workspaceId);
		return $http.post($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/complete", {}).success(success).error(error);
	};
	
	this.forward = function(input, success, error) {
		Log.debug("Forward task (id: %s) on workspace (id: %s) to user (id: %s)", input.taskId, input.workspaceId, input.username);
		return $http.post($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/forward", {username: input.username}).success(success).error(error);
	};
	
	this.getComments = function(input, success, error) {
		Log.debug("Getting task (id: %s) comments from server", input.taskId);
		return $http.get($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/comment", {}).success(success).error(error);
	};
	
	this.addComment = function(input, success, error) {
		Log.debug("Adding new comment (comment: %o) to task (id: %s)", input.comment, input.taskId);
		return $http.post($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/comment", input.comment).success(success).error(error);
	};
	
	this.getEvents = function(input, callback) {
		Log.debug("Getting task (id: %s) events from server", input.taskId);
		return $http.get($rootScope.appBaseUrl+"api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/event", {}).success(success).error(error);
	};
}]);


shareTaskApp.service('Gravatar', ['$rootScope', '$http', function($rootScope, $http) {
	
	this.get = function(input, success, error) {
		Log.debug("Getting profile data for user (id: %s) from server", input.hash);
		return $http.jsonp("http://www.gravatar.com/"+input.hash+".json?callback=JSON_CALLBACK").success(success).error(error);
	};
}]);


shareTaskApp.service('Logger', ['$rootScope', function($rootScope) {
	
	this.init = function(logLevel) {
		window.Log = window.Log || {};
		
		Log['error'] = function () {
			if (!window.console) return; // prevents errors on IE
			if (logLevel === 'debug' || logLevel === 'info' || logLevel === 'warn' || logLevel === 'error') {
				console['error'].apply(console, arguments);
			}
		};
	    
		Log['warn'] = function () {
			if (!window.console) return; // prevents errors on IE
			if (logLevel === 'debug' || logLevel === 'info' || logLevel === 'warn') {
				console['warn'].apply(console, arguments);
			}
		};
	    
		Log['info'] = function () {
			if (!window.console) return; // prevents errors on IE
			if (logLevel === 'debug' || logLevel === 'info') {
				console['info'].apply(console, arguments);
			}
		};
	    
		Log['debug'] = function () {
			if (!window.console) return; // prevents errors on IE
			if (logLevel === 'debug') {
				console['log'].apply(console, arguments);
			}
		};
	};
}]);


shareTaskApp.service('ErrorHandling', ['$rootScope', 'User', function($rootScope, User) {
	
	this.handle = function(data, status, script, func) {
		Log.debug("Handle error! data: %o, status: %o, function: %o", data, status, func);
		if (status == 403) {
			// logout user
			User.logout();
		}
		else {
			$rootScope.errorConsole = {show: true, data: data, status: status, func: func};
		}
	};
}]);


shareTaskApp.service('Utils', function($resource, $rootScope) {
	
	this.isCookieEnabled = function() {
		Log.debug("isCookieEnabled called");
		var cookieEnabled = (navigator.cookieEnabled) ? true : false;
		if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled) {
			document.cookie="testcookie";
			cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
		}
		return (cookieEnabled);
	};
	
	this.validateEmail = function(email) {
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if (!filter.test(email)) {
			return false;
		}
		return true;
	};
});


shareTaskApp.service('LocalStorage', function($resource) {
	
	var keyPrefix = 'sharetask-storage-';
	
	this.get = function(key) {
		return JSON.parse(localStorage.getItem(keyPrefix + key) || '[]');
	};
	
	this.store = function(key, data) {
		Log.debug("Store data (data: %o) to local storage (key: %s)", data, keyPrefix + key);
		localStorage.setItem(keyPrefix + key, JSON.stringify(data));
	};
	
	this.remove = function(key) {
		Log.debug("Remove from local storage (key: %s)", keyPrefix + key);
		localStorage.removeItem(keyPrefix + key);
	};
});
