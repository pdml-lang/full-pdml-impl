@echo off

cd ..
cd ..

rem call jreleaser config --config-file=JReleaser/jreleaser.yml
call jreleaser config --git-root-search

pause