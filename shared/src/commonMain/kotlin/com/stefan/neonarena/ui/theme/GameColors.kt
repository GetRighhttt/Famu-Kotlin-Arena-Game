package com.stefan.neonarena.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Defines the FAMU player palette and BCU opposition palette for rendering.
 *
 * School colors use the published web values from FAMU Athletics and BCU's
 * university brand guide. The dark green surfaces are game-specific shades.
 * Keeping colors here lets input and player geometry stay independent of them.
 *
 * Sources: [FAMU Athletics](https://famuathletics.com/pdf9/4488310.pdf),
 * [BCU brand guide](https://www.cookman.edu/creative-services/documents/b-cu-brand-guidelines.pdf).
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
object GameColors {
    val famuOrange = Color(0xFFF4811F)
    val famuGreen = Color(0xFF008344)
    val bcuMaroon = Color(0xFF860038)
    val bcuGold = Color(0xFFFDB913)

    val footballLeather = Color(0xFF854425)
    val background = Color(0xFF07160D)
    val arenaSurface = Color(0xFF184930)
    val turfBand = Color(0xFF1C5237)
    val cream = Color(0xFFFBF0D7)
    val rail = Color(0xFF344A3E)
    val fieldLine = Color(0xFFCEDFD2)
}
