'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('ShopDetailController', function ($scope, $rootScope, $stateParams, entity, Shop, Catalog, Pointofsale) {
        $scope.shop = entity;
        $scope.load = function (id) {
            Shop.get({id: id}, function(result) {
                $scope.shop = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:shopUpdate', function(event, result) {
            $scope.shop = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
