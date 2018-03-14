(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope'];

    function HomeController ($scope, Principal, LoginService, $state, $rootScope) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                $rootScope.$broadcast('nameAccount',{data: vm.account});
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
