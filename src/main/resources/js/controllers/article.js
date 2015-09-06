/**
 * Created by zeek on 08-27-15.
 */
'use strict';

angular.module('BlogApp')
    .controller('ArticleCtrl', function ($scope, $http, $routeParams, $sce) {
        console.log('inside article controller');

        var title = $routeParams.title;
        $http.get('/api/articles/title/'+title).success(function(data){
            console.log('Received: '+JSON.stringify(data));
            $scope.article = data[0];

            $scope.article.dispTitle = data[0].title.replace(/-/g,' ');

            $scope.article.content = $sce.trustAsHtml(data[0].content);

            console.log('retrieved this data: %j', $scope.article);
        });

    });