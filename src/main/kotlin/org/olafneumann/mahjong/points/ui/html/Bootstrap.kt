package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.label
import kotlinx.html.title
import org.olafneumann.mahjong.points.util.nextHtmlId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.reflect.KMutableProperty0

fun TagConsumer<HTMLElement>.bsButton(label: String, onClickFunction: (Event) -> Unit = {}) =
    button(classes = "btn btn-primary", type = ButtonType.button) {
        +label
        this.onClickFunction = onClickFunction
    }

fun <T> TagConsumer<HTMLElement>.radioGroup(
    label: String,
    items: List<T>,
    property: KMutableProperty0<RadioGroup<T>>? = null,
    action: (T) -> Unit = {},
) = capture2(property, { RadioGroup(it, items) }) {
    div {
        label { +label }
        div(classes = "btn-group btn-group-sm") {
            items.forEach { item ->
                val radioId = (label + item.toString()).asId
                input(type = InputType.radio, classes = "btn-check", name = label.asId) {
                    autoComplete = false
                    id = radioId
                    onInputFunction = {
                        val input = it.target!! as HTMLInputElement
                        if (input.checked) {
                            action(item)
                        }
                    }
                }
                label(classes = "btn btn-outline-primary") {
                    htmlFor = radioId
                    +item.toString()
                }
            }
        }
    }
}

fun TagConsumer<HTMLElement>.checkbox(
    label: String,
    property: KMutableProperty0<HTMLInputElement>? = null,
    action: (Boolean) -> Unit = {}
) =
    capture(property) {
        div(classes = "form-check") {
            input(type = InputType.checkBox, classes = "form-check-input") {
                value = ""
                id = label.asId
                onInputFunction = { action((it.target!! as HTMLInputElement).checked) }
            }
            label(classes = "form-check-label") {
                htmlFor = label.asId
                +label
            }
        }
    }

fun TagConsumer<HTMLElement>.verticalSwitch(label: String, action: (Event) -> Unit) {
    val htmlId = nextHtmlId
    div(classes = "text-center mr-vertical-switch") {
        div(classes = "form-check form-switch") {
            input(classes = "form-check-input", type = InputType.checkBox) {
                id = htmlId
                onInputFunction = action
            }
        }
        div {
            label(classes = "form-check-label") {
                htmlFor = htmlId
                +"Open"
            }
        }
    }
}

fun TagConsumer<HTMLElement>.closeButton(onCloseButtonClick: (Event) -> Unit) =
    button(classes = "btn-close", type = ButtonType.button) {
        attributes["aria-label"] = "Close"
        title = "Cancel"
        onClickFunction = onCloseButtonClick
    }


private val String.asId: String get() = replace(Regex("\\s+"), "")

class RadioGroup<T> internal constructor(
    elements: List<HTMLInputElement>,
    items: List<T>,
) {
    private val inputs: Map<T, HTMLInputElement>

    init {
        inputs = elements
            .filter { it.type == "radio" }
            .mapIndexed { index, radio -> items[index] to radio }
            .toMap()
    }

    fun select(item: T) {
        inputs[item]?.checked = true
    }

    fun selection(): T? = inputs.filterValues { it.checked }
        .map { it.key }
        .firstOrNull()
}
