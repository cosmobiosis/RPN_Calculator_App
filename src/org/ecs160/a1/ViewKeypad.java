package org.ecs160.a1;

import java.util.*;
import static com.codename1.ui.CN.*;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.GridLayout;

import java.io.IOException;

//normal keypad
public class ViewKeypad extends Container {
    private String[] buttonNames =
            { "Σ", "MAX", "MIN", "MEAN", "MED"
                    ,"LOG", "LN", "SIN", "COS", "TAN"
                    ,"X^2", "X^3", "Y^X", "L|CV","R|CV"
                    , "/", "7", "8", "9", "CLR"
                    , "*", "4", "5", "6", "←"
                    , "-", "1", "2", "3", "SQRT"
                    , "+", "0", ".", "EU", "PI"};

    private Hashtable<String, CustomizedButton> buttons = new Hashtable<String, CustomizedButton>();

    public ViewKeypad() {
        super();
        this.setLayout(new GridLayout(7, 5));
        this.getAllStyles().setBgColor(0x000000);//0xF4ECF7); // background is pinkish
        this.getAllStyles().setBgTransparency(255);
        for (String buttonName : buttonNames) {
            CustomizedButton button = new CustomizedButton(buttonName);
            buttons.put(buttonName, button);
            this.add(button);
        }

    }

    public CustomizedButton getButton(String buttonName) {
        return buttons.get(buttonName);
    }
}