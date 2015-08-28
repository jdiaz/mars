/**
 * Created by zeek on 08-27-15.
 */
app.controller('MainCtrl', ['$scope','$http', '$sce', function ($scope,  $http, $sce) {
        console.log('inside controller');

        $http.get('/article/recent')
            .success(function (data) {
                $scope.articles = data;
                $scope.articles.forEach(function(curr, index, arr){
                    //console.log('iterating through: ' +JSON.stringify(curr));
                    $scope.articles[index].content = $sce.trustAsHtml(curr.content);
                });
        });

}]);