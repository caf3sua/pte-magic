(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function TestController ($scope, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
        // Init controller
  		(function initController() {
  		})();
  		
  		
    }
})();
