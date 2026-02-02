Her er et forslag til **README.md** du kan legge i root av repoet ditt.

---

# Kubernetes + Argo CD Java Demo

Dette prosjektet er et **læringsprosjekt** for å forstå hvordan **Kubernetes**, **Helm** og **Argo CD (GitOps)** fungerer i praksis.
Alt kjøres **lokalt** med **WSL + Docker Desktop + Kubernetes** og påvirker ikke eksterne miljøer.

Prosjektet inneholder:

* En liten **Java HTTP-applikasjon**
* **Dockerfile** for container-bygging
* **Helm chart** (Deployment, Service, ConfigMap, Secret, HPA)
* **Argo CD Application** for GitOps-deploy

---

## Teknologier

* Java 17
* Maven
* Docker / Docker Desktop
* Kubernetes (Docker Desktop cluster)
* Helm
* Argo CD
* WSL (Windows Subsystem for Linux)

---

## Mål

* Lære grunnleggende Kubernetes-ressurser:

  * Deployment
  * Service
  * ConfigMap
  * Secret
  * Ingress
  * HPA (autoscaling)
* Forstå Helm charts og values
* Forstå GitOps-flyt med Argo CD
* Øve på containerisering av Java-applikasjon

---

## Prosjektstruktur

```
k8s-argocd-java-demo/
  app/                     # Java app + Dockerfile
  chart/java-demo/         # Helm chart
  environments/dev/        # Miljøspesifikke values
  argocd/                  # Argo CD Application manifest
```

---

## Forutsetninger

* Docker Desktop installert
* Kubernetes aktivert i Docker Desktop
* WSL2 (Ubuntu anbefalt)
* `kubectl`, `helm`, `git` installert

Sjekk:

```bash
kubectl get nodes
docker version
helm version
```

---

## Kjøre lokalt uten Argo CD (Helm)

Bygg Docker-image:

```bash
cd app
docker build -t java-demo:1.0.0 .
cd ..
```

Installer med Helm:

```bash
kubectl create namespace demo
helm upgrade --install java-demo ./chart/java-demo -n demo -f environments/dev/values.yaml
```

Test:

```bash
kubectl -n demo port-forward svc/java-demo 8080:80
curl localhost:8080
```

---

## Installere Argo CD

```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

Port-forward:

```bash
kubectl -n argocd port-forward svc/argocd-server 8081:443
```

Hent admin-passord:

```bash
kubectl -n argocd get secret argocd-initial-admin-secret \
  -o jsonpath="{.data.password}" | base64 -d; echo
```

Åpne: [https://localhost:8081](https://localhost:8081)

---

## Argo CD Application

Når repoet er pushet til GitHub/GitLab:

```bash
kubectl apply -f argocd/application-dev.yaml
```

Argo CD vil:

* Hente Helm chart fra Git
* Deploye til cluster
* Automatisk synce ved nye commits

---

## Vanlige feil

| Problem                    | Årsak                          |
| -------------------------- | ------------------------------ |
| `unable to resolve 'main'` | Feil branch i `targetRevision` |
| App vises ikke i UI        | `kubectl apply` ikke kjørt     |
| HPA virker ikke            | Metrics Server mangler         |
| Image ikke funnet          | Ikke bygget/pushet image       |

---

## Avslutte / Rydde opp

Slett demo-app:

```bash
kubectl delete application java-demo-dev -n argocd
```

Eller slett namespace:

```bash
kubectl delete namespace demo
```

Stoppe hele cluster:

* Disable Kubernetes i Docker Desktop

---

## Læringsnotat

Dette prosjektet er ment for eksperimentering:

* Endre ConfigMap/values
* Øk replicas
* Test readiness/liveness
* Se Argo CD sync i praksis

Alt kjøres lokalt og påvirker ingen eksterne systemer.
