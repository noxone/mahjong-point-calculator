package org.olafneumann.mahjong.points.game

data class HandInput(
    val tiles: List<Tile>
) {
    fun addTile(tile: Tile): HandInput {
        val newTiles = tiles.toMutableList()
        newTiles.add(tile)
        if (!newTiles.areTilesValid()) {
            return this
        }
        return copy(tiles = newTiles)
    }

    private fun List<Tile>.areTilesValid(): Boolean {
        val maxFourOfEachTile = fold(mutableMapOf<Tile, Int>())
        { map, tile ->
            map[tile] = map.getOrElse(tile) { 0 } + 1
            map
        }
            .values
            .find { it > MAX_IDENTICAL_TILE_COUNT } == null

        return maxFourOfEachTile
    }

    /*val hand: Hand get() {
        return Hand(
            figure1 = Combination()
        )
    }*/

    companion object {
        private const val MAX_IDENTICAL_TILE_COUNT = 4
    }
}