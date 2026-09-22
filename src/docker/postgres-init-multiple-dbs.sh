#!/bin/bash

set -e
set -u

function create_db_and_user() {
    local db_name=$1
    echo "Processing database and user '$db_name'"

    # 1. Create User (Idempotent: ignores "already exists" error)
    # We use a DO block to catch the duplicate_object error
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE USER $db_name;
        CREATE DATABASE $db_name;
        GRANT ALL PRIVILEGES ON DATABASE $db_name TO $db_name;
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
    echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"

    # Split the comma-separated variable into an array and iterate
    for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
        create_db_and_user $db
    done

    echo "Multiple databases processed"
fi