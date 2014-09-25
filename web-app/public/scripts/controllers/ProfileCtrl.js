'use strict';

angular.module('CourseManPublic')
  .controller('ProfileCtrl', ['$scope', 'CourseService', function($scope, CourseService) {

    $scope.upcomingCourses = [];
    $scope.registeredCourses = [];
    $scope.collapseManager = {
      upcoming: true,
      registration: true
    };

    init();
    function init() {
      CourseService.fetchUserUpcoming().then(function(upcomingCourses) {
        $scope.upcomingCourses = upcomingCourses;
      });

      CourseService.fetchUserRegistrations().then(function(registeredCourses) {
        $scope.registeredCourses = registeredCourses;
      });
    }

  }]);
