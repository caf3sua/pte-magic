(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('QuestionDetailController', QuestionDetailController);

    QuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Question', 'Exam', '$state'];

    function QuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, Question, Exam, $state) {
        var vm = this;

        vm.question = entity;
        vm.previousState = previousState.name;
        vm.checkTypeQuestion = $stateParams.selectedSkill;
        vm.back = back;
        var unsubscribe = $rootScope.$on('pteMagicApp:questionUpdate', function(event, result) {
            vm.question = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function back() {
            $state.go('question', { selectedSkill: vm.checkTypeQuestion});
        }

    }
})();
