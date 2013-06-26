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
					//Log.debug("new model, %o", new Date(newModel));
					//Log.debug("old model, %o", oldModel);
					//dpCtrl.setDate(new Date(newModel));
					dpCtrl.datepicker("setDate", new Date(newModel));
				});
			}
		};
	})
	.directive('appMenu', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/app-menu.html',
			link: function(scope, element, attrs) {
				Log.debug("scope: %o, element: %o, attrs: %o", scope, element, attrs);
			},
			controller: ['$rootScope', '$scope', '$element', '$attrs', '$transclude', '$location', '$window', 'LocalStorage', function($rootScope, $scope, $element, $attrs, $transclude, $location, $window, LocalStorage) {
				
				$scope.currentPage = $rootScope.currentPage;
				Log.debug("current page: %o", $rootScope.currentPage);
				
				$scope.logout = function() {
					Log.debug("Logout user: %s", $rootScope.loggedUser.username);
					$rootScope.loggedUser = {};
					LocalStorage.remove('logged-user');
					//$location.path("/");
					$window.location.href = $rootScope.appBaseUrl;
				};
			}]
		};
	}])
	.directive('calendar', ['$rootScope', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl: 'resources-webapp-'+$rootScope.appVersion+'/views/components/calendar.html',
			scope: {tasks:'=model'},
			link: function(scope, element, attrs) {
				Log.debug("Calendar - scope: %o, element: %o, attrs: %o", scope, element, attrs);
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
	/*
	.directive('checkStrength', function () {
	    return {
			replace: false,
			restrict: 'EACM',
			link: function (scope, iElement, iAttrs) {

	        	var strength = {
	                colors: ['#F00', '#F90', '#FF0', '#9F0', '#0F0'],
	                mesureStrength: function (p) {

	                    var _force = 0;                    
	                    var _regex = '/[$-/:-?{-~!"^_`\[\]]/g';
	                                          
	                    var _lowerLetters = /[a-z]+/.test(p);                    
	                    var _upperLetters = /[A-Z]+/.test(p);
	                    var _numbers = /[0-9]+/.test(p);
	                    var _symbols = _regex.test(p);
	                                          
	                    var _flags = [_lowerLetters, _upperLetters, _numbers, _symbols];                    
	                    var _passedMatches = $.grep(_flags, function (el) { return el === true; }).length;                                          
	                    
	                    _force += 2 * p.length + ((p.length >= 10) ? 1 : 0);
	                    _force += _passedMatches * 10;
	                        
	                    // penality (short password)
	                    _force = (p.length <= 6) ? Math.min(_force, 10) : _force;                                      
	                    
	                    // penality (poor variety of characters)
	                    _force = (_passedMatches == 1) ? Math.min(_force, 10) : _force;
	                    _force = (_passedMatches == 2) ? Math.min(_force, 20) : _force;
	                    _force = (_passedMatches == 3) ? Math.min(_force, 40) : _force;
	                    
	                    return _force;

	                },
	                getColor: function (s) {

	                    var idx = 0;
	                    if (s <= 10) { idx = 0; }
	                    else if (s <= 20) { idx = 1; }
	                    else if (s <= 30) { idx = 2; }
	                    else if (s <= 40) { idx = 3; }
	                    else { idx = 4; }

	                    return { idx: idx + 1, col: this.colors[idx] };

	                }
	            };

	            scope.$watch(iAttrs.checkStrength, function () {
	                if (scope.pw === '') {
	                    iElement.css({ "display": "none"  });
	                } else {
	                    var c = strength.getColor(strength.mesureStrength(scope.pw));
	                    iElement.css({ "display": "inline" });
	                    iElement.children('li')
	                        .css({ "background": "#DDD" })
	                        .slice(0, c.idx)
	                        .css({ "background": c.col });
	                }
	            });

	        },
	        template: '<li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li>'
	    };
	})
	*/
	;
