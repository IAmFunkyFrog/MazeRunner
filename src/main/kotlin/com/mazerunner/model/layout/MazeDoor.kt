package com.mazerunner.model.layout

import java.io.Externalizable

interface MazeDoor : Externalizable {

    fun getRooms(): Pair<MazeRoom, MazeRoom>

}