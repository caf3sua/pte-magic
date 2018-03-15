(function() {
    'use strict';

    angular
        .module('testOnlineApp')
        .controller('CreateAccountController', CreateAccountController);

    CreateAccountController.$inject = ['$rootScope', '$state', '$timeout', 'Auth', '$uibModalInstance'];

    function CreateAccountController ($rootScope, $state, $timeout, Auth, $uibModalInstance) {
        var vm = this;
    }
})();
