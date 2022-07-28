package com.mazerunner.model.generator.grid

import com.mazerunner.model.layout.*
import java.util.*
import kotlin.random.Random

// FIXME refactor me
class EulerMazeGenerator(
    width: Int,
    height: Int,
    private val eulerFactor: Double = 0.6 // TODO make this parameter modifiable by user
) : GridMazeGenerator(width, height) {

    private var maxIndex = 0

    private val indexToSet = HashMap<Int, MutableSet<Int>>()

    override fun initializeLayout(): MazeLayout {
        val mazeLayout = super.initializeLayout()

        val firstRoom = mazeLayout.getRooms().firstOrNull {
            it is GridMazeRoom && it.x == 0 && it.y == 0
        } ?: throw RuntimeException("Should not happening")

        firstRoom.stateProperty.set(
            MazeRoomStateWithInfo(AdditionalInfo(getNextIndex(), EulerAlgorithmState.INDEXING), MazeRoomState.CURRENT)
        )

        return mazeLayout
    }

    override fun makeGeneratorIteration(mazeLayout: MazeLayout): Boolean {
        if (mazeLayout.stateProperty.get() == MazeLayoutState.GENERATED) return false

        val currentRoom = mazeLayout.getRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        } as GridMazeRoom? ?: throw RuntimeException("In initialized Layout must be chosen current room")

        val additionalInfo = currentRoom.stateProperty.get().info as AdditionalInfo

        when (additionalInfo.state) {
            EulerAlgorithmState.INDEXING -> indexingPhase(currentRoom, mazeLayout)
            EulerAlgorithmState.RIGHT_BORDER_ADDITION -> rightBorderPhase(currentRoom, mazeLayout)
            EulerAlgorithmState.BOTTOM_BORDER_ADDITION -> bottomBorderPhase(currentRoom, mazeLayout)
            EulerAlgorithmState.LOWERING -> loweringPhase(currentRoom, mazeLayout)
        }

        return true
    }

    override fun toString() = "Euler"

    private fun indexingPhase(currentRoom: GridMazeRoom, mazeLayout: MazeLayout) {
        val rightRoom = currentRoom.getRightRoom(mazeLayout)
        if (rightRoom == null) {
            val first = currentRoom.getFirstRoomInRow(mazeLayout)
            first.updateEulerState(EulerAlgorithmState.RIGHT_BORDER_ADDITION)
            mazeLayout.setCurrentRoom(first, MazeRoomState.UNKNOWN)
        } else {
            val additionalInfo = rightRoom.stateProperty.get().info
            if (additionalInfo == null) {
                mazeLayout.setCurrentRoom(
                    rightRoom,
                    AdditionalInfo(getNextIndex(), EulerAlgorithmState.INDEXING),
                    MazeRoomState.UNKNOWN
                )
            } else mazeLayout.setCurrentRoom(rightRoom, MazeRoomState.UNKNOWN)
        }
    }

    private fun rightBorderPhase(currentRoom: GridMazeRoom, mazeLayout: MazeLayout) {
        val rightRoom = currentRoom.getRightRoom(mazeLayout) as GridMazeRoom?

        if (currentRoom.getBottomRoom(mazeLayout) == null) {
            currentRoom.updateEulerState(EulerAlgorithmState.LOWERING)
            makeGeneratorIteration(mazeLayout)
            return
        }

        if (rightRoom == null) {
            val first = currentRoom.getFirstRoomInRow(mazeLayout)
            first.updateEulerState(EulerAlgorithmState.BOTTOM_BORDER_ADDITION)
            mazeLayout.setCurrentRoom(first, MazeRoomState.UNKNOWN)
        } else {
            val currentRoomAdditionalInfo = currentRoom.stateProperty.get().info as AdditionalInfo
            val rightRoomAdditionalInfo = rightRoom.stateProperty.get().info as AdditionalInfo

            if (Random.nextDouble() < eulerFactor || isSameSet(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)) {
                mazeLayout.setCurrentRoom(
                    rightRoom,
                    AdditionalInfo(rightRoomAdditionalInfo.index, EulerAlgorithmState.RIGHT_BORDER_ADDITION),
                    MazeRoomState.UNKNOWN
                )
            } else {
                currentRoom.toggleBorderTo(rightRoom, mazeLayout)
                mergeSets(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)
                mazeLayout.setCurrentRoom(
                    rightRoom,
                    AdditionalInfo(rightRoomAdditionalInfo.index, EulerAlgorithmState.RIGHT_BORDER_ADDITION),
                    MazeRoomState.UNKNOWN
                )
            }
        }
    }

    private fun bottomBorderPhase(currentRoom: GridMazeRoom, mazeLayout: MazeLayout) {
        val rightRoom = currentRoom.getRightRoom(mazeLayout) as GridMazeRoom?
        val bottomRoom = currentRoom.getBottomRoom(mazeLayout) as GridMazeRoom?

        bottomRoom?.let {
            val random = Random.nextDouble()
            if (random >= eulerFactor ||
                withoutBottomBorderCountInSet(currentRoom, mazeLayout) == 0
            ) {
                currentRoom.toggleBorderTo(bottomRoom, mazeLayout)
            }
        }

        if (rightRoom == null) {
            val first = currentRoom.getFirstRoomInRow(mazeLayout)
            first.updateEulerState(EulerAlgorithmState.LOWERING)
            mazeLayout.setCurrentRoom(first, MazeRoomState.UNKNOWN)
        } else {
            val rightRoomAdditionalInfo = rightRoom.stateProperty.get().info as AdditionalInfo
            mazeLayout.setCurrentRoom(
                rightRoom,
                AdditionalInfo(rightRoomAdditionalInfo.index, EulerAlgorithmState.BOTTOM_BORDER_ADDITION),
                MazeRoomState.UNKNOWN
            )
        }
    }

    private fun loweringPhase(currentRoom: GridMazeRoom, mazeLayout: MazeLayout) {
        val rightRoom = currentRoom.getRightRoom(mazeLayout) as GridMazeRoom?
        val bottomRoom = currentRoom.getBottomRoom(mazeLayout) as GridMazeRoom?
        val currentRoomAdditionalInfo = currentRoom.stateProperty.get().info as AdditionalInfo

        if (bottomRoom != null) {
            if (currentRoom.hasBottomBorder()) {
                bottomRoom.stateProperty.set(
                    MazeRoomStateWithInfo(
                        AdditionalInfo(getNextIndex(), EulerAlgorithmState.RIGHT_BORDER_ADDITION),
                        MazeRoomState.UNKNOWN
                    )
                )
            } else {
                bottomRoom.stateProperty.set(
                    MazeRoomStateWithInfo(
                        AdditionalInfo(
                            currentRoomAdditionalInfo.index,
                            EulerAlgorithmState.RIGHT_BORDER_ADDITION
                        ), MazeRoomState.UNKNOWN
                    )
                )
            }
        } else if (rightRoom != null) {
            val rightRoomAdditionalInfo = rightRoom.stateProperty.get().info as AdditionalInfo

            if (!isSameSet(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)) {
                currentRoom.toggleBorderTo(
                    rightRoom,
                    mazeLayout
                )
                mergeSets(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)
                rightRoom.stateProperty.set(
                    MazeRoomStateWithInfo(
                        AdditionalInfo(rightRoomAdditionalInfo.index, rightRoomAdditionalInfo.state),
                        rightRoom.stateProperty.get().mazeRoomState
                    )
                )
            }
        }

        if (rightRoom == null) {
            if (bottomRoom == null) mazeLayout.stateProperty.set(MazeLayoutState.GENERATED)
            else {
                val first = bottomRoom.getFirstRoomInRow(mazeLayout)
                mazeLayout.setCurrentRoom(first, MazeRoomState.UNKNOWN)
            }
        } else {
            val rightRoomAdditionalInfo = rightRoom.stateProperty.get().info as AdditionalInfo
            mazeLayout.setCurrentRoom(
                rightRoom,
                AdditionalInfo(rightRoomAdditionalInfo.index, EulerAlgorithmState.LOWERING),
                MazeRoomState.UNKNOWN
            )
        }
    }

    private fun isSameSet(index1: Int, index2: Int): Boolean {
        val set1 = indexToSet[index1] as Set<Int>
        val set2 = indexToSet[index2] as Set<Int>
        return set1.fold(true) { acc, index -> if(!acc) acc else set2.contains(index)} &&
                set2.fold(true) { acc, index -> if(!acc) acc else set1.contains(index)}
    }

    private fun mergeSets(index1: Int, index2: Int) {
        indexToSet[index1]!!.forEach { i1 ->
            indexToSet[index2]!!.forEach { i2 ->
                if(i1 != index1) indexToSet[i1]!!.add(i2)
                if(i2 != index2) indexToSet[i2]!!.add(i1)
            }
        }
        indexToSet[index1]!!.add(index2)
        indexToSet[index2]!!.add(index1)

        indexToSet[index1]!!.forEach {
            indexToSet[index2]!!.add(it)
        }

        indexToSet[index2]!!.forEach {
            indexToSet[index1]!!.add(it)
        }
    }

    private fun withoutBottomBorderCountInSet(currentRoom: GridMazeRoom, mazeLayout: MazeLayout): Int {
        return mazeLayout.getRooms().fold(0) { acc, room ->
            if(room !is GridMazeRoom) throw RuntimeException("Should not reach")

            val currentRoomAdditionalInfo = currentRoom.stateProperty.get().info as AdditionalInfo
            if(room.y == currentRoom.y) {
                val roomAdditionalInfo = room.stateProperty.get().info as AdditionalInfo
                if(isSameSet(currentRoomAdditionalInfo.index, roomAdditionalInfo.index) && !room.hasBottomBorder()) acc + 1
                else acc
            }
            else acc
        }
    }

    private fun getNextIndex(): Int {
        val index = ++maxIndex
        indexToSet[index] = TreeSet<Int>(listOf(index))
        return index
    }

    private enum class EulerAlgorithmState {
        INDEXING,
        RIGHT_BORDER_ADDITION,
        BOTTOM_BORDER_ADDITION,
        LOWERING
    }

    private data class AdditionalInfo(val index: Int, val state: EulerAlgorithmState)

    private companion object {
        // FIXME need to extract all extensions functions to other file
        private fun GridMazeRoom.getRightRoom(mazeLayout: MazeLayout) = mazeLayout.getRooms().firstOrNull {
            it is GridMazeRoom && it.x - 1 == this.x && it.y == this.y
        }

        private fun GridMazeRoom.getBottomRoom(mazeLayout: MazeLayout) = mazeLayout.getRooms().firstOrNull {
            it is GridMazeRoom && it.x == this.x && it.y - 1 == this.y
        }

        private fun GridMazeRoom.getFirstRoomInRow(mazeLayout: MazeLayout) =
            mazeLayout.getRooms().fold(this) { acc, room ->
                if (room is GridMazeRoom && room.y == acc.y && room.x < acc.x) room
                else acc
            }

        private fun GridMazeRoom.updateEulerState(state: EulerAlgorithmState) {
            val additionalInfo = this.stateProperty.get().info as AdditionalInfo

            this.stateProperty.set(
                MazeRoomStateWithInfo(
                    AdditionalInfo(additionalInfo.index, state),
                    this.stateProperty.get().mazeRoomState
                )
            )
        }

        private fun GridMazeRoom.hasBottomBorder() = (this.borderProperty.get() and 4) != 0
    }

}