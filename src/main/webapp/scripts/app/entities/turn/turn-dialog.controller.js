'use strict';

angular.module('pOSDesignPatternsExerciseApp').controller('TurnDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Turn', 'Pointofsale', 'Sale', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Turn, Pointofsale, Sale, User) {

        $scope.turn = entity;

        //$scope.pointofsales = Pointofsale.query();
        $scope.pointofsales = Pointofsale.activePointofsales();  // Only Actives

        $scope.sales = Sale.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Turn.get({id : id}, function(result) {
                $scope.turn = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pOSDesignPatternsExerciseApp:turnUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.turn.id != null) {
                Turn.update($scope.turn, onSaveSuccess, onSaveError);
            } else {
                Turn.save($scope.turn, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStart = {};

        $scope.datePickerForStart.status = {
            opened: false
        };

        $scope.datePickerForStartOpen = function($event) {
            $scope.datePickerForStart.status.opened = true;
        };
        $scope.datePickerForEnd = {};

        $scope.datePickerForEnd.status = {
            opened: false
        };

        $scope.datePickerForEndOpen = function($event) {
            $scope.datePickerForEnd.status.opened = true;
        };
}]);
