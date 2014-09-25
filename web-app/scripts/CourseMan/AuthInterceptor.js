'use strict';

angular.module('CourseMan')
  .factory('AuthInterceptor', ['$rootScope', '$q', 'AUTH_EVENTS', function($rootScope, $q, AUTH_EVENTS) {
    var factory = {};

    factory.responseError = function(response) {
      if (response.status === 401) {
        $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated, response);
      }
      if (response.status === 403) {
        $rootScope.$broadcast(AUTH_EVENTS.notAuthorized, response);
      }
      if (response.status === 419 || response.status === 440) {
        $rootScope.$broadcast(AUTH_EVENTS.sessionTimeout, response);
      }
      return $q.reject(response);
    }

    return factory;
  }]);
