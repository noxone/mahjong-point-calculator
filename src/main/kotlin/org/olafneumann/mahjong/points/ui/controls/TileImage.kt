package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.html.injecting
import org.olafneumann.mahjong.points.ui.html.mrTile
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

class TileImage private constructor(
    private val outer: HTMLDivElement,
    private val inner: HTMLDivElement,
    tile: Tile?
) {
    var tile: Tile? = null
        set(value) {
            field = value
            setTileName(tile?.name)
            activateBackground(tile != null)
            showTile(tile != null)
            if (value == null) {
                backside = false
            }
        }

    var backside: Boolean
        get() = outer.classList.contains(CLS_BACKSIDE)
        set(value) {
            outer.classList.toggle(CLS_ROTATED, value)
            window.setTimeout({ outer.classList.toggle(CLS_BACKSIDE, value) }, ROTATION_ANIMATION_DURATION / 2)
        }

    var selectable: Boolean
        get() = !outer.classList.contains(CLS_NOT_SELECTABLE)
        set(value) {
            outer.classList.toggle(CLS_NOT_SELECTABLE, !value)
        }

    var isLastTileInRow: Boolean
        get() = outer.classList.contains(CLS_BS_FLEX_SHRINK_0)
        set(value) { outer.classList.toggle(CLS_BS_FLEX_SHRINK_0, value) }

    fun blinkForAlert() {
        toggleAlert(true)
        window.setTimeout({ toggleAlert(false) }, BLINK_DURATION)
    }

    private fun toggleAlert(enabled: Boolean) {
        outer.classList.toggle(CLS_ALERT, enabled)
    }


    private fun activateBackground(enabled: Boolean) =
        outer.classList.toggle(CLS_BACKGROUND, enabled)

    private fun showTile(show: Boolean) =
        outer.classList.toggle(CLS_BS_D_NONE, !show)

    private fun setTileName(name: String?) {
        inner.mrTile = name ?: ""
    }

    init {
        this.tile = tile
    }

    companion object {
        private const val CLS_BACKSIDE = "mr-tile-backside"
        private const val CLS_ROTATED = "mr-tile-rotated"
        private const val CLS_BACKGROUND = "mr-tile-background"
        private const val CLS_ALERT = "mr-alert"
        private const val CLS_NOT_SELECTABLE = "not-selectable"
        private const val CLS_BS_D_NONE = "mr-invisible"
        private const val CLS_BS_FLEX_SHRINK_0 = "flex-shrink-0"

        private const val ROTATION_ANIMATION_DURATION = 500
        private const val BLINK_DURATION = 920

        fun TagConsumer<HTMLElement>.tileImage(tile: Tile?, onClickFunction: (Event) -> Unit = {}): TileImage {
            var outer: HTMLDivElement by Delegates.notNull()
            var inner: HTMLDivElement by Delegates.notNull()
            injecting { outer = it as HTMLDivElement }
                .div(classes = "mr-tile") {
                    injecting { inner = it as HTMLDivElement }
                        .div(classes = "mr-tile-face") {
                        }
                    this.onClickFunction = onClickFunction
                }
            return TileImage(outer = outer, inner = inner, tile = tile)
        }
    }
}
