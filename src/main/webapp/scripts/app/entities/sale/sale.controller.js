'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('SaleController', function ($scope, $state, Sale, ParseLinks) {

        $scope.sales = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Sale.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.sales = result;
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
            $scope.sale = {
                completed: null,
                time: null,
                totalamount: null,
                payedamount: null,
                paymentauth: null,
                id: null
            };
        };
    });
