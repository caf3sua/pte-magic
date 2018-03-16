(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FreeSampleController', FreeSampleController);

    FreeSampleController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout'];

    function FreeSampleController ($scope, Principal, LoginService, $state, $rootScope, $timeout) {
        var vm = this;
        vm.listeningList = listeningList;
        vm.listQuestionShow = false;
        function listeningList() {
            $timeout(function (){
                angular.element('#listening').addClass("active");
                angular.element('#listeningMobile').addClass("activeMobile");
            });
            vm.listQuestionShow = true;
        }
    }
})();
