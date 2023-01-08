package org.olafneumann.mahjong.points.model

data class Hand(
    val figure1: Combination?,
    val figure2: Combination?,
    val figure3: Combination?,
    val figure4: Combination?,
    val pair: Combination?,
    val bonusTiles: List<Tile>,
) {
    val fullFigures: List<Combination> = listOf(figure1, figure2, figure3, figure4).mapNotNull { it }
    val allFigures: List<Combination> = listOf(figure1, figure2, figure3, figure4, pair).mapNotNull { it }

    val allTiles: List<Tile> = allFigures.flatMap { it.getTiles() }

    val isMahjong: Boolean = allFigures.size == 5
}
