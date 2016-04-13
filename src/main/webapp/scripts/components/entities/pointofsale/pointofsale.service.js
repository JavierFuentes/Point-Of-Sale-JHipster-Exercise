'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('Pointofsale', function ($resource, DateUtils) {
        return $resource('api/pointofsales/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },

            //http://localhost:8080/api/activePointofsales
            'activePointofsales': {
                method: 'GET',
                isArray: true,
                url: 'api/activePointofsales'
            }
        });
    });
