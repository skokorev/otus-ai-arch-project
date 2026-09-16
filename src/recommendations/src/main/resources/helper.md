docker pull postgres:17
docker pull dpage/pgadmin4:latest

docker run --name arch-helper-postgres -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_DB=defdb -e POSTGRES_PASSWORD=mysecretpassword -v H:\otus\data-dir\postgres:/var/lib/postgresql -d postgres

docker run --name postgres-admin -p 8432:80 -e PGADMIN_DEFAULT_EMAIL=skokorev@google.com -e PGADMIN_DEFAULT_PASSWORD=SuperSecret -d dpage/pgadmin4

docker run --rm -it alpine ip route