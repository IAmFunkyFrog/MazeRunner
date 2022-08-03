package com.mazerunner.controller.maze.grid

import com.mazerunner.model.Maze
import com.mazerunner.model.layout.grid.GridMazeRoom
import com.mazerunner.view.maze.grid.GridMazeRoomFragment
import javafx.geometry.Pos
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints

fun mazeGrid(maze: Maze, cellWidth: Double): GridPane {
    val width = maze.getMazeRooms().fold(0) { acc, gridMazeRoom ->
        if(gridMazeRoom !is GridMazeRoom) throw RuntimeException("Should not reach") // FIXME
        maxOf(acc, gridMazeRoom.x)
    }

    val height = maze.getMazeRooms().fold(0) { acc, gridMazeRoom ->
        if(gridMazeRoom !is GridMazeRoom) throw RuntimeException("Should not reach") // FIXME
        maxOf(acc, gridMazeRoom.y)
    }

    return GridPane().apply {
        for(i in 0 .. width) {
            columnConstraints.add(ColumnConstraints(cellWidth))
        }

        for(j in 0 .. height) {
            rowConstraints.add(RowConstraints(cellWidth))
        }

        for(i in 0..width) {
            for(j in 0..height) add(GridMazeRoomFragment(maze, maze.getMazeRooms().findLast {
                if(it !is GridMazeRoom) throw RuntimeException("Should not reach") // FIXME
                it.x == i&& it.y == j
            } as GridMazeRoom).root, i, j)
        }

        alignment = Pos.CENTER
    }
}