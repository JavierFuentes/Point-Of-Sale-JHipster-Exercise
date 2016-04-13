'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('PaymentMethodDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentMethod', 'Sale',
        function($scope, $stateParams, $uibModalInstance, entity, PaymentMethod, Sale) {

        $scope.paymentMethod = entity;
        $scope.sales = Sale.query();
        $scope.load = function(id) {
            PaymentMethod.get({id : id}, function(result) {
                $scope.paymentMethod = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:paymentMethodUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.paymentMethod.id != null) {
                PaymentMethod.update($scope.paymentMethod, onSaveSuccess, onSaveError);
            } else {
                PaymentMethod.save($scope.paymentMethod, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
