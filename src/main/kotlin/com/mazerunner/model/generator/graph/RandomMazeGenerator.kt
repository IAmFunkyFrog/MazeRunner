package com.mazerunner.model.generator.graph

import com.mazerunner.model.layout.*

class RandomMazeGenerator(
    roomsCount: Int,
    val randomFactor: Double = 0.5 // TODO make this parameter modifiable by user
) : GraphMazeGenerator(roomsCount) {

    val seenPairs = mutableListOf<Pair<MazeRoom, MazeRoom>>()

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

    // FIXME REFACTOR ME
    override fun makeGeneratorIteration(mazeLayout: MazeLayout): Boolean {

        if (mazeLayout.stateProperty.get() == MazeLayoutState.GENERATED) return false

        val currentRoom = mazeLayout.getRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        }

        val lastSeen = mazeLayout.getRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.SEEN
        }

        if (currentRoom == null || lastSeen == null) {
            mazeLayout.stateProperty.set(MazeLayoutState.GENERATED)
            return false
        }

        if (Math.random() < randomFactor) mazeLayout.addDoors(MazeDoorImpl(currentRoom, lastSeen))

        seenPairs.add(Pair(currentRoom, lastSeen))
        lastSeen.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.UNKNOWN))

        val nextRoom = mazeLayout.getRooms().firstOrNull { nextApplicant ->
            seenPairs.find {
                it.first == nextApplicant && it.second == currentRoom ||
                        it.second == nextApplicant && it.first == currentRoom
            } == null && nextApplicant != currentRoom
        }

        if (nextRoom == null) {
            currentRoom.stateProperty.set(MazeRoomStateWithInfo(currentRoom.stateProperty.get(), MazeRoomState.UNKNOWN))
            val nextCurrentRoom = mazeLayout.getRooms().firstOrNull {
                it.stateProperty.get().info == null
            }
            if (nextCurrentRoom == null) mazeLayout.stateProperty.set(MazeLayoutState.GENERATED)
            else {
                val nextLastSeenRoom = mazeLayout.getRooms().firstOrNull { nextApplicant ->
                    seenPairs.find {
                        it.first == nextApplicant && it.second == nextCurrentRoom ||
                                it.second == nextApplicant && it.first == nextCurrentRoom
                    } == null && nextApplicant != nextCurrentRoom
                }
                if(nextLastSeenRoom == null) mazeLayout.stateProperty.set(MazeLayoutState.GENERATED)
                else {
                    nextCurrentRoom.stateProperty.set(
                        MazeRoomStateWithInfo(
                            true,
                            MazeRoomState.CURRENT
                        )
                    )
                    nextLastSeenRoom.stateProperty.set(
                        MazeRoomStateWithInfo(
                            null,
                            MazeRoomState.SEEN
                        )
                    )
                }
            }
        } else {
            nextRoom.stateProperty.set(MazeRoomStateWithInfo(null, MazeRoomState.SEEN))
        }

        return true
    }

}