(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .directive('resize', resize);

    resize.$inject = ['$window'];

    function resize($window) {
        return function (scope, element) {
            var w = angular.element($window);
            scope.getWindowDimensions = function () {
                return {
                    'h': w.height()
                };
            };
            scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
                scope.windowHeight = newValue.h;
                scope.windowWidth = newValue.w;

                scope.style = function () {
                    return {
                        'min-height': (newValue.h) + 'px',
                    };

                };

            }, true);

            w.bind('resize', function () {
                scope.$apply();
            });
        };
    }
})();
