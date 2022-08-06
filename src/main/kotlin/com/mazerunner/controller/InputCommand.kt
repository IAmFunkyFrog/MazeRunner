package com.mazerunner.controller

import javafx.scene.input.InputEvent

interface InputCommand<ObjectT, EventType : InputEvent> {

    val callback: (ObjectT, EventType) -> Unit

    fun check(inputEvent: EventType): Boolean

    fun getDescription(): String

    override fun toString(): String

    companion object {

        fun <ObjectT, EventType : InputEvent> makeInputCommand(
            callback: (ObjectT, EventType) -> Unit,
            check: (EventType) -> Boolean,
            description: String,
            commandName: String
        ) = object : InputCommand<ObjectT, EventType> {
            override val callback = callback

            override fun check(inputEvent: EventType) = check(inputEvent)

            override fun getDescription() = description

            override fun toString() = commandName

        }

    }

}