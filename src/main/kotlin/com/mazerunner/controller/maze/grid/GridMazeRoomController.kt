package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.InputCommand
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.toggleBorderTo
import com.mazerunner.view.maze.grid.GridMazeRoomFragment
import com.mazerunner.view.maze.grid.GridMazeStylesheet
import javafx.scene.input.InputEvent
import javafx.scene.input.MouseEvent
import tornadofx.Controller
import tornadofx.addClass
import tornadofx.removeClass

@Suppress("DuplicatedCode")
class GridMazeRoomController : Controller() {

    private var lastClickedMazeRoom: GridMazeRoomFragment? = null

    val commandList: List<InputCommand<Pair<Maze, GridMazeRoomFragment>>> = listOf(
        object: InputCommand<Pair<Maze, GridMazeRoomFragment>> {
            override val callback: (Pair<Maze, GridMazeRoomFragment>, InputEvent) -> Unit = { (maze, fragment), _ ->
                lastClickedMazeRoom?.gridMazeRoom?.toggleBorderTo(fragment.gridMazeRoom, maze)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown && inputEvent.isControlDown

            override fun getDescription() = "Toggle border"

            override fun toString() = "Left mouse click + Ctrl"

        },
        object: InputCommand<Pair<Maze, GridMazeRoomFragment>> {
            override val callback: (Pair<Maze, GridMazeRoomFragment>, InputEvent) -> Unit = { (maze, fragment), _ ->
                maze.setMazeRunnerOnRoom(fragment.gridMazeRoom)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown && inputEvent.isShiftDown

            override fun getDescription() = "Set maze runner on maze room"

            override fun toString() = "Left mouse click + Shift"

        },
        object: InputCommand<Pair<Maze, GridMazeRoomFragment>> {
            override val callback: (Pair<Maze, GridMazeRoomFragment>, InputEvent) -> Unit = { (_, fragment), _ ->
                lastClickedMazeRoom?.root?.removeClass(GridMazeStylesheet.selectedGridMazeRoom)
                lastClickedMazeRoom = fragment
                fragment.root.addClass(GridMazeStylesheet.selectedGridMazeRoom)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown

            override fun getDescription() = "Choose maze room"

            override fun toString() = "Left mouse click"

        }
    )

    fun onGridMazeRoomMouseClicked(maze: Maze, gridMazeRoomFragment: GridMazeRoomFragment, event: MouseEvent) {
        if(maze.mazeLayoutStateProperty.get() != MazeLayoutState.GENERATED) return

        for(command in commandList) {
            if(command.check(event)) {
                command.callback(Pair(maze, gridMazeRoomFragment), event)
                break
            }
        }
    }

}