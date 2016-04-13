'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


