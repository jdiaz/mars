'use strict';

angular.module('BlogApp', ['ngRoute'])
    .config(function ($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                templateUrl: '/partials/main.html',
                controller: 'MainCtrl'
            }).
            when('/articles', {
                templateUrl: '/partials/main.html',
                controller: 'MainCtrl'
            })
            .when('/articles/new', {
                templateUrl: '/partials/new_article.html'
            })
            .when('/articles/:year/:id', {
                templateUrl: '/partials/article.html',
                controller: 'ArticleCtrl'
            })
            //.when('/login', {
            //    templateUrl: '/partials/login.html'//,
            //    // controller: 'LoginCtrl'
            //})
//      .when('/user', {
//            templateUrl: '/partials/user.html'
//       })
            .otherwise({
                redirectTo: '/'
            });


    });



