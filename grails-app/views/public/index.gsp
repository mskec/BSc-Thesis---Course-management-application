<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en" ng-app="CourseManPublic">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimal-ui">
    <title>CourseMan</title>

    <link rel="stylesheet" href="${resource(dir: 'lib/bootstrap/css', file: 'bootstrap-cerulean.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'lib/font_awesome/css', file: 'font-awesome.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'public/css', file: 'app.css')}"/>

</head>
<body ng-controller="ApplicationCtrl">

    <nav class="navbar navbar-default" role="navigation" ng-controller="HeaderCtrl">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#"><span class="fa fa-graduation-cap"></span> CourseMan</a>

                <div ng-if="isAuthenticated()" class="navbar-icon-group visible-xs">
                    <a href="#/profile" class="btn navbar-btn navbar-icon"><span class="glyphicon glyphicon-user"></span></a>
                    <a href="#/notifications" class="btn navbar-btn navbar-icon">
                        <span class="glyphicon glyphicon-globe"></span>
                        <span class="badge" ng-show="notificationCount > 0" id="notification-count">{{ notificationCount }}</span>
                    </a>
                </div>
            </div><!-- nav header end-->

            <div class="collapse navbar-collapse" id="main-navbar-collapse">
                <ul class="nav navbar-nav">
                    <li><a href="#/course/list" ng-click="hideNav()"><span class="glyphicon glyphicon-list"></span> All courses</a></li>
                </ul><!-- Main nav end-->

                <div class="col-sm-3 nopadding">
                    <form class="navbar-form navbar-left" role="search" ng-submit="search.submit(search.query)" novalidate>
                        <div class="input-group">
                            <input type="text" ng-model="search.query" class="form-control" placeholder="Search" ng-minlength="3" required
                                tooltip="Enter at least 3 characters."  tooltip-trigger="focus" tooltip-placement="bottom">
                            <div class="input-group-btn">
                                <button type="submit" class="btn btn-default" ng-click="hideNav()"><span class="glyphicon glyphicon-search"></span> Search</button>
                            </div>
                        </div>
                    </form><!-- Search form end-->
                </div>

                <div ng-switch="isAuthenticated()">
                    <form ng-switch-when="false" class="navbar-form navbar-right" role="login" ng-submit="login(credentials)">
                        <div class="form-group">
                            <label for="credentials.username" class="sr-only">Username</label>
                            <input type="text" ng-model="credentials.username" class="form-control" id="credentials.username" placeholder="Username">
                        </div>
                        <div class="form-group">
                            <label for="credentials.password" class="sr-only">Username</label>
                            <input type="password" ng-model="credentials.password" class="form-control" id="credentials.password" placeholder="Password">
                        </div>
                        <button type="submit" class="btn btn-default">Login</button>
                        <a class="btn btn-default" href="#/registration" ng-click="hideNav()">Registration</a>
                    </form><!-- Login form end-->

                    <ul ng-switch-when="true" class="nav navbar-nav navbar-right">
                        <li><a href="#/profile" ng-click="hideNav()"><span class="glyphicon glyphicon-user"></span> My profile</a></li>
                        <li>
                            <a href="#/notifications" ng-click="hideNav()">
                                <span class="glyphicon glyphicon-globe"></span> Notifications <span class="badge" ng-show="notificationCount > 0">{{ notificationCount }}</span>
                            </a>
                        </li>
                        <li><h4 class="navbar-text">Welcome, {{ currentUser.firstName }}</h4></li>
                        <li style="margin-right: 5px; margin-left: 15px">
                            <button class="btn btn-default navbar-btn" ng-click="logout(); hideNav()"><i class="glyphicon glyphicon-log-out"></i> Logout</button>
                        </li>
                    </ul><!-- Logout end-->
                </div>
            </div><!-- nav body end-->
        </div>
    </nav><!-- nav end-->

    <div class="container" id="main-content">

        <div class="alert alert-dismissable" ng-class="message.style" ng-show="message.visible">
            <button type="button" class="close" ng-click="message.visible = false" aria-hidden="true">&times;</button>
            {{ message.text }}
        </div><!-- div alert -->

        <div ng-view=""></div>

    </div>

    <nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
        <div class="container">
            <p class="navbar-text">CourseMan public app: v<span app-version></span></p>
        </div>
    </nav><!-- footer end -->

    <!-- In production use:
          <script src="//ajax.googleapis.com/ajax/libs/angularjs/x.x.x/angular.min.js"></script>
          -->
    %{--<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>--}%

    <script src="${resource(dir: 'lib/jquery', file: 'jquery-2.1.1.min.js')}"></script>
    <script src="${resource(dir: 'lib/bootstrap/js', file: 'bootstrap.min.js')}"></script>
    <script src="${resource(dir: 'lib/angular', file: 'angular.js')}"></script>
    <script src="${resource(dir: 'lib/angular', file: 'angular-route.js')}"></script>
    <script src="${resource(dir: 'lib/angular', file: 'angular-cookies.js')}"></script>
    <script src="${resource(dir: 'lib/ui.bootstrap', file: 'ui-bootstrap-tpls-0.9.0.min.js')}"></script>

    <script src="${resource(dir: 'scripts/CourseMan', file: 'CourseMan.js')}"></script>
    <script src="${resource(dir: 'scripts/CourseMan', file: 'AuthService.js')}"></script>
    <script src="${resource(dir: 'scripts/CourseMan', file: 'AuthInterceptor.js')}"></script>
    <script src="${resource(dir: 'scripts/CourseMan', file: 'Filters.js')}"></script>

    <script src="${resource(dir: 'public/scripts', file: 'app.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'ApplicationCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'CourseCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'HeaderCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'MainCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'NotificationCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'ProfileCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'RegistrationCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts/controllers', file: 'SearchCtrl.js')}"></script>
    <script src="${resource(dir: 'public/scripts', file: 'directives.js')}"></script>
    <script src="${resource(dir: 'public/scripts/services', file: 'CourseService.js')}"></script>
    <script src="${resource(dir: 'public/scripts/services', file: 'CourseUserService.js')}"></script>
    <script src="${resource(dir: 'public/scripts/services', file: 'services.js')}"></script>

</body>
</html>
