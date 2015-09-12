/**
 * Created by zeek on 09-11-15.
 */

angular.module('BlogApp').service('Authentication', function($q, $http, $timeout){

    var authenticatedUser = null;

    return {
        requestUser: function(){
            var deferred = $q.defer();

            $http.get('/user.json').success(function(user){

                $timeout(function(){
                    authenticatedUser = user;
                    deferred.resolve(user);
                }, 1000);

            }).error(function(error)
            {
               deferred.reject(error);
            });

          return deferred.promise;
        },

        getUser: function(){
            return authenticatedUser;
        },

        exists: function(){
            return authenticatedUser;
        },

        login: function(credentials){

            var deferred = $q.defer();

            $http.post('/auth/login', credentials).success(function(user){

                if(user){
                    authenticatedUser = user;
                    deferred.resolve(user);
                }else{
                    deferred.reject(user);
                }

            }).error(function(error){

            });

            return deferred.promise;
        },

        logout: function(){
            authenticatedUser = null;
        },

        isAdmin: function(){
            return this.exists() && authenticatedUser.type == 'admin';
        }


    }
});