'use strict';

angular.module('CourseManAdmin')
  .factory('NotificationService', ['$http', '$q', 'adminUrl', function ($http, $q, adminUrl) {
    var factory = {};
    var notificationUrl = adminUrl + '/notification';

    factory.fetchNotifications = function () {
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
  }]);
