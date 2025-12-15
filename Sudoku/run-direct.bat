@echo off
echo Starting Sudoku Application (Direct Java)...
echo.

REM Find JavaFX JARs in Maven repository
set JAVAFX_BASE=%USERPROFILE%\.m2\repository\org\openjfx
set JAVAFX_VERSION=21

REM Build module path
set MODULE_PATH=%JAVAFX_BASE%\javafx-controls\%JAVAFX_VERSION%\javafx-controls-%JAVAFX_VERSION%.jar;%JAVAFX_BASE%\javafx-fxml\%JAVAFX_VERSION%\javafx-fxml-%JAVAFX_VERSION%.jar;%JAVAFX_BASE%\javafx-graphics\%JAVAFX_VERSION%\javafx-graphics-%JAVAFX_VERSION%.jar

REM Build classpath (include all dependencies)
set CLASSPATH=target\classes

REM Add JavaFX to classpath as well (for non-modular mode)
for %%f in ("%JAVAFX_BASE%\javafx-controls\%JAVAFX_VERSION%\*.jar") do set CLASSPATH=!CLASSPATH!;%%f
for %%f in ("%JAVAFX_BASE%\javafx-fxml\%JAVAFX_VERSION%\*.jar") do set CLASSPATH=!CLASSPATH!;%%f
for %%f in ("%JAVAFX_BASE%\javafx-graphics\%JAVAFX_VERSION%\*.jar") do set CLASSPATH=!CLASSPATH!;%%f

REM Run with module path and add modules
java --module-path "%MODULE_PATH%" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "%CLASSPATH%" cz.cvut.fit.sudoku.SudokuApplication

if errorlevel 1 (
    echo.
    echo Error: Failed to start application.
    echo Make sure you have compiled the project first: mvnw.cmd clean compile
)

pause

