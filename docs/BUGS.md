# Bugs Log

Every time something breaks in a non-trivial way, add an entry with:
**Expected**, **Actual**, **Root cause**, **Fix**. This file is direct
interview material; keep it honest and specific.

---

## 2026-09-19 — `contextLoads()` failing in CI: DataSourceBeanCreationException

**Expected:** `./gradlew build` passes cleanly in `coordinator`.

**Actual:** `contextLoads()` failed with
`DataSourceProperties$DataSourceBeanCreationException` during Spring
context startup, both locally and in CI.

**Root cause:** `spring-boot-starter-data-jpa` and `org.postgresql:postgresql`
were present as unmodified Spring Initializr defaults, with no
`spring.datasource.*` configured anywhere yet. The JDBC starter on the
classpath triggers `DataSourceAutoConfiguration` automatically, which then
has no connection info to build a `DataSource` bean from.

**Fix:** removed both dependencies (and the matching `-test` starter)
until real persistence work started, since nothing in Wave 0/1 touches a
database. Re-added properly once `application.yaml` and
`docker-compose.yml` had real Postgres connection details.

---

## 2026-09-19 — `worker/.gradle/` committed to git

**Expected:** only source and config files under `worker/` are tracked.

**Actual:** `worker/.gradle/` — Gradle's internal build cache (checksums,
file hashes, execution history) was committed, 14 files.

**Root cause:** `coordinator/` has its own `.gitignore` (from Spring
Initializr, covers `.gradle` and `build/`); `worker/` never got one.

**Fix:** copied `coordinator/.gitignore` to `worker/.gitignore`, then
`git rm -r --cached worker/.gradle`.

<!-- Template for the next entry:

## YYYY-MM-DD — Short title

**Expected:**

**Actual:**

**Root cause:**

**Fix:**

-->
