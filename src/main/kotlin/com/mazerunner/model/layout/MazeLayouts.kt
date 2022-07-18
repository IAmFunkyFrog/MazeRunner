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

fun <T> MazeLayout.setCurrentRoom(mazeRoom: MazeRoom, info: T, oldCurrentRoomNewState: MazeRoomState = MazeRoomState.VISITED) {
    this.getRooms().firstOrNull {
        it == mazeRoom
    } ?: throw RuntimeException("MazeLayout must contain mazeRoom")

    this.getRooms().forEach {
        if(it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT) it.stateProperty.set(
            MazeRoomStateWithInfo(it.stateProperty.get().info, oldCurrentRoomNewState)
        )
    }

    mazeRoom.stateProperty.set(
        MazeRoomStateWithInfo(info, MazeRoomState.CURRENT)
    )
}

fun MazeLayout.setCurrentRoom(mazeRoom: MazeRoom, oldCurrentRoomNewState: MazeRoomState = MazeRoomState.VISITED) {
    this.setCurrentRoom(mazeRoom, mazeRoom.stateProperty.get().info, oldCurrentRoomNewState)
}

// This layout type guarantees that all MazeRooms are GridMazeRoom and also
// if there is MazeDoor between 2 GridMazeRoom then distance between them is 1
// FIXME test code
fun generateGridMazeLayout(width: Int, height: Int): MazeLayout {
    if (width < 0 || height < 0) return MazeLayoutImpl(emptyList(), emptyList(), MazeLayoutState.GENERATED)

    val arrayList = ArrayList<GridMazeRoom>()
    for (i in (0..width)) {
        for (j in (0..height)) {
            arrayList.add(GridMazeRoom(i, j))
        }
    }

    return MazeLayoutImpl(arrayList, emptyList(), MazeLayoutState.GENERATED)
}