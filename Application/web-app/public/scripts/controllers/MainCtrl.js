'use strict';

angular.module('CourseManPublic')
  .controller('MainCtrl', ['$scope', '$http', 'CourseService', function($scope, $http, CourseService) {

    $scope.courseInstances = [];

    init();
    function init() {
      var loadMaxCourses = 5;

      CourseService.fetchUpcoming(loadMaxCourses).then(function(courseInstances) {
        $scope.courseInstances = courseInstances;
      });
    }

  }]);
