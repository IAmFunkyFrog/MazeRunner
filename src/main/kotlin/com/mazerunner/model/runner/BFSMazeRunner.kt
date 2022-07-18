package com.mazerunner.model.runner

import com.mazerunner.model.layout.*
import kotlin.random.Random

class BFSMazeRunner : MazeRunner {

    override fun initOnRoom(mazeRoom: MazeRoom, mazeLayout: MazeLayout) {
        if(!mazeLayout.getRooms().contains(mazeRoom)) throw RuntimeException("Room expected to be in maze")

        mazeLayout.getRooms().forEach {
            it.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.UNKNOWN))
        }

        mazeLayout.getAdjoiningRooms(mazeRoom).forEachIndexed { index, room ->
            room.stateProperty.set(MazeRoomStateWithInfo(index + 1, MazeRoomState.SEEN))
        }

        mazeRoom.stateProperty.set(MazeRoomStateWithInfo(0, MazeRoomState.CURRENT))
    }

    override fun makeTurn(mazeLayout: MazeLayout): Boolean {
        val seenRooms = mazeLayout.getRooms().filter {
            it.stateProperty.get().mazeRoomState == MazeRoomState.SEEN
        }

        if(seenRooms.isEmpty()) return false

        val nextRoom = seenRooms.fold(seenRooms[0]) { acc, room ->
            val oldIndex = acc.stateProperty.get().info as Int
            val newIndex = room.stateProperty.get().info as Int

            if(oldIndex > newIndex) room
            else acc
        }

        mazeLayout.setCurrentRoom(nextRoom)

        mazeLayout.getAdjoiningRooms(nextRoom).forEach {
            val maxIndex = mazeLayout.getRooms().fold(Int.MIN_VALUE) { acc, room ->
                val currentIndex = room.stateProperty.get().info as Int? ?: acc
                maxOf(currentIndex, acc)
            }
            if(it.stateProperty.get().mazeRoomState == MazeRoomState.UNKNOWN) it.stateProperty.set(
                MazeRoomStateWithInfo(maxIndex + 1, MazeRoomState.SEEN)
            )
        }

        return true
    }

}