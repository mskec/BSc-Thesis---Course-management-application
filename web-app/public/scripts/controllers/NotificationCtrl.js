'use strict';

angular.module('CourseManPublic')
  .controller('NotificationCtrl', ['$scope', 'NotificationService', function ($scope, NotificationService) {
    $scope.notifications = [];

    init();
    function init() {
      NotificationService.fetchNew().then(function(notifications) {
        $scope.notifications = notifications;
        $scope.$parent.notificationCount = notifications.length;
      });
    }

    $scope.removeNotification = function (idx) {
      var notification = $scope.notifications[idx];
      $scope.notifications.splice(idx, 1);
      NotificationService.setSeen(notification).then(function() {
        $scope.$parent.notificationCount--;
      });
    };

  }]);
