'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('ShopDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Shop', 'Catalog', 'Pointofsale',
        function($scope, $stateParams, $uibModalInstance, entity, Shop, Catalog, Pointofsale) {

        $scope.shop = entity;
        $scope.catalogs = Catalog.query();
        $scope.pointofsales = Pointofsale.query();
        $scope.load = function(id) {
            Shop.get({id : id}, function(result) {
                $scope.shop = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:shopUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.shop.id != null) {
                Shop.update($scope.shop, onSaveSuccess, onSaveError);
            } else {
                Shop.save($scope.shop, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
