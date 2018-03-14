(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamDetailController', ExamDetailController);

    ExamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Exam', 'ExamType', 'Question', 'Answer'];

    function ExamDetailController($scope, $rootScope, $stateParams, previousState, entity, Exam, ExamType, Question, Answer) {
        var vm = this;

        vm.exam = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pteMagicApp:examUpdate', function(event, result) {
            vm.exam = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
