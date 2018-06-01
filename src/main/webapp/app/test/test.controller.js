(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
    	, '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce', 'entity', 'ngAudio'];

    function TestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
    		, $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce, entity, ngAudio) {

    	var vm = this;
    	// Function
    	vm.answer = answer;
    	vm.trustAsHtml = $sce.trustAsHtml;

    	// Variable, flag
    	vm.examTypeId;
    	vm.exam = entity;
    	vm.questions = entity.questions;
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
    	vm.audio;
    	vm.fileUpload;
    	vm.btnEnable = true;
    	vm.toggleRecording = toggleRecording;

    	vm.txtInfoCountdown = "Begining in ";
    	vm.countdownRecording = 5;
    	vm.isRecording = false;
    	vm.btnTxt = 'Next';
    	
        vm.checkAudioSeconds = true;
        vm.checkStatusPlay = false;
    	vm.countdownSpeaking = 5;

    	function stopRecording() {
    		// stop recording
	        audioRecorder.stop();
	        audioRecorder.getBuffers( gotBuffers );
	        vm.btnEnable = true;
	        clearInterval(vm.intervalProgress);
    	}

        vm.checkDisabled = false;

        function initAnswer() {
    		// Stop audio
//    		var audio = $("#player");
//    		if (audio[0] != undefined) {
//    			audio[0].pause();
//    		}

            // Stop timer
            $scope.$broadcast('timer-stop');
  			$scope.$broadcast('timer-reset');

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

  			initAnswer();
            vm.countdownPercent = 0;
            var selectedQuestionId = vm.selectedQuestion.id;

  			// Upload if questionGroup == SPEAKING
  			if (vm.questionGroup == 'SPEAKING') {
  				var questionId = vm.selectedQuestion.id;
            	stopRecording();
            	$timeout(function(){
            		uploadRecording(questionId);
            	}, 1000 );
  			} else {
  				console.log(vm.selectedQuestion);
  	  			// Get answer
  	  			vm.getUserAnswer();
  	  			console.log(vm.answers);

  	  			// Save answer
  	  			vm.saveAnswer();
  			}

  			// Next question
  			nextQuestion();
  		}

  		$scope.$watch('vm.fileUpload', function (file) {
  			var file;
  			if (file) {
              file.upload = Upload.upload({
                  url: '/api/file/upload/answer',
                  data: {file: vm.fileUpload},
                  ignoreLoadingBar: true
              });

              file.upload.then(function (response) {
                  $timeout(function () {
                      file.result = response.data;
                  });
              }, function (response) {
                  if (response.status > 0)
                      $scope.errorMsg = response.status + ': ' + response.data;
              }, function (evt) {
                  file.progress = Math.min(100, parseInt(100.0 *
                                           evt.loaded / evt.total));
              });
          }
        });

  		function uploadRecording(selectedQuestionId) {
  			var blobUrl = $("#save").attr('href');
  			console.log(blobUrl);
  			var xhr = new XMLHttpRequest();
  			xhr.open('GET', blobUrl, true);
  			xhr.responseType = 'blob';
  			xhr.onload = function(e) {
  			  if (this.status == 200) {
  				  	var blob = this.response;
  			    	console.log(blob);
  			    	// myBlob is now the blob that the object URL pointed to.
  			    	var filename = "recording_" + vm.exam.examDTO.id + "_" + selectedQuestionId + ".wav";
  			    	vm.fileUpload = new File([blob], filename);

  			    	// save answer
  			    	vm.saveAnswerSpeaking(selectedQuestionId, filename);
  			  }
  			};
  			xhr.send();
  		}

  		function nextQuestion() {
  			$('#areaTextWriting').val("");
  			vm.WordsLength = 0;
  			vm.selectedQuestion = vm.questions.shift();
  			vm.resetProgressStatus();
  			vm.audioProgressing = 0;
  			
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
  				vm.updateQuestionInfo(vm.selectedQuestion);

  				console.log(vm.selectedQuestion);

  				vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);
  				console.log(vm.questionGroup);

  				// Enable/disable button Answer
  				// if (vm.questionGroup == 'SPEAKING') {
  				// 	vm.btnEnable = false;
  				// } else {
  				// 	vm.btnEnable = true;
  				// }

  				$scope.$broadcast('timer-start');

	    		// Load player
//	    		initPlayer();

	    		// Load record audio
	    		initAudio();

	    		// Play mp3 audio
	    		vm.countAudio = 3;
	    		vm.checkAudioSeconds = true;
	            vm.checkStatusPlay = false;
                vm.txtStatusAudio = 'Playing';
                vm.intervalAudio = setInterval(function() {
                    vm.countAudio--;
                    // Display 'counter' wherever you want to display it.
                    if (vm.countAudio == 0) {

                        vm.playAudio(vm.selectedQuestion.audioLink, 1000);
                        clearInterval(vm.intervalAudio);
                    }
                }, 1000);


  				vm.countdownToRecording();
  			}
  		}

    }
})();
