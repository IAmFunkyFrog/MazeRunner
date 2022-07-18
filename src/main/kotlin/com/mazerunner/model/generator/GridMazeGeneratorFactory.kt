package com.mazerunner.model.generator

interface GridMazeGeneratorFactory : MazeGeneratorFactory {

    fun makeMazeGenerator(width: Int, height: Int): MazeGenerator

}