'use strict';

angular.module('CourseMan', ['ngCookies'])
  .constant('baseUrl', '/CourseMan/api')
  .constant('adminUrl', '/CourseMan/api/admin')
  .constant('publicUrl', '/CourseMan/api/public')
  .constant('restrictedUrl', '/CourseMan/api/restricted')
  .value('Session', {
    user: {
      id: '',
      firstName: '',
      lastName: '',
      displayName: '',
      email: '',
      authorities: []
    }
  })
  .constant('AUTH_EVENTS', {
    loginSuccess: 'auth-login-success',
    loginFailed: 'auth-login-failed',
    logoutSuccess: 'auth-logout-success',
    authenticated: 'auth-success',
    sessionTimeout: 'auth-session-timeout',
    notAuthenticated: 'auth-not-authenticated',
    notAuthorized: 'auth-not-authorized'
  })
  .constant('USER_ROLES', {
    anonymous: 'ROLE_ANONYMOUS',
    user: 'ROLE_USER',
    admin: 'ROLE_ADMIN'
  });