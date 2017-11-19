@echo off

mvn package || goto :error
docker build -t dynamicip-chrome-java . || goto :error
docker run -v /dev/shm:/dev/shm dynamicip-chrome-java || goto :error

goto :EOF
:error
echo Failed with error #%errorlevel%.
exit /b %errorlevel%