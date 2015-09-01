<h1>Testing the routes!</h1>
<!--button class="button" ng-click="find()">Find</button-->

<div ng-repeat="post in articles">
    <h1><a href="/article/{{post.year}}/{{post.title}}">{{post.title}}</a></h1>
    <p>Posted on <i>{{post.date}}</i> by <strong>{{post.author}}</strong></p>
    <!-- Fix automatic newline appending here...-->
    <button ng-repeat="tag in post.tags">{{ tag }}</button>
    <div ng-bind-html="post.content">
    </div>
    <br>
    <div ng-bind-html="post.content"></div>

    <div id="disqus_thread"></div>
    <script type="text/javascript">
        /* * * CONFIGURATION VARIABLES * * */
        var disqus_shortname = 'blogducktaped';

        /* * * DON'T EDIT BELOW THIS LINE * * */
        (function() {
            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
    <noscript>Please enable JavaScript to view the
        <a href="https://disqus.com/?ref_noscript" rel="nofollow">comments powered by Disqus.</a>
    </noscript>
</div>