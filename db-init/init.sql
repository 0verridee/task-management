SELECT 'CREATE DATABASE "task-db"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'task-db')\gexec

SELECT 'CREATE DATABASE "user-db"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user-db')\gexec