# 🌐 NetPulse

**NetPulse** is a lightweight, high-performance network monitoring tool designed for real-time latency tracking and availability analysis. Built with modern Java 25 and Spring Boot 4, it provides a professional "NOC-style" dashboard for visual oversight.

![NetPulse Dashboard](docs/NetPulse-snapshot.png)

## 🚀 Key Features

*   **Real-time Latency Tracking:** Visualized via Chart.js with dynamic color coding.
*   **TCP-based Probing:** Measures application-layer latency via port 443 (HTTPS).
*   **Persistent Logging:** Automatically logs all metrics to a portable CSV format.
*   **Docker Ready:** Multi-stage build for minimal footprint and easy deployment.
*   **NOC Dashboard:** Dark-themed web interface with KPIs (Avg/Max Latency, Uptime %).

## 🎯 Use Case: Application Availability

Unlike traditional ping tools that just check if a server is *online* (ICMP), NetPulse measures **application reachability**.

By opening a real TCP socket to port 443 (HTTPS), it simulates a user actually trying to connect to the service. This detects issues like:
*   Firewall rules dropping traffic.
*   Web server processes crashing (while the OS is still up).
*   Network congestion affecting handshake times.

It is ideal for monitoring critical SaaS dependencies (e.g., Google API, AWS endpoints) or internal microservices from a containerized environment where root privileges are restricted.

## 🛠 Tech Stack

*   **Backend:** Java 25 (LTS), Spring Boot 4.0.1.
*   **Frontend:** HTML5, Bootstrap 5, Chart.js, Vanilla JS.
*   **Infrastructure:** Docker, Docker Compose.

## 🏁 Quick Start

### Using Docker Compose (Recommended)

1.  Build the image:
    ```bash
    docker build -t netpulse:latest .
    ```
2.  Start the service:
    ```bash
    docker compose up -d
    ```
3.  Open your browser at `http://localhost:8080`.

### Using Docker Run

If you don't have Docker Compose installed:
```bash
docker run -d \
  --name netpulse \
  -p 8080:8080 \
  -v $(pwd)/logs:/app/logs \
  --restart unless-stopped \
  netpulse:latest
```

## ⚙️ Configuration

You can customize the monitoring behavior in `src/main/resources/application.yml`:

```yaml
netpulse:
  monitor:
    target-host: google.com      # Host to monitor
    interval-ms: 5000            # Delay between probes
    thresholds:
      warning-ms: 100            # Yellow status threshold
      critical-ms: 500           # Red status/Timeout threshold
  logging:
    csv-path: logs/network-metrics.csv
```

## 📊 Data Persistence

By default, NetPulse persists all measurement data to a CSV file. When running via Docker, this is mapped to your local file system:

*   **Internal Path:** `/app/logs/network-metrics.csv`
*   **Host Path:** `./logs/network-metrics.csv` (Relative to your project root)

## 🏗 Architecture

This project follows a strict architectural process recorded in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

---
*Developed by Jimmy Grafström - 2026*
