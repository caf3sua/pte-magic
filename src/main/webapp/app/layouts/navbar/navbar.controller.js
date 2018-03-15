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
        vm.user = [];
        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.loggedIn = false;
        vm.notloggedIn = false;
        vm.$state = $state;
        $scope.$on('nameAccount', function(event, des) {
            vm.data = des.data;
            if(vm.data  != null){
                vm.loggedIn = true;
                vm.user.lastName = vm.data.lastName;
                vm.notloggedIn = false;
            }else {
                vm.notloggedIn = true;
                vm.loggedIn = false;
            }
        });

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
