(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('SkillTestController', SkillTestController);

    SkillTestController.$inject = ['$scope', '$window', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function SkillTestController ($scope, $window, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
    	var vm = this;
        vm.showExamList = showExamList;
        vm.startTest = startTest;
        vm.examShowFlag = false;
        vm.listeningExams = [];
        vm.readingExams = [];
        vm.speakingExams = [];
        vm.writingExams = [];
        vm.selectedType;
        vm.selectedExams = [];

        vm.totalExamQuestionReading = 0;
        vm.totalExamQuestionListening = 0;
        vm.totalExamQuestionSpeaking = 0;
        vm.totalExamQuestionWriting = 0;
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

        function startTest(examId) {
        	//var url = '/#/test?type=' + examId;
        	//$window.open(url,"_blank", "toolbar=no,scrollbars=no, resizable=no, width=1200, height=700");
        }

//        $scope.$watch('vm.exams', function(newVal, oldVal){
//        });

        // Init controller
  		(function initController() {
  	        vm.totalExamQuestionReading = 0;
  	        vm.totalExamQuestionListening = 0;
  	        vm.totalExamQuestionSpeaking = 0;
  	        vm.totalExamQuestionWriting = 0;
//	  	      vm.listeningExams = [];
//	          vm.readingExams = [];
//	          vm.speakingExams = [];
//	          vm.writingExams = [];

  	        ExamType.getAllByType({type: 'MEMBER_QUESTION_LISTENING'},
  					function(data, headers) {
  	        			if (data != null) {
  	        				vm.listeningExams = data;

  			  				angular.forEach(data, function(value, key){
  		  	            		vm.totalExamQuestionListening += value.totalQuestion;
  			  	            });
  	        			}
  					},
  					function(error) {
  					});

  			ExamType.getAllByType({type: 'MEMBER_QUESTION_READING'},
  					function(data, headers) {
  						if (data != null) {
			  				vm.readingExams = data;

			  				angular.forEach(data, function(value, key){
		  	            		vm.totalExamQuestionReading += value.totalQuestion;
			  	            });
  						}
  					},
  					function(error) {
  					});

  			ExamType.getAllByType({type: 'MEMBER_QUESTION_SPEAKING'},
  					function(data, headers) {
  						if (data != null) {
			  				vm.speakingExams = data;

			  				angular.forEach(data, function(value, key){
		  	            		vm.totalExamQuestionSpeaking += value.totalQuestion;
			  	            });
  						}
  					},
  					function(error) {
  					});

  			ExamType.getAllByType({type: 'MEMBER_QUESTION_WRITING'},
  					function(data, headers) {
  						if (data != null) {
  							vm.writingExams = data;

			  				angular.forEach(data, function(value, key){
		  	            		vm.totalExamQuestionWriting += value.totalQuestion;
			  	            });
  						}
  					},
  					function(error) {
  					});
  		})();
    }
})();
