'use strict';

angular.module('CourseManPublic')
  .controller('CourseDisplayCtrl', ['$scope', '$routeParams', '$location', 'CourseService',
    function($scope, $routeParams, $location, CourseService) {

      $scope.course = {
        upcoming: []
      };

      init();
      function init() {
        var courseId = $routeParams.id;

        if (!courseId) {
          $location.path('/');
        }

        CourseService.fetchCourse(courseId).then(function(course) {
          $scope.course = course;
        }, function() {
          $location.path('/');
        });
      }

  }])
  .controller('CourseListCtrl', ['$scope', '$http', 'CourseService', function($scope, $http, CourseService) {
    $scope.courses = [];

    init();
    function init() {
      CourseService.fetchAll().then(function(courses) {
        $scope.courses = courses;
      });
    }

  }])
  .controller('CourseInstanceCtrl', ['$scope', '$routeParams', '$location', '$modal', 'restrictedUrl', 'Session', 'AuthService', 'CourseService', 'CourseUserService', 'DocumentService',
    function ($scope, $routeParams, $location, $modal, restrictedUrl, Session, AuthService, CourseService, CourseUserService, DocumentService) {

      $scope.restrictedUrl = restrictedUrl;

      $scope.course = {};
      $scope.courseInstance = {};
      $scope.isAttending = false;
      $scope.isRegistered = true;

      $scope.isGraded = true;
      $scope.grade = 0;

      $scope.documents = [];

      $scope.registrationModal = {};


      init();
      function init() {
        var courseInstanceId = $routeParams.id;

        CourseService.fetchInstance(courseInstanceId).then(function(courseInstance) {
          $scope.courseInstance = courseInstance;
          $scope.course = courseInstance.course;

          if (AuthService.isAuthenticated()) {
            CourseUserService.isAttending(courseInstance).then(function(isAttending) {
              $scope.isAttending = isAttending;

              if (isAttending) {
                DocumentService.fetchDocuments(courseInstance).then(function(documents) {
                  $scope.documents = documents;
                });

                CourseService.getGrade(courseInstance).then(function(courseGrade) {
                  $scope.courseGrade = courseGrade;
                })
              }
            });

            CourseUserService.isRegistered(courseInstance).then(function(isRegistered) {
              $scope.isRegistered = isRegistered;
            });
          }
        }, function() {
          $location.path('/');
        });
      }

      $scope.register = function(courseInstance) {
        $scope.registrationModal = $modal.open({
          templateUrl: 'partials/course/courseRegistrationModal.html',
          scope: newRegistrationModalScope($scope.course, courseInstance),
          size: 'sm'
        });
      };

      // Registration modal
      function newRegistrationModalScope(course, courseInstance) {
        var registrationScope = $scope.$new();

        registrationScope.courseUser = {
          comment: '',
          user: Session.user.id,
          type: 'REGISTRATION',
          courseInstance: courseInstance.id
        };

        registrationScope.course = course;
        registrationScope.courseInstance = courseInstance;

        registrationScope.submit = function(courseUser) {
          CourseUserService.register(courseUser).then(function() {
            $scope.isRegistered = true;
          });

          $scope.registrationModal.dismiss('submit');
        };

        return registrationScope;
      }

      $scope.submitGrade = function (courseGrade) {
        if (!courseGrade.id) {
          courseGrade.user = Session.user.id;
          courseGrade.courseInstance = $scope.courseInstance.id;
          CourseService.submitGrade(courseGrade)
        }
      };

  }]);
