(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('MockTestController', MockTestController);

    MockTestController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout'];

    function MockTestController ($scope, Principal, LoginService, $state, $rootScope, $timeout) {
        var vm = this;
    }
})();
