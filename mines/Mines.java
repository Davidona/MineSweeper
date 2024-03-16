package mines;


import java.util.Random;

public class Mines {
    private int height;
    private int width;
    private int numMines;
    private Board[][] board;
    private int opened = 0; // counts opened points
    private boolean showAll;


    public Mines(int height, int width, int numMines) {
        Random rand = new Random();
        this.height = height;
        this.width = width;
        this.numMines = 0;
        board = new Board[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Board();
            }

        }
        for (int i = 0; i < numMines; i++) {
            if (!addMine(rand.nextInt(height), rand.nextInt(width))) {   //we add mines randomly
                i--;    // in case there is a mine there we reduce i by 1 to try again thus we keep the number of mines correct
            }
        }

    }

    public static class Board {     //this class will help us give a meaning to each point on the grid
        private boolean open;
        private boolean bomb;
        private int numOfBombs = 0;     //this holds the number of neighboring bombs
        private boolean flag;

        public boolean setBomb() {
            if (bomb)
                return false;
            bomb = true;
            return true;
        }

        public void open() {
            open = true;
        }

        public void incNeighbour(int d) {   // d is just a number we give it for example 1 to increase and -1 to decrease
            numOfBombs=numOfBombs+d;
        }

        public void setFlag() {
            flag = !flag;
        }

        public String toString(boolean f) {
            if (flag)
                return "F";
            if (!open && !f)
                return ".";
            if (bomb)   // open is true here because it is skipped the previous if
                return "X";

            if (numOfBombs == 0)
                return " ";
            else
                return "" + numOfBombs + "";
        }


        private boolean isBomb() {
            return bomb;
        }

        private boolean isFlag() {
            return flag;
        }

        private boolean isOpen() {
            return open;
        }

        private boolean hasNoBombsNear() {
            return (numOfBombs == 0);
        }

        private void removeBomb() {
            bomb=false;

        }

    }// end of class Board


    public boolean addMine(int i, int j) {
        if (i >= height || j >= width || i < 0 || j < 0)    //if the points are not on the board
            return false;
        if (board[i][j].setBomb()) {     //if we can insert a bomb there or there is one already
            numMines++;
            for (int k = i - 1; k < i + 2; k++) {
                for (int w = j - 1; w < j + 2; w++) {
                    if (!((k == i && w == j) || (k >= height || w >= width || k < 0 || w < 0)))
                        board[k][w].incNeighbour(1);     // we add 1 to all 8 neighbours
                }
            }
            return true;
        }

        return false;
    }

    public boolean open(int i, int j) {
        if (i >= height || j >= width || i < 0 || j < 0)    //in case it is out of bounds
            return false;
        if (board[i][j].isBomb())    //if its a bomb we return false
            return false;
        if (board[i][j].isFlag() || board[i][j].isOpen())   //if its open already or flagged we do not proceed
            return true;

        board[i][j].open();
        opened++;   // increase the number of opened points on board
        if (board[i][j].hasNoBombsNear()) {     //if it has no bombs near we start opening the neighbours with open() again
            open(i + 1, j + 1);
            open(i + 1, j);
            open(i, j + 1);
            open(i + 1, j - 1);
            open(i - 1, j + 1);
            open(i - 1, j - 1);
            open(i - 1, j);
            open(i, j - 1);
        }
        return true;

    }

    public void toggleFlag(int x, int y) {
        if (!board[x][y].open)
            board[x][y].setFlag();
    }

    public boolean isDone() {
        return height * width - numMines == (opened);

    }

    public String get(int i, int j) {

        return board[i][j].toString(showAll);
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public String toString() {
        StringBuilder str;
        str = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                str.append(get(i, j));
            }
            str.append('\n');

        }
        return str.toString();
    }

    public void deleteBomb(int i, int j) {      //deletes bomb
        board[i][j].removeBomb();
        for (int k = i - 1; k < i + 2; k++) {
            for (int w = j - 1; w < j + 2; w++) {
                if (!((k == i && w == j) || (k >= height || w >= width || k < 0 || w < 0)))
                    board[k][w].incNeighbour(-1);
            }
        }
        numMines--;
    }
}
