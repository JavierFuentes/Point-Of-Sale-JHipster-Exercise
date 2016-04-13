'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('admin', {
                abstract: true,
                parent: 'site'
            });
    });
