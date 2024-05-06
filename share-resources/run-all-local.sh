#!/bin/bash
pid_file=logs/local.pid
kill_process_script=share-resources/kill_processes.sh
export GEOCODE_SERVICE_URL=localhost:9097
export PRODUCT_SERVICE_URL=localhost:9092

chmod +x "${kill_process_script}"

if [[ -f "$pid_file" ]] ; then
  ./"${kill_process_script}" "${pid_file}"
  > "${pid_file}"
else
  touch "${pid_file}"
fi

echo "Clean the project..."
gradle clean

echo "Build..."
gradle :combined-nontenancy:bootJar :combined-product-account:bootJar
result="$?"
if [[ "$result" != "0" ]] ; then
  echo "Build fail!!"
  exit 1
fi

#echo "Build combined-product-account..."
#gradle :combined-product-account:bootJar
#result="$?"
#if [[ "$result" != "0" ]] ; then
#  echo "Build combined-product-account fail!!"
#  exit 1
#fi

java -jar combined-nontenancy/build/libs/combined-nontenancy-0.0.16-SNAPSHOT-boot.jar --server.port=8889 &
# Capture the PID of the background task
background_pid=$!
# Write PID to file
echo "${background_pid}" >> "${pid_file}"
# Wating for server start
sleep 10

java -jar combined-product-account/build/libs/combined-product-account-0.0.16-SNAPSHOT-boot.jar --server.port=8988 &
# Capture the PID of the background task
background_pid=$!
# Write PID to file
echo "${background_pid}" >> "${pid_file}"
# Wating for server start
sleep 20

java -jar combined-product-account/build/libs/combined-product-account-0.0.16-SNAPSHOT-boot.jar --server.port=8989 &
# Capture the PID of the background task
background_pid=$!
# Write PID to file
echo "${background_pid}" >> "${pid_file}"
