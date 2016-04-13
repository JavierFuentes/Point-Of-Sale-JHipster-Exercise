'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('PointofsaleDetailController', function ($scope, $rootScope, $stateParams, entity, Pointofsale, Shop, Turn) {
        $scope.pointofsale = entity;
        $scope.load = function (id) {
            Pointofsale.get({id: id}, function(result) {
                $scope.pointofsale = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:pointofsaleUpdate', function(event, result) {
            $scope.pointofsale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
