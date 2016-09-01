#!/bin/bash
mongoimport --db test --collection articles --type json --file seed.json --jsonArray
