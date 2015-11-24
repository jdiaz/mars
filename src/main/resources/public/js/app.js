'use strict';

angular.module('BlogApp', ['ngRoute'])
    .config(function ($routeProvider, $locationProvider) {
        var context = 'blog';
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/'+context, {
                templateUrl: '/partials/main.html',
                controller: 'MainCtrl'
            })
            .when('/'+context+'/articles/loading', {
                templateUrl: '/partials/loading.html',
                controller: 'LoadingCtrl'
            })
            .when('/'+context+'/articles', {
                templateUrl: '/partials/main.html',
                controller: 'MainCtrl'
            })
            .when('/'+context+'/articles/admin', {
                templateUrl: '/partials/admin.html',
                controller: 'AdminCtrl'
            })
            .when('/'+context+'/articles/:year/:title', {
                templateUrl: '/partials/article.html',
                controller: 'ArticleCtrl'
            })
            .when('/'+context+'/articles/login', {
                templateUrl: '/partials/login.html',
                controller: 'LoginCtrl'
            })
            .when('/'+context+'/articles/logout', {
                templateUrl: '/partials/logout.html',
                controller: 'LogoutCtrl'
            })
            .otherwise({
                redirectTo: '/'+context
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



