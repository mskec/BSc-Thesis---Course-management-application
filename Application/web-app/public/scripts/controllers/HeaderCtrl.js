'use strict';

angular.module('CourseManPublic')
  .controller('HeaderCtrl', ['$scope', '$location', 'AuthService',
    function($scope, $location, AuthService) {
      $scope.search = {
        query: '',
        submit: undefined
      };

      $scope.credentials = {
        username: '',
        password: ''
      };

      $scope.search.submit = function(query) {
        if (query.length >= 3) {
          $location.path('/search').search({ 'query': query });
        }
      };

      $scope.login = function(credentials) {
        AuthService.authenticate(credentials).then(function() { },
          function(message) {
            $scope.errorMessage = message;
          });
      };

      $scope.logout = function() {
        AuthService.logout();
      };

      $scope.hideNav = function() {
        var navbarToggle = $('.navbar-toggle');
        if(navbarToggle.css('display') !== 'none'){
          navbarToggle.trigger('click');
        }
      };

    }]);
