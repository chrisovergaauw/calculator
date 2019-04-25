package nl.overgaauw;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nl.overgaauw.service.ArithmeticService;

public class BasicController {

    private ArithmeticService arithmeticService;

    @FXML
    public Label calculationDisplay;

    // numbers
    @FXML
    public Button one;
    @FXML
    public Button two;
    @FXML
    public Button three;
    @FXML
    public Button four;
    @FXML
    public Button five;
    @FXML
    public Button six;
    @FXML
    public Button seven;
    @FXML
    public Button eight;
    @FXML
    public Button nine;


    // operators
    @FXML
    public Button divide;
    @FXML
    public Button times;
    @FXML
    public Button zero;
    @FXML
    public Button comma;
    @FXML
    public Button equals;
    @FXML
    public Button plus;
    @FXML
    public Button minus;

    // functions
    @FXML
    public Button clear;
    @FXML
    public Button invert;
    @FXML
    public Button percentage;

    public void initialize(){
        arithmeticService = new ArithmeticService();

        zero.setOnAction(e -> arithmeticService.handleNumInput(0));
        one.setOnAction(e -> arithmeticService.handleNumInput(1));
        two.setOnAction(e -> arithmeticService.handleNumInput(2));
        three.setOnAction(e -> arithmeticService.handleNumInput(3));
        four.setOnAction(e -> arithmeticService.handleNumInput(4));
        five.setOnAction(e -> arithmeticService.handleNumInput(5));
        six.setOnAction(e -> arithmeticService.handleNumInput(6));
        seven.setOnAction(e -> arithmeticService.handleNumInput(7));
        eight.setOnAction(e -> arithmeticService.handleNumInput(8));
        nine.setOnAction(e -> arithmeticService.handleNumInput(9));
        comma.setOnAction(e -> arithmeticService.handleDotInput());

        plus.setOnAction(e -> arithmeticService.handleOperatorInput('+'));
        minus.setOnAction(e -> arithmeticService.handleMinusInput());
        times.setOnAction(e -> arithmeticService.handleOperatorInput('*'));
        divide.setOnAction(e -> arithmeticService.handleOperatorInput('/'));

        equals.setOnAction(e -> arithmeticService.performCalculation());

        clear.setOnAction(e -> arithmeticService.clearAndReturnAllClearState());
        arithmeticService.allClear.addListener(this::toggleClearButton);

        invert.setOnAction(e -> arithmeticService.invertCurrentOperand());

        calculationDisplay.textProperty().bind(arithmeticService.equation);
        calculationDisplay.textProperty().addListener(this::resizeDisplay);

    }

    private void resizeDisplay(Observable e) {
        if ( calculationDisplay.textProperty().getValue().length() > 10 ) {
            calculationDisplay.setStyle("-fx-font-size: 20;");
            System.out.println(calculationDisplay.getStyle());
        } else {
            calculationDisplay.setStyle("-fx-font-size: 28;");
        }
    }

    private void toggleClearButton(Observable e) {
        if (arithmeticService.allClear.getValue()) {
            clear.textProperty().setValue("AC");
        } else {
            clear.textProperty().setValue("C");
        }
    }
}
