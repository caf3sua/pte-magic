(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .filter('secondsToDateTime', secondsToDateTime)
        .controller('TestController', TestController);

    TestController.$inject = ['$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'ngAudio'];

    function secondsToDateTime() {
    	return function(seconds) {
            return new Date(1970, 0, 1).setSeconds(seconds);
        };
    }
    
    function TestController ($scope, $window, $stateParams, Principal, LoginService, $state, $rootScope, $timeout, ExamType, Exam, Answer, ngAudio) {
        
    	var vm = this;
    	vm.answer = answer;
    	vm.closeExam = closeExam;
    	
    	vm.examTypeId;
    	vm.exam;
    	vm.questions = [];
    	vm.selectedQuestion;
    	vm.answers = [];
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
    	vm.countdown = 100;
    	vm.audio;
    	
    	// Init controller
  		(function initController() {
  			vm.examTypeId = $stateParams.type;
  			//vm.audio = ngAudio.load("https://storage.googleapis.com/pte-magic/CHINA_1.mp3"); // returns NgAudioObject
  			
  			Exam.startExams({
  				examTypeId: vm.examTypeId
            }, onSuccess, onError);
            function onSuccess(data, headers) {
            	vm.exam = data;
            	vm.questions = data.questions;
            	console.log(data);
            	
            	// Next question
            	vm.selectedQuestion = vm.questions.shift();
            	//vm.audio = ngAudio.load(vm.selectedQuestion.audioLink);
            	
            	// "https://storage.googleapis.com/pte-magic/CHINA_1.mp3"
            	var audio = $("#player");      
                $("#mp3_src").attr("src", vm.selectedQuestion.audioLink);
                audio[0].pause();
                audio[0].load();
            }
            function onError(error) {
            }
  		})();

  		function closeExam() {
  			$window.close();
  		}
  		
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
  			$scope.$broadcast('timer-stop');
  			$scope.$broadcast('timer-reset');
  			console.log(vm.selectedQuestion);
  			// Get answer
  			getUserAnswer();
  			console.log(vm.answers);
  			
  			// Save result
  			var answer = {};
  		    answer.examId = vm.exam.examDTO.id;
  		    answer.questionId = vm.selectedQuestion.id;
  		    answer.answer = vm.answers.join(',');;
  		    // answer.audioLink;
  		    // answer.description;
  		    
  			Answer.save(answer, onSaveAnswerSuccess, onSaveAnswerError);
  			
  			// Next question
  			vm.selectedQuestion = vm.questions.shift();
  			if (vm.selectedQuestion == null || vm.selectedQuestion == undefined) {
  				vm.isFinish = true;
  				// Service finish exam
  				Exam.finishExam({
  	  				id: vm.exam.examDTO.id
  	            }, onSuccessFinish, onErrorFinish);
  	            function onSuccessFinish(data, headers) {
  	            	$scope.$broadcast('timer-stop');
  	            	console.log('Finish exam');
  	            	return;
  	            }
  	            function onErrorFinish(error) {
  	            	$scope.$broadcast('timer-stop');
  	            	console.log('Finish exam error');
  	            	return;
  	            }
  			} else {
  				$scope.$broadcast('timer-start');
  				var audio = $("#player");      
                $("#mp3_src").attr("src", vm.selectedQuestion.audioLink);
                audio[0].pause();
                audio[0].load();
  			}
  		}
  		
  		function onSaveAnswerSuccess() {
  			
  		}
  		
  		function onSaveAnswerError() {
  			
  		}
    }
})();
