package com.stefan.neonarena.game.model

/**
 * Describes whether the current run accepts movement or has ended in a tackle.
 * ArenaState owns transitions; the UI reads this value to show the result.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
enum class RunStatus { Running, Tackled }
