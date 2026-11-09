# Simplii Spring Boot Sample

A Spring Boot reference application demonstrating a complete CI/CD pipeline with GitHub Actions and Harness.

## What's Included

- **Spring Boot App** (Java 17, Maven)
- **Dockerfile** for containerization
- **Kubernetes manifests** for deployment
- **GitHub Actions CI** for build, scan, and artifact signing
- **Harness CD** for deployment to Kubernetes

---

## Project Structure

```
src/main/java/com/harness/    # Application code
k8s/                          # Kubernetes manifests (Harness-templated)
.github/workflows/ci.yml      # GitHub Actions CI pipeline
.harness/                     # Harness YAML definitions
security-scans/               # SBOM and security scan results
```

---

## CI/CD Pipeline

### GitHub Actions (CI)

The pipeline (`.github/workflows/ci.yml`) runs on pull requests to `main`:

1. **Build** - Maven build, SBOM generation, Docker image push to DockerHub
2. **Scan** - Gitleaks secret scanning, Snyk container vulnerability scan
3. **Sign** - Triggers Harness signing pipeline via webhook
4. **Notify** - Triggers Harness CD pipeline via webhook

#### Required GitHub Secrets

- `DOCKERHUB_TOKEN` - DockerHub access token
- `SNYK_TOKEN` - Snyk API token
- `HARNESS_SIGNING_WEBHOOK_URL` - Harness signing pipeline webhook
- `HARNESS_WEBHOOK_URL` - Harness CD pipeline webhook

#### Required GitHub Variables

- `DOCKERHUB_USERNAME` - DockerHub username
- `SNYK_ORG` - Snyk organization ID

### Harness (CD + STO + SCS)

Harness handles:
- **Security scan ingestion via STO**
- **SLSA provenance generation and attestation**
- **SLSA verification before deployment**
- **Kubernetes deployment**
- **Governance and compliancevia OPA policies**


---

## Kubernetes Manifests

The `k8s/` directory contains Harness-templated manifests:

- `deployment.yaml` - Deployment with Harness expressions
- `service.yaml` - LoadBalancer service
- `values.yaml` - Template values using Harness expressions (e.g., `<+artifact.image>`)

---

## Harness Definitions (`.harness/`)

The `.harness/` folder contains Harness entity definitions:

### Pipelines

- **`pipeline/Simplii_Lower_Env_Deployment.yaml`** - Lower-environment pipeline (runs on every push to `main`)
  - Audit stage - Logs trigger payload and opens a Jira ticket
  - Security scan ingestion via STO, SLSA verification, attestation report to Jira
  - Rolling deployment + smoke test to CIBC DEV, then CIBC SIT

- **`pipeline/Simplii_AO_Deployment.yaml`** - AO pipeline (run manually to promote a build)
  - Audit stage - Logs trigger payload
  - Security scan ingestion via STO
  - SLSA verification
  - Blue/green deployment to CIBC UAT, ServiceNow approval, canary + rolling to CIBC Prod

- **`pipeline/Simplii_Artifact_Signature.yaml`** - Artifact signing pipeline
  - SLSA provenance generation and attestation
  - Triggered via webhook from GitHub Actions

### Service

- **`service/Simplii_Sample.yaml`** - Kubernetes service definition
  - References k8s manifests from GitHub
  - DockerHub artifact source with pipeline variables

### Environments

- **`environment/cibc_dev/`** - Development environment
- **`environment/cibc_sit/`** - System integration test environment
- **`environment/cibc_uat/`** - Pre-production environment
- **`environment/cibc_prod/`** - Production environment

### Input Sets

- **`inputset/webhook_input_set.yaml`** - Maps webhook payload to pipeline variables
  - `image_name`, `image_tag`, `repo_name`, `branch`, `repo_full_name`

---

## Local Development

```bash
# Build
mvn clean install

# Run locally
mvn spring-boot:run

# Build Docker image
docker build -t simplii-spring-boot-sample .
```
