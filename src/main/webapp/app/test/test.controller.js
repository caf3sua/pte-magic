(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
    	, '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer'];

    function TestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
    		, $rootScope, $timeout, ExamType, Exam, Answer) {
        
    	var vm = this;
    	// Function
    	vm.answer = answer;
    	vm.closeExam = closeExam;
    	
    	// Variable, flag
    	vm.examTypeId;
    	vm.exam;
    	vm.questions = [];
    	vm.selectedQuestion;
    	vm.answers = [];
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
    	vm.countdown = 100;
    	vm.audio;
    	vm.questionGroup;
    	
    	function initPlayer() {
    		var audio = $("#player");      
            audio[0].addEventListener('ended', callBackAudioEnded);
    	}
    	
    	function callBackAudioEnded() {
    		console.log('audio ended!');
    	}
    	
    	function playAudio(link, timeout) {
    		var audio = $("#player");      
            $("#mp3_src").attr("src", link); // https://storage.googleapis.com/pte-magic/CHINA_1.mp3
            audio[0].pause();
            audio[0].load();
            
            $timeout(function(){
            	audio[0].play();
            }, timeout );
    	}
    	
    	function initAnswer() {
    		// Stop audio
    		var audio = $("#player");      
            audio[0].pause();
            
            // Stop timer
            $scope.$broadcast('timer-stop');
  			$scope.$broadcast('timer-reset');
    	}
    	
    	angular.element(document).ready(function () {
    		$timeout(function(){
	    		// Load player
	    		initPlayer();
	    		
	    		// Load record audio
	    		initAudio();
    		}, 1000 );
        });
    	
    	// Init controller
  		(function initController() {
  			// instantiate base controller
			//$controller('PteMagicBaseController', {
			//	vm : vm
			//});
  					
  			vm.examTypeId = $stateParams.type;
  			
  			Exam.startExams({
  				examTypeId: vm.examTypeId
            }, onSuccess, onError);
            function onSuccess(data, headers) {
            	vm.exam = data;
            	vm.questions = data.questions;
            	console.log(data);
            	
            	// Next question
            	nextQuestion();
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
  			initAnswer();
  			
  			console.log(vm.selectedQuestion);
  			// Get answer
  			getUserAnswer();
  			console.log(vm.answers);
  			
  			// Save answer
  			saveAnswer();
  			
  			// Next question
  			nextQuestion();
  		}
  		
  		function nextQuestion() {
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
  				// Get question group
  				vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);
  				console.log(vm.questionGroup);
  				
  				$scope.$broadcast('timer-start');
  				playAudio(vm.selectedQuestion.audioLink, 3000);
  			}
  		}
  		
  		function saveAnswer() {
  			// Save result
  			var answer = {};
  		    answer.examId = vm.exam.examDTO.id;
  		    answer.questionId = vm.selectedQuestion.id;
  		    answer.answer = vm.answers.join(',');;
  		    // answer.audioLink;
  		    // answer.description;
  		    
  			Answer.save(answer, onSaveAnswerSuccess, onSaveAnswerError);
  			
  			function onSaveAnswerSuccess() {
  	  		}
  	  		
  	  		function onSaveAnswerError() {
  	  		}
  		}
    }
})();
