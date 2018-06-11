(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('QuestionDialogController', QuestionDialogController);

    QuestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Question', 'Upload'];

    function QuestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Question, Upload) {
        var vm = this;

        vm.question = entity;
        vm.clear = clear;
        vm.save = save;
        vm.uploadFiles = uploadFiles;
        vm.questionGroup;
        vm.changeQuestionType = changeQuestionType;
        vm.checkTypeQuestion = $stateParams.selectedSkill;
        function changeQuestionType() {
        	vm.questionGroup = getQuestionGroup(vm.question.type);
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function uploadFiles(file, errFiles) {
            vm.f = file;
            vm.errFile = errFiles && errFiles[0];
            if (file) {
            	// validate
            	if (vm.question.type == 'SPEAKING_DESCRIBE_IMAGE') {
                	// Check image
                	if (vm.f.type == 'image/png' || vm.f.type == 'image/bmp' || vm.f.type == 'image/gif'
                		 || vm.f.type == 'image/jpeg') {
                		console.log('Upload image');
                	} else {
                		alert('Only allow image upload (png, jpeg, bmp)');
                		return;
                	}
                } else {
                	// Check audio
                	if (vm.f.type == 'audio/mpeg' || vm.f.type == 'audio/x-wav') {
    	           		console.log('Upload audio');
    	           	} else {
    	           		alert('Only allow audio upload (mp3, wav)');
    	           		return;
    	           	}
                }
            	
                file.upload = Upload.upload({
                    url: '/api/file/upload/question',
                    data: {file: file},
                    ignoreLoadingBar: true
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                    	console.log(response);
                    	vm.question.audioLink = response.data.filename;
                    });
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                                             evt.loaded / evt.total));
                });
            }
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.question.id !== null) {
                Question.update(vm.question, onSaveSuccess, onSaveError);
            } else {
                Question.save(vm.question, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pteMagicApp:questionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
