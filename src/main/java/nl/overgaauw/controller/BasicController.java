package nl.overgaauw.controller;

import javafx.animation.FadeTransition;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nl.overgaauw.component.Calculator;

import java.util.ArrayList;
import java.util.List;

public class BasicController {

    private Calculator calculator;

    @FXML
    public Label calculationDisplay;
    private FadeTransition calculationDisplayBlink;

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

    private List<Button> operators = new ArrayList<>();

    // functions
    @FXML
    public Button clear;
    @FXML
    public Button invert;
    @FXML
    public Button percentage;

    public void initialize(){
        calculator = new Calculator();

        zero.setOnAction(e -> calculator.handleNumInput(0));
        one.setOnAction(e -> calculator.handleNumInput(1));
        two.setOnAction(e -> calculator.handleNumInput(2));
        three.setOnAction(e -> calculator.handleNumInput(3));
        four.setOnAction(e -> calculator.handleNumInput(4));
        five.setOnAction(e -> calculator.handleNumInput(5));
        six.setOnAction(e -> calculator.handleNumInput(6));
        seven.setOnAction(e -> calculator.handleNumInput(7));
        eight.setOnAction(e -> calculator.handleNumInput(8));
        nine.setOnAction(e -> calculator.handleNumInput(9));
        comma.setOnAction(e -> calculator.handleDotInput());

        plus.setOnAction(e -> calculator.handleOperatorInput('+'));
        minus.setOnAction(e -> calculator.handleMinusInput());
        times.setOnAction(e -> calculator.handleOperatorInput('*'));
        divide.setOnAction(e -> calculator.handleOperatorInput('/'));

        equals.setOnAction(e -> calculator.performCalculation());

        clear.setOnAction(e -> calculator.clearAndReturnAllClearState());
        calculator.allClearState.addListener(this::toggleClearButton);

        invert.setOnAction(e -> calculator.negateCurrentOperand());

        percentage.setOnAction(e -> calculator.handlePercentageInput());

        calculationDisplay.textProperty().bind(calculator.calculationDisplay);
        calculationDisplay.textProperty().addListener(this::resizeDisplay);

        calculationDisplayBlink = new FadeTransition(Duration.seconds(0.1), calculationDisplay);
        calculationDisplayBlink.setFromValue(0.0);
        calculationDisplayBlink.setToValue(1.0);
        calculationDisplayBlink.setCycleCount(1);

    }

    private void resizeDisplay(Observable e) {
        if ( calculationDisplay.textProperty().getValue().length() > 10 ) {
            calculationDisplay.setStyle("-fx-font-size: 20;");
            System.out.println(calculationDisplay.getStyle());
        } else {
            calculationDisplay.setStyle("-fx-font-size: 28;");
        }
        calculationDisplayBlink.play();
    }

    private void toggleClearButton(Observable e) {
        if (calculator.allClearState.getValue()) {
            clear.textProperty().setValue("AC");
        } else {
            clear.textProperty().setValue("C");
        }
    }
}
