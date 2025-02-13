#!/bin/bash

siege -d0 -c1 -t10S \
  -H 'Content-Type: application/json' \
  'http://localhost:8080/ws/rest/external/v10/messages/send POST { "header": "7071","msisdn": "905011002022","text": "Hoş geldin GRPC"}' \
  -H 'Accept:application/json' \
