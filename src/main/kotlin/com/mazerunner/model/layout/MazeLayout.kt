package com.mazerunner.model.layout

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import java.io.Externalizable

enum class MazeLayoutState {
    INITIALIZED,
    GENERATED
}

interface MazeLayout : Externalizable {

    val stateProperty: SimpleObjectProperty<MazeLayoutState>

    fun getRooms(): ObservableList<MazeRoom>

    fun getDoors(): ObservableList<MazeDoor>

}