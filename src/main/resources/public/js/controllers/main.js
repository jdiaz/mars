/**
 * Created by zeek on 08-27-15.
 */
'use strict';

angular.module('BlogApp')
    .controller('MainCtrl', function ($scope, $sce, $http, Authentication, RouteFilter) {
        console.log('inside controller');

        $scope.canAccess = function(route){
            return RouteFilter.canAccess(route);
        }

        console.log(Authentication.getUser());

        $http.get('/api/articles/recent').success(function (data) {
               $scope.articles = data;
               $scope.articles.forEach(function(curr, index, arr){
                   console.log('iterating through: ' +JSON.stringify(curr));
                   $scope.articles[index].htmlContent = $sce.trustAsHtml(curr.htmlContent);
                   $scope.articles[index].dispTitle = $scope.articles[index].title.replace(/-/g,' ');
                   $scope.articles[index].preview = $sce.trustAsHtml($scope.articles[index].preview);
               });
           });
    });