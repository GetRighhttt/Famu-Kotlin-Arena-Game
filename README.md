# Neon Arena

A Compose Multiplatform game built as a sequence of runnable lessons.

## Current lesson: project-setup

Each platform has a small launcher that calls the same `App()` composable.
The shared screen displays a title on a dark background. There is no arena or
player yet. Gradle modules separate platform launchers from shared code.

## Run

```sh
./gradlew :desktopApp:run
```

Android: `./gradlew :androidApp:assembleDebug` builds an APK without an emulator.
iOS: open `iosApp/NeonArena.xcodeproj` in Xcode. All launchers call the shared
`App()` in `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt`.
Use JDK 17 or newer and configure your Android SDK in ignored `local.properties`.

## Experiment

Change the title font size from `32.sp` to `48.sp`, then run again.
The title grows while staying centered.

## Lesson branches

Checkpoints are cumulative. Each branch starts from the preceding lesson's
merge into `develop`, and each has its own runnable implementation and README.

1. `bayne/project-setup`: platform launchers and a shared screen.
2. `bayne/draw-arena`: a canvas and arena boundary.
3. `bayne/draw-player`: a circle and drawing coordinates.
4. `bayne/tap-input`: position state and tap gestures.
5. `bayne/player-state`: player data, bounds, and responsibility-based packages.

The original experimental history remains on `master`. This lesson sequence
was reconstructed from those experiments; it is not their original chronology.
Commits use Stefan Bayne's authorship and contain no co-author trailers.
