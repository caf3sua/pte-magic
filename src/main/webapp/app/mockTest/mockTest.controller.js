(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('MockTestController', MockTestController);

    MockTestController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout'];

    function MockTestController ($scope, Principal, LoginService, $state, $rootScope, $timeout) {
        var vm = this;
        vm.pastA = pastA;
        vm.listQuestionShow = false;
        function pastA() {
            $timeout(function (){
                angular.element('#pastA').addClass("active");
                angular.element('#pastAMobile').addClass("activeMobile");
            });
            vm.listQuestionShow = true;
        }
    }
})();
