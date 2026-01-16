# Architecture Decision Records (ADR)

This document serves as a log of significant architectural decisions made during the development of NetPulse.

## ADR 1: Data Model & Immutability (2026-01-13)
*   **Status:** Accepted
*   **Context:** We needed a robust way to structure measurement data (`NetworkMetric`) that would be passed between the Probing Service, Analysis Service, and Logging Service in a multi-threaded environment.
*   **Decision:** Use Java `records` (introduced in Java 14/16) instead of traditional POJOs.
*   **Consequence:** 
    *   **Pros:** Guarantees immutability and thread-safety by default. Reduces boilerplate (getters/setters/equals/hashCode).
    *   **Cons:** Cannot modify data once created (must create new instances), which is actually a desired feature here.

## ADR 2: Type-safe Configuration (2026-01-15)
*   **Status:** Accepted
*   **Context:** Accessing configuration values via string keys (e.g., `@Value("${property}")`) is error-prone and hard to refactor.
*   **Decision:** Implement `@ConfigurationProperties` using nested Java Records (`NetPulseProperties`).
*   **Consequence:** 
    *   Provides "fail-fast" validation at startup (app won't start if config is invalid).
    *   IDE autocompletion support for configuration in Java code.

## ADR 3: Network Probing Strategy (2026-01-16)
*   **Status:** Accepted
*   **Context:** The standard `InetAddress.isReachable()` method relies on ICMP (Ping). On Linux, sending ICMP packets requires Root privileges. Without root, Java falls back to a TCP Echo (port 7) check which often fails or times out (leading to false 500ms latency readings).
*   **Decision:** Switch to `java.net.Socket` targeting **TCP port 443 (HTTPS)**.
*   **Consequence:** 
    *   **Pros:** Provides accurate latency measurements (~10-20ms) in user-space without requiring `sudo` or special Docker capabilities (`NET_ADMIN`).
    *   **Cons:** Slightly higher overhead than raw ICMP, but negligible for monitoring intervals > 1s.

## ADR 4: Visualization Strategy - Web Dashboard (2026-01-16)
*   **Status:** Accepted
*   **Context:** The original plan involved a Java Swing desktop GUI. However, modern deployment targets (Docker/Cloud) are headless.
*   **Decision:** Pivoted to a **Client-Server architecture**.
    *   **Backend:** Exposes REST endpoints (`/api/metrics`).
    *   **Frontend:** Serves a static HTML/JS dashboard via Spring Boot.
*   **Consequence:** 
    *   Decouples the UI from the execution environment.
    *   Allows the application to run on a headless server (NAS/VPS) while being viewed on any device (Mobile/Laptop).
