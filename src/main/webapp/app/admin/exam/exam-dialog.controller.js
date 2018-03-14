(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamDialogController', ExamDialogController);

    ExamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Exam', 'ExamType', 'Question', 'Answer'];

    function ExamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Exam, ExamType, Question, Answer) {
        var vm = this;

        vm.exam = entity;
        vm.clear = clear;
        vm.save = save;
        vm.examtypes = ExamType.query({filter: 'exam-is-null'});
        $q.all([vm.exam.$promise, vm.examtypes.$promise]).then(function() {
            if (!vm.exam.examTypeId) {
                return $q.reject();
            }
            return ExamType.get({id : vm.exam.examTypeId}).$promise;
        }).then(function(examType) {
            vm.examtypes.push(examType);
        });
        vm.questions = Question.query();
        vm.answers = Answer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.exam.id !== null) {
                Exam.update(vm.exam, onSaveSuccess, onSaveError);
            } else {
                Exam.save(vm.exam, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pteMagicApp:examUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
