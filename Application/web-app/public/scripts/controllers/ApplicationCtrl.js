"use strict";

angular.module('CourseManPublic')
  .controller('ApplicationCtrl', ['$scope', '$rootScope', 'USER_ROLES', 'AUTH_EVENTS', 'AuthService', 'NotificationService',
    function($scope, $rootScope, USER_ROLES, AUTH_EVENTS, AuthService, NotificationService) {

      $scope.currentUser = null;
      $scope.userRoles = USER_ROLES;
      $scope.isAuthorized = AuthService.isAuthorized;
      $scope.isAuthenticated = AuthService.isAuthenticated;

      $scope.notificationCount = '';

      init();
      function init() {
        $rootScope.$on(AUTH_EVENTS.loginSuccess, function() {
          $scope.currentUser = AuthService.getCurrentUser();
        });

        $rootScope.$on(AUTH_EVENTS.authenticated, function() {
          NotificationService.countNew().then(function(count) {
            $scope.notificationCount = count;
          })
        });

        if (AuthService.isAuthenticated()) {
          AuthService.loadCurrentUser().then(function(user) {
            $scope.currentUser = user;
          });
        }
      }

    }]);

