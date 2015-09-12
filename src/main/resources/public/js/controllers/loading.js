/**
 * Created by zeek on 09-11-15.
 */
angular.module('BlogApp')
    .controller('LoadingCtrl', function($scope, Application, $location){

    Application.registerListener(function(){
       //When ready redirecto to index
        $location.path('/');
    });
});