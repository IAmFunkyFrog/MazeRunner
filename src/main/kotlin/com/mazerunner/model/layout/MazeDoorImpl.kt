package com.mazerunner.model.layout

import java.io.ObjectInput
import java.io.ObjectOutput

class MazeDoorImpl(
    private val room1: MazeRoom,
    private val room2: MazeRoom
) : MazeDoor {

    override fun getRooms(): Pair<MazeRoom, MazeRoom> {
        return Pair(room1, room2)
    }

    override fun writeExternal(objectOutput: ObjectOutput?) {
        objectOutput?.apply {
            write(room1.hashCode())
            write(room2.hashCode())
        }
    }

    override fun readExternal(objectInput: ObjectInput?) {
        throw RuntimeException("Must be deserialized manually by using constructor")
    }

}