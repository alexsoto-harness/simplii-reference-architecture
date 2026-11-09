# Simplii Sample

A Spring Boot reference architecture demonstrating GitHub Actions and Harness CD best practices for Kubernetes, featuring comprehensive security scanning, blue/green and canary deployments, and ServiceNow change management integration.

## Overview

This project serves as a reference implementation for teams adopting Kubernetes using modern CI/CD practices.

## Architecture

### CI Pipeline (GitHub Actions)

The CI pipeline consists of 4 sequential jobs:

1. **Build** - Maven build, SBOM generation, Docker build and push to DockerHub
2. **Scan** - Gitleaks secret detection, Snyk container scanning
3. **Sign** - Triggers Harness pipeline for artifact signing (Cosign)
4. **Notify** - Triggers main Harness deployment pipeline

### CD Pipeline (Harness)

The Harness pipeline includes:

- **Audit Stage** - Creates Jira ticket for tracking
- **Security Stage** - Code quality, SCA, SAST, and container image scans with OPA policy enforcement
- **Deploy to UAT** - Blue/Green deployment with release verification and QA testing
- **ServiceNow Approval** - Change request workflow
- **Deploy to Prod** - Canary deployment with automated rollback capabilities

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker
- Kubernetes cluster

### Build

```bash
mvn clean package
```

### Run Locally

```bash
java -jar target/spring-boot-sample-0.0.1-SNAPSHOT.jar
```

### Docker

```bash
docker build -t simplii-sample .
docker run -p 8080:8080 simplii-sample
```

## Links

- [GitHub Repository](https://github.com/alexsoto-harness/simplii-reference-architecture)
- [Harness Pipeline](https://app.harness.io/ng/account/EeRjnXTnS4GrLG5VNNJZUw/module/cd/orgs/sandbox/projects/soto_sandbox/pipelines/Simplii_AO_Deployment/pipeline-studio/)
