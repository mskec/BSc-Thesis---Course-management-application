'use strict';

var myApp = angular.module('CourseManPublic', [
  'ngRoute',
  'ngCookies',
  'ui.bootstrap',
  'CourseMan',
  'CourseManPublic.directives'
]);

myApp.config(['$routeProvider', function($routeProvider) {
  $routeProvider
    .when('/', {
      templateUrl: 'partials/main.html',
      controller: 'MainCtrl'
    })
    .when('/registration', {
      templateUrl: 'partials/registration.html',
      controller: 'RegistrationCtrl'
    })
    .when('/search', {
      templateUrl: 'partials/search.html',
      controller: 'SearchCtrl'
    })
    .when('/course/list', {
      templateUrl: 'partials/course/list.html',
      controller: 'CourseListCtrl'
    })
    .when('/course/display/:id', {
      templateUrl: 'partials/course/display.html',
      controller: 'CourseDisplayCtrl'
    })
    .when('/course/instance/:id', {
      templateUrl: 'partials/course/instance.html',
      controller: 'CourseInstanceCtrl'
    })
    .when('/profile', {
      templateUrl: 'partials/profile.html',
      controller: 'ProfileCtrl',
      authentication: {
        required: true
      }
    })
    .when('/notifications', {
      templateUrl: 'partials/notifications.html',
      controller: 'NotificationCtrl',
      authentication: {
        required: true
      }
    })
    .otherwise({ redirectTo: '/'});
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
    var authentication = next.authentication;

    if (authentication && authentication.required) {
      var authorizedRole = authentication.authorizedRole;
      if (!AuthService.isAuthenticated() || (authorizedRole && !AuthService.isAuthorized(authorizedRole))) {
        event.preventDefault();
        if (AuthService.isAuthenticated()) {
          $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
        }
        else {
          $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
        }
      }
    }

  });

  $rootScope.$on(AUTH_EVENTS.notAuthenticated, function() {
    $location.path('/');
  });

  $rootScope.$on(AUTH_EVENTS.logoutSuccess, function() {
    $location.path('/');
  });

  // TODO slušaj na AUTH_EVENTS i odradi potrebne akcije

}]);
