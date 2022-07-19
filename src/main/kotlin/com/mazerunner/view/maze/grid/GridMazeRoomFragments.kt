package com.mazerunner.view.maze.grid

import com.mazerunner.model.layout.GridMazeRoom
import com.mazerunner.model.Maze
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints
import tornadofx.*

fun mazeGrid(maze: Maze, op: GridPane.() -> Unit = {}): GridPane {
    val width = maze.getMazeRooms().fold(0) { acc, gridMazeRoom ->
        if(gridMazeRoom !is GridMazeRoom) throw RuntimeException() // FIXME
        maxOf(acc, gridMazeRoom.x)
    }

    val height = maze.getMazeRooms().fold(0) { acc, gridMazeRoom ->
        if(gridMazeRoom !is GridMazeRoom) throw RuntimeException() // FIXME
        maxOf(acc, gridMazeRoom.y)
    }

    return GridPane().apply {
        for(i in 0 .. width) {
            columnConstraints.add(ColumnConstraints(50.0))
        }

        for(j in 0 .. height) {
            rowConstraints.add(RowConstraints(50.0))
        }

        for(i in 0..width) {
            for(j in 0..height) add(GridMazeRoomFragment(maze.getMazeRooms().findLast {
                if(it !is GridMazeRoom) throw RuntimeException() // FIXME
                it.x == i&& it.y == j
            } as GridMazeRoom).root, i, j)
        }

        alignment = Pos.CENTER
    }
}