package com.mazerunner.model.generator

interface MazeGeneratorFactory<T> {

    fun makeMazeGenerator(info: T): MazeGenerator

    override fun toString(): String

}