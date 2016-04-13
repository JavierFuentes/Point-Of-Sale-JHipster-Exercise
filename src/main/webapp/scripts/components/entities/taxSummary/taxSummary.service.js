'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('TaxSummary', function ($resource, DateUtils) {
        return $resource('api/taxSummarys/:id', {}, {
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
