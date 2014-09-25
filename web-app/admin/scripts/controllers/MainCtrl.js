'use strict';

angular.module('CourseManAdmin')
  .controller('MainCtrl', ['$scope', '$location', 'AuthService', 'NotificationService', function($scope, $location, AuthService, NotificationService) {
    $scope.notifications = [];

    init();
    function init() {
      NotificationService.fetchNotifications().then(function(notifications) {
        $scope.notifications = notifications;
        $scope.$parent.notificationCount = notifications.length;
      });
    }

    $scope.open = function (notification) {
      $location.path('/courseInstance/display/' + notification.courseId + '/' + notification.courseInstanceId);
    };

    $scope.removeNotification = function (idx) {
      var notification = $scope.notifications[idx];
      $scope.notifications.splice(idx, 1);
      NotificationService.setSeen(notification);
    };

    $scope.logout = function() {
      AuthService.logout();
    }
  }]);
