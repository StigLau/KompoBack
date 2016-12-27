#!/usr/bin/env bash
echo stopping KompoBack
docker stop KompoBack
echo removing KompoBack
docker rm KompoBack
echo list active docker containers
docker ps
