#!/bin/sh
set -e

echo "Arrancando PostgreSQL..."
su-exec postgres pg_ctl start -D /var/lib/postgresql/data -w

echo "Configurando la base de datos y usuario para Login..."
su-exec postgres psql -c "CREATE DATABASE backmac;" || true
su-exec postgres psql -c "CREATE USER springuser WITH PASSWORD 'springpassword';" || true
su-exec postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE backmac TO springuser;" || true
su-exec postgres psql -d backmac -c "GRANT ALL ON SCHEMA public TO springuser;" || true

echo "Arrancando microservicio de Login..."
exec java -jar /app/app.jar
