package com.mazerunner.model.layout

import com.mazerunner.model.Maze
import com.mazerunner.model.layout.grid.GridMazeRoom
import java.io.ObjectInput

fun deserializeMazeRoom(objectInput: ObjectInput): MazeRoom {
    val id = objectInput.readInt()
    return when(Maze.idToMazeRoomImplementation[id]) {
        GridMazeRoom::class -> {
            val room = GridMazeRoom(0, 0)
            room.readExternal(objectInput)
            room
        }
        else -> throw ClassNotFoundException("Not found MazeRoom class with id[$id]")
    }
}