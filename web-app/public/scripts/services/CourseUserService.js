'use strict';

angular.module('CourseManPublic')
  .factory('CourseUserService', ['$http', '$q', '$rootScope', 'restrictedUrl', function($http, $q, $rootScope, restrictedUrl) {
    var factory = {};
    var courseUserUrl = restrictedUrl + '/courseUser';

    factory.register = function(courseUser) {
      var defered = $q.defer();

      $http({ method: 'POST', url: courseUserUrl + '/register', data: courseUser
      }).success(function(response) {
        $rootScope.message = {
          text: response.message,
          style: 'alert-success',
          visible: true
        };
        defered.resolve();
      }).error(function(data) {
        $rootScope.message = {
          text: data.error,
          style: 'alert-danger',
          visible: true
        };
        defered.reject();
      });

      return defered.promise;
    };

    factory.isRegistered = function(courseInstance) {
      var defered = $q.defer();

      $http({ method: 'GET', url: courseUserUrl + '/isRegistered', params: {id: courseInstance.id}
      }).success(function(data) {
        defered.resolve(data.isRegistered);
      }).error(function() {
        defered.reject(false);
      });

      return defered.promise;
    };

    factory.isAttending = function (courseInstance) {
      var defered = $q.defer();

      $http({ method: 'GET', url: courseUserUrl + '/isAttending', params: {id: courseInstance.id}
      }).success(function (response) {
        defered.resolve(response.isAttending);
      }).error(function () {
        defered.reject();
      });

      return defered.promise;
    };

    return factory;
  }]);
