'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('ItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Item', 'Discount', 'Tax', 'Catalog', 'Sale',
        function($scope, $stateParams, $uibModalInstance, entity, Item, Discount, Tax, Catalog, Sale) {

        $scope.item = entity;
        $scope.discounts = Discount.query();
        $scope.taxs = Tax.query();
        $scope.catalogs = Catalog.query();
        $scope.sales = Sale.query();
        $scope.load = function(id) {
            Item.get({id : id}, function(result) {
                $scope.item = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:itemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.item.id != null) {
                Item.update($scope.item, onSaveSuccess, onSaveError);
            } else {
                Item.save($scope.item, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.onCatalogChange = function() {
            console.log($scope.item);

            //$scope.item.barcode = $scope.item.catalog.barcode;
            $scope.item.description = $scope.item.catalog.product.description;
            $scope.item.price = $scope.item.catalog.price;
        }
}]);
