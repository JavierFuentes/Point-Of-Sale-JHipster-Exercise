'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('TurnDetailController', function ($scope, $rootScope, $stateParams, entity, Turn, Pointofsale, Sale, User) {
        $scope.turn = entity;
        $scope.load = function (id) {
            Turn.get({id: id}, function(result) {
                $scope.turn = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:turnUpdate', function(event, result) {
            $scope.turn = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
