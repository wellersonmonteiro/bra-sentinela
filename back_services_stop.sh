#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$HOME/Documents/bra-sentinela/back-end"
LOG_DIR="$ROOT_DIR/logs"

stop_service() {
  local name="$1"
  local pid_file="$LOG_DIR/$name.pid"
  if [[ -f "$pid_file" ]]; then
    PID=$(cat "$pid_file" || true)
    if [[ -n "${PID:-}" ]]; then
      echo "[STOP] $name (PID $PID)"
      kill "$PID" || true
      rm -f "$pid_file"
    fi
  else
    echo "[STOP] $name pid file not found"
  fi
}

# Stop Spring Boot apps
stop_service "admin-service"
stop_service "api-gateway"
stop_service "auth-service"
stop_service "complaint-service"
stop_service "user-service"

echo "All services stopped."
