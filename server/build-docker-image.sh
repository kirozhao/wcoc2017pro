#!/usr/bin/env bash


docker build -f Dockerfile -t wcoc2017pro/live-demo:latest .

docker stop wcoc2017pro
docker rm wcoc2017pro
docker run --name wcoc2017pro -p8000:8000 -p8001:8001 -d wcoc2017pro/live-demo:latest

# docker push wcoc2017pro/live-demo:latest
