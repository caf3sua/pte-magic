(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('CreateAccountController', CreateAccountController);

    CreateAccountController.$inject = ['$rootScope', '$state', '$timeout', 'Auth', '$uibModalInstance', 'createAccountService'];

    function CreateAccountController ($rootScope, $state, $timeout, Auth, $uibModalInstance, createAccountService) {
        var vm = this;
        vm.createUser = createUser;
        vm.onSaveSuccess = onSaveSuccess;
        vm.onSaveFail = onSaveFail;
        vm.user ={
            activated : true,
        }
        function createUser() {
            createAccountService.save(vm.user, onSaveSuccess, onSaveFail);
        }
        function onSaveSuccess() {
            alert("Thanh cong");
        }
        function onSaveFail() {

        }
    }
})();
