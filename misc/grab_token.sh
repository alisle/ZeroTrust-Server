#!/bin/sh

GRANT_TYPE="password"
CLIENT_ID="rest_client_id"
CLIENT_SECRET='secret'
USERNAME="admin@localhost.com"
PASSWORD="secret"

curl $CLIENT_ID:$CLIENT_SECRET@localhost:4300/oauth/token -d grant_type=password -d username=$USERNAME -d password=$PASSWORD
