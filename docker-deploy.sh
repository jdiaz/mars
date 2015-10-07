#!/bin/bash

container_name=mars
image_name=mars-img
port=80
link_alias=db
link_with=db

echo "Building container image..."
docker build -t $image_name .
echo "Finished building container image."

echo "Started container..."
docker run --name $container_name -d --link $link_with:$link_alias -p $port:3000 $image_name

