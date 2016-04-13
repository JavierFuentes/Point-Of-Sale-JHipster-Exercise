 'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-pOSDesignPatternsExerciseApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-pOSDesignPatternsExerciseApp-params')});
                }
                return response;
            }
        };
    });
