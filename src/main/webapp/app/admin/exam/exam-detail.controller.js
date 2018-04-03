(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamDetailController', ExamDetailController)
        app.filter('trusted', ['$sce', function ($sce) {
		    return function(url) {
		        return $sce.trustAsResourceUrl(url);
		    };
		}]);

    ExamDetailController.$inject = ['$scope', '$state', '$timeout', '$rootScope', '$stateParams', 'previousState', 'entity', 'Exam', 'ExamType', 'Question', 'Answer', '$sce'];

    function ExamDetailController($scope, $state, $timeout, $rootScope, $stateParams, previousState, entity, Exam, ExamType, Question, Answer, $sce) {
        var vm = this;

        vm.exam = entity;
        vm.previousState = previousState.name;
        vm.updateAnswer = updateAnswer;
        vm.finishMarkingExam = finishMarkingExam;
        
        angular.element(document).ready(function () {
    		$timeout(function(){
    			angular.forEach(vm.exam.answerQuestions, function(value, key){
    				// TODO
  	            });
    		}, 1000 );
        });
        
        function finishMarkingExam() {
        	Exam.finishMarkingExam({
	  				id: vm.exam.examDTO.id
	            }, onSuccessFinish, onErrorFinish);
	            function onSuccessFinish(data, headers) {
	            	$state.go('exam', null, { reload: 'exam' });
	            }
	            function onErrorFinish(error) {
	            	$state.go('exam', null, { reload: 'exam' });
	            }
        }

        function updateAnswer(answer, status) {
        	var answerParam = {};
        	answerParam.id = answer.id;
        	answerParam.score = status;
  		    
  			Answer.updateStatus(answerParam, onSaveAnswerSuccess, onSaveAnswerError);
  			
  			function onSaveAnswerSuccess() {
  				answer.status = status;
  	  		}
  	  		
  	  		function onSaveAnswerError() {
  	  		}
        }
        
        var unsubscribe = $rootScope.$on('pteMagicApp:examUpdate', function(event, result) {
            vm.exam = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
