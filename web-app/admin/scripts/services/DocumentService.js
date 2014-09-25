'use strict';

angular.module('CourseManAdmin')
  .factory('DocumentService', ['$http', '$q', '$rootScope', '$upload', 'adminUrl', 'restrictedUrl',
    function ($http, $q, $rootScope, $upload, adminUrl, restrictedUrl) {
    var factory = {};
    var documentUrl = adminUrl + '/document';
    var restrictedDocumentUrl = restrictedUrl + '/document';

    factory.upload = function (file, courseInstance) {
      var defered = $q.defer();

      factory.currentUpload = $upload.upload({ url: documentUrl + '/upload', method: 'POST',
        file: file, fileFormDataName: 'data',
        data: {
          courseInstance: courseInstance.id,
          fileName: file.name
        }
      }).success(function(response) {
        $rootScope.message = {
          text: response.message,
          style: 'alert-success',
          visible: true
        };
        factory.currentUpload = {};
        defered.resolve(response.document);
      }).error(function(response) {
        $rootScope.message = {
          text: response.error,
          style: 'alert-danger',
          visible: true
        };
        factory.currentUpload = {};
        defered.reject();
      });

      return defered.promise;
    };

    factory.delete = function (file) {
      var defered = $q.defer();

      $http({ method: 'DELETE', url: documentUrl, params: {id: file.id}
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

    factory.fetchDocuments = function (courseInstanceId) {
      var defered = $q.defer();

      $http({ method: 'GET', url: restrictedDocumentUrl + '/list', params: {id: courseInstanceId}
      }).success(function (documents) {
        defered.resolve(documents);
      }).error(function () {
        console.log('| DocumentService - #fetchDocuments - error while fetching documents.');
        defered.reject();
      });

      return defered.promise;
    };

    return factory;
  }]);
