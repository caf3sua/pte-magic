(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamTypeDetailController', ExamTypeDetailController);

    ExamTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExamType'];

    function ExamTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, ExamType) {
        var vm = this;

        vm.examType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pteMagicApp:examTypeUpdate', function(event, result) {
            vm.examType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
