'use strict';

angular.module('CourseManPublic')
  .controller('SearchCtrl', ['$scope', '$location', 'CourseService', function ($scope, $location, CourseService) {

    $scope.query = '';
    $scope.courses = [];

    $scope.search = function(query) {
      CourseService.courseSearch(query).then(function(courses) {
        $scope.courses = courses;
      });
    };

    init();
    function init() {
      $scope.query = ($location.search()).query || '';
      if ($scope.query.length > 0) {
        $scope.search($scope.query);
      }
    }

  }]);
