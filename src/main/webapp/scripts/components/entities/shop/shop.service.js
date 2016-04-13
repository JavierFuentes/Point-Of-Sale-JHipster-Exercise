'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('Shop', function ($resource, DateUtils) {
        return $resource('api/shops/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
