#!/bin/sh

GRANT_TYPE="password"
CLIENT_ID="rest_client_id"
CLIENT_SECRET='secret'
USERNAME="admin"
PASSWORD="nimda"

curl $CLIENT_ID:$CLIENT_SECRET@localhost:8080/oauth/token -d grant_type=password -d username=$USERNAME -d password=$PASSWORD
