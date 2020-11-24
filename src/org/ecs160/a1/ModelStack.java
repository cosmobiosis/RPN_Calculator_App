package org.ecs160.a1;

import java.util.*;

public class ModelStack {
    private Stack<String> stack;
    private final String DEFAULT_STR = Helper.getInstance().getDefaultStr();
    private String name = Helper.getInstance().getNewId(); // meta data init;
    private double rootCurveA = -1;

    public ModelStack() {
        // meta data init
        stack = new Stack<String>();
        // System.out.println(name);
    }

    public ModelStack(String[] vals) {
        // Suppose a0 is the top of the stack, then the file format would be:
        // a0, a1, a2, a3....
        // first reverse the list read from the storage
        stack = new Stack<String>();
        for (int i = vals.length - 1; i >= 0; i--) {
            stack.push(vals[i]);
        }
        // System.out.println(name);
    }

    public ModelStack(Vector<String> vals) {
        // Suppose a0 is the top of the stack, then the file format would be:
        // a0, a1, a2, a3....
        // first reverse the list read from the storage
        stack = new Stack<String>();
        for (int i = vals.size() - 1; i >= 0; i--) {
            stack.push(vals.get(i));
        }
        // System.out.println(name);
    }

    public ModelStack(Stack<String> stackParam) {
        stack = (Stack<String>) stackParam.clone();
    }

    public String[] getTopFourStr() {
        Stack<String> copy = (Stack<String>) stack.clone();
        String[] retVals = new String[4];
        int index = 0;
        while (!copy.empty() && index < 4) {
            retVals[index] = copy.pop();
            index += 1;
        }
        for (; index < 4; index++) {
            retVals[index] = DEFAULT_STR;
        }
        return retVals;
    }

    public String getTopStr() {
        if (stack.size() < 1 || stack.peek().length() == 0) {
            return DEFAULT_STR;
        }
        return stack.peek();
    }

    public double getTopNumber() {
        if (stack.size() < 1 || stack.peek().length() == 0) {
            return 0.0;
        }
        return Double.parseDouble(stack.peek());
    }

    public double get2ndTopNumber() {
        if (stack.size() < 2 || stack.get(stack.size() - 2).length() == 0) {
            // if there's only one or zero elements
            return 0.0;
        }
        return Double.parseDouble(stack.get(stack.size() - 2));
    }

    public void popTop() {
        if (!stack.empty()) {
            stack.pop();
        }
    }

    public double popTopNumber() {
        if (!stack.empty()) {
            return Double.parseDouble(stack.pop());
        }
        return 0;
    }

    public void updateTopStr(String val) {
        if (!stack.empty()) {
            stack.pop();
        }
        stack.push(val);
    }

    public void updateTopNumber(double val) {
        // automatically change top of the stack to formatted new double
        if (!stack.empty()) {
            stack.pop();
        }
        stack.push(fmt(val));
    }

    public void mergeTopTwoNumbers(double newVal) {
        if (!stack.empty()) {
            stack.pop();
        }
        if (!stack.empty()) {
            stack.pop();
        }
        stack.push(fmt(newVal));
    }

    public void switchTopTwo() {
        if (stack.empty()) {
            return;
        }
        if (stack.size() == 1) {
            // if there's only 1 in stack, we switch it with 0.000... by simply pushing 0.000...
            stack.push(DEFAULT_STR);
            return;
        }
        // normal switch
        String newBottomY = stack.pop();
        String newTopX = stack.pop();
        stack.push(newBottomY);
        stack.push(newTopX);
    }

    public void pushNumber(double newVal) {
        stack.push(fmt(newVal));
    }

    public void pushStr(String newVal) {
        stack.push(newVal);
    }

    public String fmt(double number) {
        return Helper.getInstance().fmt(number);
    }

    public void flush() {
        stack = new Stack<String>();
    }

    public void rollDown() {
        if (!stack.empty()) {
            String top = stack.pop();
            stack.add(0, top);
        }
    }

    public void rollUp() {
        if (!stack.empty()) {
            String bottom = stack.remove(0);
            stack.push(bottom);
        }
    }

    public String getName() {
        return name;
    }

    public String getDefaultStr() {
        return DEFAULT_STR;
    }

    public String[] getAllStrs() {
        // get all strings in stack
        Stack<String> copy = (Stack<String>) stack.clone();
        String[] retVals = new String[copy.size()];
        int index = 0;
        while (!copy.empty()) {
            retVals[index] = copy.pop();
            index += 1;
        }
        return retVals;
        // Suppose a0 is the top of the stack, then retVals would be:
        // a0, a1, a2, a3....
    }

    public void replaceAllStrs(String[] vals) {
        // vals: top a0, a1, a2, ...
        Stack<String> copy = new Stack<String>();
        for (int i = vals.length - 1; i >= 0; i--) {
            copy.push(vals[i]);
        }
        stack = copy;
    }

    public Stack<String> getDataClone() {
        return (Stack<String>) stack.clone();
    }

    public int getSize() {
        return stack.size();
    }

    public void setName(String nameParam) {
        name = nameParam;
    }

    public void setRootCurveA(double a) { rootCurveA = a; }
    public double getRootCurveA() { return rootCurveA; }
}
