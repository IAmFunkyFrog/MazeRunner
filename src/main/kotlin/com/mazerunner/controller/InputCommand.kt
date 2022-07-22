package com.mazerunner.controller

import javafx.scene.input.InputEvent

interface InputCommand<T> {

    val callback: (T, InputEvent) -> Unit

    fun check(inputEvent: InputEvent): Boolean

    fun getDescription(): String

    override fun toString(): String

}