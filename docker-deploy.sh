#!/bin/bash

cname=mars
iname=mars-img
port=80
link_alias=db
link_with=db

docker build -t $iname .

docker run --name $cname -d --link $link_with:$link_alias -p $port:3000 $iname

