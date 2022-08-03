package com.mazerunner.controller.maze.graph

import com.mazerunner.model.GraphMaze
import com.mazerunner.model.layout.graph.GraphMazeRoom
import com.mazerunner.view.maze.graph.GraphMazeDoorShape
import com.mazerunner.view.maze.graph.GraphMazeRoomShape
import javafx.scene.layout.Pane
import tornadofx.add
import tornadofx.clear

fun makeMazeGraph(roomsPane: Pane, doorsPane: Pane, maze: GraphMaze) {
    roomsPane.clear()
    doorsPane.clear()
    maze.getMazeRooms().forEach {
        roomsPane.add(
            GraphMazeRoomShape(maze, it as GraphMazeRoom)
        )
    }
    maze.getMazeDoors().forEach {
        doorsPane.add(
            GraphMazeDoorShape(maze, it)
        )
    }
}