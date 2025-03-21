@echo off

cd ..
cd ..

call jreleaser full-release --dry-run --git-root-search

pause