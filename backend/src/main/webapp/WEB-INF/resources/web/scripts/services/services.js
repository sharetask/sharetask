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
var shareTaskWeb = angular.module('shareTaskWeb.services', ['ngResource']);


shareTaskWeb.service('User', ['$rootScope', '$location', 'LocalStorage', '$resource', '$http', function($rootScope, $location, LocalStorage, $resource, $http) {
	
	this.authenticate = function(user, success, error) {
		console.log("Login user (user: %o)", user);
		return $http.post($rootScope.appBaseUrl+"api/user/login", {username: user.username, password: user.password}).success(success).error(error);
	};
	
	this.get = function(input, success, error) {
		console.log("Getting user (id: %s) from server", input.username);
		return $http.get($rootScope.appBaseUrl+"api/user/"+input.username, {}).success(success).error(error);
	};
	
	this.create = function(input, success, error) {
		console.log("Create user (user: %o)", input.user);
		return $http.post($rootScope.appBaseUrl+"api/user", input.user).success(success).error(error);
	};
}]);


shareTaskWeb.service('Logger', function($log) {
	
	this.debug = function() {
		
	};
});


shareTaskWeb.service('ErrorHandling', ['User', function(User) {
	
	this.handle = function(data, status) {
		console.log("Handle error! data: %o, status: %o", data, status);
		if (status == 403) {
			// logout user
			User.logout();
		}
	};
}]);


shareTaskWeb.service('LocalStorage', function($resource) {
	
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
