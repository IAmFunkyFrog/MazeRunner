package com.mazerunner.controller.maze.graph

import com.mazerunner.controller.InputCommand
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeDoorImpl
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.view.maze.graph.GraphMazeRoomShape
import com.mazerunner.view.maze.graph.GraphMazeStylesheet
import javafx.geometry.Point2D
import javafx.scene.input.InputEvent
import javafx.scene.input.MouseEvent
import tornadofx.Controller
import tornadofx.addClass
import tornadofx.removeClass

@Suppress("DuplicatedCode")
class GraphMazeRoomController : Controller() {

    private var lastClickedGraphRoom: GraphMazeRoomShape? = null

    val commandListDrag: List<InputCommand<Pair<Maze, GraphMazeRoomShape>>> = listOf(
        object: InputCommand<Pair<Maze, GraphMazeRoomShape>> {
            override val callback: (Pair<Maze, GraphMazeRoomShape>, InputEvent) -> Unit = { (_, shape), inputEvent ->
                shape.graphMazeRoom.positionProperty.set(
                    with(inputEvent as MouseEvent) {
                        Point2D(x, y)
                    }
                )
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    !inputEvent.isStillSincePress

            override fun getDescription() = "Move maze room"

            override fun toString() = "Left mouse hold + move mouse"

        }
    )

    val commandListMouse: List<InputCommand<Pair<Maze, GraphMazeRoomShape>>> = listOf(
        object: InputCommand<Pair<Maze, GraphMazeRoomShape>> {
            override val callback: (Pair<Maze, GraphMazeRoomShape>, InputEvent) -> Unit = { (maze, shape), _ ->
                maze.setMazeRunnerOnRoom(shape.graphMazeRoom)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown && inputEvent.isShiftDown

            override fun getDescription() = "Set maze runner on maze room"

            override fun toString() = "Left mouse click + Shift"

        },
        object: InputCommand<Pair<Maze, GraphMazeRoomShape>> {
            override val callback: (Pair<Maze, GraphMazeRoomShape>, InputEvent) -> Unit = { (maze, shape), _ ->
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
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown && inputEvent.isControlDown

            override fun getDescription() = "Toggle path between rooms"

            override fun toString() = "Left mouse click + Ctrl"

        },
        object: InputCommand<Pair<Maze, GraphMazeRoomShape>> {
            override val callback: (Pair<Maze, GraphMazeRoomShape>, InputEvent) -> Unit = { (_, shape), _ ->
                lastClickedGraphRoom?.removeClass(GraphMazeStylesheet.selectedGraphMazeRoom)
                lastClickedGraphRoom = shape
                shape.addClass(GraphMazeStylesheet.selectedGraphMazeRoom)
            }

            override fun check(inputEvent: InputEvent) = inputEvent is MouseEvent &&
                    inputEvent.isPrimaryButtonDown

            override fun getDescription() = "Choose maze room"

            override fun toString() = "Left mouse click"

        }
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
