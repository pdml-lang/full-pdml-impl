@echo off

cd ..

call ..\..\gradlew clean
call ..\..\gradlew build

rem Build without running tests
rem call gradlew build -x test

pause
