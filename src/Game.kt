import kotlin.system.exitProcess

class Game {
    private val board = MutableList<Cell>(9) {
        Cell.Empty
    }
    private var status: Status = Status.Idle
    private lateinit var player: Player

    fun start(){
        status = Status.Running
        println(" ------------------------------ ")
        println("| Welcome to TIC-TAC-TOE Game! |")
        println("|    Pick a number from 0-8    |")
        println(" ------------------------------ ")
        getName()

        while (status is Status.Running) {
            getCell()
        }

    }

    private fun getName(){
        print("Choose Your Name: ")
        val name = readlnOrNull()
        try {
            require(value = name != null)
            player = Player(name = name, symbol = 'X')
            println("It's your move, $name")
            printBoard()
        }catch (e: Throwable){
            println("Invalid Name.")
        }
    }

    private fun getCell() {
        val input = readlnOrNull()
        try {
            require(input != null)
            val cellNumber = input.toInt()
            require(cellNumber in 0..8)
            setCell(cellNumber = cellNumber)
        }catch (e: Throwable){
            println("Invalid number.")
        }
    }

    private fun setCell(cellNumber: Int) {
        val cell = board[cellNumber]
        if(cell is Cell.Empty){
            board.set(
                index = cellNumber,
                element = Cell.Filled(player = player)
            )
            checkTheBoard()
            generateComputerMove()
            printBoard()
        }else {
            println("Cell token, Choose Another.")
        }
    }

    private fun generateComputerMove(){
        try {
            val availableCell = mutableListOf<Int>()
            board.forEachIndexed { index, cell ->
                if (cell is Cell.Empty) availableCell.add(index)
            }
            if (availableCell.isNotEmpty()) {
                val randomCell = availableCell.random()
                board.set(
                    index = randomCell,
                    element = Cell.Filled(player = Player())
                )
            }
        }catch (e: Throwable){
            println("Error: ${e.message}")
        }
    }

    private fun checkTheBoard() {
        val winner = listOf(
            listOf(0,1,2),
            listOf(3,4,5),
            listOf(6,7,8),
            listOf(0,3,6),
            listOf(0,4,8),
            listOf(1,4,7),
            listOf(2,5,8),
            listOf(2,4,6)
        )
        val player1Cells = mutableListOf<Int>()
        val player2Cells = mutableListOf<Int>()
        board.forEachIndexed { index, cell ->  
            if(cell.placeholder == 'X')
                player1Cells.add(index)
            if(cell.placeholder == 'O')
                player2Cells.add(index)
        }
        println("Your Moves: $player1Cells")
        println("Computer Moves: $player2Cells")
        run combinationLoop@ {
            winner.forEach { combination ->
                if (player1Cells.containsAll(combination)) {
                    won()
                    return@combinationLoop
                }
                if (player2Cells.containsAll(combination)) {
                    lost()
                    return@combinationLoop
                }
            }
        }
        if (board.none { it is Cell.Empty } &&
            status is Status.Running) {
            draw()
        }

        if (status is Status.GameOver){
            finish()
            playAgain()
        }
    }

    private fun playAgain() {
        println("Do You wish to play another game? Y/N: ")
        val input = readlnOrNull()
        try {
            require(input != null)
            val capitalizedInput = input.replaceFirstChar(
                Char::titlecase)
            val positive = capitalizedInput.contains('Y')
            val negative = capitalizedInput.contains('N')
            require(positive || negative)
            if (positive)
                start()
            if (negative)
                exitProcess(0)
        }catch (e: IllegalArgumentException){
            println("Wrong option, Type either 'Y' or 'N'")
            playAgain()
        }
    }

    private fun finish() {
        status = Status.Idle
        board.replaceAll { Cell.Empty }
    }

    private fun won(){
        status = Status.GameOver
        printBoard()
        println("Congratulation ${player.name}, You WON!")
    }

    private fun lost(){
        status = Status.GameOver
        printBoard()
        println("Sorry ${player.name}, You LOST!")
    }

    private fun draw(){
        status = Status.GameOver
        printBoard()
        println("DRAW!")
    }
    private fun printBoard(){
        println()
        println(" ------- ")
        println("| ${board[0].placeholder} ${board[1].placeholder} ${board[2].placeholder} |")
        println("| ${board[3].placeholder} ${board[4].placeholder} ${board[5].placeholder} |")
        println("| ${board[6].placeholder} ${board[7].placeholder} ${board[8].placeholder} |")
        println(" ------- ")
        println()
    }
}