#!/usr/bin/env bash

#!/usr/bin/env bash
set -euo pipefail

PORTS=(3000 3001 3002 3003 3004 3005 3006 8090)

kill_port() {
  local port="$1"
  PID=$(lsof -t -i:$port || true)
  if [[ -n "${PID:-}" ]]; then
    echo "[KILL] Porta $port → PID $PID"
    kill -9 "$PID" || true
  else
    echo "[OK] Porta $port livre"
  fi
}

echo "Encerrando serviços ocupando portas Spring Boot..."

for port in "${PORTS[@]}"; do
  kill_port "$port"
done

echo "✔ Todas as portas liberadas."

