package com.mazerunner.model.generator

class EulerMazeGeneratorFactory : GridMazeGeneratorFactory {

    override fun makeMazeGenerator(width: Int, height: Int) = EulerMazeGenerator(width, height)

    override fun toString(): String {
        return "Euler"
    }
}