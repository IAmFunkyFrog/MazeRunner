package com.mazerunner.controller.maze.graph

import com.mazerunner.controller.InputCommand
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeDoorImpl
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.view.maze.graph.GraphMazeRoomShape
import com.mazerunner.view.maze.graph.GraphMazeStylesheet
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import tornadofx.Controller
import tornadofx.addClass
import tornadofx.removeClass

@Suppress("DuplicatedCode")
class GraphMazeRoomController : Controller() {

    private var lastClickedGraphRoom: GraphMazeRoomShape? = null

    val commandListDrag: List<InputCommand<Pair<Maze, GraphMazeRoomShape>, MouseEvent>> = listOf(
        InputCommand.makeInputCommand(
            callback = { (_, shape), mouseEvent ->
                shape.graphMazeRoom.positionProperty.set(
                    Point2D(mouseEvent.x, mouseEvent.y)
                )
            },
            check = {
                !it.isStillSincePress
            },
            description = "Move maze room",
            commandName = "Left mouse hold + move mouse"
        )
    )

    val commandListMouse: List<InputCommand<Pair<Maze, GraphMazeRoomShape>, MouseEvent>> = listOf(
        InputCommand.makeInputCommand(
            callback = { (maze, shape), _ ->
                maze.setMazeRunnerOnRoom(shape.graphMazeRoom)
            },
            check = {
                it.isPrimaryButtonDown && it.isShiftDown
            },
            description = "Set maze runner on maze room",
            commandName = "Left mouse click + Shift"
        ),
        InputCommand.makeInputCommand(
            callback = { (maze, shape), _ ->
                lastClickedGraphRoom?.let { graphRoomShape ->
                    val door = maze.getMazeDoors().firstOrNull {
                        it.getRooms().first == graphRoomShape.graphMazeRoom && it.getRooms().second == shape.graphMazeRoom
                                || it.getRooms().second == graphRoomShape.graphMazeRoom && it.getRooms().first == shape.graphMazeRoom
                    }
                    if(door == null) {
                        maze.addMazeDoors(MazeDoorImpl(graphRoomShape.graphMazeRoom, shape.graphMazeRoom))
                    } else {
                        maze.getMazeDoors().remove(door)
                    }
                }
            },
            check = {
                it.isPrimaryButtonDown && it.isControlDown
            },
            description = "Toggle path between rooms",
            commandName = "Left mouse click + Ctrl"
        ),
        InputCommand.makeInputCommand(
            callback = { (_, shape), _ ->
                lastClickedGraphRoom?.removeClass(GraphMazeStylesheet.selectedGraphMazeRoom)
                lastClickedGraphRoom = shape
                shape.addClass(GraphMazeStylesheet.selectedGraphMazeRoom)
            },
            check = {
                it.isPrimaryButtonDown
            },
            description = "Choose maze room",
            commandName = "Left mouse click"
        )
    )

    fun onGraphMazeRoomClicked(maze: Maze, graphMazeRoomShape: GraphMazeRoomShape, event: MouseEvent) {
        if(maze.mazeLayoutStateProperty.get() != MazeLayoutState.GENERATED) return

        for(command in commandListMouse) {
            if(command.check(event)) {
                command.callback(Pair(maze, graphMazeRoomShape), event)
                break
            }
        }
    }

    fun onGraphMazeRoomDragged(maze: Maze, graphMazeRoomShape: GraphMazeRoomShape, event: MouseEvent) {
        for(command in commandListDrag) {
            if(command.check(event)) {
                command.callback(Pair(maze, graphMazeRoomShape), event)
                break
            }
        }
    }

}
