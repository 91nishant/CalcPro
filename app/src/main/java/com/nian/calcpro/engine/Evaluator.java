package com.nian.calcpro.engine;


import static com.nian.utils.CustomLogger.printVerbose;

public final class Evaluator {
    private static final String TAG = "Evaluator";
    private static StringBuffer errorString = new StringBuffer();

    Evaluator() {

    }

    public static String InfixToPostfix(StringBuffer infix) {
        printVerbose(TAG, "InfixToPostfix, infix:", infix);
        int i = 0, len = infix.length();
        StringBuffer postfix = new StringBuffer();
        char ch, top;
        while (i < len) {
            ch = infix.charAt(i);
            if (isOperand(ch)) {
                postfix.append(ch);
                i++;
                continue;
            }
            postfix.append(" ");
            if(isConstant(ch)) {
                postfix.append(ch);
                postfix.append(" ");
            }
            if (isOperator(ch)) {
                top = stack.top1();
                while (isHighSamePriority(top, ch)) {
                    postfix.append(stack.pop1());
                    top = stack.top1();
                }
                stack.push(ch);
            }

            if (isFunction(ch)) {
                top = stack.top1();
                while (isHighSamePriority(top, ch)) {
                    postfix.append(stack.pop1());
                    top = stack.top1();
                }
                stack.push(ch);
            }

            if (ch == '(')
                stack.push(ch);
            if (ch == ')')
                while ((top = stack.pop1()) != '(')
                    postfix.append(top);
            i++;
        }
        while ((ch = stack.pop1()) != 'E')
            postfix.append(ch);
        printVerbose(TAG, "InfixToPostfix, postfix:", postfix);
        return postfix.toString();
    }

    public static double evaluate(StringBuffer postfix) {
        printVerbose(TAG, "evaluate, postfix:", postfix);
        double result = 0, operand = 0;
        int i = 0, len = postfix.length(),constant = 0;
        StringBuffer stringOperand = new StringBuffer();
        char ch;
        while (i < len) {
            ch = postfix.charAt(i);
            if (ch == ' ') {
                i++;
                continue;
            }
            if (isOperand(ch)) {
                stringOperand.delete(0, stringOperand.length());
                while (isOperand(ch)) {
                    stringOperand.append(ch);
                    ch = postfix.charAt(++i);
                }
                stack.push(Float.parseFloat(stringOperand.toString()));
            }
            if (isConstant(ch)) {
                constant++;
                switch(ch) {
                    case 'p':
                        stack.push((float) Math.PI);
                        break;
                    case 'e':
                        stack.push((float) Math.E);
                        break;
                }
            }
            if (isOperator(ch)) {
                operand = stack.pop2();
                result = stack.pop2();
                result = combine(result, operand, ch);
                stack.push(result);
            }
            if (isFunction(ch)) {
                result = stack.pop2();
                result = combine(result, ch);
                stack.push(result);
            }
            i++;
        }
        if (constant == 1 && result == 0)
            return stack.pop2();
        printVerbose(TAG, "evaluate, result:", result);
        return result;
    }

    public static boolean isErrorExp(StringBuffer infix2) {
        printVerbose(TAG, "isErrorExp, infix2:", infix2);
        int i = 0, len = infix2.length(), NoOfOperator = 0, NoOfOperand = 0, NoOfFunction = 0,
                NoOfDot = 0, NoOfConstant = 0, balance = 0;
        char ch;
        errorString.delete(0, errorString.length());
        while (i < len) {
            ch = infix2.charAt(i);
            if (ch == ' ') {
                i++;
                continue;
            } else if (isOperand(ch)) {
                NoOfOperand++;
                if (ch == '.')
                    NoOfDot++;
                System.out.println("dot = " + NoOfDot);
                for (; i>=0 && i<len-1 && isOperand(infix2.charAt(i+1)); i++)
                    if (infix2.charAt(i+1) == '.')
                        NoOfDot++;
                System.out.println("dot = " + NoOfDot);
                if (NoOfDot > 1) {
                    errorString.append("Error : 'Decimal Points' ");
                    return true;
                }
            }
            else if(isConstant(ch)) {
                NoOfOperand++;
                NoOfConstant++;
            }
            else if (isOperator(ch))
                NoOfOperator++;
            else if (ch == '(')
                balance++;
            else if (ch == ')')
                balance--;
            else if (isFunction(ch)) {
                if (i!=0 && isFunction(infix2.charAt(i-1))) {
                    errorString.append("Error : Use 'Parenthesis' to separate functions");
                    return true;
                }
                NoOfFunction++;
            }
            else
                return true;
            i++;
            NoOfDot=0;
        }

        if (NoOfOperand == (NoOfOperator + 1) && balance == 0) {
            if (NoOfOperand == 1 && NoOfFunction == 0 && NoOfConstant==0) {
                errorString.append("Error : Use some mathematical 'Sign' or 'function'");
                return true;
            }
            else
                return false;
        } else {
            errorString.append("Error : Unbalanced 'Parenthesis' or 'Operators'");
            return true;
        }
    }

    private static double combine(double result, char ch) {
        printVerbose(TAG, "combine, result:", result, ", ch:", ch);
        double combinationResult = 0;
        switch (ch) {
            case '~':
                combinationResult = -1f*result;
                break;
            case 's':
                combinationResult = Math.sin(result*(Math.PI/180));
                break;
            case 'c':
                combinationResult = Math.cos(result*(Math.PI/180));
                break;
            case 't':
                combinationResult = Math.tan(result*(Math.PI/180));
                break;
            case 'l':
                combinationResult = Math.log10(result);
                break;
            case 'n':
                combinationResult = Math.log(result);
                break;
            case 'q':
                combinationResult = Math.sqrt(result);
                break;
            case 'u':
                combinationResult = Math.asin(result)*180/Math.PI;
                break;
            case 'v':
                combinationResult = Math.acos(result)*180/Math.PI;
                break;
            case 'w':
                combinationResult = Math.atan(result)*180/Math.PI;
                break;
            case 'y':
                combinationResult = Math.pow(10, result);
                break;
            case '!':
                combinationResult = factorial(result);
                break;
        }
        printVerbose(TAG, "combine, combinationResult:", combinationResult);
        return combinationResult;
    }

    private static double factorial (double result) {
        printVerbose(TAG, "factorial, result:", result);
        double fact = 1;
        for ( ; result > 1 ; result--)
            fact *= result;
        printVerbose(TAG, "factorial, fact:", fact);
        return fact;
    }
    private static double combine(double result, double operand, char ch) {
        printVerbose(TAG, "combine, result:", result, ", operand:", operand, ", ch:", ch);
        double combinationResult = 0;
        switch (ch) {
            case '-':
                combinationResult = result - operand;
                break;
            case '+':
                combinationResult = result + operand;
                break;
            case 'X':
                combinationResult = result * operand;
                break;
            case '/':
                combinationResult = result / operand;
                break;
            case '^':
                combinationResult = Math.pow(result, operand);
                break;
        }
        printVerbose(TAG, "combine, combinationResult:", combinationResult);
        return combinationResult;
    }



    private static boolean isHighSamePriority(char top, char ch) {
        switch (top) {
            case 'E':
                break;
            case '~':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':

                    case '^':
                    case '~':
                        return true;
                }
            case '!':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':

                    case '^':
                    case '!':
                        return true;
                }
            case '^':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':

                    case '^':
                        return true;
                }
            case 'u':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'v':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'w':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'y':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 's':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'c':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 't':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'l':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'n':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case 'q':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':

                    case 's':
                    case 'c':
                    case 't':
                    case 'l':
                    case 'n':
                    case 'q':

                    case 'u':
                    case 'v':
                    case 'w':
                    case 'y':
                        return true;
                }
            case '/':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                    case '/':
                        return true;
                }
            case 'X':
                switch (ch) {
                    case '-':
                    case '+':
                    case 'X':
                        return true;
                }
            case '+':
                switch (ch) {
                    case '-':
                    case '+':
                        return true;
                }
            case '-':
                switch (ch) {
                    case '-':
                    case '+':
                        return true;
                }
        }
        return false;
    }

    public static boolean isOperand(char ch) {
        switch (ch) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
                return true;
        }
        return false;
    }

    public static boolean isOperator(char ch) {
        switch (ch) {
            case '+':
            case '-':
            case '/':
            case 'X':
            case '^':
                return true;
        }
        return false;
    }

    public static boolean isFunction(char ch) {
        switch (ch) {
            case '~':
            case 's':
            case 'c':
            case 't':
            case 'l':
            case 'n':
            case 'q':
            case 'u':
            case 'v':
            case 'w':
            case 'y':
            case '!':
                return true;
        }
        return false;
    }

    public static boolean isConstant(char ch) {
        switch(ch) {
            case 'p':
            case 'e':
                return true;
        }
        return false;
    }

    public static String getErrorString() {
        return errorString.toString();
    }
}

class stack {
    static char stack1[] = new char[20];
    static double stack2[] = new double[20];
    static int top1 = -1, top2 = -1;

    static char top1() {
        if (top1 != -1)
            return stack1[top1];
        else
            return 'E';
    }

    static void push(char ch) {
        if (top1 < 20)
            stack1[++top1] = ch;
        else
            System.out.println("Stack Full");
    }

    static char pop1() {
        if (top1 != -1) {
            return stack1[top1--];
        } else
            return 'E';
    }

    public static void push(double x) {
        if (top2 < 20)
            stack2[++top2] = x;
        else
            System.out.println("Stack Full");
    }

    public static double pop2() {
        if (top2 != -1)
            return stack2[top2--];
        else
            return 1.23f;
    }

}
