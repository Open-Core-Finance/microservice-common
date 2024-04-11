#!/bin/bash
pid_file=logs/local.pid
kill_process_script=share-resources/kill_processes.sh
chmod +x "${kill_process_script}"

if [[ -f "$pid_file" ]] ; then
  ./"${kill_process_script}" "${pid_file}"
  echo "" > "${pid_file}"
else
  touch "${pid_file}"
fi
