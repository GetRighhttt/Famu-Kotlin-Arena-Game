# Neon_Kotlin_Arena_Game
Kotlin game development practice.

A Kotlin / Compose Multiplatform game built one learning step at a time.

## Current step: position state and input

The arena has two cyan circles. The first starts at 5% of the arena width and
moves to wherever you click or tap. The second stays in the center. All three
launchers use the same `App()` composable. Continuous movement, enemies,
collisions, and the survival timer will be added in separate lessons.

## Project map

| Location | Purpose |
| --- | --- |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt` | Shared Compose screen; our first learning entry point |
| `shared/src/iosMain/` | Exposes the Compose screen to UIKit |
| `androidApp/` | Android application and activity |
| `desktopApp/` | Desktop window and entry point |
| `iosApp/` | SwiftUI launcher and Xcode project |
| `gradle/libs.versions.toml` | Dependency and plugin versions |

## Run

Open this directory in Android Studio with the Kotlin Multiplatform plugin.
Use its bundled JDK as the Gradle JDK. The first sync needs internet access
to download any missing dependencies.

Desktop:

```sh
./gradlew :desktopApp:run
```

Android: run the `androidApp` module on an emulator or connected device.
To build an APK without launching:

```sh
./gradlew :androidApp:assembleDebug
```

iOS: open `iosApp/NeonArena.xcodeproj` in Xcode, select the `NeonArena`
scheme and an iOS simulator, then run. Its build phase builds the shared
Kotlin framework automatically. A physical device requires your signing team
in Xcode. The included iOS targets support Apple Silicon simulators and
ARM64 devices.

If Android Studio asks for an SDK path, use your local Android SDK installation.
`local.properties` is machine-specific and ignored by Git.

## Learning sequence

1. **Draw the arena and player:** learn canvas coordinates and shapes.
2. **Add movement:** represent position and connect input to it.
3. **Introduce the game loop:** update using elapsed time.
4. **Add an enemy:** move it toward the player.
5. **Detect collisions:** compare distances and radii.
6. **Survive 60 seconds:** add the timer, game-over state, and restart.

Each lesson will add one small working change and explain it before we move on.

## How the screen starts

Start with `desktopApp/src/jvmMain/kotlin/com/stefan/neonarena/Main.kt`.
Its `main()` function starts a Compose application, opens a window, and calls
`App()` inside that window.

Then read `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt`.
`@Composable` lets this function describe UI. It now contains a `Canvas`,
which gives us an area for drawing the arena and player.

The Android activity and iOS view controller also call `App()`. That is how
the platforms use the same screen. There is no game loop yet.

## Lesson 1: coordinates and shapes

This describes the original drawing step. The current code includes Stefan's
second player and the input changes explained in lesson 2 below.

Read `App.kt` from top to bottom:

1. The modifiers fill the window, paint its background, and leave space for
   system bars or cutouts with `safeDrawingPadding()`. Another `24.dp` of
   padding separates the arena from the edges. The border marks its boundary,
   and `clipToBounds()` keeps drawing inside it.
2. Inside `Canvas`, `size.width` and `size.height` are the arena's measured
   dimensions in pixels, after padding. The top-left is `(0, 0)`. Increasing
   `x` goes right; increasing `y` goes down.
3. `drawRect` paints the arena. Drawing happens in order, so the circle drawn
   afterward appears on top of that rectangle.
4. `Offset(size.width / 2f, size.height / 2f)` describes the center. The `f`
   makes each divisor a `Float`, the number type used by drawing coordinates.
5. `18.dp.toPx()` converts a density-independent radius to the pixels the
   canvas expects. The circle's diameter is twice its radius.
6. `drawCircle` uses that position, radius, and color to draw the player.

Resizing the desktop window changes the canvas size. Compose draws it again
using the new dimensions, so the circle stays centered. This is layout and
drawing; we will introduce position state and movement in the next lesson.

Try changing `x = size.width / 2f` to `x = size.width * 0.25f`, then run again.
Predict the result first: the circle should move halfway from its current
position toward the left edge, while keeping the same vertical position.
Restore the centered position when you finish. As a second experiment, change
the radius from `18.dp` to `28.dp`.

Reference: [Compose drawing and coordinates](https://developer.android.com/develop/ui/compose/graphics/draw/overview).

## Lesson 2: position state and input

A drawing variable calculated from the canvas size always gives the same
position at that size. To move the player in response to input, we need to
store a position that can change.

At the start of `App()`, `tappedPosition` uses `remember` and `mutableStateOf`.
`remember` keeps the state through recomposition; `mutableStateOf` makes changes
observable to Compose. Its type is `Offset?`, so it can start as `null` before
the player has clicked or tapped. It resets when this composition is recreated
or the app is relaunched.

`pointerInput(Unit)` installs a gesture handler on the arena. `Unit` is a
constant key, so position changes do not restart the handler.
`detectTapGestures` gives us the click or tap position in local pixels. Because
the handler is after the padding modifier, its coordinates match the canvas.
We assign that position to `tappedPosition`.

Inside the drawing block, `tappedPosition ?: Offset(...)` uses the stored
position if it exists, or the original starting position otherwise. Reading
this state in the drawing block lets Compose schedule another draw when it
changes. The second circle still uses your original position calculation.

Run the desktop app and click several places inside the arena. The first
circle should jump to each click while the second stays centered. Click the
second circle to see them overlap: it is drawn last, on top of the first.

For an experiment, make the second circle a different color. Then try changing
the assignment to `tappedPosition = Offset(position.x, size.height / 2f)`.
Inside `pointerInput`, `size.height` is an integer pixel count. Dividing by
`2f` gives the floating-point value `Offset` expects. Predict which direction
the first player can move before running it.

Positions are currently stored in pixels. Resizing can leave a clicked position
outside the arena, and clicking near an edge can clip part of the circle.
We will handle arena boundaries when we add continuous movement. For now,
this lesson connects input to stored position and drawing.

References: [Compose state](https://developer.android.com/develop/ui/compose/state)
and [tap input](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/tap-and-press).
