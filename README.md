# 🌐 NetPulse

**NetPulse** is a lightweight, high-performance network monitoring tool designed for real-time latency tracking and availability analysis. Built with modern Java 25 and Spring Boot 4, it provides a professional "NOC-style" dashboard for visual oversight.

![NetPulse Dashboard](docs/NetPulse-snapshot.png)

## 🚀 Key Features

*   **Real-time Latency Tracking:** Visualized via Chart.js with dynamic color coding.
*   **TCP-based Probing:** Measures application-layer latency via port 443 (HTTPS).
*   **Log Download:** Instant access to historical data via a "Download CSV" button in the UI.
*   **Persistent Logging:** Automatically logs all metrics to a portable CSV format.
*   **Docker Ready:** Multi-stage build with optimized permissions for secure and easy deployment.
*   **NOC Dashboard:** Dark-themed web interface with KPIs (Avg/Max Latency, Uptime %).

## 🎯 Use Case: Application Availability

Unlike traditional ping tools that just check if a server is *online* (ICMP), NetPulse measures **application reachability**.

By opening a real TCP socket to port 443 (HTTPS), it simulates a user actually trying to connect to the service. This detects issues like:
*   Firewall rules dropping traffic.
*   Web server processes crashing (while the OS is still up).
*   Network congestion affecting handshake times.

## 🏁 Quick Start

### Using Docker (Fastest)

No cloning required. Just run this command and open your browser at `http://localhost:8080`:

```bash
docker run -d \
  --name netpulse \
  -p 8080:8080 \
  --restart unless-stopped \
  ghcr.io/jimmy-grafstrom/netpulse:latest
```

> **Note:** To ensure you have the latest features, run `docker pull ghcr.io/jimmy-grafstrom/netpulse:latest` to update your local image.

### Advanced: Persistent Local Logs

If you want the CSV logs to be saved directly on your host machine (outside the container) for long-term persistence, use a volume:

#### Linux / macOS (Bash)
```bash
# Create directory and give write access
mkdir -p logs && chmod 777 logs

docker run -d \
  --name netpulse \
  -p 8080:8080 \
  -v $(pwd)/logs:/app/logs:z \
  ghcr.io/jimmy-grafstrom/netpulse:latest
```

#### Windows (PowerShell)
```powershell
# Create directory
mkdir logs

docker run -d `
  --name netpulse `
  -p 8080:8080 `
  -v ${PWD}/logs:/app/logs `
  ghcr.io/jimmy-grafstrom/netpulse:latest
```

## ⚙️ Configuration

The easiest way to configure NetPulse is via environment variables:

| Property | Env Variable | Default |
| :--- | :--- | :--- |
| Target Host | `NETPULSE_MONITOR_TARGET_HOST` | `google.com` |
| Interval (ms) | `NETPULSE_MONITOR_INTERVAL_MS` | `5000` |
| Warning Threshold | `NETPULSE_MONITOR_THRESHOLDS_WARNING_MS` | `100` |
| Critical Threshold | `NETPULSE_MONITOR_THRESHOLDS_CRITICAL_MS` | `500` |

**Example:**
```bash
docker run -d -e NETPULSE_MONITOR_TARGET_HOST=github.com ...
```

## 📊 Data Access

NetPulse provides two ways to access your monitoring data:

*   **Live Log (Persistence):** If you use the Volume method, the `network-metrics.csv` file on your host machine is updated in real-time. This is ideal for long-term archiving or external analysis.
*   **Web Download (Snapshot):** Use the **"Download CSV Log"** button on the Dashboard for an instant snapshot copy of the logs. Perfect for quick sharing or manual inspection without server access.

**Internal Path:** `/app/logs/network-metrics.csv`

## 🏗 Architecture

This project follows a strict architectural process (ADR) recorded in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

---
*Developed by Jimmy Grafström - 2026*