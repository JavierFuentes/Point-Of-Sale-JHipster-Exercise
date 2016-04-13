'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entity', {
                abstract: true,
                parent: 'site'
            });
    });
