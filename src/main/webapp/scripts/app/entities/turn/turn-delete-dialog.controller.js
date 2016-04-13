'use strict';

angular.module('pOSDesignPatternsExerciseApp')
	.controller('TurnDeleteController', function($scope, $uibModalInstance, entity, Turn) {

        $scope.turn = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Turn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
