'use strict';

angular.module('pOSDesignPatternsExerciseApp')
	.controller('SaleDeleteController', function($scope, $uibModalInstance, entity, Sale) {

        $scope.sale = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Sale.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
