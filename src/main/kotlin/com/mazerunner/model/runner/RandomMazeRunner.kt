package com.mazerunner.model.runner

import com.mazerunner.model.layout.*
import kotlin.random.Random

class RandomMazeRunner : MazeRunner {

    override fun initOnRoom(mazeRoom: MazeRoom, mazeLayout: MazeLayout) {
        if(!mazeLayout.getRooms().contains(mazeRoom)) throw RuntimeException("Room expected to be in maze")

        mazeLayout.getRooms().forEach {
            it.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.UNKNOWN))
        }

        mazeLayout.getAdjoiningRooms(mazeRoom).forEach {
            it.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.SEEN))
        }

        mazeRoom.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.CURRENT))
    }

    override fun makeTurn(mazeLayout: MazeLayout): Boolean {
        val seenRooms = mazeLayout.getRooms().filter {
            it.stateProperty.get().mazeRoomState == MazeRoomState.SEEN
        }

        if(seenRooms.isEmpty()) return false

        val randomRoom = seenRooms[Random.nextInt(0, seenRooms.size)]

        mazeLayout.setCurrentRoom(randomRoom)

        mazeLayout.getAdjoiningRooms(randomRoom).forEach {
            if(it.stateProperty.get().mazeRoomState == MazeRoomState.UNKNOWN) it.stateProperty.set(
                MazeRoomStateWithInfo(it.stateProperty.get().info, MazeRoomState.SEEN)
            )
        }

        return true
    }

}