(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('AdminLoginController', AdminLoginController);

    AdminLoginController.$inject = ['$rootScope', '$state', '$timeout', 'Auth'];

    function AdminLoginController ($rootScope, $state, $timeout, Auth) {
        var vm = this;

        vm.authenticationError = false;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
        vm.username = null;

        $timeout(function (){angular.element('#username').focus();});

        function login (event) {
            event.preventDefault();
            
            Auth.logout();
            
            Auth.login({
                username: vm.username,
                password: vm.password,
                role: 'ROLE_ADMIN'
            }).then(function () {
                vm.authenticationError = false;

                $rootScope.$broadcast('authenticationSuccess');

                $state.go('user-management');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is successful, go to stored previousState and clear previousState
//                if (Auth.getPreviousState()) {
//                    var previousState = Auth.getPreviousState();
//                    Auth.resetPreviousState();
//                    $state.go(previousState.name, previousState.params);
//                }
            }).catch(function () {
                vm.authenticationError = true;
            });
        }
    }
})();
