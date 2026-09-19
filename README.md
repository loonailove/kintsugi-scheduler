# Kintsugi

A DAG-aware distributed job scheduler with a live chaos-injection dashboard.

> _Kintsugi: the Japanese art of repairing broken pottery with gold, making
> the breakage part of the object's history rather than something to hide._
> Fitting for a system whose whole point is watching it break: a worker
> dies, a job fails, a dependent gets held, and repair itself on camera.

## Architecture

```
                 submit job                claim / ack / heartbeat
   Client  ─────────────────────▶  Coordinator  ◀───────────────────▶  Worker(s)
                                        │
                                        ├── PostgreSQL (source of truth for job state)
                                        ├── dependency graph  (Wave 4)
                                        └── metrics ──────────────────▶ Dashboard (Lanterna, Wave 5-6)
                                        │
                                        ▼
                                  Dead-Letter Queue
```

- **`coordinator/`** - holds the job queue, the dependency graph, and the
  Postgres-backed source of truth. Hands eligible jobs to workers over
  HTTP. Owns the database; the worker never touches it directly.
- **`worker/`** - pulls jobs from the coordinator, executes them, acks
  success/failure, sends heartbeats.
- **`coordinator` and `worker` are independent Spring Boot apps**, each
  with its own Gradle build. They share no code, only the REST contract
  documented in `DECISIONS.md`. Changing the `Job` shape or an endpoint
  means updating both sides in the same PR.

## How to run it

**Via Docker Compose** (recommended — starts Postgres, coordinator, and worker together):

```bash
docker compose up --build
```

- Coordinator: `http://localhost:8080`
- Postgres: `localhost:5432` (db `kintsugi_db`, user `user`)

**Locally, without Docker** (each app needs its own terminal, plus a
Postgres running separately with a `kintsugi_db` database):

```bash
cd coordinator && ./gradlew bootRun
cd worker && ./gradlew bootRun
```
## Exit test (Wave 0)

Start the coordinator, then the worker. The worker should reach the
coordinator's handshake endpoint successfully. Nothing functional beyond
that yet, this only proves the two services can talk before real job
logic gets built on top.

## Design decisions & trade-offs

See [`DECISIONS.md`](./DECISIONS.md) for the full rationale, including:

- Two independent Spring Boot apps, no shared module, and how the `Job`
  contract stays in sync without one
- PostgreSQL via Docker Compose, never H2, including in tests
- The atomic claim query (`FOR UPDATE SKIP LOCKED`) that prevents two
  workers from ever claiming the same job

## License

MIT — see [`LICENSE`](./LICENSE).