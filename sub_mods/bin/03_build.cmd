@echo off

cd ..

call ../gradlew build

rem Build without running tests
rem call gradlew build -x test

pause
