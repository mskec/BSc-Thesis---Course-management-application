<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en" ng-app="CourseManAdmin">
<head>
    <meta charset="utf-8">
    <title>CourseMan</title>
    %{--<link rel="stylesheet" href="${resource(dir: 'lib/bootstrap/css', file: 'bootstrap.css')}"/>--}%
    %{--<link rel="stylesheet" href="${resource(dir: 'lib/bootstrap/css', file: 'bootstrap-spacelab.min.css')}"/>--}%
    <link rel="stylesheet" href="${resource(dir: 'lib/bootstrap/css', file: 'bootstrap-flatly.min.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'lib/font_awesome/css', file: 'font-awesome.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'admin/css', file: 'app.css')}"/>
</head>
<body ng-controller="ApplicationCtrl">
    <div ng-if="!isAuthorized(userRoles.admin)" ng-controller="LoginCtrl">
        <div class="container">
            <div class="col-md-4 center-block well clearfix" id="login-form">
                <!-- Start Error box -->
                <div class="alert alert-danger" ng-show="errorMessage">
                    <button type="button" class="close" ng-click="errorMessage = false"> &times;</button>
                    <h4>Error!</h4>
                    {{ errorMessage }}
                </div> <!-- End Error box -->

                <form name="form" role="form" ng-submit="login(credentials)">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" ng-model="credentials.username" class="form-control" id="username" name="username" placeholder="Username">
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" ng-model="credentials.password" class="form-control" id="password" name="password" placeholder="Password">
                    </div>

                    <button type="submit" class="btn btn-primary pull-right">Login</button>
                </form>

            </div>
        </div>
    </div><!-- Login div -->

    <div ng-if="isAuthorized(userRoles.admin)" ng-controller="MainCtrl">
        <nav class="navbar navbar-default" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#"><span class="fa fa-graduation-cap"></span> CourseMan</a>
                </div><!-- nav header end-->

                <div class="collapse navbar-collapse" id="main-navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="#/course"><span class="fa fa-list"></span> Show all courses</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <h4 class="navbar-text">Welcome, {{ currentUser.firstName }}</h4>
                        </li>
                        <li style="margin-right: 5px;">
                            <button class="btn btn-primary navbar-btn" ng-click="logout()"><i class="glyphicon glyphicon-log-out"></i> Logout</button>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container" id="main-content">
            <div class="alert alert-dismissable" ng-class="message.style" ng-show="message.visible">
                <button type="button" class="close" ng-click="message.visible = false" aria-hidden="true">&times;</button>
                {{ message.text }}
            </div><!-- div alert -->

            <div ng-view></div>

        </div>

    </div><!-- div wrap -->

    <nav class="navbar navbar-default navbar-fixed-bottom" role="navigation" style="margin-top: 40px">
        <div class="container">
            <p class="navbar-text">CourseMan admin app: v<span app-version></span></p>
        </div>
    </nav>

    <!-- In production use:
      <script src="//ajax.googleapis.com/ajax/libs/angularjs/x.x.x/angular.min.js"></script>
      -->
    %{--<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>--}%

    <script src="${resource(dir: 'lib/jquery', file: 'jquery-2.1.1.min.js')}"></script>
    <script src="${resource(dir: 'lib/bootstrap/js', file: 'bootstrap.min.js')}"></script>
    <script src="${resource(dir: 'lib/angular-file-upload', file: 'angular-file-upload-shim.min.js')}"></script>
    <script src="${resource(dir: 'lib/angular', file: 'angular.js')}"></script>
    <script src="${resource(dir: 'lib/angular-file-upload', file: 'angular-file-upload.min.js')}"></script>
    <script src="${resource(dir: 'lib/angular', file: 'angular-route.js')}"></script>
    <script src="${resource(dir: 'lib/angular', file: 'angular-cookies.js')}"></script>
    <script src="${resource(dir: 'lib/ui.bootstrap', file: 'ui-bootstrap-tpls-0.9.0.min.js')}"></script>

    <script src="${resource(dir: 'scripts/CourseMan', file: 'CourseMan.js')}"></script>
    <script src="${resource(dir: 'scripts/CourseMan', file: 'Filters.js')}"></script>
    <script src="${resource(dir: 'scripts/CourseMan', file: 'AuthService.js')}"></script>
    <script src="${resource(dir: 'scripts/CourseMan', file: 'AuthInterceptor.js')}"></script>

    <script src="${resource(dir: 'admin/scripts', file: 'app.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/controllers', file: 'ApplicationCtrl.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/controllers', file: 'CourseCtrl.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/controllers', file: 'CourseInstanceCtrl.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/controllers', file: 'LoginCtrl.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/controllers', file: 'MainCtrl.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/services', file: 'CourseUserService.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/services', file: 'DocumentService.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/services', file: 'NotificationService.js')}"></script>
    <script src="${resource(dir: 'admin/scripts/services', file: 'services.js')}"></script>
    <script src="${resource(dir: 'admin/scripts', file: 'directives.js')}"></script>

</body>
</html>
