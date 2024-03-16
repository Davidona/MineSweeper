package mines;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Controller {
    //FXML from scene builder
    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private TextField mines;
    @FXML
    private HBox Hbox;
    @FXML
    private ImageView YouLose;
    @FXML
    private ImageView YouWon;
    @FXML
    private Text flagCount;
    @FXML
    private Label emptyFields;
    @FXML
    private Label tooMany;
    @FXML
    private Label started;
    @FXML
    private Label numbersOnly;
    @FXML
    private Label negativeNum;


    private Stage myStage;  //we keep the stage from the main file to adjust screen size
    private int intWidth;
    private int intHeight;
    private Mines minesObj;
    private Point[][] point;
    private boolean isDone; //this tells us if game is finished whether the user won or lost.
    private int flags;
    private boolean isFirst = true;     //this helps us check if its the users first move or not


    public HBox getHBox() {
        return Hbox;
    }

    public void setMyStage(Stage s) {
        myStage = s;
    }

    public void resetClicked() {
        //starting of reinitialize (we set things but to starting state)
        isDone = false;
        isFirst = true;
        emptyFields.setOpacity(0);  //we use stackPane with multiple labels and pictures above each other then we show them with toggling the opacity;
        YouLose.setOpacity(0);
        YouWon.setOpacity(0);
        tooMany.setOpacity(0);
        numbersOnly.setOpacity(0);
        negativeNum.setOpacity(0);
        //ending of reinitialize
        if (mines.getText().isEmpty() || height.getText().isEmpty() || width.getText().isEmpty()) {   //checking if fields are empty
            emptyFields.setOpacity(1);
            started.setOpacity(0);
        } else if (isNumeric(mines.getText()) || isNumeric(height.getText()) || isNumeric(width.getText())) {   //checking if fields are not numbers
            numbersOnly.setOpacity(1);
            started.setOpacity(0);
        } else {
            int mineNum = Integer.parseInt(mines.getText());

            intWidth = Integer.parseInt(height.getText());
            intHeight = Integer.parseInt(width.getText());
            if(mineNum==0&&intWidth==0&&intHeight==0){
                Hbox.getChildren().remove(1);
                StackPane sPane=new StackPane();
                getHBox().getChildren().add(sPane);
                sPane.setMinSize(678,542);
                sPane.setStyle("-fx-background-image: url('/mines/joke.png');");
                myStage.sizeToScene();
            }
            else if (mineNum >= intHeight * intWidth) {      //checking if the number of mines is more than possible
                tooMany.setOpacity(1);
                started.setOpacity(0);
            }
            else if (mineNum <= 0 || intHeight <= 0 || intWidth <= 0) {
                negativeNum.setOpacity(1);
                started.setOpacity(0);
            } else {
                started.setOpacity(0.7);    //start label
                minesObj = new Mines(intHeight, intWidth, mineNum); // we create a Mine obj
                point = new Point[intHeight][intWidth];
                flagCount.setText("" + mineNum + "");   //sets the number of flags for the game
                flags = mineNum; //keeps the number in -> flags

                Hbox.getChildren().remove(1);
                GridPane grid = new GridPane();
                Hbox.getChildren().add(grid);
                for (int i = 0; i < intHeight; i++)
                    for (int j = 0; j < intWidth; j++) {
                        Button b = new Button(minesObj.get(i, j));

                        int finalI = i;
                        int finalJ = j;
                        b.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                            if (!isDone) {

                                if (e.isPrimaryButtonDown() && e.isSecondaryButtonDown()) {
                                    bothClicked(finalI, finalJ);
                                } else if (e.isPrimaryButtonDown()) {
                                    leftClick(finalI, finalJ);
                                } else if (e.isSecondaryButtonDown()) {
                                    rightClick(finalI, finalJ);
                                }
                                if (minesObj.isDone()) {    //we check if the game is finished (winning)
                                    YouWon.setOpacity(1);
                                    started.setOpacity(0);

                                    isDone = true;
                                }
                                isFirst = false;
                            }

                        });    //we set each button on mouse click

                        b.setMinWidth(50);
                        b.setMinHeight(50);
                        b.setStyle("-fx-border-color: #2b7480; -fx-border-width: 2px;");    //color change for the button
                        b.setFont(new Font("Arial", 20));
                        point[i][j] = new Point(b);
                        grid.add(point[i][j].b, i, j);

                    }//end of inner loop

                myStage.sizeToScene();// this helps us resize the window accordingly
            }
        }
    }

    private static class Point {

        private Button b;
        private boolean flag;

        Point(Button b) {

            this.b = b;
        }

        public Button getB() {
            return b;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }

    private void updateBoard() {    // this method updates the squares on the board in case of opening, finding a mine etc

        for (int i = 0; i < intHeight; i++)
            for (int j = 0; j < intWidth; j++) {
                if (minesObj.get(i, j).equals("X")) {
                    point[i][j].getB().setText(null);   // the text is changed and replaced with a picture of a bomb
                    point[i][j].getB().setStyle("-fx-background-image: url('/mines/mine.gif');-fx-border-color: #2b7480; -fx-border-width: 2px;");
                } else if (minesObj.get(i, j).equals("F")) {
                    point[i][j].getB().setText(null);   // the text is changed and replaced with a picture of a flag
                    point[i][j].getB().setStyle("-fx-background-image: url('/mines/flag.png');-fx-border-color: #2b7480; -fx-border-width: 2px;");
                } else {
                    point[i][j].getB().setText(minesObj.get(i, j));
                    if (!minesObj.get(i, j).equals("."))
                        point[i][j].getB().setStyle("-fx-background-color: #2b7480;-fx-text-fill: #ffffff;-fx-border-width: 2px;-fx-border-color: #000000");
                }


            }
    }


    public static boolean isNumeric(String str) {   // this method checks if given string is a number or not
        try {
            Double.parseDouble(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    void changePose(int i, int j) { // we use this method to change the position of a mine in case its the first one clicked on.

        for (int k = 0; k < intHeight; k++)
            for (int w = 0; w < intWidth; w++) {
                if (minesObj.addMine(k, w)) {
                    minesObj.deleteBomb(i, j);
                    return;
                }
            }

    }

    private void leftClick(int i, int j) {
        if (!minesObj.open(i, j)) { //if its a bomb
            if (isFirst) {  // we check if its the first turn
                changePose(i, j);
                minesObj.open(i, j);
                if (minesObj.get(i, j).equals(" "))
                    updateBoard();

            } else {
                isDone = true;  // since its a bomb we change the game to finished hence isDone=true;
                YouLose.setOpacity(1);  //display you lose picture
                started.setOpacity(0);
                minesObj.setShowAll(true);      //we set show all to true since we want to show all of the bombs in the game
            }
            updateBoard();
        } else if (minesObj.get(i, j).equals(" "))  //if its open
            updateBoard();

        else if (!(minesObj.get(i, j).equals("F"))) {// if its flag
            point[i][j].getB().setText(minesObj.get(i, j));
            point[i][j].getB().setStyle("-fx-background-color: #2b7480;-fx-text-fill: #ffffff");
        }
    }

    private void rightClick(int i, int j) {
        minesObj.toggleFlag(i, j);      // flag status is changes
        if (minesObj.get(i, j).equals("F")) {   //if its flag we remove the F and put the flag picture
            point[i][j].getB().setText(null);
            point[i][j].getB().setStyle("-fx-background-image: url('/mines/flag.png');-fx-border-color: #2b7480; -fx-border-width: 2px;");
            flags--;    //we reduce the amount of flags available
            flagCount.setText("" + flags + "");
            point[i][j].setFlag(true);
        } else {
            if (point[i][j].isFlag()) {
                flags++;
                flagCount.setText("" + flags + "");
                point[i][j].setFlag(false);
            }
            if (minesObj.get(i, j).equals(".")) {
                point[i][j].getB().setStyle(null);
                point[i][j].getB().setStyle("-fx-border-color: #2b7480; -fx-border-width: 2px;");
                point[i][j].getB().setText(minesObj.get(i, j));
            }
        }
    }

    private void bothClicked(int i, int j) {
        leftClick(i, j);
        if (i != 0 && j != 0)
            leftClick(i - 1, j - 1);

        if (j != 0)
            leftClick(i, j - 1);

        if (i != 0)
            leftClick(i - 1, j);


        if (i != (intHeight - 1)) {
            leftClick(i + 1, j);

            if (j != (intWidth - 1))
                leftClick(i, j + 1);

            if (j != (intWidth - 1) && i != (intHeight - 1))
                leftClick(i + 1, j + 1);

            if(j!=0&&i != (intHeight - 1))
            leftClick(i + 1, j - 1);

            if(i!=0&&j != (intWidth - 1))
                leftClick(i - 1, j + 1);

        }
    }
}
