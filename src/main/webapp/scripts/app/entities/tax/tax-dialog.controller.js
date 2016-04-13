'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('TaxDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tax', 'Product', 'TaxSummary', 'Item',
        function($scope, $stateParams, $uibModalInstance, entity, Tax, Product, TaxSummary, Item) {

        $scope.tax = entity;
        $scope.products = Product.query();
        $scope.taxsummarys = TaxSummary.query();
        $scope.items = Item.query();
        $scope.load = function(id) {
            Tax.get({id : id}, function(result) {
                $scope.tax = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:taxUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tax.id != null) {
                Tax.update($scope.tax, onSaveSuccess, onSaveError);
            } else {
                Tax.save($scope.tax, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
