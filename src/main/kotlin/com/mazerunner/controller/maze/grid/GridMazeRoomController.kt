package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.InputCommand
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.toggleBorderTo
import com.mazerunner.view.maze.grid.GridMazeRoomFragment
import com.mazerunner.view.maze.grid.GridMazeStylesheet
import javafx.scene.input.InputEvent
import javafx.scene.input.MouseEvent
import tornadofx.*

@Suppress("DuplicatedCode")
class GridMazeRoomController : Controller() {

    private val maze = Maze.getInstance()
    private var lastClickedMazeRoom: GridMazeRoomFragment? = null

    val commandList: List<InputCommand<GridMazeRoomFragment>> = listOf(
        object: InputCommand<GridMazeRoomFragment> {
            override val callback: (GridMazeRoomFragment, InputEvent) -> Unit = { fragment, _ ->
                lastClickedMazeRoom?.gridMazeRoom?.toggleBorderTo(fragment.gridMazeRoom, maze)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown && inputEvent.isControlDown

            override fun getDescription() = "Toggle border"

            override fun toString() = "Left mouse click + Ctrl"

        },
        object: InputCommand<GridMazeRoomFragment> {
            override val callback: (GridMazeRoomFragment, InputEvent) -> Unit = { fragment, _ ->
                maze.setMazeRunnerOnRoom(fragment.gridMazeRoom)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown && inputEvent.isShiftDown

            override fun getDescription() = "Set maze runner on maze room"

            override fun toString() = "Left mouse click + Shift"

        },
        object: InputCommand<GridMazeRoomFragment> {
            override val callback: (GridMazeRoomFragment, InputEvent) -> Unit = { fragment, _ ->
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

    fun onGridMazeRoomMouseClicked(gridMazeRoomFragment: GridMazeRoomFragment, event: MouseEvent) {
        if(maze.mazeLayoutStateProperty.get() != MazeLayoutState.GENERATED) return

        for(command in commandList) {
            if(command.check(event)) {
                command.callback(gridMazeRoomFragment, event)
                break
            }
        }
    }

}