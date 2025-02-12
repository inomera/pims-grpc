#!/bin/bash

siege -d0 -c5 -t2S \
  -H 'Content-Type: application/json' \
  'http://localhost:8080/ws/rest/external/v10/messages/send/process POST { "header": "7070","msisdn": "905077505015","text": "Hoş geldin process GRPC"}' \
  -H 'Accept:application/json' \
  -H 'apikey: e888f6a4-f617-4e04-accf-6cefba63b06a'
