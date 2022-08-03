package com.mazerunner.model

import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.generator.graph.RandomMazeGenerator
import com.mazerunner.model.generator.grid.EulerMazeGenerator
import com.mazerunner.model.layout.*
import com.mazerunner.model.runner.MazeRunner
import com.mazerunner.model.runner.RandomMazeRunner
import javafx.beans.property.SimpleObjectProperty
import java.io.ObjectInput
import java.io.ObjectOutput

abstract class Maze protected constructor(
    private var mazeLayout: MazeLayout,
    var mazeRunner: MazeRunner,
    var mazeGenerator: MazeGenerator
) {
    val mazeLayoutStateProperty: SimpleObjectProperty<MazeLayoutState> = SimpleObjectProperty(mazeLayout.stateProperty.get()).apply {
        bind(mazeLayout.stateProperty)
    }

    fun getMazeRooms() = mazeLayout.getRooms()

    fun getMazeDoors() = mazeLayout.getDoors()

    fun addMazeRooms(vararg mazeRooms: MazeRoom) = mazeLayout.addRooms(*mazeRooms)

    fun addMazeDoors(vararg mazeDoors: MazeDoor) {
        mazeLayout.addDoors(*mazeDoors)

        val currentRoom = getMazeRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        } ?: return

        mazeRunner.initOnRoom(currentRoom, mazeLayout)
    }

    fun setMazeRunnerOnRoom(mazeRoom: MazeRoom) = mazeRunner.initOnRoom(mazeRoom, mazeLayout)

    fun makeMazeRunnerTurn() = mazeRunner.makeTurn(mazeLayout)

    fun initializeMazeGeneratorLayout() {
        mazeLayout = mazeGenerator.initializeLayout()
        mazeLayoutStateProperty.bind(mazeLayout.stateProperty)
    }

    fun setMazeLayoutGenerated() {
        mazeLayout.stateProperty.set(MazeLayoutState.GENERATED)
        mazeLayout.getRooms().forEach {
            if(it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT) it.stateProperty.set(
                MazeRoomStateWithInfo(it.stateProperty.get().info, MazeRoomState.UNKNOWN)
            )
        }
    }

    fun makeMazeGeneratorIteration(): Boolean {
        val returnValue = mazeGenerator.makeGeneratorIteration(mazeLayout)
        if(mazeLayout.stateProperty.get() == MazeLayoutState.GENERATED) setMazeLayoutGenerated()
        return returnValue
    }

    fun saveInObjectOutput(objectOutput: ObjectOutput) {
        mazeLayout.writeExternal(objectOutput)
    }

    fun loadFromObjectOutput(objectInput: ObjectInput) {
        mazeLayout.readExternal(objectInput)
    }

    companion object {

        fun makeGridMazePattern(): GridMaze {
            val eulerMazeGenerator = EulerMazeGenerator(5, 5) // FIXME hardcode bad decision
            return GridMaze(
                eulerMazeGenerator.initializeLayout(), RandomMazeRunner(), eulerMazeGenerator
            )
        }

        fun makeGraphMazePattern(): GraphMaze {
            val randomMazeGenerator = RandomMazeGenerator(5) // FIXME hardcode bad decision
            return GraphMaze(
                randomMazeGenerator.initializeLayout(), RandomMazeRunner(), randomMazeGenerator
            )
        }
    }
}