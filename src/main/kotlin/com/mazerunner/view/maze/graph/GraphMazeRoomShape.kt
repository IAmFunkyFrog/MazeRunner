package com.mazerunner.view.maze.graph

import com.mazerunner.controller.maze.graph.GraphMazeRoomController
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.graph.GraphMazeRoom
import javafx.scene.shape.Circle
import tornadofx.addClass
import tornadofx.find
import tornadofx.onChange
import tornadofx.removeClass

class GraphMazeRoomShape(
    val maze: Maze,
    val graphMazeRoom: GraphMazeRoom
) : Circle() {

    private val controller: GraphMazeRoomController = find(GraphMazeRoomController::class)

    private var lastChosenBackground =
        GraphMazeStylesheet.graphMazeRoomBackgrounds[graphMazeRoom.stateProperty.get().mazeRoomState.ordinal]

    init {
        addClass(GraphMazeStylesheet.graphMazeRoom)

        addClass(lastChosenBackground)
        graphMazeRoom.stateProperty.onChange {
            it?.let {
                removeClass(lastChosenBackground)
                lastChosenBackground = GraphMazeStylesheet.graphMazeRoomBackgrounds[it.mazeRoomState.ordinal]
                addClass(lastChosenBackground)
            }
        }

        radius = 16.0 // FIXME

        graphMazeRoom.positionProperty.onChange {
            it?.let {
                centerX = it.x
                centerY = it.y
            }
        }

        centerX = graphMazeRoom.positionProperty.get().x
        centerY = graphMazeRoom.positionProperty.get().y

        setOnMousePressed {
            controller.onGraphMazeRoomClicked(maze, this, it)
        }

        setOnMouseDragged {
            controller.onGraphMazeRoomDragged(maze, this, it)
        }
    }

}