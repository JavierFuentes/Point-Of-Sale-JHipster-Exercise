'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('SaleDetailController', function ($scope, $rootScope, $stateParams, entity, Sale, Turn, PaymentMethod, TaxSummary) {
        $scope.sale = entity;
        $scope.load = function (id) {
            Sale.get({id: id}, function(result) {
                $scope.sale = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:saleUpdate', function(event, result) {
            $scope.sale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
