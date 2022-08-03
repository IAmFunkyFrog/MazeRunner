package com.mazerunner.model.layout

import com.mazerunner.model.layout.graph.GraphMazeRoom
import com.mazerunner.model.layout.grid.GridMazeRoom
import javafx.geometry.Point2D
import java.io.ObjectInput

fun deserializeMazeRoom(objectInput: ObjectInput): MazeRoom {
    val id = objectInput.readInt()
    return when(MazeRoom.idToMazeRoomImplementation[id]) {
        GridMazeRoom::class -> {
            val room = GridMazeRoom(0, 0)
            room.readExternal(objectInput)
            room
        }
        GraphMazeRoom::class -> {
            val room = GraphMazeRoom(Point2D.ZERO)
            room.readExternal(objectInput)
            room
        }
        else -> throw ClassNotFoundException("Not found MazeRoom class with id[$id]")
    }
}