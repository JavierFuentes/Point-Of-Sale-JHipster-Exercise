'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('PaymentMethod', function ($resource, DateUtils) {
        return $resource('api/paymentMethods/:id', {}, {
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
