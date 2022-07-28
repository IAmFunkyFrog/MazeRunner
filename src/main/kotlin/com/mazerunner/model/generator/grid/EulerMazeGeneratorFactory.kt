package com.mazerunner.model.generator.grid

class EulerMazeGeneratorFactory : GridMazeGeneratorFactory {

    override fun makeMazeGenerator(width: Int, height: Int) = EulerMazeGenerator(width, height)

    override fun toString(): String {
        return "Euler"
    }
}