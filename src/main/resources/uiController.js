app.controller('UIController', ['$scope', '$http', function($scope, $http, $sce){
   console.log("Using UI Controller");
   $scope.find = function() {

       $http.get('/article/all')
           .success(function (data) {
               $scope.articles = data;
               console.log('Articles recieved: ' + data);
           });
   }
}]);