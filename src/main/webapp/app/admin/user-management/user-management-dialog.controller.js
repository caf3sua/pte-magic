(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService'];

    function UserManagementDialogController ($scope, $stateParams, $uibModalInstance, entity, User, JhiLanguageService) {
        var vm = this;

        vm.authorities = ['ROLE_FREE_MEMBER', 'ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;
        
        $scope.$watch('vm.user.authorities', function () {
            console.log(vm.user.authorities);
        	if (!$.inArray('ROLE_FREE_MEMBER', vm.user.authorities)) {
        		vm.user.remainDays = null;
            } else {
            	if (vm.user.remainDays == null) {
            		vm.user.remainDays = 120;
            	}
            }
        });

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();
