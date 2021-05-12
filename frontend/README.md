# Angular Products Web App

Simple Products Web App.

## Requirements

  * [Docker] 20+ (used version, maybe older versions also work, use them )

## Installation

```python
git clone https://github.com/fmct/fullstack-project; cd fullstack-project/frontend
docker build -t frontend .
# You can use for example BACKEND_URI="http://localhost:8080" and EXPOSED_PORT as 80
docker run --env BACKEND_URI="YOUR_BACKEND_URI" -p EXPOSED_PORT:80 frontend:latest
```



