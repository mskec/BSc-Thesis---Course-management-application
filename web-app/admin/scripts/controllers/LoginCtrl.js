'use strict';

angular.module('CourseManAdmin')
  .controller('LoginCtrl', ['$scope', 'AuthService',
    function($scope, AuthService) {

    $scope.credentials = {
      username: '',
      password: ''
    };

    $scope.login = function(credentials) {
      AuthService.authenticate(credentials).then(function() { },
        function(message) {
          $scope.errorMessage = message;
      });
    }

  }]);