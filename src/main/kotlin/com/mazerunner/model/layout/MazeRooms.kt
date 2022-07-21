package com.mazerunner.model.layout

import java.io.ObjectInput

fun deserializeMazeRoom(objectInput: ObjectInput): MazeRoom {
    val id = objectInput.read()
    return when(MazeRoom.idToMazeRoomImplementation[id]) {
        GridMazeRoom::class -> {
            val room = GridMazeRoom(0, 0)
            room.readExternal(objectInput)
            room
        }
        else -> throw ClassNotFoundException("Not found MazeRoom class with id[$id]")
    }
}