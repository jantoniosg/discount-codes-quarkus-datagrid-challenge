#!/bin/sh
set -e

curl -H "Content-Type: application/json" http://localhost:8081/discounts -d '{ "name": "PROMO12", "amount": 20, "enterprise": "ALBACETEBANK", "type": "VALUE", "lifespan": 3600}'
curl -H "Content-Type: application/json" http://localhost:8081/discounts -d '{ "name": "PROMO35", "amount": 10, "enterprise": "MERCADONO", "type": "PERCENT" , "lifespan": 3600}'
curl http://localhost:8081/discounts/consume/PROMO12
echo ""
curl http://localhost:8081/discounts/consume/PROMO35
echo ""
curl http://localhost:8081/discounts/VALUE
echo ""
curl http://localhost:8081/discounts/PERCENT
echo ""