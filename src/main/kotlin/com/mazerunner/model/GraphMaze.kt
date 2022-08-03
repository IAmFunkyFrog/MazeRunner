package com.mazerunner.model

import com.mazerunner.model.generator.graph.GraphMazeGenerator
import com.mazerunner.model.layout.MazeLayout
import com.mazerunner.model.runner.MazeRunner

class GraphMaze(
    mazeLayout: MazeLayout,
    mazeRunner: MazeRunner,
    mazeGenerator: GraphMazeGenerator
): Maze(mazeLayout, mazeRunner, mazeGenerator)