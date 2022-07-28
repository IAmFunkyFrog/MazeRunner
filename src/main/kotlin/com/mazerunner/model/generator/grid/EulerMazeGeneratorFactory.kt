package com.mazerunner.model.generator.grid

class EulerMazeGeneratorFactory : GridMazeGeneratorFactory {

    override fun makeMazeGenerator(info: Pair<Int, Int>) = EulerMazeGenerator(info.first, info.second)

    override fun toString(): String {
        return "Euler"
    }
}