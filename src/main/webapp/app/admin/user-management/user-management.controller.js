(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['Principal', 'User', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants'
    	, 'JhiLanguageService', 'kendoConfig', 'Exam'];

    function UserManagementController(Principal, User, ParseLinks, AlertService, $state, pagingParams, paginationConstants
    		, JhiLanguageService, kendoConfig, Exam) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.currentAccount = null;
        vm.languages = null;
        vm.loadAll = loadAll;
        vm.setActive = setActive;
        
        vm.users = [];
    	vm.usersInit = [];
    	
        vm.page = 1;
        vm.totalItems = null;
        vm.clear = clear;
        vm.links = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.transition = transition;
        vm.resetLimitTest = resetLimitTest;

        angular.element(document).ready(function () {
        });

    	// Init controller
  		(function initController() {
  			//          vm.loadAll();
  	        setupUserTableGrid([]);
  		})();
  		
        
        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function resetLimitTest(userId) {
        	Exam.resetLimitTest({userId : userId}, onResetLimitTestSuccess, onResetLimitTestError);

            function onResetLimitTestSuccess() {
            }

            function onResetLimitTestError() {
            }
        }
        
        function setActive (user, isActivated) {
            user.activated = isActivated;
            User.update(user, function () {
                vm.loadAll();
                vm.clear();
            });
        }

        function loadAll () {
        	vm.users = [];
        	vm.usersInit = [];
        	
        	User.getAll({
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.users = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function clear () {
            vm.user = {
                id: null, login: null, firstName: null, lastName: null, email: null,
                activated: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                resetKey: null, authorities: null
            };
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        
        function setupUserTableGrid(data) {
    		vm.userTableGridOption = kendoConfig
	            .getGridOptions({
	                autoBind : true,
	                resizable: true,
	                dataSource :[],
	                noRecords : true,
	                scrollable: true,
	                pageable: {
	                    refresh: false,
	                    pageSize: 10,
	                    pageSizes: [10, 15, 20, 25],
	                    messages: {
	                        display: "{0}-{1} của {2} kết quả",
	                        itemsPerPage: "kết quả/trang",
	                        empty: "Không có kết quả hiển thị"
	                    }
	                },
	                messages : { noRecords : ""},
	                schema: {
	                    model: {
	                        fields: {
	                            signedDate: { type: "date" }
	                        }
	                    }
	                },
	//                editable: true,
	                columns : [
	                    {
	                        title: "STT",
	                        headerAttributes: { "class": "columns-title" },
	                        template: dataItem => $("#userTableGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
	                        width : 40,
	                        attributes: { class:"text-center" }
	                    },
	                    {
	                        title : "UserId",
	                        field : "description",
	                        headerAttributes: { "class": "columns-title" },
	                        attributes: { class:"text-left" },
	                        width : 180,
	                        editable: true
	                    },
	                    {
	                        title : "Email",
	                        field : "code",
	                        headerAttributes: { "class": "columns-title" },
	                        attributes: { class:"text-left" },
	                        width : 180,
	                        validation: { required: true },
	                        editable: true
	                    },
	                    {
	                        title : "Profiles",
	                        field : "code",
	                        headerAttributes: { "class": "columns-title" },
	                        attributes: { class:"text-left" },
	                        width : 180,
	                        validation: { required: true },
	                        editable: true
	                    },
	                    {
	                        title : "Created Date",
	                        field : "code",
	                        headerAttributes: { "class": "columns-title" },
	                        attributes: { class:"text-left" },
	                        width : 180,
	                        validation: { required: true },
	                        editable: true
	                    },
	                    {
	                        title : "Action",
	                        headerAttributes: { "class": "columns-title" },
	                        template :''+
	        	       		'<div class="btn-group margin-left-button">'+
	        	       	        //xoa
	        	       	        '<button ng-disabled="vm.showView" '+
	        	       	        ' type="button" class="btn btn-default padding-button box-shadow" ng-click="vm.removeItemHangmuc(dataItem)" uib-tooltip ="Xóa">' +
	        	       	        '<div class="action-button remove"   translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
	        	       	        '</button>'+
	        	       	    '</div>',
	                        width : 80
	                    } 
	                ]
	            });
    	}
    }
})();
