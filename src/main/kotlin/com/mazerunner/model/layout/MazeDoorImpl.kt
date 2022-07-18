package com.mazerunner.model.layout

class MazeDoorImpl(
    private val room1: MazeRoom,
    private val room2: MazeRoom
) : MazeDoor {

    override fun getRooms(): Pair<MazeRoom, MazeRoom> {
        return Pair(room1, room2)
    }

}