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
        vm.formatFIB = formatFIB;
        vm.formatFIBAnswer = formatFIBAnswer;
        
        angular.element(document).ready(function () {
    		$timeout(function(){
    			angular.forEach(vm.exam.answerQuestions, function(value, key){
    				// TODO
  	            });
    		}, 1000 );
        });
        
        function formatFIBAnswer(question) {
        	if (question.expectAnswer == "" || question.expectAnswer == undefined) {
        		return question.expectAnswer;
        	}
        	
        	if (question.type != 'READING_FIB_R') {
        		return question.expectAnswer;
        	}
        	
        	
        	let arrAnswer = question.expectAnswer.split(",");
        	let newAnswer = [];
        	angular.forEach(arrAnswer, function(val, key) {
        		let answerItem = "answer" + val.trim();
				let item = val.trim() + "(" + question["answer" + val.trim()] + ")";
				newAnswer.push(item);
            });
        	
        	return newAnswer.join(", ");
        }
        
        function formatFIB(question, answer) {
        	if (answer == null || answer == undefined || question == null || question == undefined) {
        		return question;
        	}
        	
        	let arrAnswer = answer.split(",");
        	angular.forEach(arrAnswer, function(val, key){
				let item = '<span class="pte-answer-highlight">' + val.trim() + "</span>";
				question = question.replace('@Blank@', item);
            });
        	
        	return question;
        }
        
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
