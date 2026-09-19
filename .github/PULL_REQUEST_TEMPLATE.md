## What does this change

<!-- One or two sentences. Which wave/task does this cover? -->

## Design trade-off (if any)

<!--
Chose between two real approaches here? Write it down — free interview
material later. No real trade-off? Delete this section.
-->

## How was this tested

<!-- Unit tests added? @SpringBootTest? Ran coordinator + worker manually and watched X happen? -->

## Checklist

- [ ] `./gradlew build` and `./gradlew test` pass locally, in every module you touched
- [ ] If you changed the `Job` shape or an API path, both `coordinator`'s and `worker`'s DTOs were updated in this PR (see DECISIONS.md → Contract sync)
- [ ] `BUGS.md` updated, if something broke along the way
- [ ] `DECISIONS.md` updated, if this changes a locked-in decision
