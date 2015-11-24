'use strict';

angular.module('BlogApp', ['ngRoute'])
    .config(function ($routeProvider, $locationProvider) {

        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                templateUrl: '/partials/main.html',
                controller: 'MainCtrl'
            })
            .when('/articles/loading', {
                templateUrl: '/partials/loading.html',
                controller: 'LoadingCtrl'
            })
            .when('/articles', {
                templateUrl: '/partials/main.html',
                controller: 'MainCtrl'
            })
            .when('/articles/admin', {
                templateUrl: '/partials/admin.html',
                controller: 'AdminCtrl'
            })
            .when('/articles/:year/:title', {
                templateUrl: '/partials/article.html',
                controller: 'ArticleCtrl'
            })
            .when('/articles/login', {
                templateUrl: '/partials/login.html',
                controller: 'LoginCtrl'
            })
            .when('/articles/logout', {
                templateUrl: '/partials/logout.html',
                controller: 'LogoutCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });

    })
    .run(function(Authentication, Application, $rootScope, $location, RouteFilter){
        //
        //Authentication.requestUser().then(function(){
        //  Application.makeReady();
        //});
        Application.makeReady();

        $rootScope.$on('$locationChangeStart', function(scope, next, current){

            if($location.path() === '/articles/loading') return;

            if(! Application.isReady()){
                $location.path('/articles/loading');
            }

            RouteFilter.run($location.path());
        });

    }); //end of run



