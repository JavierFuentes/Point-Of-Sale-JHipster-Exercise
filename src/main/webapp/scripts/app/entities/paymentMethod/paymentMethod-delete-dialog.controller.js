'use strict';

angular.module('pOSDesignPatternsExerciseApp')
	.controller('PaymentMethodDeleteController', function($scope, $uibModalInstance, entity, PaymentMethod) {

        $scope.paymentMethod = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PaymentMethod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
