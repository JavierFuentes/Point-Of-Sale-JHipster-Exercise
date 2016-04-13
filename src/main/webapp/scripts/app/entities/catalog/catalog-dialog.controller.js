'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('CatalogDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Catalog', 'Product', 'Shop', 'Item',
        function($scope, $stateParams, $uibModalInstance, entity, Catalog, Product, Shop, Item) {

        $scope.catalog = entity;
        $scope.products = Product.query();
        $scope.shops = Shop.query();
        $scope.items = Item.query();
        $scope.load = function(id) {
            Catalog.get({id : id}, function(result) {
                $scope.catalog = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:catalogUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.catalog.id != null) {
                Catalog.update($scope.catalog, onSaveSuccess, onSaveError);
            } else {
                Catalog.save($scope.catalog, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
