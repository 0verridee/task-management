-- Эта строчка нужна, чтобы скрипт не падал, если базы уже существуют (при перезапусках)
SELECT 'CREATE DATABASE "task-db"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'task-db')\gexec

SELECT 'CREATE DATABASE "user-db"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user-db')\gexec