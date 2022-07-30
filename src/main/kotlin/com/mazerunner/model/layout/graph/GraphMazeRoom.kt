package com.mazerunner.model.layout.graph

import com.mazerunner.model.layout.MazeRoom
import com.mazerunner.model.layout.MazeRoomState
import com.mazerunner.model.layout.MazeRoomStateWithInfo
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import java.io.ObjectInput
import java.io.ObjectOutput

class GraphMazeRoom(
    initialPosition: Point2D
): MazeRoom {

    val positionProperty: SimpleObjectProperty<Point2D> = SimpleObjectProperty(initialPosition)

    override val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>> = SimpleObjectProperty(
        MazeRoomStateWithInfo<Any>(null, MazeRoomState.UNKNOWN)
    ) // TODO serialize this property

    override fun writeExternal(p0: ObjectOutput?) {
        TODO("Not yet implemented")
    }

    override fun readExternal(p0: ObjectInput?) {
        TODO("Not yet implemented")
    }
}