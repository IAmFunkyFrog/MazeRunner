package com.mazerunner.model.generator

import com.mazerunner.model.layout.*
import java.util.TreeSet
import kotlin.random.Random

class EulerMazeGenerator(
    width: Int,
    height: Int,
    private val eulerFactor: Double = 0.6
) : GridMazeGenerator(width, height) {

    private var maxIndex = 0

    private val withoutBottomBorderCount = HashMap<Int, Int>()
    private val setMap = HashMap<Int, TreeSet<Int>>()

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

    override fun toString(): String {
        return "Euler"
    }

    private fun indexingPhase(currentRoom: GridMazeRoom, mazeLayout: MazeLayout) {
        val rightRoom = currentRoom.getRightRoom(mazeLayout)
        if (rightRoom == null) {
            val first = currentRoom.getFirstRoomInRow(mazeLayout)
            first.updateEulerState(EulerAlgorithmState.RIGHT_BORDER_ADDITION)
            mazeLayout.setCurrentRoom(first, MazeRoomState.UNKNOWN)
        } else {
            val additionalInfo = rightRoom.stateProperty.get().info
            if (additionalInfo == null) {
                val index = getNextIndex()
                mazeLayout.setCurrentRoom(
                    rightRoom,
                    AdditionalInfo(index, EulerAlgorithmState.INDEXING),
                    MazeRoomState.UNKNOWN
                )
                withoutBottomBorderCount[index] = (withoutBottomBorderCount[index] ?: 0) + 1
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

            val oldSetIndex = minOf(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)
            val newSetIndex = maxOf(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)
            if (Random.nextDouble() < eulerFactor || isSameSet(oldSetIndex, newSetIndex)) {
                mazeLayout.setCurrentRoom(
                    rightRoom,
                    AdditionalInfo(rightRoomAdditionalInfo.index, EulerAlgorithmState.RIGHT_BORDER_ADDITION),
                    MazeRoomState.UNKNOWN
                )
            } else {
                currentRoom.toggleBorderTo(rightRoom, mazeLayout)
                currentRoom.stateProperty.set(
                    MazeRoomStateWithInfo(
                        AdditionalInfo(oldSetIndex, currentRoomAdditionalInfo.state),
                        currentRoom.stateProperty.get().mazeRoomState
                    )
                )
                mazeLayout.setCurrentRoom(
                    rightRoom,
                    AdditionalInfo(oldSetIndex, EulerAlgorithmState.RIGHT_BORDER_ADDITION),
                    MazeRoomState.UNKNOWN
                )
                withoutBottomBorderCount[oldSetIndex] =
                    (withoutBottomBorderCount[oldSetIndex] ?: 0) + 1
                withoutBottomBorderCount[newSetIndex] =
                    (withoutBottomBorderCount[newSetIndex] ?: 1) - 1
                mergeSets(oldSetIndex, newSetIndex)
            }
        }
    }

    private fun bottomBorderPhase(currentRoom: GridMazeRoom, mazeLayout: MazeLayout) {
        val rightRoom = currentRoom.getRightRoom(mazeLayout) as GridMazeRoom?
        val bottomRoom = currentRoom.getBottomRoom(mazeLayout) as GridMazeRoom?

        val currentRoomAdditionalInfo = currentRoom.stateProperty.get().info as AdditionalInfo
        bottomRoom?.let {
            if (Random.nextDouble() < eulerFactor &&
                (withoutBottomBorderCount[currentRoomAdditionalInfo.index] ?: 0) > 1
            ) {
                withoutBottomBorderCount[currentRoomAdditionalInfo.index] =
                    (withoutBottomBorderCount[currentRoomAdditionalInfo.index] ?: 1) - 1
            } else {
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
                val index = getNextIndex()
                bottomRoom.stateProperty.set(
                    MazeRoomStateWithInfo(
                        AdditionalInfo(getNextIndex(), EulerAlgorithmState.RIGHT_BORDER_ADDITION),
                        MazeRoomState.UNKNOWN
                    )
                )
                withoutBottomBorderCount[index] = (withoutBottomBorderCount[index] ?: 0) + 1
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

            val oldSetIndex = minOf(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)
            val newSetIndex = maxOf(currentRoomAdditionalInfo.index, rightRoomAdditionalInfo.index)
            if (!isSameSet(oldSetIndex, newSetIndex)) {
                currentRoom.toggleBorderTo(
                    rightRoom,
                    mazeLayout
                )
                mergeSets(oldSetIndex, newSetIndex)
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
        val set1 = setMap[index1] as Set<Int>
        val set2 = setMap[index2] as Set<Int>
        val intersection = TreeSet(set1)
        intersection.retainAll(set2)
        return intersection.size > 0
    }

    private fun mergeSets(index1: Int, index2: Int) {
        val set1 = setMap[index1] as TreeSet<Int>
        val set2 = setMap[index2] as TreeSet<Int>

        set1.addAll(set2)
        set2.addAll(set2)
    }

    private fun getNextIndex(): Int {
        val nextIndex = ++maxIndex
        setMap[nextIndex] = TreeSet(listOf(nextIndex))
        return nextIndex
    }

    private enum class EulerAlgorithmState {
        INDEXING,
        RIGHT_BORDER_ADDITION,
        BOTTOM_BORDER_ADDITION,
        LOWERING
    }

    private data class AdditionalInfo(val index: Int, val state: EulerAlgorithmState)

    private companion object {
        // FIXME need to extrct all extensions functions to other file
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