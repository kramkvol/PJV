@echo off
echo Starting Sudoku Application...
echo.

REM Build the project first
call mvnw.cmd clean compile

REM Run using javafx-maven-plugin
call mvnw.cmd javafx:run

pause
