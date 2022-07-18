package com.mazerunner.model.layout

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class MazeLayoutImpl(
    rooms: List<MazeRoom> = emptyList(),
    doors: List<MazeDoor> = emptyList(),
    mazeLayoutState: MazeLayoutState = MazeLayoutState.INITIALIZED
) : MazeLayout {

    private val rooms  = FXCollections.observableArrayList(rooms)
    private val doors = FXCollections.observableArrayList(doors) // FIXME: must check that all doors rooms there are in rooms collection

    override val stateProperty: SimpleObjectProperty<MazeLayoutState> = SimpleObjectProperty(mazeLayoutState)

    override fun getRooms(): ObservableList<MazeRoom> = rooms

    override fun getDoors(): ObservableList<MazeDoor> = doors

    override fun addRooms(vararg mazeRooms: MazeRoom) {
        rooms.addAll(mazeRooms)
    }

    override fun addDoors(vararg mazeDoors: MazeDoor) {
        mazeDoors.forEach {
            if(!rooms.contains(it.getRooms().first) || !rooms.contains(it.getRooms().second)) throw RuntimeException("Rooms from door are not presented in layout")
        }

        doors.addAll(mazeDoors)
    }
}