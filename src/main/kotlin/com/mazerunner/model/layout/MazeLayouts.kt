package com.mazerunner.model.layout

fun MazeLayout.getAdjoiningRooms(mazeRoom: MazeRoom): List<MazeRoom> {
    val incidentDoors = this.getDoors().filter {
        it.getRooms().first == mazeRoom || it.getRooms().second == mazeRoom
    }

    return incidentDoors.map {
        if (it.getRooms().first == mazeRoom) it.getRooms().second
        else it.getRooms().first
    }
}

fun <T, V> MazeLayout.setCurrentRoom(
    mazeRoom: MazeRoom,
    info: T,
    oldCurrentRoomNewInfo: V,
    oldCurrentRoomNewState: MazeRoomState
) {
    this.getRooms().firstOrNull {
        it == mazeRoom
    } ?: throw RuntimeException("MazeLayout must contain mazeRoom")

    this.getRooms().forEach {
        if (it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT) it.stateProperty.set(
            MazeRoomStateWithInfo(
                oldCurrentRoomNewInfo ?: it.stateProperty.get().info,
                oldCurrentRoomNewState
            )
        )
    }

    mazeRoom.stateProperty.set(
        MazeRoomStateWithInfo(info, MazeRoomState.CURRENT)
    )
}

fun <T> MazeLayout.setCurrentRoom(
    mazeRoom: MazeRoom,
    info: T,
    oldCurrentRoomNewState: MazeRoomState = MazeRoomState.VISITED
) {
    return setCurrentRoom(mazeRoom, info, null, oldCurrentRoomNewState)
}

fun MazeLayout.setCurrentRoom(mazeRoom: MazeRoom, oldCurrentRoomNewState: MazeRoomState = MazeRoomState.VISITED) {
    this.setCurrentRoom(mazeRoom, mazeRoom.stateProperty.get().info, oldCurrentRoomNewState)
}