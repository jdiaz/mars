/**
 * Created by zeek on 08-27-15.
 */
'use strict';

angular.module('Blogapp')
    .controller('ArticleCtrl', function ($scope, $http, $routeParams, $sce) {
        console.log('inside article controller');

        var title = $routeParams.title;
        var year = $routeParams.year;
        $http.get('/articles/'+year+"/"+title).success(function(data){
            $scope.article = data;

            console.log('retrieved this data: %j',data);
        });

});