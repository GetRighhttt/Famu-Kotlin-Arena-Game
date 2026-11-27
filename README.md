# Neon_Kotlin_Arena_Game
Kotlin game development practice.

A Kotlin / Compose Multiplatform game built one learning step at a time.

## Current step: a scrolling field and Rattlers end zone

The field is three viewport heights long. A camera starts at the near end and
advances toward the Rattlers end zone at 32.dp per second. The runner steers
within the visible area at 240.dp per second while a BCU defender chases at
140.dp per second. Yard numbers, goalposts, and RATTLERS! lettering use the
same world coordinates as the field.

Contact stops the run with a tackle. Once the far end is fully revealed,
reaching its goal line scores a touchdown. Either result offers Try again,
which resets players and camera progress. Scrolling runs only during an active
run, so a stopped result screen is intentionally stationary.

## Project map

| Location | Purpose |
| --- | --- |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt` | Connects arena state, input, and frame updates |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/model/RunStatus.kt` | Running, tackled, and touchdown states |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/physics/PlayerCollision.kt` | Circle contact calculation |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/ui/arena/RunResultOverlay.kt` | Result card and restart action |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/state/FieldCamera.kt` | World/viewport coordinates and automatic camera motion |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/state/ArenaState.kt` | Owns both football players, field size, and frame updates |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/physics/DefenderMovement.kt` | Pursuit direction and travel distance |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/rendering/DefenderDrawing.kt` | BCU uniform and heading toward the runner |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/loop/GameLoop.kt` | Frame timing and elapsed-time updates |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/physics/PlayerMovement.kt` | Velocity-based movement within arena bounds |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/ui/arena/Arena.kt` | Canvas layout and arena rendering |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/input/TouchState.kt` | Drag displacement, dead zone, and direction |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/input/TouchInput.kt` | Drag gesture lifecycle and cancellation |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/input/KeyboardState.kt` | Held movement keys and normalized direction |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/input/KeyboardInput.kt` | Keyboard events and arena focus |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/input/ArenaInput.kt` | Reusable tap input modifier extension |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/rendering/PlayerDrawing.kt` | Reusable player drawing extension |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/rendering/FootballFieldDrawing.kt` | Field surface, yard lines, hash marks, and end zones |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/ui/theme/GameColors.kt` | School colors and game surface colors |
| `shared/src/commonMain/kotlin/com/stefan/neonarena/game/model/Player.kt` | Player data and arena boundary calculations |
| `shared/src/commonTest/kotlin/com/stefan/neonarena/game/model/PlayerTest.kt` | Boundary calculation tests |
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

The initial drawing and tap experiments are complete. Each new lesson branches
from the updated `develop`, then merges back after review and checks. `master`
holds stable milestones. Branch names use `bayne/<lesson-name>`. Commits have
no co-author trailers.

| Branch | Lesson |
| --- | --- |
| `bayne/project-setup` | Platform launchers and shared screen |
| `bayne/draw-arena` | Canvas layout and arena drawing |
| `bayne/draw-player` | Circle drawing and coordinates |
| `bayne/tap-input` | Observable position and tap gestures |
| `bayne/player-state` | Player data and arena boundaries |
| `bayne/color-scheme` | FAMU player colors and BCU opposition palette |
| `bayne/game-loop` | Frames, velocity, and elapsed time |
| `bayne/keyboard-controls` | Held keys, focus, and diagonal movement |
| `bayne/touch-controls` | Continuous movement from touch input |
| `bayne/chasing-enemy` | Direction vectors and a chasing BCU defender |
| `bayne/collisions` | Circle contact, tackles, and restart |
| `bayne/end-zone` | Camera scrolling, field markings, and touchdown (current) |
| `bayne/survival-round` | The 60-second objective and restart |
| `bayne/enemy-spawning` | Spawn timing and increasing difficulty |
| `bayne/pause-and-resume` | Pausing time and handling app lifecycle |
| `bayne/game-feedback` | A compact HUD and visual feedback |

Each lesson will add one small working change and explain it before we move on.

## How the screen starts

Start with `desktopApp/src/jvmMain/kotlin/com/stefan/neonarena/Main.kt`.
Its `main()` function starts a Compose application, opens a window, and calls
`App()` inside that window.

Then read `shared/src/commonMain/kotlin/com/stefan/neonarena/App.kt`.
`@Composable` lets this function describe UI. It remembers `ArenaState` and
connects input to frame updates. `Arena()` contains the canvas for drawing the
field, defender, and runner.

The Android activity and iOS view controller also call `App()`. That is how
the platforms use the same screen. `App()` also calls `GameLoop()` to update
player state as frames arrive.

## Lesson 1: coordinates and shapes

This describes the original drawing step before the later input and cleanup
lessons. Drawing now lives in `Arena.kt` and `PlayerDrawing.kt`.

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

This describes the tap experiment before the player-state lesson below.

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

## Lesson 3: player state and arena boundaries

Branch: `bayne/player-state`. This section describes that checkpoint, before
the automatic movement lesson below.

A circle's position describes its center. If the center reaches `x = 0`, half
the circle is outside the arena. A radius of 18 pixels means its center must
stay at least 18 pixels from every edge:

```text
radius <= x <= arena width - radius
radius <= y <= arena height - radius
```

`Player.kt` groups `position` and `radius` in an immutable data class. Both use
pixels. `App()` converts `18.dp` to pixels before creating the player.
`constrainedTo()` uses `coerceIn(minimum, maximum)` to limit each coordinate,
then returns a `copy` with that position. It does not change the original
player. Assigning that copy to Compose state updates what the canvas draws.

For example, in a 200-pixel-wide arena with radius 18, a tap at `x = 199`
produces a center at `x = 182`. The circle's right edge reaches 200 exactly.

In `App.kt`, player state starts as `null` until the arena reports its
measured size. `App()` creates the initial player at 5% of the arena width
and halfway down. On later size changes it constrains the existing position.
A tap also constrains the requested position before storing it.

`Arena()` receives that state and reports events through callbacks. Its
`onSizeChanged` modifier reports the measured canvas bounds. Its
`onArenaTap` modifier extension reports the tap position and bounds together.
The drawing extension reads the player's position and radius.

The player resets when this composition is recreated or the app restarts.

If the arena becomes smaller than a player's diameter, the usual coordinate
range cannot fit the circle. `radiusIn()` temporarily reduces its visible
radius to half the shorter arena dimension. Its stored radius stays the same,
so the circle returns to normal size when space is available. A zero-sized
arena has a visible radius of zero. After resizing, players stay at their
constrained positions rather than returning to an earlier out-of-bounds tap.

Try these checks in the desktop app:

1. Click each corner. The player should stay fully inside.
2. Position it near the bottom-right, then shrink the window. Its center
   should move inward as needed. Enlarge the window and it should stay there.

Experiment: change `18.dp` to `28.dp`. Before running, calculate the maximum
center x-coordinate for a 200-pixel-wide arena with a 28-pixel radius.
The answer is 172. In the app, `dp` is converted to pixels, so the actual pixel
radius depends on display density.

Run the boundary tests with:

```sh
./gradlew :shared:jvmTest
```

## Separating state, input, and drawing

The two-player experiment is complete. The current lesson uses one player,
with `Player` still holding its geometry and boundary rules.

Read the files in this order:

1. `App.kt` owns the observable player state. It decides how taps and size
   changes update that state.
2. `Arena.kt` arranges the canvas, reports events, and draws the scene. It
   receives state and callbacks rather than owning a second copy of state.
3. `ArenaInput.kt` adds `onArenaTap` to `Modifier`. It handles gesture details
   and reports coordinates in the canvas's local pixel space. It keeps the latest callback with `rememberUpdatedState`, while
   `pointerInput(Unit)` keeps a gesture alive through frame updates.
4. `PlayerDrawing.kt` adds `drawPlayer` to `DrawScope`, the receiver inside a
   canvas drawing block. It uses the player's geometry and accepts a color.

An extension adds callable behavior to an existing type without editing that
type. `Modifier.onArenaTap` can be used on another arena, and
`DrawScope.drawPlayer` can draw another player when we need one again.
`Arena` is a composable because it owns layout; `Player` remains a data class
because it owns player geometry.

Experiment: change the call in `Arena.kt` to
`drawPlayer(it, color = Color.Magenta)`. The input and boundary code should
need no changes.

## Package structure

Packages group the game by responsibility beneath `com.stefan.neonarena`:

```text
App.kt                       Shared entry point and composition
 game/
   model/Player.kt           Player geometry and arena boundary rules
   model/RunStatus.kt        Running or tackled state
   loop/GameLoop.kt          Frame timing
   physics/PlayerMovement.kt  Velocity-based movement
   physics/DefenderMovement.kt  Pursuit direction and distance
   physics/PlayerCollision.kt  Circle contact
   state/ArenaState.kt       Runner, defender, and field state
   state/FieldCamera.kt      Viewport and world coordinate conversion
   input/ArenaInput.kt       Pointer input expressed in arena coordinates
   input/KeyboardInput.kt    Keyboard events and focus
   input/KeyboardState.kt    Held keys and direction
   input/TouchInput.kt       Drag gesture events
   input/TouchState.kt       Drag displacement and direction
   rendering/PlayerDrawing.kt  Compose drawing for player geometry
   rendering/DefenderDrawing.kt  BCU body and outline
 ui/
   arena/Arena.kt            Arena screen layout and scene composition
   arena/RunResultOverlay.kt    Tackle result and restart
   theme/GameColors.kt       Shared rendering palette
```

The platform launchers stay in their platform modules or source sets.
`commonTest` mirrors the source packages, so player tests live in
`game/model/PlayerTest.kt`.

This is a small project layout, not a universal game-engine standard. Package
names are lowercase and directories follow those names. Input, model, and
rendering have different owners so later lessons have clear places to grow.
There is no generic utilities package or extra architecture layer. The current
model uses Compose's `Offset` and `Size`; it is shared across the supported
platforms, but is not yet independent of Compose geometry.

See [Kotlin source organization conventions](https://kotlinlang.org/docs/coding-conventions.html#source-code-organization).

## Reconstructed lesson history

The first four lesson branches separate the original combined implementation
into runnable checkpoints: project setup, arena drawing, player drawing, and
tap input. Each checkpoint has code and explanations for just that stage.
`bayne/player-state` builds on them with one player, boundary tests, reusable
input and drawing extensions, and the package structure above.

The earlier two-player and double-tap experiments remain on `master` in their
original commits. The rebuilt `develop` sequence is a teaching progression,
not a claim about the original commit chronology. Future lessons branch from
its latest merge. Select `develop` for the current integrated learning project.

## Color scheme: FAMU and BCU

This describes the palette checkpoint before the defender lesson below.

Branch: `bayne/color-scheme`, starting from `develop`.

The game is inspired by the annual [Florida Classic](https://floridaclassic.org/about/)
between FAMU and Bethune-Cookman in Orlando. The intended direction is an
arcade interpretation of that football rivalry, with FAMU as the player side
and BCU as the opposition. The gameplay direction is a FAMU runner weaving
through BCU football defenders. Scoring and round rules are still open; this
lesson establishes the palette and field appearance. The original survival-game
lesson sequence is a learning roadmap that we can adapt as football mechanics take shape.

| Color | Web value | Game use |
| --- | --- | --- |
| FAMU Athletics orange | `#F4811F` | Player |
| FAMU Athletics green | `#008344` | Arena boundary |
| BCU Blood Maroon | `#860038` | Defined for future opposition |
| BCU Sun Gold | `#FDB913` | Defined for future opposition accents |

FAMU values use the HEX (WEB) entries on page 33 of the
[FAMU Athletics style guide](https://famuathletics.com/pdf9/4488310.pdf).
These are the athletics palette; university publications can use other
orange and green specifications. BCU values use the primary colors on page 6
of its [university brand guide](https://www.cookman.edu/creative-services/documents/b-cu-brand-guidelines.pdf).

`ui/theme/GameColors.kt` owns these colors and two custom dark green surfaces:
`#07160D` for the background and `#102C1D` for the arena. These surfaces are
chosen for the game, not official school colors. BCU colors are defined but
no opposition is drawn yet. Field markings use the custom pale color `#CEDFD2`.

`Arena` reads the surface and border colors. `drawPlayer` defaults to FAMU
orange and still accepts a color override. The `Player` data class and input
code do not need school-color properties.

In `Color(0xFFF4811F)`, the first `FF` is full opacity. The remaining six
hexadecimal digits are the web color's red, green, and blue channels.

Experiment: change the call to `drawPlayer(it, color = GameColors.bcuMaroon)`
in `Arena.kt`, then restore it. The player changes sides visually without any
change to its geometry or input handling.

## Drawing the football field

`game/rendering/FootballFieldDrawing.kt` adds `drawFootballField()` to
`DrawScope`. The arena calls it before `drawPlayer()`, so the runner appears
on top of the field markings.

The field is divided into ten equal sections. `size.height / sections` gives
one section's height in pixels. For line indices 1 through 9, multiplying that
height by the index gives the y-coordinate. Each line runs from x = 0 to the
canvas width, with the same y-coordinate at both ends. That makes it horizontal.

Positions scale with the canvas size; line thickness uses `1.dp.toPx()` so it
stays consistent across display densities. The markings are visual and do not
affect player position or collisions.

Experiment: change `sections` from 10 to 20. Predict how many interior lines
will be drawn and how far apart they will be before running the app.

## Lesson 7: the game loop

This describes the automatic-movement checkpoint before keyboard controls.

Branch: `bayne/game-loop`, created from `develop` after the color-scheme PR
was merged. The runner now starts centered at 85% of the field height and
moves toward the top. Negative y goes up in canvas coordinates.

A game loop repeatedly updates the world. Compose handles drawing from state;
our loop updates the player's state once per frame:

```text
wait for a frame -> measure elapsed time -> update position -> Compose draws
```

`game/loop/GameLoop.kt` uses `LaunchedEffect(running)` to own a coroutine.
`withFrameNanos` suspends until the frame clock supplies a timestamp. The first
frame establishes the starting time. Subtracting consecutive timestamps gives
elapsed nanoseconds; dividing by 1,000,000,000 converts them to seconds.

`rememberUpdatedState` keeps the update callback current without restarting the
loop on each recomposition. Setting `running` to false or removing the loop from
composition cancels it. `App()` enables it after the player and field size exist.
This lesson does not yet connect `running` to app background or window focus;
explicit pause and resume remains a later lesson.

`game/physics/PlayerMovement.kt` applies:

```text
new position = current position + velocity * elapsed seconds
```

Velocity is an `Offset` in pixels per second. `App()` converts `120.dp` to pixels
and uses it as the upward speed. At 60 frames per second, each update moves
about 2.dp; at 120 frames per second, each moves about 1.dp. Over one second,
both move about 120.dp, subject to the boundary.

The loop caps each update at 0.05 seconds. A long stall therefore advances at
most 6.dp on the next update instead of jumping across the field. This
intentionally discards excess time; under sustained rates below 20 frames per
second the simulation slows down. We are using a variable timestep for this
lesson, not a fixed-timestep physics simulation.

The movement extension returns a new `Player` and constrains it to the arena.
At the top edge, the player stops one radius from the boundary. There is no
bounce, score, or touchdown state yet. Clicking elsewhere moves the player
there, and automatic movement continues from that point.

The input modifier also uses `rememberUpdatedState` now. Restarting its gesture
handler whenever an update callback changes could cancel taps during continuous
frame updates. A constant `pointerInput(Unit)` key keeps an in-progress gesture
alive while the callback stays current.

Try running the desktop app. Watch the runner move upward, tap near the bottom
to repeat the run, and resize the window while it moves. For an experiment,
change `120.dp` to `240.dp`. Predict how long the same journey will take.
Then try `velocity = Offset(runningSpeed, 0f)` to move horizontally.

The movement tests compare one-second travel at 30 and 120 updates, zero elapsed
time, and stopping at the top edge. Run them alongside the boundary tests with
`./gradlew :shared:jvmTest`.

References: [Compose effects](https://developer.android.com/develop/ui/compose/side-effects)
and [Compose runtime frame APIs](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary).

## Lesson 8: keyboard controls

This describes the keyboard-only checkpoint before the touch lesson below.

Branch: `bayne/keyboard-controls`, created from local `develop` after merging
the completed `bayne/game-loop` lesson locally. The game-loop merge has not
been pushed to remote `develop` yet.

The runner now stays still until you hold WASD or an arrow key. Stefan increased
the running speed from 120.dp to 160.dp per second. Each frame
uses the held-key direction to calculate velocity:

```text
held keys -> direction -> velocity = direction * speed
position += velocity * elapsed seconds
```

`game/input/KeyboardState.kt` owns a set of physical keys. `press()` adds a
movement key; `release()` removes it. A set makes repeated key-down events
harmless. Each alias stays separate, so releasing W while Up is held keeps
upward movement active. Other keys return false so they can continue through
Compose's event handling.

`direction()` combines the four directions. Up is `(0, -1)`, right is `(1, 0)`,
and no keys gives `(0, 0)`. Opposing keys cancel. W and D together initially
give `(1, -1)`, whose length is about 1.414. Dividing by that length gives
approximately `(0.707, -0.707)`, a vector of length one. Multiplying that vector
by speed keeps diagonal travel as fast as straight travel.

`game/input/KeyboardInput.kt` adds `Modifier.keyboardControls(state)`. It
requests keyboard focus after composition and again on pointer presses,
without consuming the pointer event. `onKeyEvent` routes key-down and key-up
events to the state. Ctrl, Command, and Alt combinations are left to other
handlers and clear movement. Shift does not add a sprint boost.

Held keys are cleared when the arena loses focus, when its window loses focus,
and when the modifier leaves composition. This prevents a missing key-up event
from leaving the runner moving. The game loop remains active, but a zero
direction produces zero velocity. Full pause/resume behavior is a later lesson.

`App()` remembers the keyboard state and passes it to the modifier. Its existing
frame callback now reads `keyboardState.direction()` rather than using a
constant upward velocity. Player movement and arena bounds use the same
calculation as the previous lesson. The renderer only adds a description of
the available controls.

Run the desktop app and check:

1. Hold W or Up, then release it. The runner should move up and stop.
2. Hold W and D together. It should move diagonally at the same overall speed.
3. Hold A and D together, then release A. It should stop, then move right.
4. Hold a movement key, switch to another app, and release the key there.
   Return to the game; the runner should remain still until a new key press.
5. Click the field, then use the keys again. Tap positioning and keyboard
   movement should work together.

The shared input code also supports hardware keyboards on platforms that
provide these key events. Phones still have tap positioning; continuous touch
movement comes next. The current lesson's automated tests check key mapping,
aliases, repeats, opposite directions, diagonal speed, and clearing state.
Window focus and real hardware input need the desktop checks above.

Experiment: change `runningSpeed` from `160.dp` to `320.dp`. The direction
vector should stay the same; only the distance covered each frame changes.

References: [Compose key events](https://developer.android.com/develop/ui/compose/touch-input/keyboard-input/commands)
and [requesting focus](https://developer.android.com/develop/ui/compose/touch-input/focus/request-focus).

## Lesson 9: touch controls

Branch: `bayne/touch-controls`, created from local `develop` after merging the
completed keyboard-controls lesson locally. The game-loop and keyboard lesson
merges have not been pushed to remote `develop` yet.

Drag on the field to steer. The initial pointer-down position becomes the
anchor once Compose recognizes a drag. Direction is the normalized displacement
from that anchor to the current finger position:

```text
displacement = current pointer position - initial pointer position
direction = displacement / displacement length
velocity = direction * running speed
```

For example, starting at `(200, 300)` and moving to `(230, 260)` gives `(30, -40)`.
Its length is 50, so direction is `(0.6, -0.8)`. At 240.dp per second, the
velocity components are 144.dp per second right and 192.dp per second up.
Their combined speed is still 240.dp per second.

`game/input/TouchState.kt` owns the anchor, displacement, and drag status.
An 8.dp dead zone, converted to pixels in `App()`, returns zero direction for
small displacements. Returning your finger to the anchor stops movement while
keeping the drag active. Holding your finger away from the anchor maintains
the direction even when the finger is no longer moving. Greater drag distance
does not add speed in this lesson.

`game/input/TouchInput.kt` connects `detectDragGestures` to that state. The
extended drag-start callback provides the original down event, rather than
anchoring at the later position where drag recognition passes touch slop.
`onDrag` consumes movement, which cancels the separate tap detector. Release
and cancellation both clear touch input. Losing window focus or leaving
composition also clears it. Tap and drag detectors use separate `pointerInput`
modifiers so each detector can receive events.

`Arena` attaches touch input after padding, in the same local coordinate system
as its drawing and tap input. `App()` chooses touch direction during an active
drag, including zero direction inside the dead zone. Otherwise it uses keyboard
direction. If keyboard keys are still held at touch release, keyboard movement
resumes. Stefan increased running speed to 240.dp per second after trying the device
build. Both paths use the same speed, game loop, movement extension, and bounds.

To try it on desktop, hold the mouse button and drag within the field. On a
phone, use one finger. A short tap still repositions the player; a recognized
drag steers it without teleporting to the gesture's starting point.

Check an upward drag, a diagonal drag, holding the finger stationary away from
the anchor, returning to the dead zone, releasing, and tapping afterward.
The automated state tests cover relative coordinates, dead-zone boundaries,
normalization, holding direction, new anchors, and clearing late updates.
Gesture recognition and cancellation also need a device or desktop check.

Experiment: increase the dead zone from `8.dp` to `24.dp`. Predict how far your
finger must move from its starting point before the runner begins moving.

Reference: [Compose gesture detection](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/understand-gestures).

### Connected-device verification

Installed and tested the debug build on a connected Samsung SM-G990U.
Screenshot-based position checks confirmed tap positioning, upward and diagonal
drag steering, stopping on release, boundary constraints, holding a direction,
returning to the dead zone, and gesture cancellation. These gesture checks used
the 160.dp/s build. After Stefan requested 240.dp/s, all platform build checks
and 22 unit tests passed again, and the updated build was installed and opened
on the same device for hands-on speed tuning.

## Lesson 10: a chasing BCU defender

This describes the pursuit checkpoint before the collision lesson below.

Branch: `bayne/chasing-enemy`, created from local `develop` after merging the
completed touch lesson locally. Those lesson merges have not been pushed to
remote `develop` yet.

The runner starts near the bottom at 85% of the field height. A maroon defender
with a gold outline starts near the top at 15%. Runner speed stays at 240.dp
per second; defender speed is 140.dp per second. Both are converted to pixels
in `App()`. The slower defender gives the runner room to evade it.

### Direction toward a target

`game/physics/DefenderMovement.kt` adds `Player.chasing(...)`. Both the runner
and defender are football players, so they reuse the existing `Player` data
class. The difference is how each one's direction is chosen:

```text
runner direction   = keyboard or touch input
defender direction = (runner position - defender position) / distance
```

For a defender at `(100, 100)` and runner at `(400, 500)`, subtracting positions
gives `(300, 400)`. Its length is 500. Dividing gives `(0.6, 0.8)`, a direction
of length one pointing right and down. At 140 pixels per second over 0.02
seconds, the defender moves 2.8 pixels: 1.68 right and 2.24 down.

Subtracting the positions gives both direction and distance. Normalizing
removes the distance, so being farther away does not make the defender faster.
Each frame recalculates direction toward the runner's newest position.

`travel = minOf(speed * elapsedSeconds, distance)` prevents crossing past the
target on arrival. If the centers already match, the function returns without
normalizing a zero-length vector. It then uses the existing arena constraints.

### State and drawing

`game/state/ArenaState.kt` owns the player objects and field size. It creates
both players after a nonempty measurement, constrains both after a resize,
and updates them each frame. The runner moves first; the defender then chases
that updated position. Tapping repositions only the runner. Input and game-loop
code remain separate from these world updates.

`App()` remembers `ArenaState` and passes converted sizes and speeds into its
updates. If density changes create a new state, `key(arenaState)` recreates the
arena's measurement callback so the new state receives its field size.

`game/rendering/DefenderDrawing.kt` reuses `drawPlayer` for the maroon body and
adds a gold stroke inside its radius. The field is drawn first, the defender
second, and the FAMU runner last. On complete overlap, the runner covers the
defender. Move the runner again and the defender becomes visible as it follows.

This lesson has pursuit but no collision outcome. The defender can overlap the
runner and will wait at its center if the runner stays still. Tackles, losing
a run, and restart behavior come in later lessons.

Try standing still to watch the approach, then drag or use keys to evade it.
Tap to another part of the field and watch the defender change direction.

Experiment: change `defenderSpeed` in `App.kt` from `140.dp` to `200.dp`.
Predict whether the defender can catch a runner moving straight at 240.dp/s,
and how turning or reaching a boundary changes the situation.

Pursuit tests check diagonal direction, arrival, matching centers, target
changes, frame-rate consistency, and bounds. Arena-state tests check initial
placement, tap ownership, update order, idle pursuit, and resizing both players.

### Defender verification

All 33 unit tests passed, as did the desktop JVM build, Android debug APK build,
and shared Kotlin compilation for the iOS ARM64 simulator. On the connected
Samsung SM-G990U, screenshot-based position checks confirmed the maroon defender
was visible, advanced toward the runner, and turned toward a new target after
a tap. The debug build was installed and reopened on that device for testing.

## Lesson 11: tackles and restart

This describes the collision checkpoint before the scrolling-field lesson.

Branch: `bayne/collisions`. The completed chasing lesson was saved as commit
`8cd8a98` and merged into local `develop` before creating this branch. These
local lesson merges have not been pushed to remote `develop` yet.

Two circles touch when the distance between their centers is at most their
combined radii. For radii 18 and 18, first contact is at distance 36:

```text
distance <= runner radius + defender radius
```

`game/physics/PlayerCollision.kt` adds `Player.touches(other, arenaSize)`.
The code compares squared values instead of computing a square root:

```text
dx * dx + dy * dy <= combinedRadius * combinedRadius
```

Both quantities are nonnegative, so this gives the same result. Equality counts
as contact. The radii come from `radiusIn(arenaSize)`, matching the circles
actually drawn when the arena is very small.

`game/model/RunStatus.kt` names the two current states: `Running` and `Tackled`.
`ArenaState` owns their transitions. It checks contact after tap positioning,
after resizing, and before and after the movement steps in a frame. Checking
immediately after the runner moves prevents the defender's later movement from
hiding that contact. These are discrete circle checks; this lesson does not
calculate continuous contact along an entire movement path.

When contact occurs, status becomes `Tackled`. `isRunning` becomes false, the
frame loop is cancelled, and field input modifiers are removed. Removing input
also clears stored keyboard and touch state. State methods independently reject
movement while tackled, so an extra callback cannot move a stopped run.

`ui/arena/RunResultOverlay.kt` shows a compact result card above the dimmed field.
Its Try again action uses Compose's clickable behavior and button semantics,
so it supports tapping and keyboard activation. The arena stays measured under
the card; its players stop where contact was detected.

Restart clears recorded input, resets status, and places both players at their
initial positions using the current field size. The frame loop starts with a
fresh timestamp, so time spent on the result screen does not become a movement
jump. If a field is too small to separate the players, contact is detected again
rather than allowing overlapping spawns to continue.

Try leaving the runner still until the defender reaches it. The Tackled card
should appear. Dragging or tapping the field should not move either player.
Choose Try again and evade the defender using touch or keyboard input.

Experiment: change the player radius from `18.dp` to `24.dp`. Predict the new
center distance that counts as contact. At a display density of one, the sum is
48 pixels; other densities scale both radii consistently.

Tests cover exact contact, a small gap, diagonal distance, overlap, visible radii,
frame contact, input after a tackle, restart, and resize-induced contact.

### Tackle verification

All 43 unit tests passed, along with desktop and Android builds and shared
Kotlin compilation for the iOS ARM64 simulator. On the connected Samsung,
verified the tackle result card, a frozen field despite drag input, restart,
and a second pursuit/tackle cycle. Inspected the result-card screenshot for
readable text and button layout. The debug build is installed on that device.

## Lesson 12: scrolling a world through a viewport

Branch: `bayne/end-zone`. The open remote PR stack is unchanged. This lesson
starts from the collision merge in local `develop` and is not committed yet.

The earlier desktop build had no camera code. This lesson creates a field
three times the visible canvas height, then shows only one screen-sized part
of it. This is a rendering lesson: world positions describe where things are
on the whole field; screen positions describe where the viewport draws them.

`game/state/FieldCamera.kt` stores the viewport size and its top edge in world
coordinates, `offsetY`. At the start, this is `fieldHeight - viewportHeight`,
which shows the near end. Moving the camera toward zero reveals the far end:

```text
screenY = worldY - cameraY
cameraY -= scrollSpeed * elapsedSeconds
```

For a stationary world line at y = 1400, a camera at y = 1200 draws it at 200.
After the camera advances to 1168, that line is drawn at 232: it moves downward
by 32 pixels on screen. The line's world coordinate did not change.

`Arena` applies `translate(top = -camera.offsetY)` to the field and both player
drawings. Its canvas is clipped to the viewport, so world content outside the
visible area stays hidden. Collision checks continue to compare world positions.
Taps use the reverse conversion, `worldY = screenY + cameraY`.

`ArenaState.update()` advances the camera and carries the runner forward by the
same world distance. That keeps an idle runner at the same screen position while
the ground passes beneath it. Steering adds movement inside the visible bounds.
The defender stays in world coordinates and pursues the runner's current world
position; it is not carried forward by the camera.

Camera speed is 32.dp/s, separate from the runner's 240.dp/s and defender's
140.dp/s. These become pixels in App before entering the calculations. A faster
runner cannot reveal the hidden field early because its position is constrained
to the current viewport. Tapping remains a learning shortcut for repositioning
within that viewport.

### Field markings and results

`FootballFieldDrawing.kt` draws yard numbers on both sides in the sequence
10, 20, 30, 40, 50, 40, 30, 20, 10. Their y-coordinates use fractions of the
full playing field, so text and lines scroll together. The end zones are shaded
green. The far end says RATTLERS! and has gold uprights, a crossbar, and a stem.

Two columns of short hash marks divide each ten-yard interval into single yards.
A thin line crosses the field at five yards. Each mark uses the same camera
translation as the numbers, so their spacing stays consistent while scrolling.

Each player is drawn around a local center. The renderer scales the uniform to
the player radius, then places it at the world position. The BCU helmet points
toward the runner. FAMU faces upfield with the football tucked under one arm.
Tackle detection still uses the radius around each center.

When camera offset reaches zero, the far end zone is fully visible. The runner
scores when its leading circle edge reaches that goal line. Contact takes
priority if the defender is touching it at the same update. Both Touchdown and
Tackled stop the loop and use `RunResultOverlay` for restart. Restart resets the
camera to the near end as well as resetting both players and stored input.

The field markings are an arcade representation of football. This lesson does
not model a ball, official scoring rules, downs, or goalpost collisions.

### Experiments and verification

Try changing only `scrollSpeed` from `32.dp` to `48.dp`. Yard lines should pass
faster without changing steering speed. Then restore it and change only
`runningSpeed`; steering should change while scrolling stays at the same pace.

Camera tests cover coordinate conversion, scroll direction, frame-rate
consistency, progress on resize, bounds, and reset. Scrolling-arena tests cover
carrying the runner, tap conversion, hidden-world limits, touchdown, and tackle
priority. Earlier one-screen arena tests use `fieldScreens = 1f` so they continue
testing their original collision geometry.

Desktop rendering tests in `FieldRenderingTest.kt` drive real Compose frame
clock timestamps through App and check that a yard line moves about 32 pixels
in one second. They also render the far end zone and check for gold goalposts
and lettering. Previews are saved under `desktopApp/build/reports/field-camera`.
Run these with `./gradlew :desktopApp:jvmTest`.

Verification: all 54 shared tests and both desktop rendering tests passed.
Desktop and Android builds and shared iOS compilation also passed. The real
App rendering test measured approximately 32 pixels of yard-line movement over
one second at density 1. Desktop-sized screenshots confirmed numbered yard
lines, goalposts, and RATTLERS! lettering. The desktop app was reopened with
this build. Scrolling pauses on a tackle or touchdown until Try again.
