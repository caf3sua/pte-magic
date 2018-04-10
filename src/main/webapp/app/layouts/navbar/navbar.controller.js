(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', '$scope', '$timeout'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, $scope, $timeout) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.showActive = showActive;
        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });
        vm.user;
        vm.login = login;
        vm.logout = logout;
        vm.logoutAdmin = logoutAdmin;
        vm.register = register;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.loggedIn = false;
        vm.notloggedIn = false;
        vm.$state = $state;
        vm.delActive = delActive;
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
        showActive();
        function showActive(type) {
            if(type == 'PRACTICE'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("dropdown")).removeClass("active");
                    angular.element('#Home').removeClass("active");
                    angular.element('#PRACTICE').addClass("active");
                });

                vm.selectedExams = vm.listeningExams;
            }else if(type == 'USER'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("dropdown")).removeClass("active");
                    angular.element('#Home').removeClass("active");
                    angular.element('#USER').addClass("active");
                });
            }
        }

        function register() {
            collapseNavbar();
            LoginService.openRegisterForm();
        }

        function login() {
        	Auth.logout();

            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }
        function logoutAdmin() {
            collapseNavbar();
            Auth.logout();
            $state.go('login');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
        function delActive() {
            $timeout(function () {
                angular.element(document.getElementsByClassName("checkActive")).removeClass("active");
                angular.element('#Home').addClass("active");
            })
        }
    }
})();
