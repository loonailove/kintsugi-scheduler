# Contributing

Two people, but we use PRs anyway; see DECISIONS.md's Git conventions.
The point isn't process for its own sake, it's a paper trail: PR
descriptions and commit history become the answer to "why did you choose
X" in an interview, months after you've forgotten.

## Branching

- `<type>/<short-description>`, e.g. `feature/atomic-claim-skip-locked`
- Types: `feature/`, `fix/`, `chore/`, `test/`
- Delete the branch after merge

## Commits

Conventional Commits: `feat: ...`, `fix: ...`, `chore: ...`, `docs: ...`,
`test: ...`. PR titles usually become the squash-merge commit on GitHub,
so keep the title itself in this format too, not just individual commits.

## Workflow

1. Branch off `main`.
2. Keep PRs scoped to one wave-task or one bug fix — not "wave 2 and also
   refactored the entity."
3. Open a PR even for solo, reviewed-by-nobody-but-yourself changes. CI
   must pass before merge.
4. If something broke in a non-trivial way while building the PR, add an
   entry to `BUGS.md` in the same PR — not after, you'll lose the details.
5. If the PR changes something recorded in `DECISIONS.md` (schema, an
   endpoint, a stack choice), update that file in the same PR. It should
   always describe what the code actually does, not what was once planned.

## The contract boundary

`coordinator` and `worker` share no code — only the REST contract in
`DECISIONS.md`. If a PR changes the `Job` shape or an endpoint on one
side, it must update the matching DTO/call site on the other side in the
**same PR**. A PR that only touches one side of that boundary is a bug in
review, not a style nitpick.

## Before opening a PR

- [ ] `./gradlew build` and `./gradlew test` pass, in every module touched
- [ ] Contract changes mirrored on both sides (see above)
- [ ] `BUGS.md` updated, if something broke along the way
- [ ] `DECISIONS.md` updated, if this changes a locked-in decision
