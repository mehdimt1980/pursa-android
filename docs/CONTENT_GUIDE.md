# Content Guide

`پرسا | Pursa` content is for children approximately ages 8-12. It should invite philosophical inquiry through Persian-first stories, questions, and activities.

## Editorial Standards

- Write in natural contemporary Persian.
- Prefer short sentences.
- Use age-appropriate vocabulary.
- Keep situations culturally familiar for children living in Iran.
- Ask for reasons.
- Introduce alternative viewpoints.
- Use examples and counterexamples.
- Encourage children to revise opinions when they encounter better reasons.
- Avoid moral preaching.
- Avoid a single predetermined correct answer.
- Avoid memorization of philosophers, schools, dates, or definitions.

## Cultural and Safety Standards

Content must avoid:

- Ethnic, linguistic, regional, economic, gender, disability, religious, and family stereotypes.
- Political or ideological propaganda.
- Religious instruction.
- Requests for sensitive personal disclosure.
- Shaming, fear, manipulation, or pressure.
- Real children's personal data.

## Story Review Checklist

Before accepting a story, check:

- Is the target age clear?
- Is the story short enough for the age range?
- Does it include a genuine philosophical tension?
- Does it ask children for reasons?
- Does it show more than one reasonable viewpoint?
- Does it include examples or counterexamples?
- Does it avoid preaching?
- Does it avoid stereotypes and propaganda?
- Does it avoid sensitive personal disclosure?
- Does it work in Persian and RTL?

## Sample Mission Structure

1. Short illustrated story.
2. First open question.
3. Reason prompt.
4. Alternative viewpoint.
5. Example or counterexample activity.
6. Reflection prompt.
7. Optional family discussion prompt.

## Content JSON Principles

Authored story JSON should:

- Keep text content separate from UI code.
- Use stable semantic IDs.
- Support Persian RTL text.
- Represent story steps, questions, activities, and reflection prompts explicitly.
- Avoid embedding presentation-only layout decisions.
- Include review metadata such as theme, target age, and safety notes.

## Story JSON Format

Persian authored content lives under `app/src/main/assets/content/fa/`. Each story uses one lowercase ASCII filename, one stable lowercase ASCII story ID, and one JSON file. Every production story must be registered in `content/fa/manifest.json`; the app does not scan folders for unregistered stories.

The authoring schema is documented at:

```text
app/src/main/assets/content/schema/story.schema.json
```

Required top-level fields are `schemaVersion`, `id`, `worldId`, `title`, `summary`, `recommendedMinAge`, `recommendedMaxAge`, `estimatedDurationMinutes`, `themes`, `introduction`, ordered `steps`, and `completion`.

Production stories also include `contentRevision`, a positive integer used to decide whether a saved in-progress session can be safely restored. Increment `contentRevision` when step IDs, option IDs, step order, or answer compatibility changes. Do not increment it for trivial punctuation corrections unless restoration compatibility is affected. Stable story IDs must not change.

Supported Phase 5 step types are exactly:

- `narrative`: story text with optional title and required body.
- `single_choice`: one question and two to four options.
- `reason_prompt`: one question and authored reason categories.
- `perspective`: a speaker label, viewpoint, follow-up question, and two reflective responses.
- `counterexample`: a changed situation, question, and three neutral choices.
- `reflection`: a final comparison question and two to four valid reflection choices.

Do not add correct answers, scores, points, rewards, badges, ranks, psychological profiles, or hidden scoring fields. All choices must be valid. Changing one’s mind must not be treated as better than keeping one’s view.

## Phase 6 Truth Mission Guidance

The first complete content world uses four Truth missions in this manifest-defined order:

1. `truth_broken_vase`
2. `truth_group_photo`
3. `truth_strange_news`
4. `truth_friend_secret`

The sequence moves from personal responsibility toward digital representation, source reliability, and friendship secrets. Future world sets should use a similarly intentional order when the child-facing learning sequence matters.

Recommended mission length:

- 7 to 9 authored steps for most missions.
- Introduction plus completion reflection.
- Approximately 8 to 10 minutes.
- Narrative bodies usually under 90 Persian words.
- Perspective text usually under 70 Persian words.
- Option labels short enough to scan on compact screens.

Philosophical tension is required. A mission should hold at least two defensible values in tension, such as truth versus privacy, speed versus verification, loyalty versus fairness, or intention versus consequence. Avoid options that make one answer obviously educationally approved.

Perspective steps should represent another viewpoint charitably. Counterexamples should change one meaningful condition, not merely repeat the first question. Reflection should allow children to keep, revise, complicate, or remain unsure about their view without praise or correction.

Options should express reasons rather than random actions. Avoid absurd distractors, hidden adult-approved answers, duplicate meanings, negative wording traps, pressure to confess, and options that require personal experience. Use uncertainty only when it helps the inquiry.

Perspective steps must express a real reason, sound plausible, avoid caricature, introduce a relevant value, and avoid revealing the author's preferred answer.

Counterexamples must change a relevant condition, test a principle, avoid shocking extremes, remain connected to the original problem, and support reconsideration without requiring it.

Completion copy should mention relevant concepts, acknowledge multiple viewpoints, avoid formulaic repetition, and never evaluate the child.

Digital-media scenarios may discuss images, captions, sharing, uncertainty, and source reliability without naming commercial platforms or imitating official announcements. Privacy and consent scenarios should distinguish a true image from fair publication and accurate representation. Secret scenarios must stay low-risk unless a dedicated safety review exists; do not use severe harm, abuse, self-harm, violence, sexual content, drugs, or dangerous adult behavior as ordinary mission material.

Completion reflections should mention the values considered, acknowledge that more than one position may be reasonable, and emphasize reasons and perspective-taking. They must not grade choices, reward changing one's mind, or declare a single final answer.

Persian editorial quality:

- Use natural contemporary Persian.
- Use Persian `ی` and `ک`.
- Use نیم‌فاصله consistently.
- Keep punctuation readable in RTL.
- Avoid adult academic vocabulary unless explained naturally.
- Avoid baby talk and patronizing wording.

## Phase 8 Justice Mission Guidance

The second complete content world uses four Justice and fairness missions in this manifest-defined order:

1. `justice_last_cake`
2. `justice_class_representative`
3. `justice_playground_rule`
4. `justice_team_prize`

The sequence moves from familiar sharing decisions toward class representation, school rules, and group contribution. Friendship remains unauthored until a later phase.

Justice stories should keep more than one reasonable value alive at the same time: equality, need, effort, representation, majority choice, minority concern, safety, authority, contribution, circumstances, and visible or less-visible work. Avoid presenting adults, rules, votes, or equal shares as automatically correct. Children may conclude that equal treatment is fair, that differences matter, or that the answer depends on the reason and context.

School and group scenarios should stay low-risk and non-political. Authority figures may have good safety reasons, but children should still be invited to question rules respectfully. Representation scenarios should stay in classroom terms and avoid governmental or partisan language. Reward scenarios should avoid money, expensive prizes, ranking, scores, or shame.

## Phase 9 Friendship Mission Guidance

The third complete content world uses four Friendship and loyalty missions in this manifest-defined order:

1. `friendship_new_friend`
2. `friendship_difficult_promise`
3. `friendship_whose_side`
4. `friendship_game_without_them`

The sequence moves from beginning friendship and belonging toward promises, conflict, support without blind agreement, and group responsibility. Friendship content should avoid formulas such as "a real friend always..." unless a character says them as a view to examine.

Friendship scenarios can feel personal, so keep them fictional, third-person, and low-risk. Do not ask children to reveal personal friendship histories, secrets, names, exclusions, or private experiences. Avoid bullying, humiliation, threats, dangerous secrecy, public shaming, and permanent isolation.

Loyalty should not mean blind agreement. Support can include listening, private correction, truthfulness, repair, and staying respectfully present. Inclusion should not mean forced intimacy: groups may have boundaries, one-time exclusion differs from repeated exclusion, and not every difference must be resolved through closeness. The goal is philosophical inquiry, not etiquette instruction.

## Production Content Review Checklist

Before registering a story:

### Structure

- confirm all IDs are stable lowercase ASCII;
- confirm the story is registered in the manifest;
- confirm story IDs and asset paths are unique;
- confirm `artworkKey` uses `story_<story_id>` format and resolves in `PursaArtworkRegistry`;
- confirm manifest ID and world ID match parsed story content;
- confirm only supported step types are used;
- confirm valid option counts;
- confirm the mission includes a reflection step;
- confirm no orphan production story file remains unregistered;

### Philosophy

- confirm Persian text is natural, age-appropriate, and RTL-safe;
- confirm the story has a genuine philosophical tension;
- confirm it includes reasons, another viewpoint, a counterexample, and reflection;
- confirm more than one defensible value is represented;
- confirm no option is marked or framed as the predetermined correct answer;

### Child Safety

- confirm no personal disclosure is requested;
- confirm no adult is framed as unquestionably correct;
- confirm no shame, fear, threats, or harsh punishment are used;
- confirm no serious-harm secret scenario is used without dedicated safety review;
- confirm friendship stories do not request personal disclosure;
- confirm loyalty is not framed as blind agreement;
- confirm inclusion is not framed as forced closeness;
- confirm repeated exclusion is examined separately from one-time limits;
- confirm no stereotyping, propaganda, or current political controversy is included;

### Verification

- confirm the manifest entry points to an existing file;
- run or update parser, validation, repository, state, and UI tests.
- run or update artwork registry and production-content artwork tests.

See [ARTWORK_GUIDE.md](ARTWORK_GUIDE.md) and [NEW_STORY_CHECKLIST.md](NEW_STORY_CHECKLIST.md) before adding a production story.

Every production story also needs an educational review file and index entry. See [EDUCATIONAL_REVIEW.md](EDUCATIONAL_REVIEW.md), [CONTENT_REVIEW_CHECKLIST.md](CONTENT_REVIEW_CHECKLIST.md), and [CONTENT_MAP.md](CONTENT_MAP.md).

## Sample Persian Question Patterns

```text
تو چه فکر می‌کنی؟
چرا این‌طور فکر می‌کنی؟
می‌توانی یک دلیل بیاوری؟
آیا همیشه همین‌طور است؟
می‌توانی یک مثال پیدا کنی؟
کسی که مخالف توست چه می‌گوید؟
چه چیزی ممکن است نظر تو را تغییر دهد؟
```
