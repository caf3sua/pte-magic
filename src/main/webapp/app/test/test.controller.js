(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('TestController', TestController);

    TestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
    	, '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce'];

    function TestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
    		, $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce) {

    	var vm = this;
    	// Function
    	vm.answer = answer;
    	vm.closeExam = closeExam;
    	vm.trustAsHtml = $sce.trustAsHtml;

    	// Variable, flag
    	vm.examTypeId;
    	vm.exam;
    	vm.questions = [];
    	vm.selectedQuestion;
    	vm.answers = [];
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
    	vm.countdown = 130; // 2min10second
    	vm.audio;
    	vm.questionGroup;
    	vm.fileUpload;
    	vm.btnEnable = true;
    	vm.toggleRecording = toggleRecording;
    	vm.startRecording = startRecording;

    	vm.txtInfoCountdown = "Begining in ";
    	vm.countdownRecording = 5;
    	vm.isRecording = false;
    	vm.btnTxt = 'Next';

    	function startRecording() {
    		// start recording
	        if (!audioRecorder)
	            return;
	        audioRecorder.clear();
	        audioRecorder.record();
	        vm.btnEnable = true;
	        vm.txtInfoCountdown = "Recording ..."
        	vm.isRecording = true;
    	}

    	function stopRecording() {
    		// stop recording
	        audioRecorder.stop();
	        audioRecorder.getBuffers( gotBuffers );
	        vm.btnEnable = true;
    	}

//    	function toggleRecording() {
//    	    if ($('#record').hasClass("recording")) {
//    	        // stop recording
//    	        audioRecorder.stop();
//    	        $('#record').removeClass("recording");
//    	        audioRecorder.getBuffers( gotBuffers );
//    	        vm.btnEnable = true;
//    	    } else {
//    	        // start recording
//    	        if (!audioRecorder)
//    	            return;
//    	        $('#record').addClass("recording");
//    	        audioRecorder.clear();
//    	        audioRecorder.record();
//    	    }
//    	}

    	function initPlayer() {
			var audio = $("#player");
    		if (audio[0] != undefined) {
    			audio[0].addEventListener('ended', callBackAudioEnded);
    		}
    	}

    	function callBackAudioEnded() {
    		console.log('audio ended!');
    	}

    	function playAudio(link, timeout) {
            $timeout(function(){
            	var audio = $("#player");
        		if (audio[0] != undefined) {
        			$("#mp3_src").attr("src", link); // https://storage.googleapis.com/pte-magic/CHINA_1.mp3
                    audio[0].pause();
                    audio[0].load();
                    audio[0].play();
        		}
            }, timeout );
    		
    	}

    	function initAnswer() {
    		// Stop audio
    		var audio = $("#player");
    		if (audio[0] != undefined) {
    			audio[0].pause();
    		}

            // Stop timer
            $scope.$broadcast('timer-stop');
  			$scope.$broadcast('timer-reset');

  			vm.txtInfoCountdown = "Begining in ";
  	    	vm.isRecording = false;
    	}

    	angular.element(document).ready(function () {
//    		$timeout(function(){
//	    		// Load player
//	    		initPlayer();
//
//	    		// Load record audio
//	    		initAudio();
//    		}, 1000 );
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

            	// Load player
	    		initPlayer();

	    		// Load record audio
	    		initAudio();
	    		
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

  			if (vm.selectedQuestion.type == 'WRITING_SUMMARIZE_WRITTEN_TEXT' || vm.selectedQuestion.type == 'WRITING_ESSAY') {
  				var answer = $('#areaTextWriting').val();
  				vm.answers.push(answer);
  			} else if (vm.selectedQuestion.type == 'LISTENING_FIB_L') {
  				$('.input_answer').each(function(){
  					vm.answers.push($(this).val());
  				 });
  			} else {
  				angular.forEach(vm.listItemAnswer, function(value, key){
  	  				if ($('#answer' + value).is(":checked")) {
  	  	  				vm.answers.push(value);
  	  	  			}
  	  				$("#answer" + value).prop( "checked", false );
  	            });
  			}
  		}

  		function answer() {
  			initAnswer();
  			var selectedQuestionId = vm.selectedQuestion.id;

  			// Upload if questionGroup == SPEAKING
  			if (vm.questionGroup == 'SPEAKING') {
  				stopRecording();
  				$timeout(function(){
  					uploadRecording(selectedQuestionId);
  	    		}, 1000 );

  			} else {
  				console.log(vm.selectedQuestion);
  	  			// Get answer
  	  			getUserAnswer();
  	  			console.log(vm.answers);

  	  			// Save answer
  	  			saveAnswer();
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
  			    	saveAnswerSpeaking(selectedQuestionId, filename);
  			  }
  			};
  			xhr.send();
  		}

  		function updateQuestionInfo(selQuestion) {
  			// Replace @Blank@
  			if (selQuestion.type == 'LISTENING_FIB_L') {
  				selQuestion.description = selQuestion.description.replace(/@Blank@/g, '<input type="text" name="input" class="input_answer pte-writing-input"/>');
  				//selQuestion.description.split('@Blank@').join('xxxxxxx');
  			}
  		}


  		function nextQuestion() {
  			$('#areaTextWriting').val("");
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
  				updateQuestionInfo(vm.selectedQuestion);

  				console.log(vm.selectedQuestion);

  				vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);
  				console.log(vm.questionGroup);

  				// Enable/disable button Answer
  				if (vm.questionGroup == 'SPEAKING') {
  					vm.btnEnable = false;
  				} else {
  					vm.btnEnable = true;
  				}

  				$scope.$broadcast('timer-start');
  				
	    		// Load player
	    		initPlayer();

	    		// Load record audio
	    		initAudio();
  				
  				playAudio(vm.selectedQuestion.audioLink, 3000);
  			}
  		}

  		function saveAnswer() {
  			// Save result
  			var answer = {};
  		    answer.examId = vm.exam.examDTO.id;
  		    answer.questionId = vm.selectedQuestion.id;
  		    answer.answer = vm.answers.join(',');
  		    if (vm.questionGroup == 'WRITING' || vm.questionGroup == 'SPEAKING') {
		    	answer.status = 'MARKING';
		    }
  		    // answer.audioLink;
  		    // answer.description;

  			Answer.save(answer, onSaveAnswerSuccess, onSaveAnswerError);

  			function onSaveAnswerSuccess() {
  	  		}

  	  		function onSaveAnswerError() {
  	  		}
  		}

  		function saveAnswerSpeaking(selectedQuestionId, audioLink) {
  			var answer = {};
  		    answer.examId = vm.exam.examDTO.id;
  		    answer.questionId = selectedQuestionId;
  		    answer.audioLink = audioLink;
  		 	answer.status = 'MARKING';
  		    // answer.description;

  			Answer.save(answer, onSaveAnswerSuccess, onSaveAnswerError);

  			function onSaveAnswerSuccess() {
  	  		}

  	  		function onSaveAnswerError() {
  	  		}
  		}
    }
})();
