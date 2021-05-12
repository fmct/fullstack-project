# Angular Products Web App

Simple Products Web App.

## Requirements

  * [Docker] 20+ (used version, maybe older versions also work, use them )

## Installation

```python
	git checkout https://github.com/
	docker build -t frontend .
    # You can use for example BACKEND_URI="http://localhost:8080"
	docker run --env BACKEND_URI="YOUR_BACKEND_URI" -p EXPOSED_PORT:80 frontend:latest
```



