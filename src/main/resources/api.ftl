<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Article API</title>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    </head>
    <body>
        <h1>Article API</h1>
        <table class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>Route</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <#list routes as route>
                <tr>
                    <td>${route.url}</td>
                    <td>${route.description}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </body>
</html>
