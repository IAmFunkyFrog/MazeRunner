package com.mazerunner.model

import com.mazerunner.model.generator.grid.EulerMazeGenerator
import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.layout.*
import com.mazerunner.model.layout.grid.GridMazeRoom
import com.mazerunner.model.runner.MazeRunner
import com.mazerunner.model.runner.RandomMazeRunner
import javafx.beans.property.SimpleObjectProperty
import java.io.*
import kotlin.reflect.KClass

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

    fun initializeMazeGeneratorLayout(onInitialize: (Maze) -> Unit = {}) {
        mazeLayout = mazeGenerator.initializeLayout()
        mazeLayoutStateProperty.bind(mazeLayout.stateProperty)
        onInitialize(this)
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

    fun saveInFile(file: File) {
        val stream = FileOutputStream(file)
        mazeLayout.writeExternal(ObjectOutputStream(stream))
    }

    fun loadFromFile(file: File) {
        val stream = FileInputStream(file)
        mazeLayout.readExternal(ObjectInputStream(stream))
    }

    companion object {

        val idToMazeRoomImplementation = HashMap<Int, KClass<*>>().apply {
            this[0] = GridMazeRoom::class
        }

        val MazeRoomImplementationToId = HashMap<KClass<*>, Int>().apply {
            for((key, value) in idToMazeRoomImplementation) {
                this[value] = key
            }
        }

        fun makeGridMazePattern(): GridMaze {
            val eulerMazeGenerator = EulerMazeGenerator(5, 5) // FIXME hardcode bad decision
            return GridMaze(
                eulerMazeGenerator.initializeLayout(), RandomMazeRunner(), eulerMazeGenerator
            )
        }
    }
}