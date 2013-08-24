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
angular.module('shareTaskWeb.directives', [])
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
	.directive('autoFillableField', function() {
		return {
			restrict: "A",
			require: "?ngModel",
			link: function(scope, element, attrs, ngModel) {
				setInterval(function() {
					if (!(element.val()=='' && ngModel.$pristine)) {
						scope.$apply(function() {
							ngModel.$setViewValue(element.val());
						});
					}
				}, 300);
			}
		};
	})
	;
