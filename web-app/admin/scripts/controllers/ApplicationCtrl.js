'use strict';

angular.module('CourseManAdmin')
  .controller('ApplicationCtrl', ['$scope', '$rootScope', 'USER_ROLES', 'AUTH_EVENTS', 'AuthService',
    function($scope, $rootScope, USER_ROLES, AUTH_EVENTS, AuthService) {

      $scope.currentUser = null;
      $scope.userRoles = USER_ROLES;
      $scope.isAuthorized = AuthService.isAuthorized;

      $rootScope.$on(AUTH_EVENTS.loginSuccess, function() {
        $scope.currentUser = AuthService.getCurrentUser();
      });

      if (AuthService.isAuthenticated()) {
        AuthService.loadCurrentUser().then(function(user) {
          $scope.currentUser = user;
        });
      }

  }]);
