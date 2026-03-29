@echo off
setlocal

set BASE_DIR=%~dp0
set WRAPPER_PROPERTIES=%BASE_DIR%\.mvn\wrapper\maven-wrapper.properties

if not exist "%WRAPPER_PROPERTIES%" (
  echo No se encontro %WRAPPER_PROPERTIES%
  exit /b 1
)

for /f "tokens=1,* delims==" %%A in (%WRAPPER_PROPERTIES%) do (
  if "%%A"=="distributionUrl" set DISTRIBUTION_URL=%%B
)

if "%DISTRIBUTION_URL%"=="" (
  echo No se encontro distributionUrl en %WRAPPER_PROPERTIES%
  exit /b 1
)

for %%I in ("%DISTRIBUTION_URL%") do set ARCHIVE_NAME=%%~nxI
set MAVEN_BASE_DIR=%USERPROFILE%\.m2\wrapper\dists
set ARCHIVE_PATH=%MAVEN_BASE_DIR%\%ARCHIVE_NAME%
set UNPACK_DIR=%MAVEN_BASE_DIR%\%ARCHIVE_NAME:.zip=%

if not exist "%MAVEN_BASE_DIR%" mkdir "%MAVEN_BASE_DIR%"

if not exist "%ARCHIVE_PATH%" (
  powershell -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%ARCHIVE_PATH%'"
)

if not exist "%UNPACK_DIR%" (
  powershell -Command "Expand-Archive -LiteralPath '%ARCHIVE_PATH%' -DestinationPath '%UNPACK_DIR%' -Force"
)

for /d %%D in ("%UNPACK_DIR%\apache-maven-*") do (
  if /I not "%%~fD"=="%UNPACK_DIR%" set MAVEN_HOME=%%D
)

if "%MAVEN_HOME%"=="" (
  echo No se pudo localizar la distribucion de Maven desempaquetada.
  exit /b 1
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
