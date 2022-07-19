package com.mazerunner.model.runner

interface MazeRunnerFactory {

    fun makeMazeRunner(): MazeRunner

    override fun toString(): String

}