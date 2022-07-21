package com.mazerunner.model.layout

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.IOException
import java.io.ObjectInput
import java.io.ObjectOutput

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

    override fun writeExternal(objectOutput: ObjectOutput?) {
        objectOutput?.apply {
            write(stateProperty.get().ordinal)
            write(rooms.size)
            rooms.forEach {
                it.writeExternal(objectOutput)
            }
            write(doors.size)
            doors.forEach {
                it.writeExternal(objectOutput)
            }
        }
    }

    override fun readExternal(objectInput: ObjectInput?) {
        val hashToRoom = HashMap<Int, MazeRoom>()
        objectInput?.apply {
            val stateOrdinal = read()
            stateProperty.set(MazeLayoutState.values()[stateOrdinal])

            rooms.clear()
            val roomCount = read()
            for(i in 0 until roomCount) {
                val hash = read()
                val mazeRoom = deserializeMazeRoom(objectInput)
                rooms.add(mazeRoom)
                hashToRoom[hash] = mazeRoom
            }

            doors.clear()
            val doorCount = read()
            for(i in 0 until doorCount) {
                val room1 = hashToRoom[read()] ?: throw IOException("Corrupted data")
                val room2 = hashToRoom[read()] ?: throw IOException("Corrupted data")

                doors.add(MazeDoorImpl(room1, room2))
            }
        }
    }
}