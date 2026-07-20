@echo off
cd /d %~dp0
start "dcargo-service" cmd /k java -jar target\dcargo-service-vers 1.jar
exit
