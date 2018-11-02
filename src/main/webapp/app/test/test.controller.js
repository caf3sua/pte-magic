(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
    	, '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce', 'entity', 'ngAudio', '$interval'];

    function TestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
    		, $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce, entity, ngAudio, $interval) {

    	var vm = this;
    	// Function
    	vm.answer = answer;
    	vm.trustAsHtml = $sce.trustAsHtml;

    	// Variable, flag
    	vm.examTypeId;
    	vm.exam = entity;
    	vm.questions = entity.questions;
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'];
    	vm.audio;
    	vm.toggleRecording = toggleRecording;

    	vm.txtInfoCountdown = "Begining in ";
    	vm.countdownRecording = 5;
    	vm.isRecording = false;
    	vm.btnTxt = 'Next';

        vm.checkAudioSeconds = true;
        vm.checkStatusPlay = false;
    	vm.countdownSpeaking = 5;
    	vm.gotoBack = gotoBack;
    	
    	function gotoBack() {
    		if (vm.exam.examTypeDTO.type.indexOf("MEMBER_QUESTION_") != -1) {
    			$state.go("skill-test");
    		} else {
    			$state.go("freeSample");
    		}
    	}

    	function stopRecording() {
    		console.log('stopRecording');
    		// stop recording
	        audioRecorder.stop();
	        audioRecorder.getBuffers( gotBuffers );
	        clearInterval(vm.intervalProgress);
    	}

        vm.checkDisabled = false;

        function initAnswer() {
        	// Stop audio
            var audio = $("#player");
            if (audio[0] != undefined && audio[0].duration > 0 && !audio[0].paused) {
                audio[0].pause();
            }
            
            // Stop timer
//            $scope.$broadcast('timer-stop');
//  			$scope.$broadcast('timer-reset');

  			vm.txtInfoCountdown = "Begining in ";
  	    	vm.isRecording = false;
            // vm.txtStatusAudio = 'Playing';
            vm.checkAudioSeconds = true;
            vm.checkStatusPlay = false;
    	}

    	angular.element(document).ready(function () {
    		// Load record audio
    		initAudio();
        });


    	// Init controller
  		(function initController() {
  			// instantiate base controller
  		    $controller('PteMagicBaseController', { vm: vm, $scope: $scope });
            $timeout(function (){
                angular.element(document.getElementById("content")).removeClass("background-color-222d32");
            });
            for (var i = 0; i < vm.questions.length; i++) {
                if(vm.questions[i].type == 'LISTENING_SUMMARIZE_SPOKEN_TEXT'
                    || vm.questions[i].type == 'WRITING_SUMMARIZE_WRITTEN_TEXT'
                ){
                    vm.countdown = 600; // 10min
                }else if(vm.questions[i].type == 'WRITING_ESSAY'){
                    vm.countdown = 1200; // 20min
                }else {
                    vm.countdown = 130; // 2min10second
                }
            }
        	// Next question
        	nextQuestion();
  		})();

  		function answer() {
  			if(vm.intervalAudio) {
  				clearInterval(vm.intervalAudio);
            }

        	if (vm.intervalProgress) {
        		clearInterval(vm.intervalProgress);
        	}
        	if (vm.intervalCounter) {
        		clearInterval(vm.intervalCounter);
        	}
        	if (vm.intervalToRecording) {
        		clearInterval(vm.intervalToRecording);
        	}
        	
        	console.log('Answer: ' + vm.selectedQuestion.id);

  			initAnswer();
            if (vm.selectedQuestion.type != 'TIME_BREAK') {
                // Upload if questionGroup == SPEAKING
                if (vm.questionGroup == 'SPEAKING') {
                	var questionId = vm.selectedQuestion.id;
                	stopRecording();
                	$timeout(function(){
                		vm.uploadRecording(questionId);
                	}, 2000 );
                } else {
                    console.log(vm.selectedQuestion);
                    // Get answer
                    vm.getUserAnswer();
                    console.log(vm.answers);

                    // Save answer
                    vm.saveAnswer();
                }
            }

  			// Next question
  			nextQuestion();
  		}

  		function nextQuestion() {
  			vm.isShowAnswer = false;
            vm.Text = "";
            $('#areaTextWriting').val("");
            $('#areaTextWriting').html('');
            vm.WordsLength = 0;
            vm.selectedQuestion = vm.questions.shift();
            vm.audioProgressing = 0;
            vm.resetProgressStatus();
            if(vm.intervalAudio) {
        		$interval.cancel(vm.intervalAudio);
            }
        	if (vm.intervalProgress) {
        		$interval.cancel(vm.intervalProgress);
        	}
        	if (vm.intervalCounter) {
        		$interval.cancel(vm.intervalCounter);
        	}
        	if (vm.intervalToRecording) {
        		$interval.cancel(vm.intervalToRecording);
        	}
        	
        	console.log(vm.intervalAudio);
        	console.log(vm.intervalProgress);
        	console.log(vm.intervalCounter);
        	console.log(vm.intervalToRecording);
            
            // enable button
            vm.enableNextButton();

            // Timeout
            $timeout(function(){
            	if (vm.selectedQuestion == null || vm.selectedQuestion == undefined) {
                    vm.isFinish = true;
                    $scope.$broadcast('timer-stop');
                    // Service finish exam
                    Exam.finishExam({
                        id: vm.exam.examDTO.id
                    }, onSuccessFinish, onErrorFinish);
                    function onSuccessFinish(data, headers) {
                        console.log('Finish exam');
                        return;
                    }
                    function onErrorFinish(error) {
                        console.log('Finish exam error');
                        return;
                    }
                } else {
                    // update button lable
                    if (vm.selectedQuestion.type == 'TIME_BREAK') {
                        vm.btnTxt = 'Skip Timebreak';
                        vm.initCountQuestion();
                    } else {
                        vm.btnTxt = 'Next';
                        // Count question
                        vm.currentQuestion++;
                    }
                    
                    console.log('======== Next question, questionId :' + vm.selectedQuestion.id + ', type: ' + vm.selectedQuestion.type + ' ============');
                    console.log('======== currentSKill :' + vm.currentSKill + ',' + vm.currentQuestion + '/' + vm.totalQuestion + ' ============');
                    
                    // Get question group
                    vm.updateQuestionInfo(vm.selectedQuestion);
                    vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);

                    vm.setCountdownTimer();

                    // Load record audio
                    initAudio();
                    
                    vm.checkAudioSeconds = true;
    	            vm.checkStatusPlay = false;
                    vm.txtStatusAudio = 'Playing';
                    
                    // Check
                    if (vm.selectedQuestion.audioLink != null && vm.selectedQuestion.audioLink != undefined) {
                    	if(vm.intervalAudio) {
                    		$interval.cancel(vm.intervalAudio);
                        }
                        //vm.countAudio = 3;
                    	vm.calCountdownAudio();
                        vm.intervalAudio = $interval(function() {
                        	console.log("vm.countAudio: " + vm.countAudio);
                        	if (vm.countAudio > 0) {
                        		vm.countAudio--;
                        	} else if (vm.countAudio == 0) {
                        		vm.playAudio(vm.selectedQuestion.audioLink, 1000);
                        		$interval.cancel(vm.intervalAudio);
                        	}
                        }, 1000);
                    }
                    
                    vm.countdownToRecording();
                }
        	});
  		}
    }
})();
