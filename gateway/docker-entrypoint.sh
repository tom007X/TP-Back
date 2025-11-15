#!/usr/bin/env bash
set -euo pipefail

KEYCLOAK_URL="http://keycloak:8080/realms/tpi-backend/.well-known/openid-configuration"
TIMEOUT=120
SLEEPTIME=2

echo "Waiting for Keycloak to be reachable at ${KEYCLOAK_URL} (timeout ${TIMEOUT}s)"

start_ts=$(date +%s)
while true; do
  if curl -sSf --max-time 5 "${KEYCLOAK_URL}" >/dev/null 2>&1; then
    echo "Keycloak is available. Continuing start-up."
    break
  fi

  now_ts=$(date +%s)
  elapsed=$((now_ts - start_ts))
  if [ "$elapsed" -ge "$TIMEOUT" ]; then
    echo "Timed out waiting for Keycloak after ${TIMEOUT}s" >&2
    exit 1
  fi

  echo "Keycloak not ready yet (elapsed ${elapsed}s). Sleeping ${SLEEPTIME}s..."
  sleep ${SLEEPTIME}
done

# Exec the Java process (preserve signals)
exec java -jar app.jar