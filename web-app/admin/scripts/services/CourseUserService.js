'use strict';

angular.module('CourseManAdmin')
    .factory('CourseUserService', ['$http', '$q', '$rootScope', 'adminUrl', function($http, $q, $rootScope, adminUrl) {
        var factory = {};

        factory.accept = function(courseUser) {
            var deferred = $q.defer();

            $http({ method: 'POST', url: adminUrl + '/courseUser/accept', data: courseUser
            }).success(function(response) {
                $rootScope.message = {
                    text: response.message,
                    style: 'alert-success',
                    visible: true
                };
                deferred.resolve(response.courseUser);
            }).error(function(data) {
                $rootScope.message = {
                    text: data.error,
                    style: 'alert-danger',
                    visible: true
                };
                deferred.reject();
            });

            return deferred.promise;
        };

        factory.decline = function(courseUser) {
            var deferred = $q.defer();

            $http({ method: 'POST', url: adminUrl + '/courseUser/decline', data: courseUser
            }).success(function(response) {
                $rootScope.message = {
                    text: response.message,
                    style: 'alert-success',
                    visible: true
                };
                deferred.resolve(response.courseUser);
            }).error(function(data) {
                $rootScope.message = {
                    text: data.error,
                    style: 'alert-danger',
                    visible: true
                }
                deferred.reject();
            });

            return deferred.promise;
        };

        return factory;
    }]);