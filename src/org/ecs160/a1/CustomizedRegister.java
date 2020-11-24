package org.ecs160.a1;

import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.Container;

import java.awt.*;

public class CustomizedRegister extends Container {
    private Label labelDisplay;
    private String registerName;
    private final String PADDING = "                                            ";

    public CustomizedRegister(String txt) {
        super();
        // if need the font for registers to be bigger, can change "small" to "medium"
        Font smallBoldSystemFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_SMALL);
        this.registerName = txt;
        this.setLayout(new BoxLayout(BoxLayout.X_AXIS));
        this.labelDisplay = new Label(txt);
        this.add(labelDisplay);
        this.labelDisplay.getAllStyles().setFgColor(0xffffff);
        this.labelDisplay.getAllStyles().setFont(smallBoldSystemFont);
        this.labelDisplay.setSize(new Dimension(1, 1)); // I'M fcking giving up lol
    }

    public void updateView(String val) {
        this.labelDisplay.setText(this.registerName.concat(val).concat(PADDING));
        this.labelDisplay.getAllStyles().setFgColor(0xffffff);
    }

    public void updateColorView(String val, String color) {
        this.labelDisplay.setText(this.registerName.concat(val).concat(PADDING));
        switch (color) {
            case "RED":
                this.labelDisplay.getAllStyles().setFgColor(0xe12120);
                break;
            case "GREEN":
                this.labelDisplay.getAllStyles().setFgColor(0X52BE80);
                break;
            case "YELLOW":
                this.labelDisplay.getAllStyles().setFgColor(0XF4D03F);
                break;
        }
    }
}