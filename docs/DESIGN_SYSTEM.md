# Design System

`پرسا | Pursa` now has a foundational Jetpack Compose Material 3 design system under `app/src/main/java/org/pursa/app/designsystem`.

## Visual Principles

The visual language is warm, calm, imaginative, approachable, and suitable for children approximately ages 8-12. It should feel child-friendly without becoming childish, noisy, competitive, or gamified.

Avoid preschool-style visuals, neon colors, excessive rainbow palettes, heavy shadows, glass effects, fake 3D, dense layouts, and manipulative engagement patterns.

## Semantic Color Roles

The theme uses Material 3 semantic roles for:

- primary and primary container;
- secondary and secondary container;
- tertiary and tertiary container;
- background and surface;
- surface variant;
- outline and outline variant;
- error and error container.

Pursa-specific semantic colors are also available for curiosity, reflection, discovery, success, and warning. These names describe product meaning rather than palette position.

Dynamic color is disabled so the visual identity remains deterministic. Dark theme remains out of scope for the current MVP foundation.

## Typography

Typography uses Android platform-safe font fallback only. No custom, bundled, proprietary, or downloadable font is included.

The Material typography roles are tuned for Persian readability with comfortable line heights, readable body text, clear headings, and button labels that remain practical for children. Persian and mixed Persian/Latin text should not be manually reshaped or reversed.

## Persian and RTL

Persian RTL is first-class:

- runtime user-facing strings remain in Android string resources;
- `PursaRoot` applies RTL layout direction explicitly;
- components use start/end-friendly Compose layout patterns;
- previews demonstrate Persian RTL content;
- component APIs accept caller-provided text and content descriptions.

## Spacing and Size Scale

Spacing is centralized through CompositionLocal-backed tokens:

- none;
- extraSmall;
- small;
- medium;
- large;
- extraLarge;
- huge.

Shared size tokens include minimum touch target, compact and standard screen padding, preferred content maximum width, icon sizes, welcome mark size, and top bar height.

Interactive components target at least 48dp.

## Shape Scale

The shape scale uses Material 3 roles:

- extraSmall;
- small;
- medium;
- large;
- extraLarge.

Shapes are soft and approachable without turning every element into a pill.

## Component Inventory

Implemented reusable components:

- `PursaButton`: primary, secondary, tertiary, enabled, disabled, loading, optional leading icon, full-width option.
- `PursaCard`: clickable and non-clickable content cards with leading/trailing slots and an optional semantic accent strip.
- `PursaLabelChip` and `PursaSelectableChip`: label and selectable chips with accessible touch targets.
- `PursaLinearProgress` and `PursaStepIndicator`: non-competitive progress displays with safe value coercion.
- `PursaMessage`: inline info, success, warning, error, and empty-state messages.
- `PursaTopBar`: simple top bar with optional navigation action, testable navigation affordance, and action slot.

## Accessibility Rules

Components should preserve:

- readable text sizes;
- sufficient contrast;
- minimum 48dp interactive targets;
- meaningful content descriptions for icon-only actions;
- progress semantics;
- disabled-state readability;
- RTL screen-reader order;
- support for text scaling and compact screens.

Do not use color as the only indicator of selection, warning, error, success, or progress.

## Preview and Showcase Strategy

`PursaDesignSystemShowcase` provides preview-only examples of colors, typography, buttons, cards, chips, progress, messages, top bar, RTL Persian text, and mixed Persian/Latin text. It is not reachable from production UI and requires no ViewModel, repository, network, or Android service.

## Intentionally Out of Scope

The current foundation does not include story models, JSON content, Room, DataStore, Hilt, Media3, audio, networking, AI, Firebase, custom fonts, dark theme, dynamic color, screenshot tests, release signing, or production illustration assets.

## Phase 11 Visual Identity Refresh

The current app uses a warm, light-only visual identity. Dynamic color remains disabled so Pursa keeps a stable identity across devices.

Implemented palette:

- Brand violet `#6250A4`, brand container `#E8E0FF`, reflection soft `#F3EEFF`.
- Canvas warm `#FAF1E4`, canvas secondary `#FFF8EE`, reading surface `#FFFDF8`.
- Ink strong `#302D38`, ink default `#4B4654`, ink muted `#726C79`.
- Truth teal `#168C8C`, container `#CDEFEA`, soft `#EAF8F6`.
- Justice ochre `#9A6618`, container `#FFF0C7`, soft `#FFF8E7`.
- Friendship coral `#D86D66`, container `#FFE0DB`, soft `#FFF1EE`.
- Success `#378A58`, warning `#9A6618`, info `#3E73A8`, error `#B94B4B`.

Feature screens should use semantic tokens from `PursaTheme.semanticColors` and Material color roles. Raw hexadecimal colors belong in theme files only.

Decorative geometry is implemented with Compose primitives in `PursaDecorativeGeometry.kt`. It is static, subtle, non-interactive, hidden from accessibility semantics, and uses semantic colors. Truth uses observation rings and connected points; Justice uses balanced blocks; Friendship uses linked forms; Reflection Journal uses quiet violet circular forms.

Screens use layered backgrounds: warm or world-soft canvas, calm reading surfaces for story text, semantic containers for selected/reflection states, and bordered low-elevation cards. World identity is never color-only; titles, descriptions, accents, and motifs work together.

Critical semantic color pairs are covered by deterministic contrast tests in `PursaColorContrastTest`.
