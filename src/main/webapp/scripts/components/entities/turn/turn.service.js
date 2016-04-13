'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('Turn', function ($resource, DateUtils) {
        return $resource('api/turns/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.start = DateUtils.convertDateTimeFromServer(data.start);
                    data.end = DateUtils.convertDateTimeFromServer(data.end);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
