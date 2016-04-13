'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('CatalogDetailController', function ($scope, $rootScope, $stateParams, entity, Catalog, Product, Shop, Item) {
        $scope.catalog = entity;
        $scope.load = function (id) {
            Catalog.get({id: id}, function(result) {
                $scope.catalog = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:catalogUpdate', function(event, result) {
            $scope.catalog = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
