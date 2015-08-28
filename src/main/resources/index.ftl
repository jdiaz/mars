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
    <!--div ng-view></div-->
    <h1>Testing the routes!</h1>
    <!--button class="button" ng-click="find()">Find</button-->

        <div ng-repeat="post in articles">
            <h1><a href="/article/{{post.title}}">{{post.title}}</a></h1>
            <p>Posted on <i>{{post.date}}</i> by <strong>{{post.author}}</strong></p>
            <!-- Fix automatic newline appending here...-->
            <button ng-repeat="tag in post.tags">{{ tag }}</button>
            <br>
            <br>
            <div ng-bind-html="post.content"></div>
            <br>
            <br>

            <div id="disqus_thread"></div>
            <script type="text/javascript">
                /* * * CONFIGURATION VARIABLES * * */
                var disqus_shortname = 'scriptfuzzy';

                /* * * DON'T EDIT BELOW THIS LINE * * */
                (function() {
                    var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
                    dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
                    (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
                })();
            </script>
            <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript" rel="nofollow">comments powered by Disqus.</a></noscript>
        </div>
    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.min.js" type="application/javascript"></script>
    <!--script src="" type="application/javascript"></script>
    <script src="" type="application/javascript"></script>
    <script src="" type="application/javascript"></script-->

    <script src="/js/app.js" ></script>
    <script src="/js/controllers/main.js" ></script>
    <!--script src="/js/controllers/article.js" ></script-->
</body>
</html>