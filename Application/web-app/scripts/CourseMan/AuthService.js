'use strict';

angular.module('CourseMan')
  .factory('AuthService', ['$rootScope', '$http', '$q', '$cookies', 'Session', 'baseUrl', 'publicUrl', 'AUTH_EVENTS',
    function($rootScope, $http, $q, $cookies, Session, baseUrl, publicUrl, AUTH_EVENTS) {
      var factory = {};

      factory.authenticate = function(user) {
        var deferred = $q.defer();

        $http({ method: 'POST', url: baseUrl + '/j_security_check',
          params: {
            ajax: true,
            j_username: user.username,
            j_password: user.password
          }
        }).success(function(data) {
          if (data.successful) {
            Session.user = data.user;
            $rootScope.$broadcast(AUTH_EVENTS.loginSuccess, data.user);
            $rootScope.$broadcast(AUTH_EVENTS.authenticated);
            deferred.resolve();
          }
          else {
            deferred.reject(data.error);
          }
        }).error(function() {
          deferred.reject();
        });

        return deferred.promise;
      };

      factory.logout = function() {
        $http({ method: 'POST', url: baseUrl + '/security/logout' });
        $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
      };

      factory.isAuthenticated = function() {
        return ($cookies.CourseMan ? true : false);
      };

      factory.getUserId = function() {
        return $cookies.CourseMan;
      };

      factory.getCurrentUser = function() {
        return Session.user;
      };

      factory.loadCurrentUser = function() {
        if (factory.isAuthenticated()) {
          var userPromise = $q.defer();
          $http({ method: 'GET', url: publicUrl + '/user', params: {id: factory.getUserId()}
          }).success(function(user) {
            Session.user = user;
            $rootScope.$broadcast(AUTH_EVENTS.authenticated);
            userPromise.resolve(user);
          }).error(function() {
            userPromise.reject();
          });
          return userPromise.promise;
        }

        $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
      };

      factory.isAuthorized = function(authorizedRole) {
        // TODO modify so that it works if array of roles is given
//        if (!angular.isArray(authorizedRoles)) {
//          authorizedRoles = [authorizedRoles];
//        }
        return (factory.isAuthenticated() && Session.user.authorities.indexOf(authorizedRole) !== -1);
      };

      return factory;
    }]);
