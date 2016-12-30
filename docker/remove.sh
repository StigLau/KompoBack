#!/usr/bin/env bash
echo stopping kompoback
docker stop kompoback
echo removing kompoback
docker rm kompoback
echo list active docker containers
docker ps
