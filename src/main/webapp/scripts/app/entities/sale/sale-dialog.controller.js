'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('SaleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sale', 'Turn', 'PaymentMethod', 'TaxSummary',
        function($scope, $stateParams, $uibModalInstance, entity, Sale, Turn, PaymentMethod, TaxSummary) {

        $scope.sale = entity;
        $scope.turns = Turn.query();
        $scope.paymentmethods = PaymentMethod.query();
        $scope.taxsummarys = TaxSummary.query();
        $scope.load = function(id) {
            Sale.get({id : id}, function(result) {
                $scope.sale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:saleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sale.id != null) {
                Sale.update($scope.sale, onSaveSuccess, onSaveError);
            } else {
                Sale.save($scope.sale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTime = {};

        $scope.datePickerForTime.status = {
            opened: false
        };

        $scope.datePickerForTimeOpen = function($event) {
            $scope.datePickerForTime.status.opened = true;
        };
}]);
