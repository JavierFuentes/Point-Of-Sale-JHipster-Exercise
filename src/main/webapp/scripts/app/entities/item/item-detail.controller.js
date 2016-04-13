'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('ItemDetailController', function ($scope, $rootScope, $stateParams, entity, Item, Discount, Tax, Catalog) {
        $scope.item = entity;
        $scope.load = function (id) {
            Item.get({id: id}, function(result) {
                $scope.item = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:itemUpdate', function(event, result) {
            $scope.item = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
