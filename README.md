# Neon Arena

A Compose Multiplatform game built as a sequence of runnable lessons.

## Current lesson: tap-input

Click or tap to move one player. `remember` retains its position across
recomposition, and `mutableStateOf` makes updates observable. The nullable
position starts as `null`; `?:` falls back to the center until the first tap.
`detectTapGestures` reports local pixel coordinates matching the canvas.
Input changes state, and Compose redraws using that state.

## Run

```sh
./gradlew :desktopApp:run
```

Android: `./gradlew :androidApp:assembleDebug` builds an APK without an emulator.
iOS: open `iosApp/NeonArena.xcodeproj` in Xcode. All launchers call the shared
`App()` in `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt`.
Use JDK 17 or newer and configure your Android SDK in ignored `local.properties`.

## Experiment

Change the assignment to `tappedPosition = Offset(position.x, size.height / 2f)`.
Predict which direction the player can move. Click near an edge to observe
clipping; the next lesson adds arena bounds. No second player is needed.

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
