# Quality, Accessibility, and Visual Review

Pursa's visual system is reviewed as a child-facing Persian RTL reading experience, not as a generic dashboard.

## Automated Checks

Run before handing off UI work:

```bash
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug --stacktrace
./gradlew :app:assembleRelease --stacktrace
./gradlew :app:assembleDebugAndroidTest --stacktrace
```

Run `connectedDebugAndroidTest` when an emulator or device is available.

## Accessibility Scanner Checklist

- Contrast for headings, body text, buttons, selected options, message states, and dialogs.
- Minimum 48dp touch targets for every interactive control.
- Meaningful labels for navigation and dialog actions.
- Logical TalkBack traversal order in RTL.
- Selected states announced and not represented by color alone.
- Large font checks at 1.5x and 2.0x.
- Compact phone, landscape, and tablet-width layouts.
- Dialog dismissal and destructive confirmations.

## Manual Visual Review

Capture or inspect Welcome, Home, all three worlds, mission list states, each story step type, Summary, Journal List, Journal Detail, Settings, and dialogs.

Review for palette consistency, excessive white, excessive saturation, clipping, Persian line breaks, world distinction, calm story-reading surfaces, decorative geometry staying behind content, and large-font behavior.

Do not commit generated screenshots unless they are intentionally reviewed artifacts.
