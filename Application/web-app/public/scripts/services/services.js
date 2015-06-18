'use strict';

/* Services */

angular.module('CourseManPublic')
  .value('version', '1.0')
  .factory('RegistrationService', ['$http', '$q', '$rootScope', 'publicUrl', function($http, $q, $rootScope, publicUrl) {
    var factory = {};

    factory.registration = function(user) {
      var defered = $q.defer();

      $http({ method: 'POST', url: publicUrl + '/user', data: user
        }).success(function(response) {
          $rootScope.message = {
            text: response.message,
            style: 'alert-success',
            visible: true
          };
          defered.resolve();
        }).error(function(response) {
          $rootScope.message = {
            style: 'alert-danger',
            visible: true
          };
          if (response.errors) {
            $rootScope.message.text = response.errors;
            defered.reject(response.errors);
          }
          else {
            $rootScope.message.text = response.error;
            defered.reject(response.error);
          }
        });

      return defered.promise;
    };

    return factory;
  }])
  .factory('NotificationService', ['$http', '$q', 'restrictedUrl', function ($http, $q, restrictedUrl) {
    var factory = {};
    var notificationUrl = restrictedUrl + '/notification';

    factory.countNew = function () {
      var defered = $q.defer();

      $http({ method: 'GET', url: notificationUrl + '/count'
      }).success(function (response) {
        defered.resolve(response.count);
      }).error(function () {
        defered.reject();
      });

      return defered.promise;
    };

    factory.fetchNew = function () {
      var defered = $q.defer();

      $http({ method: 'GET', url: notificationUrl + '/list'
      }).success(function (notifications) {
        defered.resolve(notifications);
      }).error(function () {
        defered.reject();
      });

      return defered.promise;
    };

    factory.setSeen = function (notification) {
      var defered = $q.defer();

      $http({ method: 'POST', url: notificationUrl + '/seen', data: {id: notification.id}
      }).success(function () {
        defered.resolve();
      }).error(function () {
        defered.reject();
      });

      return defered.promise;
    };

    return factory;
  }])
  .factory('DocumentService', ['$http', '$q', 'restrictedUrl', function ($http, $q, restrictedUrl) {
    var factory = {};
    var documentUrl = restrictedUrl + '/document';

    factory.fetchDocuments = function (courseInstance) {
      var defered = $q.defer();

      $http({ method: 'GET', url: documentUrl + '/list', params: {id: courseInstance.id}
      }).success(function (documents) {
        defered.resolve(documents);
      }).error(function () {
        console.log('| DocumentService - #fetchDocuments - error while fetching documents.');
        defered.reject();
      });

      return defered.promise;
    };

    return factory;
  }]);
