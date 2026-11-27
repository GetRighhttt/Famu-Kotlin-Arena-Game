import SwiftUI

/**
 * Starts the iOS app and places the shared game screen in a SwiftUI window.
 *
 * The shared Compose layout handles safe drawing insets, so the SwiftUI host
 * allows it to extend across the full screen.
 *
 * - Author: Stefan Bayne
 * - Since: 2026-11-27
 */
@main
struct NeonArenaApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
        }
    }
}
