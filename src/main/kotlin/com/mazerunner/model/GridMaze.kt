package com.mazerunner.model

import com.mazerunner.model.generator.grid.GridMazeGenerator
import com.mazerunner.model.layout.MazeLayout
import com.mazerunner.model.runner.MazeRunner
import javafx.beans.property.SimpleIntegerProperty

class GridMaze(
    mazeLayout: MazeLayout,
    mazeRunner: MazeRunner,
    mazeGenerator: GridMazeGenerator
): Maze(mazeLayout, mazeRunner, mazeGenerator)