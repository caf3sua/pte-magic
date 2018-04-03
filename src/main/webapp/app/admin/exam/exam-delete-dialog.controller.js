(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamDeleteController',ExamDeleteController);

    ExamDeleteController.$inject = ['$uibModalInstance', 'entity', 'Exam'];

    function ExamDeleteController($uibModalInstance, entity, Exam) {
        var vm = this;

        vm.exam = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	console.log('delete: ' + id);
            Exam.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
