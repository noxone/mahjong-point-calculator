package org.olafneumann.mahjong.points

import kotlinx.browser.document
import kotlinx.browser.window
import org.olafneumann.mahjong.points.ui.components.HandComponent
import org.olafneumann.mahjong.points.ui.components.OptionsComponent
import org.olafneumann.mahjong.points.ui.components.ResultComponent
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.components.TileSelectionComponent
import org.olafneumann.mahjong.points.ui.controls.MainPage
import org.olafneumann.mahjong.points.ui.js.asJQuery
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.js.json

fun main() {
    window.onload = { initMahjongPointCalculator() }
}

/* the caught exception is generic to really catch all exceptions */
@Suppress("TooGenericExceptionCaught")
private fun initMahjongPointCalculator() {
    try {
        initMahjongPointCalculatorUnsafe()
    } catch (exception: Exception) {
        console.error(exception.stackTraceToString())
        window.alert("Unable to initialize RegexGenerator: ${exception.message}")
    }
}

private fun initMahjongPointCalculatorUnsafe() {
    MainPage.translate(document.getElement("mr_main"))

    val tilesDiv = document.getElement<HTMLDivElement>("mr_tiles")
    val selectedTilesDiv = document.getElement<HTMLDivElement>("mr_selected_tiles")
    val optionsDiv = document.getElement<HTMLDivElement>("mr_options")
    val resultDiv = document.getElement<HTMLDivElement>("mr_result")

    val model = UIModel()

    TileSelectionComponent(parent = tilesDiv, model = model)
    OptionsComponent(parent = optionsDiv, model = model)
    HandComponent(parent = selectedTilesDiv, model = model)
    ResultComponent(parent = resultDiv, model = model)

    model.start()

    window.setTimeout({
        val loading = document.getElement<HTMLElement>("mr_loading")
        loading.asJQuery()
            .fadeOut(json("duration" to Constants.INTRO_FADE_DURATION, "complete" to { loading.asJQuery().remove() }))
    }, 1)
}

