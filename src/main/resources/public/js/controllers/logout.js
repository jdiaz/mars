/**
 * Created by zeek on 09-13-15.
 */
angular.module('BlogApp')
    .controller('LogoutCtrl', function($scope, Authentication){
       Authentication.logout();
    });