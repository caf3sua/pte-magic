(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamTypeDialogController', ExamTypeDialogController);

    ExamTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExamType'];

    function ExamTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExamType) {
        var vm = this;

        vm.examType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.examType.id !== null) {
                ExamType.update(vm.examType, onSaveSuccess, onSaveError);
            } else {
                ExamType.save(vm.examType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pteMagicApp:examTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
