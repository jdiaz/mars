/**
 * Created by zeek on 08-27-15.
 */
'use strict';

angular.module('BlogApp')
    .controller('MainCtrl', function ($scope, $sce, $http) {
        console.log('inside controller');

        $http.get('/api/articles/recent').success(function (data) {
               $scope.articles = data;
               $scope.articles.forEach(function(curr, index, arr){
                   console.log('iterating through: ' +JSON.stringify(curr));
                   $scope.articles[index].content = $sce.trustAsHtml(curr.content);
                   $scope.articles[index].dispTitle = $scope.articles[index].title.replace(/-/g,' ');
                   $scope.articles[index].preview = $sce.trustAsHtml($scope.articles[index].preview);
               });
           });

        $scope.article = function (id) {
            $http.get('/api/posts/'+id);
        };

    });