package com.mazerunner.controller.maze

import com.mazerunner.controller.maze.graph.GraphMazeController
import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.model.generator.MazeGeneratorFactory
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.MazeRoomState
import com.mazerunner.model.runner.MazeRunnerFactory
import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller
import tornadofx.Fragment
import java.io.*
import kotlin.reflect.KClass

abstract class MazeController<T : MazeGeneratorFactory<*>> : Controller() {

    val mazeNameProperty = SimpleStringProperty("Untitled")

    abstract val maze: Maze
    abstract val mazeLeftBarControls: Fragment
    abstract val mazeFragment: Fragment
    abstract val helpFragment: Fragment

    fun makeMazeRunnerTurn() = maze.makeMazeRunnerTurn()

    fun makeMazeGeneratorIteration() = maze.makeMazeGeneratorIteration()
    fun setMazeLayoutGenerated() {
        maze.setMazeLayoutGenerated()
    }

    fun onMazeRunnerChange(mazeRunnerFactory: MazeRunnerFactory) {
        maze.mazeRunner = mazeRunnerFactory.makeMazeRunner()
        maze.getMazeRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        }?.let {
            if (maze.mazeLayoutStateProperty.get() == MazeLayoutState.GENERATED) maze.setMazeRunnerOnRoom(it)
        }
    }

    abstract fun rewriteMaze()
    fun saveMazeInObjectOutput(objectOutput: ObjectOutput) = maze.saveInObjectOutput(objectOutput)
    fun loadFromObjectInput(objectInput: ObjectInput) = maze.loadFromObjectOutput(objectInput)

    abstract fun onMazeGeneratorChange(mazeGeneratorFactory: T)

    companion object {

        private val idToMazeControllerImplementation = HashMap<Int, KClass<*>>().apply {
            this[0] = GridMazeController::class
            this[1] = GraphMazeController::class
        }

        private val mazeControllerImplementationToId = HashMap<KClass<*>, Int>().apply {
            for((key, value) in idToMazeControllerImplementation) {
                this[value] = key
            }
        }

        fun saveMazeControllerInFile(controller: MazeController<*>, file: File) {
            val stream = ObjectOutputStream(FileOutputStream(file))
            stream.writeInt(mazeControllerImplementationToId[controller::class] ?: throw RuntimeException("Unknown controller type ${controller::class}"))
            controller.maze.saveInObjectOutput(stream)
        }

        fun loadMazeControllerFromFile(file: File) : MazeController<*> {
            val stream = ObjectInputStream(FileInputStream(file))
            val id = stream.readInt()
            val controller = when(idToMazeControllerImplementation[id]) {
                GridMazeController::class -> GridMazeController(Maze.makeGridMazePattern())
                GraphMazeController::class -> GraphMazeController(Maze.makeGraphMazePattern())
                else -> throw RuntimeException("Unknown id for controller [$id]")
            }
            controller.maze.loadFromObjectOutput(stream)
            return controller
        }

    }

}