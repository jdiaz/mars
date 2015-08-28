/**
 * Created by zeek on 08-27-15.
 */
'use strict';

angular.module('BlogApp')
    .controller('ArticleCtrl', function ($scope, $http, $routeParams, $sce) {
        console.log('inside article controller');

        var id = $routeParams.id;
        $http.get('/articles/title/'+id).success(function(data){
            $scope.article = data;

            console.log('retrieved this data: %j',data.articles_found);
        });

});