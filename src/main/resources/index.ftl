<!DOCTYPE>
<html>
<head>
    <meta charset="utf-8">
    <meta name="description" content="" >
    <meta name="viewport" content="width=device-width">
    <title>ScriptFuzz Blog</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
</head>
<body ng-app="Blogapp">
   <div ng-controller="MainCtrl">
    <!-- Content goes here -->
    <h1>Testing the routes!</h1>
    <!--button class="button" ng-click="find()">Find</button-->
    <div ng-view></div>
        <!--<div ng-repeat="post in articles">
            <h1><a href="/article/{{post.title}}">{{post.title}}</a></h1>
            <p>Posted on <i>{{post.date}}</i> by <strong>{{post.author}}</strong></p>
            <button ng-repeat="tag in post.tags">{{ tag }}</button>
            <br>
            <br>
            <div ng-bind-html="post.content"></div>
            <br>
            <br>
            -->
   </div>
    <script src="https://code.angularjs.org/1.3.5/angular.min.js" type="application/javascript"></script>
    <Script src="https://code.angularjs.org/1.3.5/angular-route.js" type="application/javascript"></Script>
    <script src="/js/app.js" type="application/javascript"></script>
    <script src="/js/controllers/main.js" type="application/javascript"></script>
    <script src="/js/controllers/article.js" type="application/javascript"></script>
</body>
</html>