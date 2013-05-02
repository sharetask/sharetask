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
	;
