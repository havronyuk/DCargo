@echo off
cd /d %~dp0
start "dcargo-service" cmd /k java -jar target\dcargo-service-1.jar
exit
