package com.mazerunner.model

import com.mazerunner.model.generator.EulerMazeGenerator
import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.layout.*
import com.mazerunner.model.runner.BFSMazeRunner
import com.mazerunner.model.runner.MazeRunner
import com.mazerunner.model.runner.RandomMazeRunner
import javafx.beans.property.SimpleObjectProperty
import java.io.*

class Maze private constructor(
    private var mazeLayout: MazeLayout,
    var mazeRunner: MazeRunner,
    var mazeGenerator: MazeGenerator
) {
    val mazeLayoutStateProperty: SimpleObjectProperty<MazeLayoutState> = SimpleObjectProperty(MazeLayoutState.INITIALIZED)

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
    }

    fun makeMazeGeneratorIteration() = mazeGenerator.makeGeneratorIteration(mazeLayout)

    fun saveInFile(file: File) {
        val stream = FileOutputStream(file)
        mazeLayout.writeExternal(ObjectOutputStream(stream))
    }

    fun loadFromFile(file: File) {
        val stream = FileInputStream(file)
        mazeLayout.readExternal(ObjectInputStream(stream))
    }

    companion object {

        private lateinit var instance: Maze

        fun getInstance(): Maze {
            synchronized(Maze::class) {
                val eulerMazeGenerator = EulerMazeGenerator(5, 5)
                if (!this::instance.isInitialized) instance = Maze(
                    eulerMazeGenerator.initializeLayout(), RandomMazeRunner(), eulerMazeGenerator
                )
            }
            return instance
        }
    }
}