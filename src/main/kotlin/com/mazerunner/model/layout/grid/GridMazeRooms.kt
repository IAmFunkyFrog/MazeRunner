package com.mazerunner.model.layout

import com.mazerunner.model.Maze
import com.mazerunner.model.layout.grid.GridMazeRoom

fun GridMazeRoom.distanceTo(gridMazeRoom: GridMazeRoom): Int {
    return (this.x - gridMazeRoom.x) * (this.x - gridMazeRoom.x) + (this.y - gridMazeRoom.y) * (this.y - gridMazeRoom.y)
}

fun GridMazeRoom.toggleBorderTo(gridMazeRoom: GridMazeRoom, mazeLayout: MazeLayout) {
    if (this.distanceTo(gridMazeRoom) != 1) return
    
    val doorBetweenRooms = mazeLayout.getDoors().firstOrNull {
        it.getRooms().first == this && it.getRooms().second == gridMazeRoom
                || it.getRooms().first == gridMazeRoom && it.getRooms().second == this
    }

    if(doorBetweenRooms == null) {
        val mazeDoor = MazeDoorImpl(this, gridMazeRoom)
        mazeLayout.getDoors().add(mazeDoor)
    } else {
        mazeLayout.getDoors().remove(doorBetweenRooms)
    }
    
    // TODO: refactor, looks awful
    if (this.x == gridMazeRoom.x) {
        if (this.y > gridMazeRoom.y) {
            this.borderProperty.set(this.borderProperty.get() xor 1)
            gridMazeRoom.borderProperty.set(gridMazeRoom.borderProperty.get() xor 4)
        } else {
            this.borderProperty.set(this.borderProperty.get() xor 4)
            gridMazeRoom.borderProperty.set(gridMazeRoom.borderProperty.get() xor 1)
        }
    } else {
        if (this.x < gridMazeRoom.x) {
            this.borderProperty.set(this.borderProperty.get() xor 2)
            gridMazeRoom.borderProperty.set(gridMazeRoom.borderProperty.get() xor 8)
        } else {
            this.borderProperty.set(this.borderProperty.get() xor 8)
            gridMazeRoom.borderProperty.set(gridMazeRoom.borderProperty.get() xor 2)
        }
    }
}

// FIXME delete reflection
fun GridMazeRoom.toggleBorderTo(gridMazeRoom: GridMazeRoom, maze: Maze) {
    val mazeLayoutField = maze.javaClass.getDeclaredField("mazeLayout")
    mazeLayoutField.isAccessible = true
    this.toggleBorderTo(gridMazeRoom, mazeLayoutField.get(maze) as MazeLayout)
}