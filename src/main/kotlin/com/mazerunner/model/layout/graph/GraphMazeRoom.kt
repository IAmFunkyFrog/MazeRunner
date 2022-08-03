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
) : MazeRoom {

    val positionProperty: SimpleObjectProperty<Point2D> = SimpleObjectProperty(initialPosition)

    override val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>> = SimpleObjectProperty(
        MazeRoomStateWithInfo<Any>(null, MazeRoomState.UNKNOWN)
    ) // TODO serialize this property

    override fun writeExternal(objectOutput: ObjectOutput?) {
        objectOutput?.apply {
            writeDouble(positionProperty.get().x)
            writeDouble(positionProperty.get().y)
        }
    }

    override fun readExternal(objectInput: ObjectInput?) {
        objectInput?.apply {
            positionProperty.set(
                Point2D(
                    objectInput.readDouble(),
                    objectInput.readDouble()
                )
            )
        }
    }
}