'use strict';
angular.module('BlogApp')
    .controller('AdminCtrl', function ($scope, $http, $sce) {
        console.log('inside admin controller');
        $scope.html = {};

        $scope.article = {title: '', author: '', content: '', tags: [], preview: ''};
        $scope.preview = function() {
            console.log('From: (Sent) '+$scope.markdown);
            $http.post('/api/articles/transform', $scope.markdown).success(function (data) {
                console.log('To: (Received) ' + JSON.stringify(data));
                $scope.html.content = $sce.trustAsHtml(data.content);
                $scope.article.content = data.content;
            });

            $scope.article.title = $scope.title.replace(/\s/g,'-');
            $scope.article.author = $scope.author;
            $scope.article.tags = $scope.tags.split(' ');
            $scope.article.date = new Date();
            $scope.article.year = $scope.article.date.getFullYear();
            console.log($scope.article);

        };

        $scope.submit = function(){
        //Submit article
            $http.post('/api/articles/html/add', $scope.article).success(function(data){
                  //Todo

            });
        };

    });