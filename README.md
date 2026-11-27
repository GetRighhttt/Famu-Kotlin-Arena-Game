# Neon Arena

A Compose Multiplatform game built as a sequence of runnable lessons.

## Current lesson: draw-player

The arena is drawn first, followed by a cyan circle. `Offset` gives its center
in pixels. Half the canvas width and height places it in the middle.
`18.dp.toPx()` converts the radius for the display density. Positive x goes
right; positive y goes down. Resizing recalculates the center.

## Run

```sh
./gradlew :desktopApp:run
```

Android: `./gradlew :androidApp:assembleDebug` builds an APK without an emulator.
iOS: open `iosApp/NeonArena.xcodeproj` in Xcode. All launchers call the shared
`App()` in `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt`.
Use JDK 17 or newer and configure your Android SDK in ignored `local.properties`.

## Experiment

Change the x-coordinate to `size.width * 0.25f`. Predict where the circle
will appear. Then change its radius and compare the results.

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
