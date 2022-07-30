package com.mazerunner.model.generator.graph

import com.mazerunner.model.layout.*

class RandomMazeGenerator(
    roomsCount: Int,
    val randomFactor: Double = 0.5
) : GraphMazeGenerator(roomsCount) {

    override fun initializeLayout(): MazeLayout {
        val mazeLayout = super.initializeLayout()

        val firstRoom = mazeLayout.getRooms().first()

        firstRoom.stateProperty.set(
            MazeRoomStateWithInfo(mutableListOf<MazeRoom>(), MazeRoomState.CURRENT)
        )

        val randomRoom = mazeLayout.getRooms().first {
            it != firstRoom
        }

        randomRoom.stateProperty.set(
            MazeRoomStateWithInfo(null, MazeRoomState.SEEN)
        )

        return mazeLayout
    }

    override fun makeGeneratorIteration(mazeLayout: MazeLayout): Boolean {

        val currentRoom = mazeLayout.getRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        } ?: return false

        val lastSeen = mazeLayout.getRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.SEEN
        } ?: return false

        if(Math.random() < randomFactor) mazeLayout.addDoors(MazeDoorImpl(currentRoom, lastSeen))

        val currentRoomSeenNeighbours = currentRoom.stateProperty.get().info as MutableList<MazeRoom>
        currentRoomSeenNeighbours.add(lastSeen)
        lastSeen.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.UNKNOWN))

        val nextRoom = mazeLayout.getRooms().firstOrNull {
            !currentRoomSeenNeighbours.contains(it)
        }

        if(nextRoom == null) {
            currentRoom.stateProperty.set(MazeRoomStateWithInfo(currentRoom.stateProperty.get(), MazeRoomState.UNKNOWN))
            val nextCurrentRoom = mazeLayout.getRooms().firstOrNull {
                it.stateProperty.get().info == null
            }
            nextCurrentRoom?.stateProperty?.set(MazeRoomStateWithInfo(mutableListOf<MazeRoom>(), MazeRoomState.CURRENT))
        } else {
            nextRoom.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.SEEN))
        }

        return true
    }

}