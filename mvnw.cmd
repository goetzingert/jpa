@echo off
setlocal

set MAVEN_VERSION=3.9.11
if not "%MAVEN_USER_HOME%"=="" (set MAVEN_CACHE=%MAVEN_USER_HOME%\wrapper\dists) else (set MAVEN_CACHE=%USERPROFILE%\.m2\wrapper\dists)
set MAVEN_HOME=%MAVEN_CACHE%\apache-maven-%MAVEN_VERSION%\apache-maven-%MAVEN_VERSION%
set MAVEN_ARCHIVE=%MAVEN_CACHE%\apache-maven-%MAVEN_VERSION%\apache-maven-%MAVEN_VERSION%-bin.zip
set MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip

if exist "%MAVEN_HOME%\bin\mvn.cmd" goto run

if not exist "%MAVEN_CACHE%\apache-maven-%MAVEN_VERSION%" mkdir "%MAVEN_CACHE%\apache-maven-%MAVEN_VERSION%"
if not exist "%MAVEN_ARCHIVE%" powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ARCHIVE%'"
if exist "%MAVEN_HOME%" rmdir /s /q "%MAVEN_HOME%"
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Force '%MAVEN_ARCHIVE%' '%MAVEN_CACHE%\apache-maven-%MAVEN_VERSION%'"

:run
call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%
