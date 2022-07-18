package com.mazerunner.controller.grid

import com.mazerunner.model.layout.MazeDoorImpl
import com.mazerunner.model.layout.distanceTo
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.toggleBorderTo
import com.mazerunner.view.maze.grid.GridMazeRoomFragment
import com.mazerunner.view.maze.grid.GridMazeStylesheet
import javafx.scene.input.MouseEvent
import tornadofx.*

class GridMazeRoomController : Controller() {

    private val maze = Maze.getInstance()
    private var lastClickedMazeRoom: GridMazeRoomFragment? = null

    fun onGridMazeRoomMouseClicked(gridMazeRoomFragment: GridMazeRoomFragment, event: MouseEvent) {
        if(maze.mazeLayoutStateProperty.get() != MazeLayoutState.GENERATED) return

        if (event.isPrimaryButtonDown) {
            when {
                event.isControlDown -> dispatchCtrlLeftMouseClick(gridMazeRoomFragment)
                event.isShiftDown -> dispatchShiftLeftMouseClick(gridMazeRoomFragment)
                else -> dispatchLeftMouseClick (gridMazeRoomFragment)
            }
        }
    }

    private fun dispatchShiftLeftMouseClick(gridMazeRoomFragment: GridMazeRoomFragment) {
        maze.setMazeRunnerOnRoom(gridMazeRoomFragment.gridMazeRoom)
    }

    private fun dispatchLeftMouseClick(gridMazeRoomFragment: GridMazeRoomFragment) {
        lastClickedMazeRoom?.root?.removeClass(GridMazeStylesheet.selectedGridMazeRoom)
        lastClickedMazeRoom = gridMazeRoomFragment
        gridMazeRoomFragment.root.addClass(GridMazeStylesheet.selectedGridMazeRoom)
    }

    private fun dispatchCtrlLeftMouseClick(gridMazeRoomFragment: GridMazeRoomFragment) {
        val castedLastClickedRoom = lastClickedMazeRoom ?: return

        castedLastClickedRoom.gridMazeRoom.toggleBorderTo(gridMazeRoomFragment.gridMazeRoom, maze)
    }

}