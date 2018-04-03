(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamTypeDialogController', ExamTypeDialogController);

    ExamTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExamType'];

    function ExamTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExamType) {
        var vm = this;

        vm.questionTypes = ['FREE_EXAM_LISTENING', 'FREE_EXAM_READING', 'MEMBER_QUESTION_LISTENING', 'MEMBER_QUESTION_READING'
        	, 'MEMBER_QUESTION_SPEAKING', 'MEMBER_QUESTION_WRITING', 'MOCK_TEST_A', 'MOCK_TEST_B', 'MOCK_TEST_FULL'];
        vm.examType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.changeExamType = changeExamType;
        vm.calTotalQuestion = calTotalQuestion; 
        
        
        // SPEAKING : cham diem
        vm.groupQuestion1 = ['1', '2', '3', '4', '5'];
        // 2 - WRITING : cham diem 
        vm.groupQuestion2 = ['6', '7'];
        // 3 - READING
        vm.groupQuestion3 = ['8', '9', '10', '11', '12'];
        // 4 - LISTENING
        vm.groupQuestion4 = ['13', '14', '15', '16', '17', '18', '19', '20'];
        
        function calTotalQuestion() {
        	console.log('calTotalQuestion');
        	vm.examType.totalQuestion = 0;
        	if (Number.isInteger(vm.examType.numQuestion1)) vm.examType.totalQuestion += vm.examType.numQuestion1;
        	if (Number.isInteger(vm.examType.numQuestion2)) vm.examType.totalQuestion += vm.examType.numQuestion2;
        	if (Number.isInteger(vm.examType.numQuestion3)) vm.examType.totalQuestion += vm.examType.numQuestion3;
        	if (Number.isInteger(vm.examType.numQuestion4)) vm.examType.totalQuestion += vm.examType.numQuestion4;
        	if (Number.isInteger(vm.examType.numQuestion5)) vm.examType.totalQuestion += vm.examType.numQuestion5;
        	if (Number.isInteger(vm.examType.numQuestion6)) vm.examType.totalQuestion += vm.examType.numQuestion6;
        	if (Number.isInteger(vm.examType.numQuestion7)) vm.examType.totalQuestion += vm.examType.numQuestion7;
        	if (Number.isInteger(vm.examType.numQuestion8)) vm.examType.totalQuestion += vm.examType.numQuestion8;
        	if (Number.isInteger(vm.examType.numQuestion9)) vm.examType.totalQuestion += vm.examType.numQuestion9;
        	if (Number.isInteger(vm.examType.numQuestion10)) vm.examType.totalQuestion += vm.examType.numQuestion10;
        	if (Number.isInteger(vm.examType.numQuestion11)) vm.examType.totalQuestion += vm.examType.numQuestion11;
        	if (Number.isInteger(vm.examType.numQuestion12)) vm.examType.totalQuestion += vm.examType.numQuestion12;
        	if (Number.isInteger(vm.examType.numQuestion13)) vm.examType.totalQuestion += vm.examType.numQuestion13;
        	if (Number.isInteger(vm.examType.numQuestion14)) vm.examType.totalQuestion += vm.examType.numQuestion14;
        	if (Number.isInteger(vm.examType.numQuestion15)) vm.examType.totalQuestion += vm.examType.numQuestion15;
        	if (Number.isInteger(vm.examType.numQuestion16)) vm.examType.totalQuestion += vm.examType.numQuestion16;
        	if (Number.isInteger(vm.examType.numQuestion17)) vm.examType.totalQuestion += vm.examType.numQuestion17;
        	if (Number.isInteger(vm.examType.numQuestion18)) vm.examType.totalQuestion += vm.examType.numQuestion18;
        	if (Number.isInteger(vm.examType.numQuestion19)) vm.examType.totalQuestion += vm.examType.numQuestion19;
        	if (Number.isInteger(vm.examType.numQuestion20)) vm.examType.totalQuestion += vm.examType.numQuestion20;
        	
        }
        
        function showQuestionGroup1() {
        	vm.flagShowQuestion1 = true;
        	vm.flagShowQuestion2 = true;
        	vm.flagShowQuestion3 = true;
        	vm.flagShowQuestion4 = true;
        	vm.flagShowQuestion5 = true;
        }
        
		function showQuestionGroup2() {
			vm.flagShowQuestion6 = true;
        	vm.flagShowQuestion7 = true;
        }
		
		function showQuestionGroup3() {
			vm.flagShowQuestion8 = true;
        	vm.flagShowQuestion9 = true;
        	vm.flagShowQuestion10 = true;
        	vm.flagShowQuestion11 = true;
        	vm.flagShowQuestion12 = true;
		}
		
		function showQuestionGroup4() {
			vm.flagShowQuestion13 = true;
        	vm.flagShowQuestion14 = true;
        	vm.flagShowQuestion15 = true;
        	vm.flagShowQuestion16 = true;
        	vm.flagShowQuestion17 = true;
        	vm.flagShowQuestion18 = true;
        	vm.flagShowQuestion19 = true;
        	vm.flagShowQuestion20 = true;
		}
		
        function resetFlagShow() {
        	vm.examType.totalQuestion = 0;
        	
        	vm.examType.numQuestion1 = null;
        	vm.examType.numQuestion2 = null;
        	vm.examType.numQuestion3 = null;
        	vm.examType.numQuestion4 = null;
        	vm.examType.numQuestion5 = null;
        	vm.examType.numQuestion6 = null;
        	vm.examType.numQuestion7 = null;
        	vm.examType.numQuestion8 = null;
        	vm.examType.numQuestion9 = null;
        	vm.examType.numQuestion10 = null;
        	vm.examType.numQuestion11 = null;
        	vm.examType.numQuestion12 = null;
        	vm.examType.numQuestion13 = null;
        	vm.examType.numQuestion14 = null;
        	vm.examType.numQuestion15 = null;
        	vm.examType.numQuestion16 = null;
        	vm.examType.numQuestion17 = null;
        	vm.examType.numQuestion18 = null;
        	vm.examType.numQuestion19 = null;
        	vm.examType.numQuestion20 = null;
        	
        	vm.flagShowQuestion1 = false;
        	vm.flagShowQuestion2 = false;
        	vm.flagShowQuestion3 = false;
        	vm.flagShowQuestion4 = false;
        	vm.flagShowQuestion5 = false;
        	vm.flagShowQuestion6 = false;
        	vm.flagShowQuestion7 = false;
        	vm.flagShowQuestion8 = false;
        	vm.flagShowQuestion9 = false;
        	vm.flagShowQuestion10 = false;
        	vm.flagShowQuestion11 = false;
        	vm.flagShowQuestion12 = false;
        	vm.flagShowQuestion13 = false;
        	vm.flagShowQuestion14 = false;
        	vm.flagShowQuestion15 = false;
        	vm.flagShowQuestion16 = false;
        	vm.flagShowQuestion17 = false;
        	vm.flagShowQuestion18 = false;
        	vm.flagShowQuestion19 = false;
        	vm.flagShowQuestion20 = false;
        }
        
        
        function changeExamType() {
        	// vm.questionTypes = ['FREE_EXAM_LISTENING', 'FREE_EXAM_READING', 'MEMBER_QUESTION_LISTENING', 'MEMBER_QUESTION_READING'
        	// , 'MEMBER_QUESTION_SPEAKING', 'MEMBER_QUESTION_WRITING', 'MOCK_TEST_A', 'MOCK_TEST_B', 'MOCK_TEST_FULL'];
        	resetFlagShow();
        	if (vm.examType.type == "" || vm.examType.type == undefined) {
        		resetFlagShow();
        		return;
        	}
        	
        	if (vm.examType.type == "MOCK_TEST_FULL") {
        		//showQuestionGroup1();
        		//showQuestionGroup2();
        		//showQuestionGroup3();
        		//showQuestionGroup4();
        	} else if (vm.examType.type == "MOCK_TEST_A") {
        		// Specking/Writing
        		//showQuestionGroup1();
        		//showQuestionGroup2();
    		} else if (vm.examType.type == "MOCK_TEST_B") {
    			// Reading/Listening
    			//showQuestionGroup3();
        		//showQuestionGroup4();
			} else if (vm.examType.type == "FREE_EXAM_LISTENING" || vm.examType.type == "MEMBER_QUESTION_LISTENING") {
				showQuestionGroup4();
			} else if (vm.examType.type == "FREE_EXAM_READING" || vm.examType.type == "MEMBER_QUESTION_READING") {
				showQuestionGroup3();
			} else if (vm.examType.type == "MEMBER_QUESTION_SPEAKING") {
				showQuestionGroup1();
			} else if (vm.examType.type == "MEMBER_QUESTION_WRITING") {
				showQuestionGroup2();
			}
        }
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.examType.id !== null) {
                ExamType.update(vm.examType, onSaveSuccess, onSaveError);
            } else {
                ExamType.save(vm.examType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pteMagicApp:examTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
