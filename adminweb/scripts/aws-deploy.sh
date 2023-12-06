#!/bin/bash

currentFolder="`pwd`"
echo "Current folder [${currentFolder}]"
if [ ! -f package.json ]; then
  cd ..
fi
export projectFolder="`pwd`"
echo "Project folder [${projectFolder}]"

echo "Building production version"
cd $projectFolder
ng build --configuration=production
echo "Compressing files"
cd dist
tar -czvf corefinance-adminweb.tar.gz corefinance-adminweb/


echo "Cleaning data in server.."
ssh trungaws "rm -rfv /app/corefinance-adminweb*"

echo "Copying to server..."
scp corefinance-adminweb.tar.gz trungaws:/app/
echo "Extracting data in server.."
ssh trungaws "cd /app && tar -xvf corefinance-adminweb.tar.gz"
