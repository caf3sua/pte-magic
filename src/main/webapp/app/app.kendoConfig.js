(function() {
    'use strict';
    angular
        .module('pteMagicApp')
        .factory('kendoConfig', kendoConfig);

    kendoConfig.$inject = ['$window'];

    function kendoConfig ($window) {
    	if (!!$window.kendoConfig) {
            return $window.kendoConfig;
        }

        function optionConfig() {
            var config = {};

            var data = new Map();

            config.set = function (key, value) {
                data.set(key, value);
            };

            config.get = function (key) {
                return angular.copy(data.get(key));
            };

            config.set('validatorOptions', {
                rules: {
                    maxTextLength: function (textarea) {
                        if (textarea.is("[data-maxtextlength-msg]") && textarea.val() != "") {
                            var maxlength = textarea.attr("data-maxtextlength");
                            var value = textarea.data("kendoEditor").value();
                            return value.replace(/<[^>]+>/g, "").length <= maxlength;
                        }

                        return true;
                    },
                    maxHtmlLength: function (textarea) {
                        if (textarea.is("[data-maxhtmllength-msg]") && textarea.val() != "") {
                            var maxlength = textarea.attr("data-maxhtmllength");
                            var value = textarea.data("kendoEditor").value();
                            return value.length <= maxlength;
                        }
                        return true;
                    },
                    cascade: function (input) {
                        if (!input.is("[cascade]") || input.val() == "")
                            return true;
                        var currValue = input.val();
                        var base = input.attr('cascade');
                        var baseValue = $("[name='" + base + "']").val();
                        return baseValue || (!baseValue && !currValue);
                    },
                    matches: function (input) {
                        var matches = input.data('matches');
                        // if the `data-matches attribute was found`
                        if (matches) {
                            // get the input to match
                            var match = $(matches);
                            // trim the values and check them
                            return $.trim(input.val()) === $.trim(match.val());
                        }
                        // don't perform any match validation on the
                        // input
                        return true;

                    },
                    textRequired: function (input) {
					 if(input.parent().parent().hasClass("k-pager-sizes")) return true;
					 if(input.is("[kendo-multi-select]"))  return !(input.is("[required]") && input.val().length == 0);
                        return !(input.is("[required]") && (input.val().trim() == '' || input.val() =="__/__/____"));
                    },
					numberRequired: function (input) {
							if((input.is("[number-required]") && input.val().trim() == '')) return false;
                        return !(input.is("[number-required]") && kendo.parseFloat(input.val(),'n0')<=0);
                    },
                    minyear: function (input) {
                        if (input.is("[min-year]") && input.val() != "") {
                            var format = "MM/dd/yyyy";
                            var date = kendo.parseDate(input.val(), format);
                            var minYear = input.attr("min-year");
                            return date.getFullYear() >= minYear;
                        }
                        return true;
                    },
                    validmask: function (input) {
                        if (input.is("[data-validmask-msg]") && input.val() != "") {
                            var maskedtextbox = input.data("kendoMaskedTextBox");
                            return maskedtextbox.value().indexOf(maskedtextbox.options.promptChar) === -1;
                        }
                        return true;
                    },
                    date: function (input) {
                        var format = "dd/MM/yyyy";						
                        if (input.is("[data-date-msg]") && input.val() !== '' && input.val() !=="__/__/____") {
                            return kendo.parseDate(input.val(), format) !== null;
                        }
                        return true;
                    },
                    dateempty: function (input) {
                    	if (input.val() == '' || input.val() == '__/__/____') {
                    		return true;
                    	}
                        var format = "dd/MM/yyyy";
                        if (input.is("[data-dateempty-msg]")) {
                            return kendo.parseDate(input.val(), format) !== null;
                        }
                        return true;
                    },
                    time: function (input) {
                        var format = "hh:mm";
                        if (input.is("[data-time-msg]") && input.val() !== '') {
                            var date = new Date();
                            var inputDate = new Date(date.format("mm/dd/yyyy") + " " + input.val());
                            //return kendo.parseDate(input.val(), format) !== null;
                            return inputDate.toString() !== "Invalid Date";
                        }
                        return true;
                    },
                    cbs: function (input) {
                        if (input.is("[data-cbs]") && input.filter("[type=checkbox]").length > 0) {
                            if (input.filter("[type=checkbox][class='required']")) {
                                var qs = input.attr("data-cbs");
                                var grs = $("input[type='checkbox'][data-cbs='" + qs + "'].required");
                                var ans = $("input[type='checkbox'][data-cbs='" + qs + "']:checked").length;
                                return parseInt(ans) > 0;
                            }
                        }
                        return true;
                    },
                    pastdate: function (input) {
                        if (input.is("[data-pastdate-msg]") && input.val() != "") {
                        	var format = "dd/MM/yyyy";
                            var date = kendo.parseDate(input.val(),format),
                                otherDate = new Date();

                            return date.getTime() < otherDate.getTime();
                        }

                        return true;
                    },
                    greaterdate: function (input) {
                        if (input.is("[data-greaterdate-msg]") && input.val() != "") {
                        	var format = "dd/MM/yyyy";
                            var date = kendo.parseDate(input.val(),format),
                                otherDate = kendo.parseDate($("[name='" + input.attr("greaterdateField") + "']").val(),format);

                            input.attr('round', '1');

                            var otherInput = $("[name='" + input.attr("greaterdateField") + "']");
                            if (otherInput.attr('round') !== '1') {
                                this.validateInput(otherInput);
                            } else {
                                otherInput.removeAttr('round');
                            }

                            return otherDate == null || otherDate.getTime() <= date.getTime();
                        }

                        $('input').removeAttr('round');

                        return true;
                    },
                    lessdate: function (input) {
                        if (input.is("[data-lessdate-msg]") && input.val() != "") {
                        	var format = "dd/MM/yyyy";
                            var date = kendo.parseDate(input.val(),format),
                                otherDate = kendo.parseDate($("[name='" + input.attr("lessdateField") + "']").val(),format);

                            input.attr('round', '1');

                            var otherInput = $("[name='" + input.attr("lessdateField") + "']");
                            if (otherInput.attr('round') !== '1') {
                                this.validateInput(otherInput);
                            } else {
                                otherInput.removeAttr('round');
                            }

                            return otherDate == null || otherDate.getTime() >= date.getTime();
                        }

                        $('input').removeAttr('round');

                        return true;
                    }
                },
                messages: {
                    //email : "That does not appear to be a valid email address",
					numberRequired: function (input) {					
                        return input.attr("data-required-msg");
                    },
					textRequired: function (input) {
					 return input.attr("data-required-msg");
                    },
                    matches: function (input) {
                        return input.data("matchesMsg");
                    },
                    cbs: function () {
                        return "At least one race must be selected!";
                    }
                }
            });

            /***************************
             * Combox Options
             * ************************/
            config.set('baseComboOptions', {
                placeholder: "Auto suggest for at least 2 characters",
                optionLabel: "",
                filter: "contains",
                delay: 1000,
                //template: "<b>#=code#</b> - #=description#",
                headerTemplate: '<div class="noDataMessage">No results to display</div>',
                autoBind: false,
                minLength: 1,
                change: function (e) {
                    if (this.selectedIndex === -1 && this.value()) {
                        if (this.dataSource.view().length > 0) {
                            this.select(0)
                        } else {
                            this.value("");
                        }
                    }
                },
                dataBound: function () {
                    var noItems = this.list.find(".noDataMessage");

                    //					if (!this.dataSource.view()[0]) {
                    if (this.dataSource.page() != undefined && (this.dataSource.view() == undefined || this.dataSource.view().length == 0)) {
                        noItems.show();
                        if (this.input.val() != "") {
                            this.popup.open();
                        }
                    } else {
                        noItems.hide();
                    }
                }
            });

            config.getGridOptions = function (options) {
                /* Common Grid Options */
                var commonGridOptions = {
                    sortable: true,
                    selectable: true,
                    //resizable: true,
                    navigatable: true,
                    editable: {
                        createAt: "bottom"
                    },
                    columnMenu: {
                        filterable: false,
                        messages: {
                            columns: "Chọn cột",
                            filter: "Lọc",
                            sortAscending: "Tăng dần",
                            sortDescending: "Giảm dần"
                        }
                    },
                    pageable: {
                        pageSize: 20,
                        pageSizes: true,
                        refresh: true,
                        messages: {
                            display: "{0} - {1} của {2} kết quả",
                            empty: "Không có kết quả nào",
                            page: "trang",
                            of: "của {0}",
                            itemsPerPage: "kết quả/trang",
                            first: "Về trang đầu",
                            previous: "Về trang trước",
                            next: "Về trang tiếp theo",
                            last: "Về trang cuối",
                            refresh: "Làm mới",
                            allPages: "Tất"
                        }
                    },
                    //height: 400,
                    autoBind: true,
                    excelExport:function (e) {
                    	
                        var sheet = e.workbook.sheets[0];
                        var columns = e.sender.columns;
                        for (var i = 1; i < sheet.rows.length; i++) {
                        	
                            var index = 0;
                            var row = sheet.rows[i];
                            if (row.type != 'data') {
                                continue;
                            }
                            var dataItem = e.data[i - 1];

                            for (var j in columns) {
                                if (!!columns[j].hidden || columns[j].template === undefined ||j==0) {
                                    continue;
                                }
                                if(index>=row.cells.length){
                             	   continue;
                                }
                                if (columns[j].template) {
                                    var template = kendo.template(columns[j].template);
                                    row.cells[index].value = $("<span>" + template(dataItem) + "</span>").text();
                                    if (row.cells[index].value == 'undefined' || row.cells[index].value == 'null') {
                                        row.cells[index].value = "";
                                    }
                                }

                                index++;
                            }
                        }
                    }
                };

                function keyListener(grid) {
                    grid.element.click(function (event) {
                        window.onkeydown = function (e) {
                            if ($(e.target).closest('td').is(':last-child') && $(e.target).closest('tr').is(':last-child')) {
                                grid.addRow();
                                return false;
                            }

                            // Extend key functions
                            if ((e.which || e.keyCode) == 116 || // F5
                                (e.which || e.keyCode) == 122) { // F11
                                grid.addRow();
                            }
                        }
                    });

                    //                grid.element.on('keydown', function (e) {
                    //
                    //                });
                }

                if (options.dataBound) {
                    var dataBound = angular.copy(options.dataBound);

                    options.dataBound = function () {
                        //keyListener(this);
                        dataBound(this);
                    };
                } else {
                    options.dataBound = function () {
                        //keyListener(this);
                    };
                }

                return angular.extend(commonGridOptions, options);
            }

            config.getDSOptions = function (options) {
                var commonDataSourceOptions = {
                    type: "json",
                    serverFiltering: true,
                    serverPaging: true,
                    pageSize: 20,
                    batch: true,
                    schema: {
                        data: "data",
                        type: 'json',
                        total: "total"
                    }
                };

                return angular.extend(commonDataSourceOptions, options);
            }

            return config;
        }

        $window.kendoConfig = optionConfig();

        return $window.kendoConfig;
    }
})();
