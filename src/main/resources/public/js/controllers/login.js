/**
 * Created by zeek on 09-12-15.
 */

angular.module('BlogApp')
  .controller('LoginCtrl', function($scope, Authentication, Application){


    $scope.login = function(){
        var credentials = {};

        credentials.username = $scope.username;
        credentials.password = $scope.password;

        Authentication.login(credentials).then(function(){
            Application.makeReady();
        });
    }


 });