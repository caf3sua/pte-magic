(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FullTestController', FullTestController);

    FullTestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
        , '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce', 'entity', '$interval', 'PTE_SETTINGS'];

    function FullTestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
        , $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce, entity, $interval, PTE_SETTINGS) {

        var vm = this;

        // Function
        vm.answer = answer;
        vm.trustAsHtml = $sce.trustAsHtml;

        // Variable, flag
        vm.examTypeId;
        vm.exam = entity;
        vm.questions = entity.questions;
        vm.selectedQuestion;
        vm.answers = [];
        vm.isFinish = false;
        vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
        vm.audio;
        vm.fileUpload;
        vm.btnEnable = true;
        vm.toggleRecording = toggleRecording;
        vm.startNewPart = startNewPart;

        vm.txtInfoCountdown = "Begining in ";
        vm.countdownRecording = 5;
        vm.isRecording = false;
        vm.checkAudioSeconds = true;
        vm.checkStatusPlay = false;
        vm.countdownTimebreak = 600;

        vm.btnTxt = 'Next';

        function startNewPart() {
            initAnswer();

            nextQuestion();
        }

        function stopRecording() {
            // stop recording
            audioRecorder.stop();
            audioRecorder.getBuffers( gotBuffers );
            vm.btnEnable = true;
            clearInterval(vm.intervalProgress);
        }

//        function initPlayer() {
//            var audio = $("#player");
//            if (audio[0] != undefined) {
//                audio[0].addEventListener('ended', vm.callBackAudioEnded);
//            }
//        }

//        function playAudio(link, timeout) {
//            if (link == null || link == "") {
//                return;
//            }
//            vm.checkAudioSeconds = false;
//            vm.checkStatusPlay = true;
//            var audio = $("#player");
//            if (audio[0] != undefined) {
//                audio[0].addEventListener('ended', vm.callBackAudioEnded);
//                console.log('Play audio: ' + link);
//                $("#mp3_src").attr("src", link); // https://storage.googleapis.com/pte-magic-2018/1523480876666_HCS1.mp3
//                audio[0].pause();
//                audio[0].load();
//
//                $timeout(function(){
//                    audio[0].play();
//                }, timeout );
//            }
//
//        }

        function initAnswer() {
            // Stop audio
            var audio = $("#player");
            if (audio[0] != undefined) {
                audio[0].pause();
            }


            vm.txtInfoCountdown = "Begining in ";
            vm.isRecording = false;
        }

        angular.element(document).ready(function () {
        });

        function initMockTest() {
            if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A') {
                vm.countdown = 110 * 60;
                vm.countdown = 30;
            } else if (vm.exam.examTypeDTO.type == 'MOCK_TEST_B') {
                vm.countdown = 90 * 60;
                vm.countdown = 50;
            } else {
                vm.countdown = 200 * 60;
            }
        }

        // Init controller
        (function initController() {
            // instantiate base controller
            $controller('PteMagicBaseController', { vm: vm, $scope: $scope });
            $timeout(function (){
                angular.element(document.getElementById("content")).removeClass("background-color-222d32");
            });
            // Load player
//            initPlayer();

            // Load record audio
            initAudio();

            // Init mocktest
            // initMockTest();

            vm.initCountQuestion();

            // Next question
            nextQuestion();
        })();

        function answer() {
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
        		clearInterval(vm.intervalToRecording);
        	}


            initAnswer();

            if (vm.selectedQuestion.type != 'TIME_BREAK') {
                // Upload if questionGroup == SPEAKING
                if (vm.questionGroup == 'SPEAKING') {
                	var questionId = vm.selectedQuestion.id;
                	stopRecording();
                	$timeout(function(){
                		uploadRecording(questionId);
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
        	console.log('uploadRecording, questionId:' + selectedQuestionId);
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

        function setCountdownTimer() {
        	console.log('setCountdownTimer');
            if (vm.selectedQuestion.type == 'TIME_BREAK') {
                vm.countdown = PTE_SETTINGS.COUNT_DOWN_TIME_BREAK;
                $scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
                return;
            }

            if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A' || vm.exam.examTypeDTO.type == 'MOCK_TEST_B' || vm.exam.examTypeDTO.type == 'MOCK_TEST_FULL') {
                if (vm.questionGroup == 'LISTENING') {
                    if (vm.selectedQuestion.type == 'LISTENING_SUMMARIZE_SPOKEN_TEXT') {
                        vm.countdown = 10 * 60;
                        $scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
                    } else {
                        if (vm.listeningTimerRunningFlag == false) {
                            vm.countdown = 16 * 2 * 60 + 2 * 60;
                            $scope.$broadcast('timer-set-countdown-seconds', vm.countdown );
                            vm.listeningTimerRunningFlag = true;
                        }
                    }
                } else if (vm.questionGroup == 'WRITING') {
                    if (vm.selectedQuestion.type == 'WRITING_SUMMARIZE_WRITTEN_TEXT') {
                        vm.countdown = 10 * 60;
                        $scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
                    } else if (vm.selectedQuestion.type == 'WRITING_ESSAY') {
                        vm.countdown = 20 * 60;
                        $scope.$broadcast('timer-set-countdown-seconds', vm.countdown );
                    }
                } else if (vm.questionGroup == 'READING') {
                    if (vm.readingTimerRunningFlag == false) {
                        vm.countdown = 40 * 60;
                        $scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
                        vm.readingTimerRunningFlag = true;
                    }
                }
            }
        }

        function nextQuestion() {
            vm.Text = "";
            $('#areaTextWriting').val("");
            $('#areaTextWriting').html('');
            vm.WordsLength = 0;
            vm.selectedQuestion = vm.questions.shift();
            vm.audioProgressing = 0;
            vm.resetProgressStatus();

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

                // Get question group
                vm.updateQuestionInfo(vm.selectedQuestion);

                console.log(vm.selectedQuestion);

                vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);
                console.log(vm.questionGroup);

                setCountdownTimer();

                // Load player
//                initPlayer();

                // Load record audio
                initAudio();
                vm.countAudio = 3;
                vm.checkAudioSeconds = true;
	            vm.checkStatusPlay = false;
                vm.txtStatusAudio = 'Playing';
                vm.intervalAudio = $interval(function() {
                    vm.countAudio--;
                    // Display 'counter' wherever you want to display it.
                    if (vm.countAudio == 0) {
                        vm.playAudio(vm.selectedQuestion.audioLink, 1000);
                        clearInterval(vm.intervalAudio);
                    }
                }, 1000);
                vm.countdownToRecording();

                $scope.$broadcast('timer-start');
            }
        }
    }
})();
