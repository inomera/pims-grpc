#!/bin/bash

siege -d0 -c10 -t10S \
  -H 'Content-Type: application/json' \
  'http://localhost:8080/ws/rest/external/v10/messages/send POST { "header": "7071","msisdn": "905011002022","text": "Hoş geldin GRPC"}' \
  -H 'Accept:application/json' \
  -H 'apikey: e888f6a4-f617-4e04-accf-6cefba63b06a'
