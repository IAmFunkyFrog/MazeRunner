package com.mazerunner.view.maze.graph

import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeDoor
import com.mazerunner.model.layout.graph.GraphMazeRoom
import javafx.scene.shape.Line
import tornadofx.onChange

class GraphMazeDoorShape(
    val maze: Maze,
    val mazeDoor: MazeDoor
) : Line() {

    init {
        (mazeDoor.getRooms().first as GraphMazeRoom).positionProperty.onChange {
            it?.let {
                startX = it.x
                startY = it.y
            }
        }
        startX = (mazeDoor.getRooms().first as GraphMazeRoom).positionProperty.get().x
        startY = (mazeDoor.getRooms().first as GraphMazeRoom).positionProperty.get().y
        (mazeDoor.getRooms().second as GraphMazeRoom).positionProperty.onChange {
            it?.let {
                endX = it.x
                endY = it.y
            }
        }
        endX = (mazeDoor.getRooms().second as GraphMazeRoom).positionProperty.get().x
        endY = (mazeDoor.getRooms().second as GraphMazeRoom).positionProperty.get().y
    }

}