'use strict';

angular.module('CourseManAdmin')
  .controller('CourseInstanceFormCtrl', ['$scope', '$routeParams', 'courseInstanceApi', 'courseApi', 'codebookApi',
    function($scope, $routeParams, courseInstanceApi, courseApi, codebookApi) {
      $scope.debug = { visible: false };

      $scope.masterCourseInstance = {
        startTime: new Date(),
        maxNumberOfParticipants: 30
      };
      $scope.courseInstance = angular.copy($scope.masterCourseInstance);
      $scope.course = {};
      $scope.lecturers = [];

      init();
      function init() {

        if ($routeParams.id) {
          // Loading CourseDefinition
          courseInstanceApi.get($routeParams.id).then(function(courseInstance) {
            $scope.course = courseInstance.courseDefinition;
            $scope.masterCourseInstance = courseInstance;
            $scope.courseInstance = angular.copy($scope.masterCourseInstance);
          });
        }
        else {
          courseApi.get($routeParams.courseId).then(function(course) {
            $scope.course = course;
            $scope.masterCourseInstance.courseDefinition = { id: course.id };
            $scope.courseInstance = angular.copy($scope.masterCourseInstance);
          });
        }

        codebookApi.lecturerList().then(function(lecturers) {
          $scope.lecturers = lecturers;
        }, function() {
          $scope.errorMessage = 'Nisam predavače dohvatio.'
        });

      }

      $scope.save = function() {
        if ($scope.courseInstance.id) {
          $scope.update();
          return;
        }

        courseInstanceApi.save($scope.courseInstance).then(function(id) {
          $scope.courseInstance.id = id;
          $scope.masterCourseInstance = angular.copy($scope.courseInstance);
          $scope.form.$setPristine();
        });
      };

      $scope.update = function() {
        courseInstanceApi.update($scope.courseInstance).then(function() {
          $scope.masterCourseInstance = angular.copy($scope.courseInstance);
          $scope.form.$setPristine();
        });
      };

      $scope.delete = function() {
        courseInstanceApi.delete($scope.courseInstance.id).then(function() {
          $scope.masterCourseInstance = {};
          $scope.courseInstance = {};
        });
      };

      $scope.reset = function() {
        $scope.courseInstance = angular.copy($scope.masterCourseInstance);
        $scope.form.$setPristine();
      };

      $scope.openDate = function($event, datePicker) {
        $event.preventDefault();
        $event.stopPropagation();

        if (datePicker === 'startDate') {
          $scope.startDateOpen = true;
        }
        else if (datePicker == 'endDate') {
          $scope.endDateOpen = true;
        }
      };

      $scope.clearDate = function(dateField) {
        $scope.form.$setDirty();
        if (dateField === 'startTime') {
          $scope.courseInstance.startTime = null;
        }
        else if (dateField === 'endTime') {
          $scope.courseInstance.endTime = null;
        }
      }
    }])
  .controller('CourseInstanceDisplayCtrl', ['$scope', '$routeParams', '$location', '$modal', '$upload', 'restrictedUrl', 'courseInstanceApi', 'CourseUserService', 'DocumentService',
    function($scope, $routeParams, $location, $modal, $upload, restrictedUrl, courseInstanceApi, CourseUserService, DocumentService) {

      $scope.restrictedUrl = restrictedUrl;
      $scope.course = {};
      $scope.statusChangeable = false;
      $scope.courseInstance = {     // Ima i druge atribute
        acceptedUsers: [],
        declinedUsers: []
      };
      $scope.collapseManager = {
        preregistration: true,
        registration: true,
        accepted: true,
        declined: true
      };

      var courseUserScope = {};

      init();
      function init() {
        var courseInstanceId = $routeParams.id;
        courseInstanceApi.get(courseInstanceId).then(function(courseInstance) {
          $scope.courseInstance = courseInstance;
          $scope.course = courseInstance.courseDefinition;
          $scope.statusChangeable = $scope.courseInstance.status === 'UPCOMING';
        });

        DocumentService.fetchDocuments(courseInstanceId).then(function(documents) {
          $scope.documents = documents;
        });
      }

      $scope.delete = function() {
        courseInstanceApi.delete($scope.courseInstance.id).then(function() {
          $location.path('/course/display/' + $scope.course.id);
        });
      };

      $scope.markAsHeld = function (courseInstance) {
        courseInstanceApi.markAsHeld(courseInstance).then(function(newStatus) {
          $scope.courseInstance.status = newStatus;
          $scope.statusChangeable = false;
        });
      };

      $scope.cancelCourse = function (courseInstance) {
        courseInstanceApi.cancelCourse(courseInstance).then(function(newStatus) {
          $scope.courseInstance.status = newStatus;
          $scope.statusChangeable = false;
        });
      };

      // Participants tab
      function newCourseUserScope(params) {
        var courseUserScope = $scope.$new();
        courseUserScope.courseUser = {
          comment: '',
          user: params.courseUser.user.id,
          type: params.type,
          courseInstance: $scope.courseInstance.id
        };

        courseUserScope.user = params.courseUser.user;
        courseUserScope.titleMessage = params.titleMessage;
        courseUserScope.courseUserModal = {};
        courseUserScope.registrationCourseUser = params.courseUser;

        // Modal submit
        courseUserScope.submit = function(courseUser) {
          console.log(courseUser);

          if (courseUser.type === 'ACCEPTED') {
            CourseUserService.accept(courseUser).then(function(courseUser) {
              $scope.courseInstance.acceptedUsers.push(courseUser);
              courseUserScope.registrationCourseUser.active = false;
              courseUserScope.$destroy();
            });
          }
          else if (courseUser.type === 'DECLINED') {
            CourseUserService.decline(courseUser).then(function(courseUser) {
              $scope.courseInstance.declinedUsers.push(courseUser);
              courseUserScope.registrationCourseUser.active = false;
              courseUserScope.$destroy();
            });
          }

          courseUserScope.courseUserModal.dismiss();
        };

        return courseUserScope;
      }

      // TODO
      $scope.acceptUser = function(courseUser) {
        $scope.openModal(courseUser, 'ACCEPTED', 'accept');
      };

      $scope.declineUser = function(courseUser) {
        $scope.openModal(courseUser, 'DECLINED', 'decline');
      };

      $scope.openModal = function(courseUser, type, message) {
        courseUserScope = newCourseUserScope({ courseUser: courseUser, type: type, titleMessage: message });

        courseUserScope.courseUserModal = $modal.open({
          templateUrl: 'courseUserComment.html',
          scope: courseUserScope,
          size: 'sm'
        });
      };


      // Course materials tab
      $scope.documents = [];

      $scope.deleteDocument = function (idx) {
        var document = $scope.documents[idx];
        DocumentService.delete(document).then(function() {
          $scope.documents.splice(idx, 1);
        });
      };


      // Upload form
      $scope.upload = {};
      $scope.upload.progress = 0;
      $scope.upload.selectedFiles = [];
      $scope.upload.onFileSelect = function($files) {
        $scope.upload.selectedFiles = $files;
      };
      $scope.upload.start = function() {
        var files = $scope.upload.selectedFiles;
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          DocumentService.upload(file, $scope.courseInstance).then(function(document) {
            $scope.selectedFiles = [];
            $scope.documents.push(document);
          });

          DocumentService.currentUpload.progress(function(evt) {
            $scope.upload.progress = parseInt(100.0 * evt.loaded / evt.total);
          });
        }
      };

      $scope.$watch('upload.progress', function(newVal) {
        if (newVal == 100) {
          setInterval(function() {
            $scope.upload.progress = 0;
            $scope.$digest();
          }, 5000);
        }
      });

    }]);