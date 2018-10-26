(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('QuestionBankMainController', QuestionBankMainController);

    QuestionBankMainController.$inject = ['$scope', '$window', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout'
                                          , 'ExamType', 'Question'];

    function QuestionBankMainController ($scope, $window, Principal, LoginService, $state, $rootScope, $timeout
    		, ExamType, Question) {
    	var vm = this;
        vm.showExamList = showExamList;
        vm.startTest = startTest;
        vm.examShowFlag = false;
        vm.selectedType;
        vm.selectedExams = [];
        vm.listeningExams = [];
        vm.readingExams = [];
        vm.speakingExams = [];
        vm.writingExams = [];

        $timeout(function (){
            angular.element(document.getElementById("content")).removeClass("background-color-222d32");
        });
        function showExamList(type) {
            if(type == 'LISTENING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#listening').addClass("active");
                    angular.element('#listeningMobile').addClass("activeMobile");
                });

                vm.selectedExams = vm.listeningExams;
            }else if(type == 'READING'){
                angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                angular.element('#reading').addClass("active");
                angular.element('#readingMobile').addClass("activeMobile");

                vm.selectedExams = vm.readingExams;
            }if(type == 'WRITING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#writing').addClass("active");
                    angular.element('#writingMobile').addClass("activeMobile");
                });
                vm.selectedExams = vm.writingExams;
            }
            if(type == 'SPEAKING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#speaking').addClass("active");
                    angular.element('#speakingMobile').addClass("activeMobile");
                });
                vm.selectedExams = vm.speakingExams;
            }
            vm.examShowFlag = true;
            vm.selectedType = type;
            console.log(vm.selectedType);
        }

        function startTest(exam) {
        	var url = '/#/question-bank?type=' + exam.type;
        	$window.open(url,"_blank", "toolbar=no,scrollbars=no, resizable=no, width=1200, height=700");
        }

        // Init controller
  		(function initController() {
  			Question.queryByQuestionCountInfo({}, onSuccess, onError);
            function onSuccess(data, headers) {
            	vm.totalExamQuestionListening = data.totalQuestionListening;
            	vm.totalExamQuestionReading = data.totalQuestionReading;
            	vm.totalExamQuestionSpeaking = data.totalQuestionSpeaking;
            	vm.totalExamQuestionWriting = data.totalQuestionWriting;
            	
            	vm.speakingExams = [
      			                    data.speakingRA
      			                    , data.speakingRS
      			                    , data.speakingDI
      			                    , data.speakingRL
      			                    , data.speakingASQ
                ];
      			vm.writingExams = [
									data.writingSWT
									, data.writingE
      			];
      			vm.readingExams = [
									data.readingFRW
									, data.readingFR
									, data.readingROP
									, data.readingMRSA
									, data.readingMRMA
      			];
      			vm.listeningExams = [
									data.listeningSST
									, data.listeningFL
									, data.listeningMLSA
									, data.listeningMLMA
									, data.listeningHCS
									, data.listeningSMW
									, data.listeningHIW
									, data.listeningD
                                 ];
            }
            function onError(error) {
                console.log(error);
            }
  		})();
    }
})();
