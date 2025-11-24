#!/usr/bin/env bash

set -euo pipefail

# ===== Configuration =====
# Change these paths to your project directories
ROOT_DIR="$HOME/Documents/bra-sentinela/back-end"

ADMIN_DIR="$ROOT_DIR/admin-service"
API_GATEWAY_DIR="$ROOT_DIR/api-gateway"
AUTH_DIR="$ROOT_DIR/auth-service"
COMPLAINT_DIR="$ROOT_DIR/complaint-service"
USER_DIR="$ROOT_DIR/user-service"
REPORT_DIR="$ROOT_DIR/report-service"

COMPOSE_DIR="$ROOT_DIR"               # where docker-compose.yml lives
LOG_DIR="$ROOT_DIR/logs"

# JVM options (optional)
JAVA_OPTS="-Xms256m -Xmx512m"

# ===== Functions =====
ensure_logs_dir() {
  mkdir -p "$LOG_DIR"
}

build_service() {
  local dir="$1"
  echo "[BUILD] $dir"
  pushd "$dir" >/dev/null
  mvn -q -DskipTests clean package
  popd >/dev/null
}

run_service() {
  local name="$1"
  local dir="$2"
  local port="$3"   # just for echoing
  local profile="${4:-default}"

  echo "[RUN] $name (port $port, profile $profile)"
  pushd "$dir" >/dev/null

  # Start in background, redirect logs
  # You can add SPRING_PROFILES_ACTIVE env if you use profiles
  SPRING_PROFILES_ACTIVE="$profile" \
  JAVA_TOOL_OPTIONS="$JAVA_OPTS" \
  nohup mvn -q spring-boot:run \
    > "$LOG_DIR/$name.log" 2>&1 &

  echo $! > "$LOG_DIR/$name.pid"
  popd >/dev/null
}

# ===== Main =====
ensure_logs_dir

# Optional: build all first for faster startup
build_service "$ADMIN_DIR"
build_service "$API_GATEWAY_DIR"
build_service "$AUTH_DIR"
build_service "$COMPLAINT_DIR"
build_service "$USER_DIR"

# Run services (adjust ports if different)
run_service "admin-service"      "$ADMIN_DIR"       3001  "default"
run_service "api-gateway"        "$API_GATEWAY_DIR" 8090  "default"
run_service "auth-service"       "$AUTH_DIR"        3003  "default"
run_service "complaint-service"  "$COMPLAINT_DIR"   3002  "default"
run_service "user-service"       "$USER_DIR"        3004  "default"
run_service "report-service"     "$REPORT_DIR"      3005  "default"

echo "All services started. Logs in: $LOG_DIR"
echo "Tip: tail -f $LOG_DIR/api-gateway.log"
