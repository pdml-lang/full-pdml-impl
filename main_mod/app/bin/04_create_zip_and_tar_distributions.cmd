@echo off

cd ..
rem Change to use the fat jar
rem call ..\gradlew distZip
call ..\..\gradlew assembleDist

pause
