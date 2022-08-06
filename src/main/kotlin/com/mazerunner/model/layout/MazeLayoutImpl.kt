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

    override fun writeExternal(objectOutput: ObjectOutput?) {
        objectOutput?.apply {
            writeInt(stateProperty.get().ordinal)
            writeInt(rooms.size)
            rooms.forEach {
                writeInt(it.hashCode())
                writeInt(MazeRoom.MazeRoomImplementationToId[it::class]!!)
                it.writeExternal(objectOutput)
            }
            writeInt(doors.size)
            doors.forEach {
                it.writeExternal(objectOutput)
            }
            flush()
        }
    }

    override fun readExternal(objectInput: ObjectInput?) {
        val hashToRoom = HashMap<Int, MazeRoom>()
        objectInput?.apply {
            val stateOrdinal = readInt()
            stateProperty.set(MazeLayoutState.GENERATED) // FIXME use stateOrdinal to reset old state

            rooms.clear()
            val roomCount = readInt()
            for(i in 0 until roomCount) {
                val hash = readInt()
                val mazeRoom = deserializeMazeRoom(objectInput)
                rooms.add(mazeRoom)
                hashToRoom[hash] = mazeRoom
            }

            doors.clear()
            val doorCount = readInt()
            for(i in 0 until doorCount) {
                val room1 = hashToRoom[readInt()] ?: throw IOException("Corrupted data")
                val room2 = hashToRoom[readInt()] ?: throw IOException("Corrupted data")

                doors.add(MazeDoorImpl(room1, room2))
            }
        }
    }
}