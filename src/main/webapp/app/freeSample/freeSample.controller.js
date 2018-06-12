(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FreeSampleController', FreeSampleController);

    FreeSampleController.$inject = ['$scope', '$window', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function FreeSampleController ($scope, $window, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
        var vm = this;
        vm.showExamList = showExamList;
        vm.startTest = startTest;
        vm.examShowFlag = false;
        vm.listeningExams = [];
        vm.readingExams = [];
        vm.selectedType;
        vm.selectedExams = [];

        vm.totalExamQuestionReading = 0;
        vm.totalExamQuestionListening = 0;
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
            }
            vm.examShowFlag = true;
            vm.selectedType = type;
            console.log(vm.selectedType);
        }

        function startTest(examId) {
        	var url = '/#/test?type=' + examId;
        	$window.open(url,"_blank", "toolbar=no,scrollbars=no, resizable=no, width=1200, height=700");
        }

//        $scope.$watch('vm.exams', function(newVal, oldVal){
//        });

        // Init controller
  		(function initController() {

  			ExamType.getAllByType({type: 'FREE_EXAM_LISTENING'},
  					function(data, headers) {
		  				vm.listeningExams = data;
		  				angular.forEach(data, function(value, key){
	  	            		vm.totalExamQuestionListening = vm.totalExamQuestionListening + value.totalQuestion;
		  	            });
  					},
  					function(error) {
  					});

  			ExamType.getAllByType({type: 'FREE_EXAM_READING'},
  					function(data, headers) {
		  				vm.readingExams = data;
		  				angular.forEach(data, function(value, key){
	  	            		vm.totalExamQuestionReading = vm.totalExamQuestionReading + value.totalQuestion;
		  	            });
  					},
  					function(error) {
  					});
  		})();
    }
})();
