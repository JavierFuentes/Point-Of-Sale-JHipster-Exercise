'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('DiscountDetailController', function ($scope, $rootScope, $stateParams, entity, Discount, Product, Item) {
        $scope.discount = entity;
        $scope.load = function (id) {
            Discount.get({id: id}, function(result) {
                $scope.discount = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:discountUpdate', function(event, result) {
            $scope.discount = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
