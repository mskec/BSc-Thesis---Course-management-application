'use strict';

angular.module('CourseManPublic')
  .factory('CourseService', ['$rootScope', '$http', '$q', 'publicUrl', 'restrictedUrl', function($rootScope, $http, $q, publicUrl, restrictedUrl) {
    var factory = {};
    var publicCourseUrl = publicUrl + '/course';
    var restrictedCourseUrl = restrictedUrl + '/course';

    factory.fetchUpcoming = function(maxUpcoming) {
      var defered = $q.defer();

      $http({ method: 'GET', url: publicCourseUrl + '/fetchUpcoming', params: {max: maxUpcoming}
        }).success(function(courseInstances) {
          defered.resolve(courseInstances);
        }).error(function() {
          console.log('| CourseService - #fetchUpcoming - unable to fetch upcoming courses.');
          defered.reject();
        });

      return defered.promise;
    };

    factory.fetchCourse = function(id) {
      var defered = $q.defer();

      $http({ method: 'GET', url: publicCourseUrl, params: {id: id}
      }).success(function(course) {
        defered.resolve(course);
      }).error(function() {
        console.log('| CourseService - #fetchCourse - unable to fetch course with id: ' + id + '.');
        defered.reject();
      });

      return defered.promise;
    };

    factory.fetchAll = function() {
        var defered = $q.defer();

        $http({ method: 'GET', url: publicCourseUrl + '/list'
        }).success(function(courses) {
            defered.resolve(courses);
        }).error(function() {
            console.log('| CourseService - #fetchAll - unable to fetch all courses.');
            defered.reject();
        });

        return defered.promise;
    };

    factory.fetchUserUpcoming = function() {
        var defered = $q.defer();

        $http({ method: 'GET', url: restrictedCourseUrl + '/userUpcoming'
        }).success(function(courses) {
            defered.resolve(courses);
        }).error(function() {
            console.log('| CourseService - #fetchUserUpcoming - unable to fetch upcoming for current user.');
            defered.reject();
        });

        return defered.promise;
    };

    factory.fetchUserRegistrations = function() {
        var defered = $q.defer();

        $http({ method: 'GET', url: restrictedCourseUrl + '/userRegistrations'
        }).success(function(courses) {
            defered.resolve(courses);
        }).error(function() {
            console.log('| CourseService - #fetchUserRegistrations - unable to fetch course registrations for current user.');
            defered.reject();
        });

        return defered.promise;
    };

    factory.courseSearch = function (query) {
      var defered = $q.defer();

      $http({ method: 'GET', url: publicCourseUrl + '/search', params: {query: query}
      }).success(function (courses) {
        defered.resolve(courses);
      }).error(function (response) {
        defered.reject();
      });

      return defered.promise;
    };

    factory.fetchInstance = function (courseInstanceId) {
      var defered = $q.defer();

      $http({ method: 'GET', url: publicCourseUrl + '/instance', params: {id: courseInstanceId}
      }).success(function (courseInstance) {
        defered.resolve(courseInstance);
      }).error(function (response) {
        $rootScope.message = {
          text: response.error,
          style: 'alert-danger',
          visible: true
        };
        defered.reject();
      });

      return defered.promise;
    };

    factory.submitGrade = function (courseGrade) {
      var defered = $q.defer();

      $http({ method: 'POST', url: restrictedCourseUrl + '/grade', data: courseGrade
      }).success(function (response) {
        $rootScope.message = {
          text: response.message,
          style: 'alert-success',
          visible: true
        };
        defered.resolve();
      }).error(function (response) {
        $rootScope.message = {
          text: response.error,
          style: 'alert-danger',
          visible: true
        };
        defered.reject();
      });

      return defered.promise;
    };

    // TODO refactor
    factory.getGrade = function (courseInstance) {
      var defered = $q.defer();

      $http({ method: 'GET', url: restrictedCourseUrl + '/getGrade', params: {id: courseInstance.id}
      }).success(function (response) {
        defered.resolve(response);
      }).error(function () {
        defered.reject();
      });

      return defered.promise;
    };

    return factory;
  }]);
