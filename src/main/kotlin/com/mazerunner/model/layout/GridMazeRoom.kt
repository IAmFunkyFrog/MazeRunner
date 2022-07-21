package com.mazerunner.model.layout

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

// GradMazeRoom can have 4 borders
// There are (each border coded as binary number):
// 0001 - top border
// 0010 - right border
// 0100 - bottom border
// 1000 - left border
//
// We can unite border with bitwise OR operation

class GridMazeRoom(
    val x: Int,
    val y: Int,
) : MazeRoom {

    override val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>> = SimpleObjectProperty(MazeRoomStateWithInfo<Any>(null, MazeRoomState.UNKNOWN))

    val borderProperty = SimpleIntegerProperty(15)
}