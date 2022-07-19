package com.mazerunner.model.runner

class RandomMazeRunnerFactory : MazeRunnerFactory {

    override fun makeMazeRunner() = RandomMazeRunner()

    override fun toString() = "Random"
}