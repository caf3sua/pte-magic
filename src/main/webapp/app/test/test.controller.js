(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$scope', '$stateParams', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType', 'Exam'];

    function TestController ($scope, $stateParams, Principal, LoginService, $state, $rootScope, $timeout, ExamType, Exam) {
        
    	var vm = this;
    	vm.answer = answer;
    	
    	vm.examTypeId;
    	vm.exam;
    	vm.questions = [];
    	vm.selectedQuestion;
    	vm.answers = [];
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
    	    	
    	// Init controller
  		(function initController() {
  			vm.examTypeId = $stateParams.type;
  		  
  			Exam.startExams({
  				examTypeId: vm.examTypeId
            }, onSuccess, onError);
            function onSuccess(data, headers) {
            	vm.exam = data;
            	vm.questions = data.questions;
            	console.log(data);
            	
            	// Next question
            	vm.selectedQuestion = vm.questions.shift();
            }
            function onError(error) {
            }
  		})();
  		
  		function getUserAnswer() {
  			vm.answers = [];
  			
  			angular.forEach(vm.listItemAnswer, function(value, key){
  				if ($('#answer' + value).is(":checked")) {
  	  				vm.answers.push(value);
  	  			}
  				$("#answer" + value).prop( "checked", false );
            });
  		}
  		
  		function answer() {
  			console.log(vm.selectedQuestion);
  			// Get answer
  			getUserAnswer();
  			console.log(vm.answers);
  			
  			// Save result
  			
  			// Next question
  			vm.selectedQuestion = vm.questions.shift();
  			if (vm.selectedQuestion == null || vm.selectedQuestion == undefined) {
  				vm.isFinish = true;
  			}
  		}
    }
})();
