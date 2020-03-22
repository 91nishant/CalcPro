package com.nian.calcpro.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.nian.calcpro.R;

import static com.nian.calcpro.engine.Evaluator.evaluate;
import static com.nian.calcpro.engine.Evaluator.getErrorString;
import static com.nian.calcpro.engine.Evaluator.isErrorExp;
import static com.nian.calcpro.engine.Evaluator.isFunction;
import static com.nian.calcpro.engine.Evaluator.isOperand;
import static com.nian.calcpro.engine.Evaluator.isOperator;
import static com.nian.calcpro.engine.Evaluator.isConstant;

import static com.nian.utils.CustomLogger.printVerbose;
import static com.nian.utils.CustomLogger.setAppTag;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG;
    boolean retain = false;
    int previous = -1, leftpos = 0, index = 0;
    Double value = 0.0;
    StringBuffer postfix, infixDisplay, infixEvaluate, answerString;
    String retainString;
    static final String PREVIOUS_DISPLAY = "previousStateDisplay", PREVIOUS_EVALUATE = "previousEvaluate",
            PREVIOUS_ANSWER = "previousAnswer", PREVIOUS_RESULT = "previousResult", PREVIOUS_RETAIN = "previousRetain";
    Button result, inverse, squareroot, square, cube, powere;
    Button log, naturallog, antilog, sin, cos, tan;
    Button power, pi, open, close;
    Button number0, number1, number2, number3, number4, number5, number6,
            number7, number8, number9, dot;
    Button multiply, divide, plus, minus, power10, answer, equalto, delete,
            allclear, left, right;
    Button factorial, evalue, sininverse, cosinverse, taninverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calculator);
        init();
        printVerbose(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        initialize();
        link();
        if (savedInstanceState != null) {
            if (infixDisplay.length() == 0)
                infixDisplay.append(savedInstanceState.getString(PREVIOUS_DISPLAY));
            if (infixEvaluate.length() == 0)
                infixEvaluate.append(savedInstanceState.getString(PREVIOUS_EVALUATE));
            if (answerString.length() == 0)
                answerString.append(savedInstanceState.getString(PREVIOUS_ANSWER));
            result.setText(savedInstanceState.getString(PREVIOUS_RESULT));
            if ((retainString = savedInstanceState.getString(PREVIOUS_RETAIN)).equals("true"))
                retain = true;
            else
                retain = false;
        }
    }

    private void init() {
        TAG = getClass().getSimpleName();
        printVerbose(TAG, "init");

        // Settings for generating logs with correct tags
        setAppTag(this);
    }

    @Override
    protected void onStart() {
        printVerbose(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        printVerbose(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        printVerbose(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PREVIOUS_DISPLAY, infixDisplay.toString());
        outState.putString(PREVIOUS_EVALUATE, infixEvaluate.toString());
        outState.putString(PREVIOUS_ANSWER, answerString.toString());
        outState.putString(PREVIOUS_RESULT, result.getText().toString());
        outState.putString(PREVIOUS_RETAIN, (retain ? "true" : "false"));
        super.onSaveInstanceState(outState);
    }

    private void initialize() {
        printVerbose(TAG, "initialize");
        result = (Button) findViewById(R.id.result);
        inverse = (Button) findViewById(R.id.inverse);
        squareroot = (Button) findViewById(R.id.squareroot);
        square = (Button) findViewById(R.id.square);
        cube = (Button) findViewById(R.id.cube);
        powere = (Button) findViewById(R.id.powere);

        log = (Button) findViewById(R.id.log);
        naturallog = (Button) findViewById(R.id.naturallog);
        sin = (Button) findViewById(R.id.sin);
        cos = (Button) findViewById(R.id.cos);
        tan = (Button) findViewById(R.id.tan);

        antilog = (Button) findViewById(R.id.antilog);
        power = (Button) findViewById(R.id.power);
        pi = (Button) findViewById(R.id.pi);
        open = (Button) findViewById(R.id.open);
        close = (Button) findViewById(R.id.close);

        number0 = (Button) findViewById(R.id.number0);
        number1 = (Button) findViewById(R.id.number1);
        number2 = (Button) findViewById(R.id.number2);
        number3 = (Button) findViewById(R.id.number3);
        number4 = (Button) findViewById(R.id.number4);
        number5 = (Button) findViewById(R.id.number5);
        number6 = (Button) findViewById(R.id.number6);
        number7 = (Button) findViewById(R.id.number7);
        number8 = (Button) findViewById(R.id.number8);
        number9 = (Button) findViewById(R.id.number9);
        dot = (Button) findViewById(R.id.dot);

        multiply = (Button) findViewById(R.id.multiply);
        divide = (Button) findViewById(R.id.divide);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        power10 = (Button) findViewById(R.id.power10);
        answer = (Button) findViewById(R.id.answer);
        equalto = (Button) findViewById(R.id.equalto);
        delete = (Button) findViewById(R.id.delete);
        allclear = (Button) findViewById(R.id.allclear);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);


        postfix = new StringBuffer();
        infixDisplay = new StringBuffer();
        infixEvaluate = new StringBuffer();
        answerString = new StringBuffer();

        factorial = (Button) findViewById(R.id.factorial);
        evalue = (Button) findViewById(R.id.evalue);
        sininverse = (Button) findViewById(R.id.sininverse);
        cosinverse = (Button) findViewById(R.id.cosinverse);
        taninverse = (Button) findViewById(R.id.taninverse);

        result.setGravity(Gravity.LEFT);

    }

    private void link() {
        printVerbose(TAG, "link");
        inverse.setOnClickListener(this);
        squareroot.setOnClickListener(this);
        square.setOnClickListener(this);
        cube.setOnClickListener(this);
        powere.setOnClickListener(this);

        log.setOnClickListener(this);
        naturallog.setOnClickListener(this);
        sin.setOnClickListener(this);
        cos.setOnClickListener(this);
        tan.setOnClickListener(this);

        antilog.setOnClickListener(this);
        power.setOnClickListener(this);
        pi.setOnClickListener(this);
        open.setOnClickListener(this);
        close.setOnClickListener(this);

        number0.setOnClickListener(this);
        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        dot.setOnClickListener(this);

        multiply.setOnClickListener(this);
        divide.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        power10.setOnClickListener(this);
        answer.setOnClickListener(this);
        equalto.setOnClickListener(this);
        delete.setOnClickListener(this);
        allclear.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        factorial.setOnClickListener(this);
        evalue.setOnClickListener(this);
        sininverse.setOnClickListener(this);
        cosinverse.setOnClickListener(this);
        taninverse.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        printVerbose(TAG, "onClick, arg0.getId():", arg0.getId());
        switch (arg0.getId()) {
            case R.id.inverse:
                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1))))
                    break;
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.inverse);
                infixEvaluate.append("1/");
                infixDisplay.append("1/");
                break;

            case R.id.square:
                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if (infixEvaluate.length() == 0 || !(isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        infixEvaluate.charAt(infixEvaluate.length() - 1) == ')')) {
                    break;
                }
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.square);
                infixEvaluate.append("^2");
                infixDisplay.append("^2");
                break;

            case R.id.squareroot:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.squareroot);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("q(");
                infixDisplay.append("sqrt(");
                break;

            case R.id.cube:
                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if (infixEvaluate.length() == 0 || !(isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        infixEvaluate.charAt(infixEvaluate.length() - 1) == ')')) {
                    break;
                }
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.cube);
                infixEvaluate.append("^3");
                infixDisplay.append("^3");
                break;

            case R.id.power:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.power);

                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if (infixEvaluate.length() > 0 && isOperator(infixEvaluate.charAt(infixEvaluate.length() - 1))) {
                    infixEvaluate.delete(infixEvaluate.length() - 1, infixEvaluate.length());
                    infixDisplay.delete(infixDisplay.length() - 1, infixDisplay.length());
                }
                infixEvaluate.append("^");
                infixDisplay.append("^");
                break;

            case R.id.powere:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.powere);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("e^");
                infixDisplay.append("e^");
                break;

            case R.id.log:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.log);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("l(");
                infixDisplay.append("Log(");
                break;

            case R.id.naturallog:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.naturallog);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("n(");
                infixDisplay.append("Ln(");
                break;

            case R.id.antilog:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.antilog);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("y(");
                infixDisplay.append("aLog(");
                break;

            case R.id.sin:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.sin);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("s(");
                infixDisplay.append("Sin(");
                break;

            case R.id.cos:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.cos);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("c(");
                infixDisplay.append("Cos(");
                break;

            case R.id.tan:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.tan);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("t(");
                infixDisplay.append("tan(");
                break;

            case R.id.pi:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.pi);
                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("p");
                infixDisplay.append("pi");
                break;

            case R.id.open:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.open);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append("(");
                infixDisplay.append("(");
                break;

            case R.id.close:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.close);

                infixEvaluate.append(")");
                infixDisplay.append(")");
                break;

            case R.id.delete:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.delete);

                if (infixEvaluate.length() > 0 && infixDisplay.length() > 0) {
                    if (isFunction(infixEvaluate.charAt(infixEvaluate.length() - 1))) { // for deleting displayed function
                        switch (infixEvaluate.charAt(infixEvaluate.length() - 1)) {
                            case 'u':
                            case 'v':
                            case 'w':
                            case 'y':
                                infixDisplay.delete(infixDisplay.length() - 4,
                                        infixDisplay.length());
                                break;

                            case 'q':
                                infixDisplay.delete(infixDisplay.length() - 4,
                                        infixDisplay.length());
                                break;
                            case 's':
                            case 'c':
                            case 't':
                            case 'l':
                                infixDisplay.delete(infixDisplay.length() - 3,
                                        infixDisplay.length());
                                break;
                            case 'n':
                            case 'p':
                                infixDisplay.delete(infixDisplay.length() - 2,
                                        infixDisplay.length());
                                break;
                            case '~':
                            case 'f':
                                infixDisplay.deleteCharAt(infixDisplay.length() - 1);
                                break;
                        }
                    } else if (isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1))) {
                        switch (infixEvaluate.charAt(infixEvaluate.length() - 1)) {
                            case 'p':
                                infixDisplay.delete(infixDisplay.length() - 2, infixDisplay.length());
                                break;
                            case 'e':
                                infixDisplay.deleteCharAt(infixDisplay.length() - 1);
                                break;
                        }
                    } else
                        infixDisplay.deleteCharAt(infixDisplay.length() - 1);
                    infixEvaluate.deleteCharAt(infixEvaluate.length() - 1);
                }

                if (infixDisplay.length() == 0)
                    changePreviousBackground(previous);
                break;

            case R.id.allclear:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.allclear);

                infixEvaluate.delete(0, infixEvaluate.length());
                infixDisplay.delete(0, infixDisplay.length());
                break;

            case R.id.divide:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.divide);

                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if (infixEvaluate.length() > 0 && isOperator(infixEvaluate.charAt(infixEvaluate.length() - 1))) {
                    infixEvaluate.delete(infixEvaluate.length() - 1, infixEvaluate.length());
                    infixDisplay.delete(infixDisplay.length() - 1, infixDisplay.length());
                }
                infixEvaluate.append("/");
                infixDisplay.append("/");
                break;

            case R.id.multiply:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.multiply);

                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if (infixEvaluate.length() > 0 && isOperator(infixEvaluate.charAt(infixEvaluate.length() - 1))) {
                    infixEvaluate.delete(infixEvaluate.length() - 1, infixEvaluate.length());
                    infixDisplay.delete(infixDisplay.length() - 1, infixDisplay.length());
                }
                infixEvaluate.append("X");
                infixDisplay.append("X");
                break;

            case R.id.plus:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.plus);

                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if (infixEvaluate.length() > 0 && isOperator(infixEvaluate.charAt(infixEvaluate.length() - 1))) {
                    infixEvaluate.delete(infixEvaluate.length() - 1, infixEvaluate.length());
                    infixDisplay.delete(infixDisplay.length() - 1, infixDisplay.length());
                }

                infixEvaluate.append("+");
                infixDisplay.append("+");
                break;

            case R.id.minus:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.minus);

                if (retain) {
                    infixEvaluate.append(answerString.toString());
                    infixDisplay.append(answerString.toString());
                }
                if ((infixEvaluate.length() != 0) && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || infixEvaluate.charAt(infixEvaluate.length() - 1) == ')')) {
                    infixEvaluate.append("-");
                    infixDisplay.append("-");
                } else {
                    infixEvaluate.append("~");
                    infixDisplay.append("~");
                }
                break;

            case R.id.power10:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.power10);
                if (infixEvaluate.length() == 0 || !(isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)) ||
                        isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("10^");
                    infixDisplay.append("10^");
                } else {
                    infixEvaluate.append("X10^");
                    infixDisplay.append("X10^");
                }
                break;

            case R.id.number0:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number0);

                infixEvaluate.append("0");
                infixDisplay.append("0");
                break;

            case R.id.number1:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number1);

                infixEvaluate.append("1");
                infixDisplay.append("1");
                break;

            case R.id.number2:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number2);

                infixEvaluate.append("2");
                infixDisplay.append("2");
                break;

            case R.id.number3:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number3);
                infixEvaluate.append("3");
                infixDisplay.append("3");
                break;

            case R.id.number4:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number4);

                infixEvaluate.append("4");
                infixDisplay.append("4");
                break;

            case R.id.number5:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number5);

                infixEvaluate.append("5");
                infixDisplay.append("5");
                break;

            case R.id.number6:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number6);

                infixEvaluate.append("6");
                infixDisplay.append("6");
                break;

            case R.id.number7:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number7);

                infixEvaluate.append("7");
                infixDisplay.append("7");
                break;

            case R.id.number8:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number8);

                infixEvaluate.append("8");
                infixDisplay.append("8");
                break;

            case R.id.number9:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.number9);

                infixEvaluate.append("9");
                infixDisplay.append("9");
                break;

            case R.id.dot:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.dot);

                infixEvaluate.append(".");
                infixDisplay.append(".");
                break;

            case R.id.answer:
                if (answerString.length() == 0)
                    break;
                if (infixEvaluate.length() != 0 && isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1)))
                    break;                                                              //for logical errors
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.answer);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixEvaluate.append(answerString.toString());
                infixDisplay.append(answerString.toString());
                break;

            case R.id.equalto:
                if (retain == true)
                    break;
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.equalto);
                if (isErrorExp(infixEvaluate))
                    result.setText(getErrorString());
                else {
                    //postfix = new StringBuffer(InfixToPostfix(infixEvaluate));
                    //value = evaluate(postfix);
                    value = evaluate(infixEvaluate);
                    answerString.delete(0, answerString.length());
                    answerString.append(value.toString());
                    if (answerString.indexOf("E") != -1)
                        answerString.replace(answerString.indexOf("E"), answerString.indexOf("E") + 1, "X10^");
                    result.setText(answerString.toString());

                    index = 0;
                    while (index < answerString.length()) {
                        if (answerString.charAt(index) == '-')
                            answerString.replace(index, index + 1, "~");
                        index++;
                    }
                }
                infixEvaluate.delete(0, infixEvaluate.length());
                infixDisplay.delete(0, infixDisplay.length());
                retain = true;
                break;

            case R.id.left:
                if (leftpos == -1) {
                    leftpos = infixDisplay.length() - 50;
                    if (leftpos > 0)
                        result.setText(infixDisplay.substring(--leftpos));
                } else if (leftpos > 0)
                    result.setText(infixDisplay.substring(--leftpos));
                break;

            case R.id.right:
               if (leftpos != -1 && (infixDisplay.length() - leftpos) > 50)
                    result.setText(infixDisplay.substring(++leftpos));
                break;

            case R.id.factorial:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.factorial);
                infixDisplay.append("!");
                infixEvaluate.append("!");
                break;

            case R.id.evalue:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.evalue);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixDisplay.append("e");
                infixEvaluate.append("e");
                break;

            case R.id.sininverse:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.sininverse);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixDisplay.append("asin(");
                infixEvaluate.append("u(");
                break;

            case R.id.cosinverse:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.cosinverse);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixDisplay.append("acos(");
                infixEvaluate.append("v(");
                break;

            case R.id.taninverse:
                if (previous != -1)
                    changePreviousBackground(previous);
                changeCurrentBackground(R.id.taninverse);

                if (infixEvaluate.length() != 0 && (isOperand(infixEvaluate.charAt(infixEvaluate.length() - 1))
                        || isConstant(infixEvaluate.charAt(infixEvaluate.length() - 1)))) {
                    infixEvaluate.append("X");
                    infixDisplay.append("X");
                }
                infixDisplay.append("atan(");
                infixEvaluate.append("w(");
                break;
        }
        if (arg0.getId() != R.id.equalto && arg0.getId() != R.id.result && arg0.getId() != R.id.left && arg0.getId() != R.id.right) {
            retain = false;
            if (infixDisplay.length() <= 50)
                result.setText(infixDisplay.toString());
            else
                result.setText(infixDisplay.substring(infixDisplay.length() - 50));
        }
    }

    private void changePreviousBackground(int a) {
        printVerbose(TAG, "changePreviousBackground, a:", a);
        switch (a) {
            case R.id.inverse:
                inverse.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.square:
                square.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.squareroot:
                squareroot.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.cube:
                cube.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.power:
                power.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.powere:
                powere.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.log:
                log.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.naturallog:
                naturallog.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.sin:
                sin.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.cos:
                cos.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.tan:
                tan.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.antilog:
                antilog.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.pi:
                pi.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.open:
                open.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.close:
                close.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.delete:
                delete.setBackgroundResource(R.drawable.red);
                break;
            case R.id.allclear:
                break;
            case R.id.divide:
                divide.setBackgroundResource(R.drawable.blue);
                break;
            case R.id.multiply:
                multiply.setBackgroundResource(R.drawable.blue);
                break;
            case R.id.plus:
                plus.setBackgroundResource(R.drawable.blue);
                break;
            case R.id.minus:
                minus.setBackgroundResource(R.drawable.blue);
                break;
            case R.id.power10:
                power10.setBackgroundResource(R.drawable.blue);
                break;
            case R.id.number0:
                number0.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number1:
                number1.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number2:
                number2.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number3:
                number3.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number4:
                number4.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number5:
                number5.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number6:
                number6.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number7:
                number7.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number8:
                number8.setBackgroundResource(R.drawable.black);
                break;
            case R.id.number9:
                number9.setBackgroundResource(R.drawable.black);
                break;
            case R.id.dot:
                dot.setBackgroundResource(R.drawable.black);
                break;
            case R.id.answer:
                answer.setBackgroundResource(R.drawable.black);
                break;
            case R.id.equalto:
                break;
            case R.id.factorial:
                factorial.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.evalue:
                evalue.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.sininverse:
                sininverse.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.cosinverse:
                cosinverse.setBackgroundResource(R.drawable.purple);
                break;
            case R.id.taninverse:
                taninverse.setBackgroundResource(R.drawable.purple);
                break;
        }
    }

    private void changeCurrentBackground(int a) {
        printVerbose(TAG, "changeCurrentBackground, a:", a);
        previous = a;
        leftpos = -1;
        switch (a) {
            case R.id.inverse:
                inverse.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.square:
                square.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.squareroot:
                squareroot.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.cube:
                cube.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.power:
                power.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.powere:
                powere.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.log:
                log.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.naturallog:
                naturallog.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.sin:
                sin.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.cos:
                cos.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.tan:
                tan.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.antilog:
                antilog.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.pi:
                pi.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.open:
                open.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.close:
                close.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.delete:
                delete.setBackgroundResource(R.drawable.redclick);
                break;
            case R.id.allclear:
                previous = -1;
                break;
            case R.id.divide:
                divide.setBackgroundResource(R.drawable.blueclick);
                break;
            case R.id.multiply:
                multiply.setBackgroundResource(R.drawable.blueclick);
                break;
            case R.id.plus:
                plus.setBackgroundResource(R.drawable.blueclick);
                break;
            case R.id.minus:
                minus.setBackgroundResource(R.drawable.blueclick);
                break;
            case R.id.power10:
                power10.setBackgroundResource(R.drawable.blueclick);
                break;
            case R.id.number0:
                number0.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number1:
                number1.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number2:
                number2.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number3:
                number3.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number4:
                number4.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number5:
                number5.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number6:
                number6.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number7:
                number7.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number8:
                number8.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.number9:
                number9.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.dot:
                dot.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.answer:
                answer.setBackgroundResource(R.drawable.blackclick);
                break;
            case R.id.equalto:
                previous = -1;
                break;
            case R.id.factorial:
                factorial.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.evalue:
                evalue.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.sininverse:
                sininverse.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.cosinverse:
                cosinverse.setBackgroundResource(R.drawable.purpleclick);
                break;
            case R.id.taninverse:
                taninverse.setBackgroundResource(R.drawable.purpleclick);
                break;
        }
    }
}