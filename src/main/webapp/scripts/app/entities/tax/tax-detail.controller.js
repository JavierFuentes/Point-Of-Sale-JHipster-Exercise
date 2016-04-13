'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('TaxDetailController', function ($scope, $rootScope, $stateParams, entity, Tax, Product, TaxSummary, Item) {
        $scope.tax = entity;
        $scope.load = function (id) {
            Tax.get({id: id}, function(result) {
                $scope.tax = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:taxUpdate', function(event, result) {
            $scope.tax = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
