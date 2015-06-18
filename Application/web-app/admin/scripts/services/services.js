'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('CourseManAdmin')
  .value('version', '1.0')
  .factory('courseApi', ['$http', '$q', '$rootScope', 'adminUrl', function($http, $q, $rootScope, adminUrl) {
    var factory = {};

    factory.list = function() {
      var deferred = $q.defer();

      $http({method: 'GET', url: adminUrl + '/course/list'
        }).success(function(data, status) {
          deferred.resolve(data);
        }).error(function(data, status) {
          deferred.reject();
          console.log('Error loading course definition list. Status: ' + status);
        });

      return deferred.promise;
    };

    factory.get = function(id) {
      var deferred = $q.defer();

      $http({ method: 'GET', url: adminUrl + '/course', params: {id: id}
        }).success(function(data, status) {
          deferred.resolve(data);
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error loading CourseDefinition. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.save = function(course) {
      var deferred = $q.defer();

      $http({ method: 'POST', url: adminUrl + '/course', data: course
        }).success(function(data, status) {
          $rootScope.message = {
            text: data.message,
            style: 'alert-success',
            visible: true
          };
          deferred.resolve(data.id);
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error saving CourseDefinition. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.update = function(course) {
      var deferred = $q.defer();

      $http({ method: 'PUT', url: adminUrl + '/course', data: course
        }).success(function(data, status) {
          $rootScope.message = {
            text: data.message,
            style: 'alert-success',
            visible: true
          };
          deferred.resolve();
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error updating CourseDefinition. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.delete = function(id) {
      console.log('Deleting course with ID: ' + id);
      var deferred = $q.defer();

      $http({ method: 'DELETE', url: adminUrl + '/course', params: {id: id}
        }).success(function(data, status) {
          $rootScope.message = {
            text: data.message,
            style: 'alert-success',
            visible: true
          };
          deferred.resolve();
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error while deleting CourseDefinition. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    return factory;
  }])
  .factory('courseInstanceApi', ['$http', '$q', '$rootScope', 'adminUrl', function($http, $q, $rootScope, adminUrl) {
    var factory = {};
    var courseInstanceUrl = adminUrl + '/courseInstance';

    factory.get = function(id) {
      var deferred = $q.defer();

      $http({ method: 'GET', url: courseInstanceUrl, params: {id: id}
        }).success(function(data, status) {
          deferred.resolve(data);
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error loading course instance. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.save = function(courseInstance) {
      var deferred = $q.defer();

      $http({ method: 'POST', url: courseInstanceUrl, data: courseInstance
        }).success(function(data, status) {
          $rootScope.message = {
            text: data.message,
            style: 'alert-success',
            visible: true
          };
          deferred.resolve(data.id);
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error saving course instance. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.update = function(courseInstance) {
      var deferred = $q.defer();

      $http({ method: 'PUT', url: courseInstanceUrl, data: courseInstance
        }).success(function(data, status) {
          $rootScope.message = {
            text: data.message,
            style: 'alert-success',
            visible: true
          };
          deferred.resolve();
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error updating course instance. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.delete = function(id) {
      console.log('Deleting course instance with ID: ' + id);
      var deferred = $q.defer();

      $http({ method: 'DELETE', url: courseInstanceUrl, params: {id: id}
        }).success(function(data, status) {
          $rootScope.message = {
            text: data.message,
            style: 'alert-success',
            visible: true
          };
          deferred.resolve();
        }).error(function(data, status) {
          $rootScope.message = {
            text: data.error,
            style: 'alert-danger',
            visible: true
          };
          deferred.reject();
          console.log('Error while deleting course instance. Status: ' + status + ', error: ' + data.error);
        });

      return deferred.promise;
    };

    factory.cancelCourse = function(courseInstance) {
      var defered = $q.defer();

      $http({ method: 'POST', url: courseInstanceUrl + '/cancel', data: {id: courseInstance.id}
        }).success(function(response) {
          $rootScope.message = {
            text: response.message,
            style: 'alert-success',
            visible: true
          };
          defered.resolve(response.status);
        }).error(function(response) {
          $rootScope.message = {
            text: response.error,
            style: 'alert-danger',
            visible: true
          };
          defered.reject();
        });

      return defered.promise;
    };

    factory.markAsHeld = function (courseInstance) {
      var defered = $q.defer();

      $http({ method: 'POST', url: courseInstanceUrl + '/held', data: {id: courseInstance.id}
      }).success(function (response) {
        $rootScope.message = {
          text: response.message,
          style: 'alert-success',
          visible: true
        };
        defered.resolve(response.status);
      }).error(function(response) {
        $rootScope.message = {
          text: response.error,
          style: 'alert-danger',
          visible: true
        };
        defered.reject();
      });

      return defered.promise;
    };

    return factory;
  }])
  .factory('codebookApi', ['$http', '$q', '$rootScope', 'adminUrl', function($http, $q, $rootScope, adminUrl) {
    var factory = {};

    factory.categoryList = function() {
      var deferred = $q.defer();

      $http({ method: 'GET', url: adminUrl + '/category/list'
        }).success(function(data, status) {
          deferred.resolve(data);
        }).error(function(data, status) {
          deferred.reject();
          console.log('Error loading category list. Status: ' + status);
        });

      return deferred.promise;
    };

    factory.lecturerList = function() {
      var deferred = $q.defer();

      $http({ method: 'GET', url: adminUrl + '/user/lecturers'
      }).success(function(data, status) {
        deferred.resolve(data);
      }).error(function(data, status) {
        deferred.reject();
        console.log('Error loading lecturer list. Status: ' + status);
      });

      return deferred.promise;
    }

    return factory;
  }]);
