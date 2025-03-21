@echo off

cd ..
cd ..

call jreleaser full-release --git-root-search

pause
