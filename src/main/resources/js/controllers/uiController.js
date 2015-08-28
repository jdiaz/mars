app.controller('UIController', ['$scope', '$http', '$sce', function($scope, $http, $sce){
   console.log("Using UI Controller");

       $http.get('/article/all')
           .success(function (data) {
               $scope.articles = data;
               $scope.articles.forEach(function(curr, index, arr){
                   //console.log('iterating through: ' +JSON.stringify(curr));
                   $scope.articles[index].content = $sce.trustAsHtml(curr.content);
               });
               //console.log('Articles recieved: ' + JSON.stringify(data));
           });

}]);