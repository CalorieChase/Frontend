## Project

Android application built with:

* Kotlin
* Jetpack Compose
* Material 3
* Android SDK
* Gradle

Always inspect the existing project structure, architecture, dependencies, theme, and reusable components before writing code.

## Before Writing Code

Before making any change:

1. Inspect the relevant files and project structure.
2. Understand the existing architecture and navigation.
3. Check existing Compose components before creating new ones.
4. Reuse existing themes, colors, typography, spacing, icons, and components.
5. Follow existing naming and coding patterns.
6. Make the smallest clean change required.

Do not create duplicate components or unnecessary files.

## UI/UX Priority

UI/UX quality is a major priority.

Every screen should look like a polished, production-ready Android application.

Focus on:

* Clean and professional design
* Strong visual hierarchy
* Consistent spacing
* Good typography
* Proper alignment
* Balanced layouts
* Intuitive navigation
* Clear user feedback
* Smooth interactions
* Accessibility
* Responsive layouts

Avoid interfaces that look generic, unfinished, cluttered, or obviously AI-generated.

Do not simply place components on the screen. Think about how the complete screen should look and feel.

## Design

Follow Material 3 principles unless the existing project has its own design system.

Reuse the project's:

* Color scheme
* Typography
* Shapes
* Spacing
* Components
* Icons
* Themes

Maintain visual consistency across all screens.

Prefer reusable UI components for repeated design patterns.

Support dark mode if the existing application supports it.

Use proper loading, empty, success, and error states when needed.

Interactions should give clear visual feedback.

## Jetpack Compose

Write clean and reusable Compose code.

* Keep composables focused.
* Break large screens into smaller meaningful composables.
* Hoist state when appropriate.
* Prefer stateless reusable components.
* Avoid unnecessary recomposition.
* Use stable keys for dynamic lists.
* Prefer `LazyColumn` / `LazyRow` for large collections.
* Avoid expensive calculations directly inside composables.
* Use `remember` and `derivedStateOf` appropriately.
* Do not misuse `LaunchedEffect`, `SideEffect`, or other effect APIs.
* Keep business logic outside UI composables.

Do not create tiny composables that add no real readability or reuse.

## Architecture

Follow the architecture already used by the project.

Typically keep responsibilities separated:

```text
UI / Compose
      ↓
ViewModel
      ↓
Repository / Service
      ↓
API / Database
```

Do not put networking, database operations, or complex business logic directly inside composables.

Use ViewModels for screen state and UI-related business logic when appropriate.

Prefer predictable, one-directional state flow.

## Kotlin

Write idiomatic Kotlin.

* Use clear and descriptive names.
* Prefer immutable values with `val`.
* Use null safety properly.
* Avoid `!!` unless absolutely necessary.
* Use data classes for structured data.
* Use sealed classes/interfaces when they clearly improve state modeling.
* Prefer simple code over unnecessary abstractions.
* Avoid deeply nested logic.
* Avoid unnecessary classes and wrappers.
* Follow existing Kotlin conventions.

## Performance

Performance matters.

While implementing features, check for:

* Unnecessary recompositions
* Expensive operations inside composables
* Large objects recreated during recomposition
* Inefficient lists
* Blocking work on the main thread
* Repeated network requests
* Repeated database queries
* Unnecessary image loading
* Memory leaks
* Excessive state updates

Move expensive work outside the UI layer.

Use coroutines and appropriate dispatchers for asynchronous work.

The UI should remain smooth and responsive.

## Images

When displaying images:

* Use the project's existing image-loading library.
* Avoid loading unnecessarily large images.
* Use placeholders where appropriate.
* Handle loading and failure states.
* Use appropriate scaling and cropping.
* Avoid causing layout shifts.

## Navigation

Follow the project's existing navigation system.

* Keep routes organized.
* Avoid duplicating navigation logic.
* Handle back navigation correctly.
* Pass only necessary data between screens.
* Avoid passing large objects through navigation arguments.

## State

Every screen should clearly handle relevant states such as:

* Loading
* Success
* Empty
* Error

Do not leave users looking at a blank screen while something is happening.

Keep UI state predictable and easy to reason about.

## Error Handling

Handle errors gracefully.

Do not crash the app for expected failures.

Show useful user-facing messages when appropriate.

Do not expose raw exceptions or backend errors directly to users.

Never silently ignore important errors.

## Accessibility

UI should be usable and accessible.

Consider:

* Sufficient touch target sizes
* Content descriptions
* Readable typography
* Good contrast
* Screen reader support
* Avoiding important information communicated only through color

## Comments

Do not over-comment.

Comments should explain why something unusual or non-obvious exists.

Do not write comments that simply repeat the code.

Keep comments concise and useful.

## Code Reuse

Before creating anything new, search the project for something similar.

Reuse:

* Composables
* Theme values
* Components
* ViewModels
* Utilities
* Models
* Repositories
* Extensions

Do not duplicate existing functionality.

If several screens use the same UI pattern, create a reusable component when it actually improves the codebase.

## Dependencies

Do not add a new dependency unless necessary.

First check whether:

1. The project already has a library for the task.
2. Android or Kotlin already provides the functionality.
3. The feature can be implemented cleanly without another dependency.

Follow the existing Gradle dependency structure.

## Keep Changes Focused

Do not:

* Refactor unrelated code
* Change architecture unnecessarily
* Rename unrelated files
* Reformat the entire project
* Introduce unnecessary abstractions
* Rewrite working code without a reason

Keep changes focused on the requested feature.

## Build Verification

The application MUST build successfully after changes.

After every meaningful implementation:

1. Check imports and compile errors.
2. Build the project.
3. Fix all build errors caused by the changes.
4. Run relevant tests if available.
5. Check for lint issues when appropriate.

Do not consider a task complete if the application does not build.

Use the project's Gradle wrapper.

Typical verification:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

If the project has relevant tests, also run them.

Never knowingly leave broken code behind.

## Before Finishing

Before considering the task complete:

1. Review every changed file.
2. Confirm the app builds successfully.
3. Check the UI for visual consistency.
4. Check spacing, typography, alignment, and responsiveness.
5. Check loading, empty, error, and success states when relevant.
6. Check for unnecessary recompositions or expensive UI work.
7. Check for duplicated code.
8. Remove debug code and unused imports.
9. Make sure unrelated functionality was not changed.
10. Make sure the result looks production-ready.

## Git
I want to increase my git contribution. So I need you to make as many commits as you can! So for every small changes you make, commit it immediately. For an example: while creating a whole page, don't create all the components and then commit. Instead, create a simple text view and commit. Then add the title text and commit. Then add the description and commit. And so on.

## Main Principle

Build Android features as if they are shipping to real users.

The result should be:

* Visually polished
* Professional
* Easy to use
* Consistent
* Responsive
* Performant
* Maintainable
* Cleanly written
* Fully buildable

Understand the existing application first, reuse what already exists, and make new code feel like a natural part of the codebase.
