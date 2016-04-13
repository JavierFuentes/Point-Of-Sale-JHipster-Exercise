'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('TaxSummaryDetailController', function ($scope, $rootScope, $stateParams, entity, TaxSummary, Tax, Sale) {
        $scope.taxSummary = entity;
        $scope.load = function (id) {
            TaxSummary.get({id: id}, function(result) {
                $scope.taxSummary = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:taxSummaryUpdate', function(event, result) {
            $scope.taxSummary = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
