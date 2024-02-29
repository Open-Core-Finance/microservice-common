@echo off

if "%~1"=="" (
    echo "Please pass compose command like up|down|run|..."
    exit 1
) else (
    echo "Calling docker compose command %*..."
)

for /f "tokens=* USEBACKQ" %%F in (`git log --format^=%%H -n 1`) do (
    set "GIT_COMMIT=%%F"
)
set "GIT_COMMIT=%GIT_COMMIT: =%"
echo "Git commit ID: %GIT_COMMIT%"

call share-resources\docker-scripts\docker-compose-env.bat

REM The setlocal enabledelayedexpansion is used to enable delayed variable expansion, allowing the use of ! to access the values inside the loop.
setlocal enabledelayedexpansion

REM Initialize array elements
set array[0]=userprofile
set array[1]=combined-product-account

REM Loop through array
for /L %%i in (0,1,6) do (
    for /f "delims=" %%a in ('findstr /i "server.port" "!array[%%i]!\src\main\resources\application-dev.yaml"') do (
        set filtered_lines=%%a
        set project_name=!array[%%i]!
        set project_name=%project_name:-=_%
        for /f "tokens=2" %%j in ("!filtered_lines!") do (
            set %project_name%_port=%%j
        )
    )
)

docker compose %*

endlocal