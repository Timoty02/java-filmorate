@echo off
echo [Windows] Project build start...
call mvn clean install
if %errorlevel% neq 0 (
    echo Ошибка при сборке Maven!
    exit /b %errorlevel%
)
call mvn spring-boot:run
echo Start successful.