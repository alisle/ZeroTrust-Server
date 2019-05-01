#!/bin/sh
docker run --rm -p 5432:5432 -e POSTGRES_PASSWORD=zerotrust_oauth_password -e POSTGRES_DB=zerotrust-oauth postgres
