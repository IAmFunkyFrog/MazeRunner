package com.mazerunner.model

import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.layout.MazeLayout
import com.mazerunner.model.runner.MazeRunner
import javafx.beans.property.SimpleIntegerProperty

class GridMaze(
    mazeLayout: MazeLayout,
    mazeRunner: MazeRunner,
    mazeGenerator: MazeGenerator,
    initialWidth: Int = 5,
    initialHeight: Int = 5
): Maze(mazeLayout, mazeRunner, mazeGenerator) {

    val widthProperty = SimpleIntegerProperty(initialWidth)
    val heightProperty = SimpleIntegerProperty(initialHeight)

}