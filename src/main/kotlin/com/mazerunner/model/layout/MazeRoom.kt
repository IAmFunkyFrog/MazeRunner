package com.mazerunner.model.layout

import javafx.beans.property.SimpleObjectProperty

enum class MazeRoomState {
    UNKNOWN,
    SEEN,
    CURRENT,
    VISITED
}

data class MazeRoomStateWithInfo<T>(val info: T?, val mazeRoomState: MazeRoomState)

interface MazeRoom {

    val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>>

}