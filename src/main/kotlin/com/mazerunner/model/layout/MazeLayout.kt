package com.mazerunner.model.layout

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList

enum class MazeLayoutState {
    INITIALIZED,
    GENERATED
}

interface MazeLayout {

    val stateProperty: SimpleObjectProperty<MazeLayoutState>

    fun getRooms(): ObservableList<MazeRoom>

    fun getDoors(): ObservableList<MazeDoor>

    fun addRooms(vararg mazeRooms: MazeRoom) // FIXME redundant method

    fun addDoors(vararg mazeDoors: MazeDoor) // FIXME redundant method

}