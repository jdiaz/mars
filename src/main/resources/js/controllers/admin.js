'use strict';
angular.module('BlogApp')
    .controller('AdminCtrl', function ($scope, $http, $sce) {
        console.log('inside admin controller');
        $scope.html = {};
        $scope.showValues = function(){
            console.log('newValue='+$scope.markdown);
        };


        $scope.transform = function() {
            console.log('From: (Sent) '+$scope.markdown);
            $http.post('/api/articles/transform', $scope.markdown).success(function (data) {
                console.log('To: (Received) ' + JSON.stringify(data));
                $scope.html.content = $sce.trustAsHtml(data.content);
            });
        };

    });