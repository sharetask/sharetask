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

/* Directives */
angular.module('shareTaskApp.directives', []).
	directive('appVersion', ['version', function(version) {
		return function(scope, elm, attrs) {
			elm.text(version);
		};
	}])
	.directive('stopEvent', function () {
		return {
			restrict: 'A',
			link: function (scope, element, attr) {
				element.bind(attr.stopEvent, function (e) {
					e.stopPropagation();
				});
			}
		};
	})
	.directive('shortcut', function() {
		return {
			restrict: 'E',
			replace: true,
			scope: true,
			link: function postLink(scope, iElement, iAttrs) {
				jQuery(document).on('keypress', function(e) {
					scope.$apply(scope.keyPressed(e));
				});
			}
		};
	})
	.directive('jquiDatepicker', function() {
		return {
			restrict: 'E',
			link: function(scope, elm, attrs) {
				// Lookup attrributes
				var model = attrs.model || null;
				var fnSelected = attrs.selected || null;
				var show = attrs.show || null;
				var options = attrs.options || null;
				var baseOptions = scope.$eval(options) || null;
				var dpCtrl = null;

				// init model to null
				scope[model] = null;

				// return an object with options for the datepicker control
				function buildOptions() {
					var opts = baseOptions || {};
					opts.onSelect = function(dateText, inst) { 
						if (model)
							scope.$apply(attrs.model+"='"+ dateText+"'");
						if (fnSelected)
							scope.$apply(fnSelected);
					};

					opts.onClose = function(dateText, inst) {
						closePicker();
					};

					return opts;
				}

				// close picker control and remove any related DOM elements 
				function closePicker() {
					if (dpCtrl) {
						dpCtrl.datepicker('destroy');
						dpCtrl.remove();
						dpCtrl = null;
					}
				}

				// create and show datepicker control
				function openPicker() {
					elm.append('<div class="datepicker"></div>');
					dpCtrl = elm.find('.datepicker');
					dpCtrl.datepicker( buildOptions() );
					dpCtrl.datepicker('option', $.datepicker.regional);
				}


				// defines a watch on the show attribute, if one was provided.
				// otherwise, always display the control
				if (show) {
					scope.$watch(show, function(show) {

						if (show) {
							openPicker();
						}
						else {
							closePicker();
						}
					});
				}
				else {
					openPicker();
				}

				if (options) {
					scope.$watch(options, function(newOptions) {
						if (dpCtrl) {
							// update our baseOptions object
							baseOptions = newOptions;
							dpCtrl.datepicker('option', buildOptions());
						}
					});
				}
				
				scope.$watch(model, function(newModel, oldModel) {
					/*
					Log.debug("new model, %o", newModel);
					Log.debug("new model date, %o", new Date(newModel));
					Log.debug("old model, %o", oldModel);
					*/
					var oldDate = new Date(oldModel);
					var newDate = new Date(newModel);
					if (oldModel !== undefined) {
						newDate.setHours(oldDate.getHours());
						newDate.setMinutes(oldDate.getMinutes());
					}
					Log.debug("new date, %o", newDate);
					/*
					var hourOptions = newDate.getHours()+":"+newDate.getMinutes();
					Log.debug("options, %o", baseOptions.dateFormat+"'T"+hourOptions+"'");
					dpCtrl.datepicker('option', baseOptions.dateFormat+"'T17:00'");
					*/
					dpCtrl.datepicker("setDate", newDate);
				});
			}
		};
	})
	.directive('appMenu', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/app-menu.html',
			link: function(scope, element, attrs) {
				//Log.debug("scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', '$window', 'User', 'ErrorHandling', 'LocalStorage', function($rootScope, $scope, $element, $attrs, $transclude, $location, $window, User, ErrorHandling, LocalStorage) {
				
				$rootScope.showHelp = false;
				$scope.currentPage = $rootScope.currentPage;
				Log.debug("current page: %o", $rootScope.currentPage);
				
				$scope.toggleHelp = function() {
					$rootScope.showHelp = !$rootScope.showHelp;
				};
				
				$scope.logout = function() {
					Log.debug("Logout user: %s", $rootScope.loggedUser.username);
					User.logout(function(data, status) {
						Log.debug("User logout success! data: %o, status: %o", data, status);
						$rootScope.loggedUser = {};
						LocalStorage.remove('logged-user');
						$window.location.href = $rootScope.appBaseUrl;
					}, function(data, status, script, func) {
						Log.debug("User logout error! data: %o, status: %o", data, status);
						ErrorHandling.handle(data, status, script, func);
					});
				};
			}]
		};
	}])
	.directive('errorConsole', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/error-console.html',
			link: function(scope, element, attrs) {
				//Log.debug("ErrorConsole - scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', '$window', function($rootScope, $scope, $element, $attrs, $transclude, $location, $window) {
				Log.debug("ErrorConsole - scope: %o, element: %o, attrs: %o", $scope, $element, $attrs);
				
				$scope.opts = {backdropFade: true, dialogFade: true};
				
				$scope.open = function () {
					$scope.shouldBeOpenEC = true;
				};
				
				$scope.send = function () {
					Log.debug("ErrorConsole - send");
					var body = {user: $rootScope.loggedUser.username, currentPage: $rootScope.currentPage, data: $scope.data, status: $scope.status, func: $scope.func};
					$window.location.href = 'mailto:' + $rootScope.mailSupport + '?subject=ShareTa.sk error ' + '&body=' + angular.toJson(body, true);
					
					$rootScope.errorConsole = {show: false, data: '', status: 0, func: {}};
				};
				
				$scope.closeWindow = function () {
					Log.debug("ErrorConsole - closeWindow");
					$scope.shouldBeOpenEC = false;
					$rootScope.errorConsole = {show: false, data: '', status: 0, func: {}};
				};
				
				$scope.$watch($attrs.show, function(newModel, oldModel) {
					Log.debug("ErrorConsole - show: %o, %o", newModel, oldModel);
					$scope.shouldBeOpenEC = newModel;
				});
				
				$scope.$watch($attrs.data, function(newModel, oldModel) {
					Log.debug("ErrorConsole - data: %o, %o", newModel, oldModel);
					$scope.data = newModel;
				});
				
				$scope.$watch($attrs.status, function(newModel, oldModel) {
					Log.debug("ErrorConsole - status: %o, %o", newModel, oldModel);
					$scope.status = newModel;
				});
				
				$scope.$watch($attrs.func, function(newModel, oldModel) {
					Log.debug("ErrorConsole - func: %o, %o", newModel, oldModel);
					$scope.func = newModel;
				});
			}]
		};
	}])
	.directive('firstWorkspaceWindow', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/first-workspace-window.html',
			link: function(scope, element, attrs) {
				//Log.debug("FirstWorkspaceWindow - scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', 'Workspace', 'Analytics', function($rootScope, $scope, $element, $attrs, $transclude, $location, Workspace, Analytics) {
				Log.debug("FirstWorkspaceWindow - scope: %o, element: %o, attrs: %o", $scope, $element, $attrs);
				
				$scope.opts = {backdropFade: true, dialogFade: true};
				
				$scope.open = function () {
					$scope.shouldBeOpenFWW = true;
				};
				
				/**
				 * Add new workspace.
				 * User adds workspace title only. All others attributes are set to default values.
				 * Workspace data are stored to server.
				 */
				$scope.addWorkspace = function() {
					Log.debug("Add new workspace (workspaceTitle: %s)", $scope.newWorkspaceTitle);
					var workspace = {title: $scope.newWorkspaceTitle, owner: {username: $scope.user.username}};
					Log.debug("Workspace: %o", workspace);
					Workspace.create({workspace: workspace}, function(data, status) {
							Log.debug("Workspace create success! data: %o, status: %o", data, status);
							$scope.newWorkspaceTitle = '';
							$rootScope.firstWorkspaceWindow = {show: false};
							$scope.$emit('EVENT-RELOAD-WORKSPACES');
							Analytics.trackEvent('Workspace', 'add', 'success', status);
						}, function(data, status, script, func) {
							Log.debug("Workspace create error!");
							ErrorHandling.handle(data, status, script, func);
							Analytics.trackEvent('Workspace', 'add', 'error', status);
						});
				};
				
				$scope.close = function () {
					$scope.shouldBeOpen = false;
					$rootScope.firstWorkspaceWindow = {show: false};
				};
				
				$scope.$watch($attrs.show, function(newModel, oldModel) {
					Log.debug("FirstWorkspaceWindow - show: %o, %o", newModel, oldModel);
					$scope.shouldBeOpenFWW = newModel;
				});
				
				$scope.$watch($attrs.user, function(newModel, oldModel) {
					Log.debug("FirstWorkspaceWindow - user: %o, %o", newModel, oldModel);
					$scope.user = newModel;
				});
			}]
		};
	}])
	.directive('calendar', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/calendar.html',
			scope: {tasks:'=model'},
			link: function(scope, element, attrs) {
				//Log.debug("Calendar - scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', 'LocalStorage', function($rootScope, $scope, $element, $attrs, $transclude, $location, LocalStorage) {
				Log.debug("Calendar - scope: %o, element: %o, attrs: %o", $scope, $element, $attrs);
				
				$scope.todayDate = new Date();
				$scope.todayDay = $scope.todayDate.getDay();
				Log.debug("Calendar - today day: %s", $scope.todayDay);
				
				$scope.startDate = new Date(new Date().setDate($scope.todayDate.getDate() - $scope.todayDay + 1));
				Log.debug("Calendar - start date: %o", new Date($scope.startDate));
				
				$scope.days = [];
				for (var i = 0; i < 7; i++) {
					$scope.days.push(new Date(new Date().setDate($scope.startDate.getDate() + i)));
				}
				Log.debug("Calendar - days: %o", $scope.days);
				
				$scope.$watch('tasks', function(newModel, oldModel) {
					Log.debug("new model, %o", newModel);
					Log.debug("old model, %o", oldModel);
					if (newModel !== undefined && oldModel === undefined) {
						$scope.tasks = newModel;
						Log.debug("Calendar - tasks: %o", $scope.tasks);
					}
				});
				
			}]
		};
	}])
	.directive('focus', function() {
		return {
			//scope: {trigger: '=ngModel'},
			link: function(scope, element, attrs) {
				//Log.debug("Focus - scope: %o, element: %o, attrs: %o", scope, element, attrs);
				//Log.debug("Focus - attrs.focus: %s", attrs.focus);
				scope.$watch(attrs.focus, function(value) {
					//Log.debug("Focus - scope: %o, element: %o, attrs: %o", scope, element, attrs);
					//Log.debug("Focus - trigger value: %o", value);
					if (value === true) {
						element[0].focus();
						//scope.trigger = false;
						attrs.focus = false;
					}
				});
			}
		};
	})
	.directive('help', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/help.html',
			link: function(scope, element, attrs) {
				//Log.debug("scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', function($rootScope, $scope, $element, $attrs, $transclude, $location) {
				
				$scope.currentPage = $rootScope.currentPage;
				Log.debug("current page: %o", $location.path());
			}]
		};
	}])
	;
