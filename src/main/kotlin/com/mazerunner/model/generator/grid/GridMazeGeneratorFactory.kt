package com.mazerunner.model.generator.grid

import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.generator.MazeGeneratorFactory

interface GridMazeGeneratorFactory : MazeGeneratorFactory {

    fun makeMazeGenerator(width: Int, height: Int): MazeGenerator

}