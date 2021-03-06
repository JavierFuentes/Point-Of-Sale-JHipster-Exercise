'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('TaxSummaryController', function ($scope, $state, TaxSummary, ParseLinks) {

        $scope.taxSummarys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            TaxSummary.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.taxSummarys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taxSummary = {
                taxbase: null,
                taxrate: null,
                taxamount: null,
                totalamount: null,
                id: null
            };
        };
    });
