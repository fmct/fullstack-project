# Spring Boot Products Rest Api

Simple Spring Boot Rest Api.

## Requirements

  * [Docker] 20+ (used version, maybe older versions also work, use them )

## Installation

```python
git clone https://github.com/fmct/fullstack-project; cd fullstack-project/backend
# Dockerfile is inside build/ directory
# so if you run this command inside project folder just put PATH_TO_DOCKERFILE as build/Dockerfile
docker build -t YOUR_TAG -f PATH_TO_DOCKERFILE .
docker run -p EXPOSED_PORT:8080 YOUR_TAG
```

## Swagger

You can access the swagger api documentation at BACKEND_URI/swagger-ui.html (for example http://localhost:8080/swagger-ui.html)



