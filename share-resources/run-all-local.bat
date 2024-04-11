@echo off

setlocal
echo Cleaning the project artifacts...
call gradle clean
if %ERRORLEVEL% equ 0 (
    echo Command executed successfully
) else (
    echo Command failed with error code %ERRORLEVEL%
    echo Build combined-product-account fail!!
    exit /b 0
)
endlocal

setlocal
echo Build combined-nontenancy...
rem call gradle clean :combined-nontenancy:bootJar
call gradle :combined-nontenancy:bootJar
if %ERRORLEVEL% equ 0 (
    echo Command executed successfully
) else (
    echo Command failed with error code %ERRORLEVEL%
    echo Build combined-product-account fail!!
    exit /b 0
)
endlocal

setlocal
echo Build combined-product-account...
rem call gradle clean :combined-product-account:bootJar
call gradle :combined-product-account:bootJar
if %ERRORLEVEL% equ 0 (
    echo Command executed successfully
) else (
    echo Command failed with error code %ERRORLEVEL%
    echo Build combined-product-account fail!!
    exit /b 0
)
endlocal

set GEOCODE_SERVICE_URL=localhost:9097
set PRODUCT_SERVICE_URL=localhost:9092

start "nontenancy" java -jar combined-nontenancy/build/libs/combined-nontenancy-0.0.16-SNAPSHOT-boot.jar --server.port=8889

timeout /t 20 /nobreak >nul

start "tenancy1" java -jar combined-product-account/build/libs/combined-product-account-0.0.16-SNAPSHOT-boot.jar --server.port=8988

timeout /t 10 /nobreak >nul

start "tenancy2" java -jar combined-product-account/build/libs/combined-product-account-0.0.16-SNAPSHOT-boot.jar --server.port=8989

