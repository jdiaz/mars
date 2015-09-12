/**
 * Created by zeek on 09-12-15.
 */
angular.module('BlogApp')
    .run(function (RouteFilter, Authentication)
    {
        RouteFilter.register('guest', ['/'], function()
        {
            return ! Authentication.exists();
        }, '/');

        //RouteFilter.register('guest', ['/signin'], function()
        //{
        //    return ! Authentication.exists();
        //}, '/');

        RouteFilter.register('admin', ['/articles/admin'], function()
        {
            return Authentication.isAdmin();
        }, '/');
    });