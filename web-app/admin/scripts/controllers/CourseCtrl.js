'use strict';

angular.module('CourseManAdmin')
  .controller('CourseListCtrl', ['$scope', 'courseApi',
    function($scope, courseApi) {
      $scope.courses = [];

      init();
      function init() {
        loadCourses();
      }

      function loadCourses() {
        courseApi.list().then(function(courses) {
          $scope.courses = courses;
        });
      }

      $scope.delete = function(idx) {
        var course = $scope.courses[idx];
        courseApi.delete(course.id).then(function() {
          $scope.courses.splice(idx, 1);
        });
      };

    }])
  .controller('CourseFormCtrl', ['$scope', '$routeParams', 'courseApi', 'codebookApi',
    function($scope, $routeParams, courseApi, codebookApi) {

      $scope.debug = { visible: false };
      $scope.course = {};
      $scope.masterCourse = {};
      $scope.categories = [];

      init();
      function init() {

        if ($routeParams.id) {
          // Loading CourseDefinition
          courseApi.get($routeParams.id).then(function(course) {
            $scope.masterCourse = course;
            $scope.course = angular.copy($scope.masterCourse);
          });
        }

        // Loading categories
        codebookApi.categoryList().then(function(categories) {
          $scope.categories = categories;
        });
      }

      $scope.save = function() {
        if ($scope.course.id) {
          $scope.update();
          return;
        }

        courseApi.save($scope.course).then(function(id) {
          $scope.course.id = id;
          $scope.masterCourse = angular.copy($scope.course);
        });
        $scope.form.$setPristine();
      };

      $scope.update = function() {
        courseApi.update($scope.course).then(function() {
          $scope.masterCourse = angular.copy($scope.course);
        });
        $scope.form.$setPristine();
      };

      $scope.delete = function() {
        courseApi.delete($scope.course.id).then(function() {
          $scope.masterCourse = {};
          $scope.course = {};
        });
      };

      $scope.reset = function() {
        $scope.course = angular.copy($scope.masterCourse);
        $scope.form.$setPristine();
      };


    }])
  .controller('CourseDisplayCtrl', ['$scope', '$routeParams', '$location', 'courseApi', 'courseInstanceApi',
    function($scope, $routeParams, $location, courseApi, courseInstanceApi) {

      $scope.course = {};

      init();
      function init() {
        courseApi.get($routeParams.id).then(function(course) {
          $scope.course = course;
        }, function() {
          // TODO alert message
          $location.path('/course');
        });
      }

      $scope.delete = function() {
        courseApi.delete($scope.course.id).then(function() {
          $scope.course = {};
          $location.path('/course');
        });
      };

      $scope.deleteCourseInstance = function(idx) {
        var courseInstance = $scope.course.courseInstances[idx];

        courseInstanceApi.delete(courseInstance.id).then(function() {
          $scope.course.courseInstances.splice(idx, 1)
        });
      }
    }]);