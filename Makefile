.PHONY: build run

build:
	docker build --tag spring-coroutines:mac .

run:
	docker run -p 8080:8080 spring-coroutines:mac
