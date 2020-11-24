package org.ecs160.a1;
import com.codename1.io.Storage;

import java.lang.Math;
import java.util.*;

public class ControllerCalculator {
    private String DEFAULT_FILENAME = "CalcSavedData";
    private ViewScreen screenRef;
    private ViewKeypad keypadRef;
    private ViewKeypadExtension keypadExRef;
    private ModelLists lists;
    private String copied; // for copy and paste button
    private boolean decimalPointMode; // user has double point
    private boolean appendMode; // only when finished calculation
    private boolean justEntered; // user just pushed a new number to the stack
    private boolean rootCurveMode;
    private boolean lambdaMapOn;

    private enum LC_STAGE { FALSE, MEAN, LOW };
    private LC_STAGE linearCurveMode = LC_STAGE.FALSE;
    private double x0;
    private double x1;


    public ControllerCalculator(ViewScreen screenRef, ViewKeypad keypadRef, ViewKeypadExtension keypadExRef) {
        ModelStack list = new ModelStack(); // initialize a stack for our new model
        lists = new ModelLists(list);
        loadData();
        resetModes(); // set appendMode and decimalPointMode to false
        this.screenRef = screenRef;
        this.keypadRef = keypadRef;
        this.keypadExRef = keypadExRef;
        copied = ""; // for copy and paste button

        this.addNumberListener();
        this.addDecimalListener();
        this.addEraseListener();

        this.addTrigonListener();
        this.addArithmeticListener();
        this.addConstantListener();
        this.addPowerListener();
        this.addLogListener();
        this.addCurveListener();

        this.addStatisticsListener();

        // stack management
        this.addENTListener();
        this.addCLRListener();
        this.addPOPListener();
        this.addRollListener();
        this.addSwitchListener();
        this.addCopyPasteListener();

        // list management
        this.addGenesisListener();
        this.addMapListener();
        this.addCreateListener();
        this.addDeleteListener();
        this.addSwipeListener();
        this.addSaveListener();
        this.addDupListener();

        this.refreshScreen();
    }

    public void addTrigonListener() {
        CustomizedButton button1 = this.keypadRef.getButton("SIN");
        button1.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.sin(val));
        });
        CustomizedButton button2 = this.keypadRef.getButton("COS");
        button2.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.cos(val));
        });
        CustomizedButton button3 = this.keypadRef.getButton("TAN");
        button3.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.tan(val));
        });
    }

    public void addArithmeticListener() {
        CustomizedButton button1 = this.keypadRef.getButton("+");
        button1.addActionListener((evt) -> {
            if (lambdaMapOn) {
                processLambdaMap("+");
                return;
            }
            double val1 = lists.getTopStack().getTopNumber();
            double val2 = lists.getTopStack().get2ndTopNumber();
            binaryOperation(val2 + val1);
        });
        CustomizedButton button2 = this.keypadRef.getButton("-");
        button2.addActionListener((evt) -> {
            if (lambdaMapOn) {
                processLambdaMap("-");
                return;
            }
            double val1 = lists.getTopStack().getTopNumber();
            double val2 = lists.getTopStack().get2ndTopNumber();
            binaryOperation(val2 - val1);
        });
        CustomizedButton button3 = this.keypadRef.getButton("*");
        button3.addActionListener((evt) -> {
            if (lambdaMapOn) {
                processLambdaMap("*");
                return;
            }
            double val1 = lists.getTopStack().getTopNumber();
            double val2 = lists.getTopStack().get2ndTopNumber();
            binaryOperation(val2 * val1);
        });
        CustomizedButton button4 = this.keypadRef.getButton("/");
        button4.addActionListener((evt) -> {
            if (lambdaMapOn) {
                processLambdaMap("/");
                return;
            }
            double val1 = lists.getTopStack().getTopNumber();
            if (val1 == 0) {
                // INPUT ERROR: cannot divided by ZERO
                screenRef.updateTopView("Divided by 0!", "RED");
                return;
            }
            double val2 = lists.getTopStack().get2ndTopNumber();
            if (Double.isNaN(val2 / val1)) {
                // TODO: Maybe we should update view Error here
                return;
            }
            binaryOperation(val2 / val1);
        });
    }

    public void addConstantListener() {
        CustomizedButton piButton = this.keypadRef.getButton("PI");
        piButton.addActionListener((evt) -> {
            unaryOperation(Math.PI);
        });
        CustomizedButton eulerButton = this.keypadRef.getButton("EU");
        eulerButton.addActionListener((evt) -> {
            unaryOperation(Math.E);
        });
    }

    public void addDecimalListener() {
        CustomizedButton button = this.keypadRef.getButton(".");
        button.addActionListener((evt) -> {
            pushDecimalToStackTop();
        });
    }

    public void addNumberListener() {
        for (int i = 0; i <= 9; i++) {
            CustomizedButton button = this.keypadRef.getButton(Integer.toString(i));
            int digit = i;
            button.addActionListener((evt) -> {
                pushDigitToStackTop(digit);
            });
        }
    }

    public void addEraseListener() {
        CustomizedButton button = this.keypadRef.getButton("←");
        button.addActionListener((evt) -> {
            eraseLastChar();

        });
    }

    public void addPowerListener() {
        CustomizedButton button1 = this.keypadRef.getButton("X^2");
        button1.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.pow(val, 2));
        });
        CustomizedButton button2 = this.keypadRef.getButton("X^3");
        button2.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.pow(val, 3));
        });
        CustomizedButton button3 = this.keypadRef.getButton("Y^X");
        button3.addActionListener((evt) -> {
            double val1 = lists.getTopStack().getTopNumber();
            double val2 = lists.getTopStack().get2ndTopNumber();
            if (lambdaMapOn) {
                processLambdaMap("^");
                return;
            }
            binaryOperation(Math.pow(val2, val1));
        });
        CustomizedButton button4 = this.keypadRef.getButton("SQRT");
        button4.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            if (val < 0) {
                // negs don't have sqrt (unless it's imaginary number)
                // TODO: Maybe we should add view update error here
                return;
            }
            unaryOperation(Math.sqrt(val));
        });
    }

    public void addLogListener() {
        CustomizedButton button1 = this.keypadRef.getButton("LOG");
        button1.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.log10(val));
        });
        CustomizedButton button2 = this.keypadRef.getButton("LN");
        button2.addActionListener((evt) -> {
            double val = lists.getTopStack().getTopNumber();
            unaryOperation(Math.log(val));
        });
    }

    public void addCurveListener() {
        CustomizedButton button1 = this.keypadRef.getButton("R|CV");
        button1.addActionListener((evt) -> {
            processRootCurve(false); // press Enter is false
        });

        CustomizedButton button2 = this.keypadRef.getButton("L|CV");
        button2.addActionListener((evt) -> {
            processLinearCurve(false);
        });
    }

    public void processRootCurve(boolean hitEnter) {
        if (!hitEnter && !rootCurveMode) {
            // PRESS R|CV
            lists.getTopStack().pushStr("0.0");
            resetModes();
            refreshScreen();
            justEntered = true; // we just entered default and ready to change it
            rootCurveMode = true;
            screenRef.updateTopView("ENT(ER) parameter a:", "YELLOW");
        } else if (hitEnter) {
            // in root curve mode and we hit enter, do special calculations instead of original ENT
            String[] vals = lists.getTopStack().getAllStrs();
            if (vals.length <= 1) {
                screenRef.updateTopView("Param size INVALID!", "RED");
                return;
            }
            String[] newVals = new String[vals.length - 1];
            double a = Double.parseDouble(vals[0]);
            if (a <= 0 || a >= 1) {
                screenRef.updateTopView("Param range INVALID", "RED");
                return;
            }
            for (int i = 1; i < vals.length; i++) {
                double score = Double.parseDouble(vals[i]);
                if (score < 0 || score > 100) {
                    screenRef.updateTopView("Param range INVALID", "RED");
                    return;
                }
                double curved = Math.pow(100, (1-a)) * Math.pow(score, a);
                newVals[i - 1] = fmt(curved);
            }
            lists.pushStack(newVals);
            lists.getTopStack().setRootCurveA(a);

            resetModes();
            refreshScreen();
        }
    }

    public void processLinearCurve(boolean hitEnter) {
        if (!hitEnter && linearCurveMode == LC_STAGE.FALSE) {
            x0 = getCurMean(); // retrieve current snapshot's x0 average
            x1 = getCurLowest(); // retrieve current snapshot's x1 lowest
            // PRESS L|CV
            lists.getTopStack().pushStr("0.0"); // slot for new MEAN
            resetModes();
            refreshScreen();
            justEntered = true; // we just entered default and ready to change it
            linearCurveMode = LC_STAGE.MEAN; // next is mean
            screenRef.updateTopView("Raw Mean: "
                    .concat(Helper.getInstance().fmtShort(x0))
                    .concat("; ENT new:"), "YELLOW");
        } else if (hitEnter && linearCurveMode == LC_STAGE.MEAN) {
            // PRESS ENT for new specified mean
            lists.getTopStack().pushStr("0.0"); // slot for new LOW
            resetModes();
            refreshScreen();
            justEntered = true; // we just entered default and ready to change it
            linearCurveMode = LC_STAGE.LOW; // next is low
            screenRef.updateTopView("Raw LOW: "
                    .concat(Helper.getInstance().fmtShort(x1))
                    .concat("; ENT new:"), "YELLOW");
        } else if (hitEnter &&  linearCurveMode == LC_STAGE.LOW) {
            // PRESS ENT for new specified lowesst
            // READY to calculate
            try {
                // pop for lowest
                double y1 = lists.getTopStack().popTopNumber();
                // pop for mean
                double y0 = lists.getTopStack().popTopNumber();
                String[] vals = lists.getTopStack().getAllStrs();
                String[] newVals = new String[vals.length];
                for (int i = 0; i < newVals.length; i++) {
                    double score = Double.parseDouble(vals[i]);
                    if (x0 == x1) {
                        throw new IllegalArgumentException("X0 should not be equal to x1");
                    }
                    double curved = y0 + (y1 - y0) * (score - x0) / (x1 - x0);
                    newVals[i] = fmt(curved);
                    if (Double.parseDouble(newVals[i]) < Double.parseDouble(vals[i])) {
                        screenRef.updateTopView("Destructive CRV! Change Params", "RED");
                        return;
                    }
                }
                lists.pushStack(newVals);
                resetModes();
                refreshScreen();
            } catch(Exception e) {
                resetModes();
                refreshScreen();
                screenRef.updateTopView("Params Invalid!","RED");
            }
        }
    }

    public void addStatisticsListener() {
        CustomizedButton button1 = this.keypadRef.getButton("Σ");
        button1.addActionListener((evt) -> {
            String[] vals = lists.getTopStack().getAllStrs();
            double sum = 0;
            for (String val : vals) {
                sum += Double.parseDouble(val);
            }
            lists.getTopStack().pushStr("0.0"); // Dummie, will be overwritten by unary op
            unaryOperation(sum);
        });

        CustomizedButton button2 = this.keypadRef.getButton("MAX");
        button2.addActionListener((evt) -> {
            if (lists.getTopStack().getSize() == 0) {
                // empty list
                unaryOperation(0);
                return;
            }
            String[] vals = lists.getTopStack().getAllStrs();
            double ans = - Double.MAX_VALUE;
            for (String val : vals) {
                double num = Double.parseDouble(val);
                ans = Math.max(ans, num);
            }
            lists.getTopStack().pushStr("0.0"); // Dummie, will be overwritten by unary op
            unaryOperation(ans);
        });

        CustomizedButton button3 = this.keypadRef.getButton("MIN");
        button3.addActionListener((evt) -> {
            if (lists.getTopStack().getSize() == 0) {
                unaryOperation(0);
                return;
            }
            double min = getCurLowest();
            lists.getTopStack().pushStr("0.0"); // Dummie, will be overwritten by unary op
            unaryOperation(min);
        });

        CustomizedButton button4 = this.keypadRef.getButton("MEAN");
        button4.addActionListener((evt) -> {
            if (lists.getTopStack().getSize() == 0) {
                unaryOperation(0);
                return;
            }
            double mean = getCurMean();
            lists.getTopStack().pushStr("0.0"); // Dummie, will be overwritten by unary op
            unaryOperation(mean);
        });

        CustomizedButton button5 = this.keypadRef.getButton("MED");
        button5.addActionListener((evt) -> {
            if (lists.getTopStack().getSize() == 0) {
                unaryOperation(0);
                return;
            }
            String[] vals = lists.getTopStack().getAllStrs();
            Vector<Double> vector = new Vector<Double>();
            for (String val : vals) {
                vector.add(Double.parseDouble(val));
            }
            Collections.sort(vector);
            lists.getTopStack().pushStr("0.0"); // Dummie, will be overwritten by unary op
            int medIndex = vector.size() / 2;
            if (vector.size() % 2 == 0) {
                // even
                unaryOperation((vector.get(medIndex) + vector.get(medIndex - 1)) / 2);
            } else {
                // odd
                unaryOperation(vector.get(medIndex));
            }
        });
    }

    public void pushDigitToStackTop(int digit) {
        ModelStack topList = lists.getTopStack();
        if (!appendMode) {
            // if we're having a new number incoming;
            // move register X's model value to register Y's model value
            if (justEntered) {
                topList.updateTopStr(Integer.toString(digit));
            } else {
                topList.pushStr(Integer.toString(digit));
            }
            resetModes(true); // reset input modes, especially justEntered
            refreshScreen();
            appendMode = true; // start to append digit to X
        } else {
            // when we're appending digits...
            if (topList.getTopStr().length() == 1 && topList.getTopStr().charAt(0) == '0') {
                // if there's only a '0' here, update it with new digit
                topList.updateTopStr(Integer.toString(digit));
            } else {
                topList.updateTopStr(topList.getTopStr().concat(Integer.toString(digit)));
            }
        }
        screenRef.updateTopView(topList.getTopStr().concat("_"));
    }

    public void pushDecimalToStackTop() {
        ModelStack topList = lists.getTopStack();
        if (decimalPointMode) {
            // already has decimal point, return
            return;
        }
        if (!appendMode) {
            // if we're having a new number to replace X, and the first button pressed is decimal point
            if (justEntered) {
                // if we just entered, we replace instead of push
                topList.updateTopStr("0.");
            } else {
                // normal situations
                topList.pushStr("0.");
            }
            resetModes(true);
            refreshScreen();
            appendMode = true; // start to append digit to X
            decimalPointMode = true;
            screenRef.updateTopView(topList.getTopStr().concat("_"));
            return;
        }
        topList.updateTopStr(topList.getTopStr().concat("."));
        refreshScreen();
        decimalPointMode = true;
        screenRef.updateTopView(topList.getTopStr().concat("_"));
    }

    public void eraseLastChar() {
        ModelStack topList = lists.getTopStack();
        String currentStr = topList.getTopStr();
        int currentLength = topList.getTopStr().length();
        if (justEntered) {
            // if we just entered a new value, we clear them all
            topList.updateTopStr("0");
            // set justEntered to false
            justEntered = false;
        } else if (!appendMode) {
            // if we aren't appending, say just finished calculation, we clear them all
            topList.updateTopStr("0");
        } else if (currentStr.length() == 1) {
            // clear till ZERO
            topList.updateTopStr("0");
        } else {
            // normal cases where you delete last char
            char lastChar = currentStr.charAt(currentLength - 1);
            if (lastChar == '.') {
                // we're erasing decimal point here
                decimalPointMode = false;
            }
            // delete last char
            topList.updateTopStr(currentStr.substring(0, currentLength - 1));
        }
        // start appending after clear
        appendMode = true;
        refreshScreen();
        // show append sign
        screenRef.updateTopView(topList.getTopStr().concat("_"));
    }

    public void unaryOperation(double val) {
        // only change the top of the stack to a new number
        lists.getTopStack().updateTopNumber(val);
        // reset everything
        resetModes();
        refreshScreen();
        screenRef.updateTopView(lists.getTopStack().getTopStr(), "GREEN");
    }

    public void binaryOperation(double val) {
        // binary reduces top two numbers into one val
        lists.getTopStack().mergeTopTwoNumbers(val);
        // reset everything
        resetModes();
        refreshScreen();
        screenRef.updateTopView(lists.getTopStack().getTopStr(), "GREEN");
    }

    // stack management buttons, using FormKeypadExtension keypadExRef
    public void addENTListener() {
        CustomizedButton button = this.keypadExRef.getButton("ENT");
        button.addActionListener((evt) -> {
            if (rootCurveMode) {
                processRootCurve(true); // we just hit ENT
                return;
            }
            if (linearCurveMode == LC_STAGE.MEAN || linearCurveMode == LC_STAGE.LOW) {
                processLinearCurve(true);
                return;
            }
            ModelStack topList = lists.getTopStack();
            topList.pushStr(topList.getTopStr());
            resetModes();
            refreshScreen();
            // set justEntered equals to true;
            justEntered = true;

        });
    }

    public void addCLRListener() {
        // CLR moved to keypad from extension!!!
        CustomizedButton button = this.keypadRef.getButton("CLR");
        button.addActionListener((evt) -> {
            ModelStack topList = lists.getTopStack();
            topList.popTop();
            topList.pushStr(topList.getDefaultStr());
            resetModes();
            refreshScreen();
            // set justEntered equals to true;
            justEntered = true;
        });
    }

    public void addPOPListener() {
        CustomizedButton button = this.keypadExRef.getButton("POP");
        button.addActionListener((evt) -> {
            lists.getTopStack().popTop();
            resetModes();
            refreshScreen();
        });
    }

    public void addSwitchListener() {
        CustomizedButton button = this.keypadExRef.getButton("X<->Y");
        button.addActionListener((evt) -> {
            lists.getTopStack().switchTopTwo();
            resetModes();
            refreshScreen();
        });
    }

    public void addCopyPasteListener() {
        CustomizedButton button1 = this.keypadExRef.getButton("COPY");
        button1.addActionListener((evt) -> {
            copied = lists.getTopStack().getTopStr();
            resetModes();
            refreshScreen();
        });
        CustomizedButton button2 = this.keypadExRef.getButton("PASTE");
        button2.addActionListener((evt) -> {
            if (copied.length() == 0) {
                return;
            }
            lists.getTopStack().pushStr(copied);
            resetModes();
            refreshScreen();
        });
    }

    public void addRollListener() {
        CustomizedButton button1 = this.keypadExRef.getButton("UP");
        button1.addActionListener((evt) -> {
            lists.getTopStack().rollUp();
            resetModes();
            refreshScreen();
        });
        CustomizedButton button2 = this.keypadExRef.getButton("DOWN");
        button2.addActionListener((evt) -> {
            lists.getTopStack().rollDown();
            resetModes();
            refreshScreen();
        });
    }

    // LIST MANAGEMENT BUTTONS, using FormKeypadExtension keypadExRef

    public void addGenesisListener() {
        // clean up all the lists
        CustomizedButton button = this.keypadExRef.getButton("◎");
        button.addActionListener((evt) -> {
            lists.flush();
            lists.getTopStack().setName("0");
            Helper.getInstance().setIdCounter(1);
            resetModes();
            refreshScreen();
        });
    }

    public void addMapListener() {
        CustomizedButton button = this.keypadExRef.getButton("MAP");
        button.addActionListener((evt) -> {
            lambdaMapOn = !lambdaMapOn;
            if (lambdaMapOn) {
                button.getAllStyles().setBgColor(0xF7DC6F);
            } else {
                button.getAllStyles().setBgColor(0x505545); // default color
            }
        });
    }

    public void processLambdaMap(String operator) {
        String[] vals = lists.getTopStack().getAllStrs();
        if (vals.length <= 1) {
            screenRef.updateTopView("Param size INVALID!", "RED");
            return;
        }
        double argument = Double.parseDouble(vals[0]);
        if (argument == 0 && operator.equals("/")) {
            // INPUT ERROR: cannot divided by ZERO
            screenRef.updateTopView("Divided by 0!", "RED");
            return;
        }
        String[] newVals = new String[vals.length - 1];
        for (int i = 1; i < vals.length; i++) {
            double target = Double.parseDouble(vals[i]);
            switch(operator) {
                case "*":
                    target = target * argument;
                    break;
                case "+":
                    target = target + argument;
                    break;
                case "-":
                    target = target - argument;
                    break;
                case "/":
                    target = target / argument;
                    break;
                case "^":
                    target = Math.pow(target, argument);
                    break;
                default:
                    System.out.println("no match");
            }
            newVals[i - 1] = fmt(target);
        }
        lists.getTopStack().replaceAllStrs(newVals);

        resetModes();
        refreshScreen();
    }

    public void addCreateListener() {
        CustomizedButton button = this.keypadExRef.getButton("CREATE");
        button.addActionListener((evt) -> {
            lists.pushStack();
            resetModes();
            refreshScreen();
        });
    }
    public void addDeleteListener() {
        CustomizedButton button = this.keypadExRef.getButton("DELETE");
        button.addActionListener((evt) -> {
            lists.popStack();
            resetModes();
            refreshScreen();
        });
    }
    public void addSwipeListener() {
        CustomizedButton button1 = this.keypadExRef.getButton("<");
        button1.addActionListener((evt) -> {
            lists.rollDown();
            resetModes();
            refreshScreen();
        });
        CustomizedButton button2 = this.keypadExRef.getButton(">");
        button2.addActionListener((evt) -> {
            lists.rollUp();
            resetModes();
            refreshScreen();
        });
    }

    public void addDupListener() {
        CustomizedButton button = this.keypadExRef.getButton("DUP");
        button.addActionListener((evt) -> {
            lists.pushStack(lists.getTopStack().getAllStrs());
            resetModes();
            refreshScreen();
        });
    }

    public void addSaveListener() {
        CustomizedButton button = this.keypadExRef.getButton("SAVE");
        button.addActionListener((evt) -> {
            String[] fileNames = Storage.getInstance().listEntries();
            Hashtable<String, Vector<String>> idListMapping = new Hashtable<String, Vector<String>>();
            for (String fileName : fileNames) {
                if (fileName.equals(DEFAULT_FILENAME)) {
                    System.out.println("File already exists, overwriting:".concat(DEFAULT_FILENAME));
                    idListMapping = (Hashtable<String, Vector<String>>)Storage.getInstance().readObject(DEFAULT_FILENAME);
                    break;
                }
            }
            if (lists.getSize() <= 1 && lists.getTopStack().getSize() == 0) {
                // when genesis, saved
                Storage.getInstance().deleteStorageFile(DEFAULT_FILENAME);
                resetModes();
                refreshScreen();
                screenRef.updateTopView("Storage Cleaned!", "YELLOW");
                return;
            }
            ModelStack listToSave = lists.getTopStack();
            Vector<String> vec = new Vector<String>(Arrays.asList(listToSave.getAllStrs()));
            idListMapping.put(listToSave.getName(), vec);
            Storage.getInstance().writeObject(DEFAULT_FILENAME, idListMapping);
            resetModes();
            refreshScreen();
            screenRef.updateTopView("Saved List ".concat(listToSave.getName()), "YELLOW");
        });
    }

    public void loadData() {
        Hashtable<String, Vector<String>> idListMapping
                = (Hashtable<String, Vector<String>>)Storage.getInstance().readObject(DEFAULT_FILENAME);
        if (idListMapping == null) {
            return;
        }
        Set<String> ids = idListMapping.keySet();
        if (ids.size() == 0) {
            return;
        }
        boolean newInitiated = false;
        int maxNameCounter = 0;
        for(String id: ids){
            maxNameCounter = Math.max(Integer.parseInt(id)+1, maxNameCounter);
            ModelStack list = new ModelStack(idListMapping.get(id));
            if (!newInitiated) {
                lists = new ModelLists(list);
                lists.getTopStack().setName(id);
                newInitiated = true;
            } else {
                lists.pushStack(idListMapping.get(id));
                lists.getTopStack().setName(id);
            }
        }
        Helper.getInstance().setIdCounter(maxNameCounter);
    }

    public String fmt(double number) {
        return Helper.getInstance().fmt(number);
    }

    public String fmtShort(double number) {
        return Helper.getInstance().fmtShort(number);
    }

    public double getCurMean() {
        String[] vals = lists.getTopStack().getAllStrs();
        if (vals.length == 0) {
            return 0;
        }
        double sum = 0;
        for (String val : vals) {
            sum += Double.parseDouble(val);
        }
        return sum / vals.length;
    }

    public double getCurLowest() {
        String[] vals = lists.getTopStack().getAllStrs();
        if (vals.length == 0) {
            return 0;
        }
        double ans = Double.MAX_VALUE;
        for (String val : vals) {
            double num = Double.parseDouble(val);
            ans = Math.min(ans, num);
        }
        return ans;
    }

    public void refreshScreen() {
        // refresh screen base on model
        ModelStack topList = lists.getTopStack();
        String metaData = topList.getName().concat(" | len: ").concat(String.format("%d", topList.getSize()));
        if (topList.getRootCurveA() < 1 && topList.getRootCurveA() > 0) {
            metaData = metaData.concat(" | a: ").concat(fmtShort(topList.getRootCurveA()));
        }
        screenRef.updateAllRegisters(topList.getTopFourStr(), metaData);
    }

    public void resetModes() {
        appendMode = false;
        decimalPointMode = false;
        justEntered = false;
        rootCurveMode = false;
        linearCurveMode = LC_STAGE.FALSE;
    }

    public void resetModes(boolean appending) {
        // when we're in appending mode, we do not reset root curve mode or linear curve mode
        appendMode = false;
        decimalPointMode = false;
        justEntered = false;
    }
}
