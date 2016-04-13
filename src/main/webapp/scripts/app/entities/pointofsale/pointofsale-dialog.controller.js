'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('PointofsaleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pointofsale', 'Shop', 'Turn',
        function($scope, $stateParams, $uibModalInstance, entity, Pointofsale, Shop, Turn) {

        $scope.pointofsale = entity;
        $scope.shops = Shop.query();
        $scope.turns = Turn.query();
        $scope.load = function(id) {
            Pointofsale.get({id : id}, function(result) {
                $scope.pointofsale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:pointofsaleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pointofsale.id != null) {
                Pointofsale.update($scope.pointofsale, onSaveSuccess, onSaveError);
            } else {
                Pointofsale.save($scope.pointofsale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
