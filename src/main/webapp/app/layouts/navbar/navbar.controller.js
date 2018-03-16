(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', '$scope'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, $scope) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });
        vm.user;
        vm.login = login;
        vm.logout = logout;
        vm.register = register;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.loggedIn = false;
        vm.notloggedIn = false;
        vm.$state = $state;
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.user = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            collapseNavbar();
            LoginService.openRegisterForm();
        }
        
        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
