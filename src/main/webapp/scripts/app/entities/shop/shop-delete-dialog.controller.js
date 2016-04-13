'use strict';

angular.module('pOSDesignPatternsExerciseApp')
	.controller('ShopDeleteController', function($scope, $uibModalInstance, entity, Shop) {

        $scope.shop = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Shop.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
