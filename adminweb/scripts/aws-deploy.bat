@echo off

set current_folder=%CD%
echo "Current folder [%current_folder%]"
IF NOT EXIST package.json (
  cd ..
)
set project_folder=%CD%
echo "Project folder [%project_folder%]"

echo "Building production version"
cd %current_folder%
call ng build --configuration=production
echo "Compressing files..."
cd dist
dir
tar -czvf corefinance-adminweb.tar.gz corefinance-adminweb

echo "Cleaning data in server.."
ssh trungaws "rm -rfv /app/corefinance-adminweb*"

echo "Copying to server..."
scp corefinance-adminweb.tar.gz trungaws:/app/
cd %project_folder%

echo "Extracting data in server.."
ssh trungaws "cd /app && tar -xvf corefinance-adminweb.tar.gz"
