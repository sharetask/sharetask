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
	.directive('focus', function() {
		console.log("focus");
	    return function(scope, element, attrs) {
	        scope.$watch(attrs.focus, function(newValue) {
	            newValue && element[0].focus();
	        });
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
					//console.log("new model, %o", new Date(newModel));
					//console.log("old model, %o", oldModel);
					//dpCtrl.setDate(new Date(newModel));
					dpCtrl.datepicker("setDate", new Date(newModel));
				});
			}
		};
	})
	.directive('sharetaskMenu', function() {
		return {
			restrict: 'E',
			templateUrl: 'resources-1.0.0/views/components/menu.html',
			link: function(scope, element, attrs) {
				console.log("scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', 'LocalStorage', function($rootScope, $scope, $element, $attrs, $transclude, $location, LocalStorage) {
				
				$scope.currentPage = $rootScope.currentPage;
				console.log("current page: %o", $rootScope.currentPage);
				
				$scope.logout = function() {
					console.log("Logout user: %s", $rootScope.loggedUser.username);
					$rootScope.loggedUser = {};
					LocalStorage.remove('logged-user');
					$location.path("/");
				};
			}]
		};
	})
	;
