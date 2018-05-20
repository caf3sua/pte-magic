(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamTypeController', ExamTypeController);

    ExamTypeController.$inject = ['$scope', '$http', '$timeout', '$window', '$state', 'ExamType', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Upload'];

    function ExamTypeController($scope, $http, $timeout, $window, $state, ExamType, ParseLinks, AlertService, paginationConstants, pagingParams, Upload) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.uploadFile = uploadFile;
        vm.downloadTemplate = downloadTemplate;
        
        loadAll();

        function downloadTemplate() {
        	var templateRoute = '/api/file/download_template';
            $window.location = templateRoute;
        }
        
        
        function uploadFile(file, errFiles) {
            vm.f = file;
            vm.errFile = errFiles && errFiles[0];
            if (file) {
                file.upload = Upload.upload({
                    url: '/api/file/create_mock_test',
                    data: {file: file}
                    //ignoreLoadingBar: true
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                    	alert("Import success");
                    	loadAll();
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
        
        
        function loadAll () {
            ExamType.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.examTypes = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
