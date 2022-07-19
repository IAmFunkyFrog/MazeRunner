package com.mazerunner.model.runner

class BFSMazeRunnerFactory : MazeRunnerFactory {
    override fun makeMazeRunner() = BFSMazeRunner()

    override fun toString() = "BFS"

}