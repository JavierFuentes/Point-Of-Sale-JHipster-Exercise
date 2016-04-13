'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('ProductDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Product', 'Discount', 'Tax', 'Catalog',
        function($scope, $stateParams, $uibModalInstance, entity, Product, Discount, Tax, Catalog) {

        $scope.product = entity;
        $scope.discounts = Discount.query();
        $scope.taxs = Tax.query();
        $scope.catalogs = Catalog.query();
        $scope.load = function(id) {
            Product.get({id : id}, function(result) {
                $scope.product = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:productUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.product.id != null) {
                Product.update($scope.product, onSaveSuccess, onSaveError);
            } else {
                Product.save($scope.product, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
