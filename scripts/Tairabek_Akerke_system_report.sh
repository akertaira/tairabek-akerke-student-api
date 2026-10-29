#!/bin/bash
# System monitoring report - Akerke Tairabek (IT2-2307, ID 38137)
# Usage: ./scripts/Tairabek_Akerke_system_report.sh

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_FILE="$SCRIPT_DIR/../logs/Tairabek_Akerke_system_report.log"
STUDENT="Akerke Tairabek"

{
    echo "=============================================="
    echo " System Report"
    echo "=============================================="
    echo "Student:   $STUDENT"
    echo "Date:      $(date '+%Y-%m-%d %H:%M:%S')"
    echo "Username:  $(whoami)"
    echo "OS:        $(grep PRETTY_NAME /etc/os-release 2>/dev/null | cut -d= -f2 | tr -d '"')"
    echo "Kernel:    $(uname -r)"
    echo
    echo "--- CPU ---"
    echo "Model:     $(grep -m1 'model name' /proc/cpuinfo | cut -d: -f2 | sed 's/^ //')"
    echo "Cores:     $(nproc)"
    echo
    echo "--- Memory ---"
    free -h
    echo
    echo "--- Disk usage ---"
    df -h
    echo
    echo "--- Available disk space on / ---"
    df -h / | awk 'NR==2 {print "Available: " $4 " of " $2 " (used " $5 ")"}'
    echo "=============================================="
    echo
} | tee -a "$LOG_FILE"

echo "Report saved to: $LOG_FILE"
