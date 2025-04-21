#!/bin/bash
set -euo pipefail

exec > >(tee build.log) 2>&1

echo "### Запуск ###"
mvn clean install
mvn spring-boot:run
echo "Успешно!"