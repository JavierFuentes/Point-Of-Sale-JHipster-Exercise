'use strict';

angular.module('pOSDesignPatternsExerciseApp')
	.controller('PointofsaleDeleteController', function($scope, $uibModalInstance, entity, Pointofsale) {

        $scope.pointofsale = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Pointofsale.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
