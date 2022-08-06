package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.InputCommand
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.toggleBorderTo
import com.mazerunner.view.maze.grid.GridMazeRoomFragment
import com.mazerunner.view.maze.grid.GridMazeStylesheet
import javafx.scene.input.MouseEvent
import tornadofx.Controller
import tornadofx.addClass
import tornadofx.removeClass

class GridMazeRoomController : Controller() {

    private var lastClickedMazeRoom: GridMazeRoomFragment? = null

    val commandList: List<InputCommand<Pair<Maze, GridMazeRoomFragment>, MouseEvent>> = listOf(
        InputCommand.makeInputCommand(
            callback = { (maze, fragment), _ ->
                lastClickedMazeRoom?.gridMazeRoom?.toggleBorderTo(fragment.gridMazeRoom, maze)
            },
            check = {
                it.isPrimaryButtonDown && it.isControlDown
            },
            description = "Toggle border",
            commandName = "Left mouse click + Ctrl"
        ),
        InputCommand.makeInputCommand(
            callback = { (maze, fragment), _ ->
                maze.setMazeRunnerOnRoom(fragment.gridMazeRoom)
            },
            check = {
                it.isPrimaryButtonDown && it.isShiftDown
            },
            description = "Set maze runner on maze room",
            commandName = "Left mouse click + Shift"
        ),
        InputCommand.makeInputCommand(
            callback = { (_, fragment), _ ->
                lastClickedMazeRoom?.root?.removeClass(GridMazeStylesheet.selectedGridMazeRoom)
                lastClickedMazeRoom = fragment
                fragment.root.addClass(GridMazeStylesheet.selectedGridMazeRoom)
            },
            check = {
                it.isPrimaryButtonDown
            },
            description = "Choose maze room",
            commandName = "Left mouse click"
        )
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
