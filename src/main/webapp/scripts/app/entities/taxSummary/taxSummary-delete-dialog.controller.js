'use strict';

angular.module('pOSDesignPatternsExerciseApp')
	.controller('TaxSummaryDeleteController', function($scope, $uibModalInstance, entity, TaxSummary) {

        $scope.taxSummary = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TaxSummary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
