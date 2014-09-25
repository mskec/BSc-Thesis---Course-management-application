'use strict';


// Declare app level module which depends on filters, and services
var myApp = angular.module('CourseManAdmin', [
  'ngRoute',
  'ngCookies',
  'ui.bootstrap',
  'angularFileUpload',
  'CourseMan',
  'CourseManAdmin.directives'
]);

myApp.config(['$routeProvider', 'USER_ROLES', function($routeProvider, USER_ROLES) {
  $routeProvider
    .when('/', {
      templateUrl: 'partials/main.html',
      controller: 'MainCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .when('/course', {
      templateUrl: 'partials/course/list.html',
      controller: 'CourseListCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .when('/course/display/:id', {
      templateUrl: 'partials/course/display.html',
      controller: 'CourseDisplayCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .when('/course/form/:id?', {
      templateUrl: 'partials/course/form.html',
      controller: 'CourseFormCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .when('/courseInstance/form/:courseId/:id?', {
      templateUrl: 'partials/courseInstance/form.html',
      controller: 'CourseInstanceFormCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .when('/courseInstance/display/:courseId/:id', {
      templateUrl: 'partials/courseInstance/display.html',
      controller: 'CourseInstanceDisplayCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .when('/document/upload/:courseInstanceId', {
      templateUrl: 'partials/documentUpload.html',
      controller: 'DocumentUploadCtrl',
      authorizedRole: USER_ROLES.admin
    })
    .otherwise({ redirectTo: '/' });
}]).config(['datepickerConfig', 'datepickerPopupConfig', 'timepickerConfig',
    function(datepickerConfig, datepickerPopupConfig, timepickerConfig) {
      datepickerConfig.yearRange = 5;
      datepickerConfig.initDate = new Date();
      datepickerConfig.showWeeks = false;
      datepickerConfig.startingDay = 1;

      datepickerPopupConfig.showButtonBar = false;
      datepickerPopupConfig.dateFormat = 'dd.MM.yyyy';

      timepickerConfig.showMeridian = false;
}]).config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push(['$injector', function($injector) {
      return $injector.get('AuthInterceptor');
    }]);
}]);


myApp.run(['$rootScope', '$location', 'AUTH_EVENTS', 'AuthService', function($rootScope, $location, AUTH_EVENTS, AuthService) {
  $rootScope.message = {
    text: '',
    visible: false,
    style: ''
  };

  $rootScope.$watch('message', function(newVal) {
    if (newVal.visible) {
      setInterval(function() {
        $rootScope.message.visible = false;
        $rootScope.$digest();
      }, 10000);
    }
  });

  $rootScope.$on("$routeChangeStart", function (event, next){
    //$rootScope.message.visible = false;

    var authorizedRole = next.authorizedRole;
    if (authorizedRole && !AuthService.isAuthorized(authorizedRole)) {
      event.preventDefault();
      if (AuthService.isAuthenticated()) {
        $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
      }
      else {
        $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
      }
    }
  });

  $rootScope.$on(AUTH_EVENTS.notAuthenticated, function() {
    $location.path('/');
  });

  // TODO slušaj na AUTH_EVENTS i odradi potrebne akcije

}]);
