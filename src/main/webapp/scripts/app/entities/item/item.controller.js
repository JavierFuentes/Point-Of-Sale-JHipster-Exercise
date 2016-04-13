'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('ItemController', function ($scope, $state, Item, ParseLinks) {

        $scope.items = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Item.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.items = result;
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
            $scope.item = {
                barcode: null,
                description: null,
                quantity: null,
                price: null,
                discountpct: null,
                discountamount: null,
                baseamount: null,
                taxrate: null,
                taxamount: null,
                totalamount: null,
                id: null
            };
        };
    });
