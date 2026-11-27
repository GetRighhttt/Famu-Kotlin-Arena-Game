import SwiftUI
import UIKit
import NeonShared

/**
 * Adapts the Kotlin Compose view controller for use inside SwiftUI.
 *
 * makeUIViewController calls the exported Kotlin entry point in NeonShared.
 * Compose owns screen updates, so updateUIViewController has no work here.
 *
 * - Author: Stefan Bayne
 * - Since: 2026-11-27
 */
struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
