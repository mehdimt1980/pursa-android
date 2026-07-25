# Design System

`پرسا | Pursa` should feel child-friendly but not childish. It should be warm, calm, readable, and suitable for children approximately ages 8-12.

## Direction

- Persian RTL is a first-class requirement.
- Text should be readable and comfortably spaced.
- Touch targets should be large enough for children.
- Screens should avoid high text density.
- Visual identity should be calm and warm without relying on manipulative reward patterns.
- Motion should be meaningful, not constant decoration.
- Reduced motion support should be planned for future implementation.

## Accessibility

Design and implementation should support:

- Accessible contrast.
- Scalable text.
- Clear focus states.
- Meaningful labels for controls.
- Layouts that remain usable in RTL.
- Interactions that do not depend only on color, sound, or motion.

## Engagement Ethics

Do not use:

- Manipulative streak systems.
- Public scores or rankings.
- Leaderboards.
- Social pressure.
- Shame-based progress states.

Progress feedback should help children understand where they are, not pressure them to continue.

## Compose Components

Future implementation should use reusable Compose components for common patterns such as story cards, question prompts, reflection inputs, progress indicators, settings rows, and family discussion prompts.

Avoid hard-coded dimensions scattered across screens. Use shared spacing, shape, typography, and color decisions once implementation begins.

## Typography

Use fonts that can be legally bundled or reliably provided by the platform. Do not select a proprietary font that cannot legally be bundled.
