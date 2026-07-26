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

## Phase 5 Story JSON Format

Persian authored content lives under `app/src/main/assets/content/fa/`. Each story uses one lowercase ASCII filename, one stable lowercase ASCII story ID, and one JSON file. Every production story must be registered in `content/fa/manifest.json`; the app does not scan folders for unregistered stories.

The authoring schema is documented at:

```text
app/src/main/assets/content/schema/story.schema.json
```

Required top-level fields are `schemaVersion`, `id`, `worldId`, `title`, `summary`, `recommendedMinAge`, `recommendedMaxAge`, `estimatedDurationMinutes`, `themes`, `introduction`, ordered `steps`, and `completion`.

Supported Phase 5 step types are exactly:

- `narrative`: story text with optional title and required body.
- `single_choice`: one question and two to four options.
- `reason_prompt`: one question and authored reason categories.
- `perspective`: a speaker label, viewpoint, follow-up question, and two reflective responses.
- `counterexample`: a changed situation, question, and three neutral choices.
- `reflection`: a final comparison question and two to four valid reflection choices.

Do not add correct answers, scores, points, rewards, badges, ranks, psychological profiles, or hidden scoring fields. All choices must be valid. Changing one’s mind must not be treated as better than keeping one’s view.

## Phase 5 Review Checklist

Before registering a story:

- confirm all IDs are stable lowercase ASCII;
- confirm Persian text is natural, age-appropriate, and RTL-safe;
- confirm the story has a genuine philosophical tension;
- confirm it includes reasons, another viewpoint, a counterexample, and reflection;
- confirm no personal disclosure is requested;
- confirm no adult is framed as unquestionably correct;
- confirm no shame, fear, threats, or harsh punishment are used;
- confirm the manifest entry points to an existing file;
- run or update parser, validation, repository, state, and UI tests.

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
