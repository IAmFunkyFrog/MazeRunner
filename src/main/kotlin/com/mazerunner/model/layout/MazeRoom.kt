package com.mazerunner.model.layout

import javafx.beans.property.SimpleObjectProperty
import java.io.Externalizable

enum class MazeRoomState {
    UNKNOWN,
    SEEN,
    CURRENT,
    VISITED
}

data class MazeRoomStateWithInfo<T>(val info: T?, val mazeRoomState: MazeRoomState)

interface MazeRoom : Externalizable {

    val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>>

}