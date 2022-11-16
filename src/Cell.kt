sealed class Cell (val placeholder: Char) {
    object Empty: Cell('_')
    data class Filled(
        val player: Player
        ): Cell(player.symbol)
}
