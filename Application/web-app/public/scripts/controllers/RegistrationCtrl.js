'use strict';

angular.module('CourseManPublic')
  .controller('RegistrationCtrl', ['$scope', '$location', 'RegistrationService', 'AuthService', function($scope, $location, RegistrationService, AuthService) {
    $scope.user = {
      username: '',
      email: '',
      firstName: '',
      lastName: '',
      password: ''
    };
    $scope.password_verify = '';
    $scope.passwordsMatchError = false;

    $scope.registration = function(user) {
      if (passwordsMatch()) {
        RegistrationService.registration(user).then(function() {
          AuthService.authenticate(user);
          $location.path('/');
        }, function(errors) {
          // TODO prikaži poruku
        });
      }
    };

    var passwordsMatch = function() {
      $scope.passwordsMatchError = $scope.user.password !== $scope.password_verify;
      return !$scope.passwordsMatchError;
    };

  }]);
