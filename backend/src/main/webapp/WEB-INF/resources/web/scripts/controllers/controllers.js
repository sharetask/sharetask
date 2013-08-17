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

/* Controllers */
angular.module('shareTaskWeb.controllers', [])
	.controller('AuthCtrl', ['$scope', '$location', '$rootScope', '$window', 'User', 'LocalStorage', function($scope, $location, $rootScope, $window, User, LocalStorage) {
		
		$scope.loginData = {processing: false, result: 0};
		
		/**
		 * Login user.
		 * User is redirected to tasks page.
		 */
		$scope.login = function() {
			console.log("Login user (username: %s) with password (password: %s)", $scope.user.username, $scope.user.password);
			$scope.loginData.processing = true;
			User.authenticate({username: $scope.user.username, password: $scope.user.password}, function(data, status) {
					console.log("User auth success! data: %o, status: %o", data, status);
					// get user profile info
					User.get({username: $scope.user.username}, function(data, status) {
							console.log("User get success! data: %o, status: %o", data, status);
							$rootScope.loggedUser = data;
							//$rootScope.loggedUser.password = $scope.user.password;
							LocalStorage.store('logged-user', $rootScope.loggedUser);
							$window.location.href = $rootScope.appBaseUrl+"webapp#/tasks";
						}, function(data, status) {
							console.log("Auth error! data: %o, status: %o", data, status);
							$scope.user = {};
							$rootScope.loggedUser = {};
							LocalStorage.remove('logged-user');
							$scope.loginData.processing = false;
						});
					$scope.loginData.result = 1;
				}, function(data, status) {
					console.log("User auth error! data: %o, status: %o", data, status);
					$scope.user = {};
					$rootScope.loggedUser = {};
					LocalStorage.remove('logged-user');
					$scope.loginData.result = -1;
					$scope.loginData.processing = false;
				});
		};
	}])
	.controller('RegisterCtrl', ['$scope', '$location', '$rootScope', '$window', 'User', 'LocalStorage', function($scope, $location, $rootScope, $window, User, LocalStorage) {
		
		$scope.newAccountData = {processing: false, result: 0};
		
		/**
		 * Register user.
		 * User is redirected to tasks page.
		 */
		$scope.register = function() {
			console.log("Register user (username: %s)", $scope.newAccount.username);
			$scope.newAccountData.processing = true;
			$scope.newAccount.password = $scope.newAccount.password1;
			delete($scope.newAccount.password1);
			delete($scope.newAccount.password2);
			User.create({user: $scope.newAccount}, function(data, status) {
					console.log("User create success! data: %o, status: %o", data, status);
					$scope.newAccount = {name: '', surName: '', email: ''};
					$scope.newAccountData.result = 1;
					$scope.newAccountData.processing = false;
					// authenticate user
					/*
					User.authenticate({username: $scope.newAccount.username, password: $scope.newAccount.password}, function(data, status) {
							console.log("User auth success! data: %o, status: %o", data, status);
							$rootScope.loggedUser = {username: $scope.newAccount.username, name: $scope.newAccount.name, surName: $scope.newAccount.surName, password: $scope.newAccount.password};
							LocalStorage.store('logged-user', $rootScope.loggedUser);
							$window.location.href = $rootScope.appBaseUrl+"webapp#/tasks";
							$scope.newAccountData.result = 1;
							$scope.newAccountData.processing = false;
						}, function(data, status) {
							console.log("User auth error! data: %o, status: %o", data, status);
							$rootScope.loggedUser = {};
							LocalStorage.remove('logged-user');
							$scope.newAccountData.result = -1;
							$scope.newAccountData.processing = false;
						});
					*/
				}, function(data, status) {
					console.log("User create error! data: %o, status: %o", data, status);
					$scope.newAccountData.result = -1;
					$scope.newAccountData.processing = false;
					if (data.type === 'USER_ALREADY_EXISTS') {
						$scope.newAccountData.result = -2;
					}
				});
		};
	}])
	;