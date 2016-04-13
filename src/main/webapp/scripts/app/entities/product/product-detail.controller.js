'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, Discount, Tax, Catalog) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
