'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('TaxSummaryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TaxSummary', 'Tax', 'Sale',
        function($scope, $stateParams, $uibModalInstance, entity, TaxSummary, Tax, Sale) {

        $scope.taxSummary = entity;
        $scope.taxs = Tax.query();
        $scope.sales = Sale.query();
        $scope.load = function(id) {
            TaxSummary.get({id : id}, function(result) {
                $scope.taxSummary = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:taxSummaryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.taxSummary.id != null) {
                TaxSummary.update($scope.taxSummary, onSaveSuccess, onSaveError);
            } else {
                TaxSummary.save($scope.taxSummary, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
