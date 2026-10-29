#!/bin/bash
# API test with curl - Akerke Tairabek
URL="${1:-http://localhost:8080}"
set -x
curl -s "$URL/"; echo
curl -s -X POST "$URL/api/students" -H "Content-Type: application/json" \
  -d '{"firstName":"Akerke","lastName":"Tairabek","email":"akerke@iitu.kz","groupName":"IT2-2307","age":20}'; echo
curl -s -X POST "$URL/api/students" -H "Content-Type: application/json" \
  -d '{"firstName":"Aruzhan","lastName":"Seitkali","email":"aruzhan@iitu.kz","groupName":"IT2-2307","age":19}'; echo
curl -s "$URL/api/students"; echo
curl -s "$URL/api/students/1"; echo
curl -s -X PUT "$URL/api/students/1" -H "Content-Type: application/json" \
  -d '{"firstName":"Akerke","lastName":"Tairabek","email":"akerke.new@iitu.kz","groupName":"IT2-2307","age":21}'; echo
curl -s -i -X DELETE "$URL/api/students/2"; echo
curl -s -i "$URL/api/students/999"; echo
