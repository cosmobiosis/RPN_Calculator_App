package org.ecs160.a1;

import com.codename1.ui.Button;
import com.codename1.ui.Font;
import com.codename1.ui.Stroke;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.plaf.Style;

import java.util.ArrayList;
import java.util.Arrays;

import static com.codename1.ui.CN.*;


public class CustomizedButton extends Button {
    // advanced -> 0x161616 or 0x 1a1a1a
    // nums -> 4d4d4d
    // clr/backspace/ reboot -> 505545
    // shift -> 8c9bac
    //addition -> 974343


    public CustomizedButton(String txt) {
        super(txt);
        Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);

        // declaration of button types to easily customize based off groups
        ArrayList<String> advKeys = new ArrayList<String>(
                Arrays.asList("Σ", "MAX", "MIN", "MEAN", "MED"
                        ,"CRV1", "CRV2", "SIN", "COS", "TAN"
                        ,"X^2", "X^3", "Y^X", "SQRT"));
        ArrayList<String> listKeys = new ArrayList<String>(
                Arrays.asList("MAP", "DUP", "SAVE", "◎", "<", "CREATE", "DELETE", ">"));
        ArrayList<String> stackKeys = new ArrayList<String>(
                Arrays.asList("ENT", "CLR", "UP", "DOWN", "POP", "X<->Y", "COPY", "PASTE"));
        ArrayList<String> arithKeys = new ArrayList<String>(
                Arrays.asList("+", "-", "/", "*", "ENT", "CLR", "◎", "←"));
        this.getAllStyles().setBgTransparency(255);

        this.getAllStyles().setFgColor(0xffffff);
        if (isInt(txt) || txt.equals(".")) {
            // int num button
            this.getAllStyles().setBgColor(0x4d4d4d);
        } else if (txt.equals("â—Ž")) {
            this.getAllStyles().setBgColor(0x505545);
        } else if (arithKeys.contains(txt)) {
            // basic arith button
            this.getAllStyles().setBgColor(0x974343);
        } else if (advKeys.contains(txt)) {
            this.getAllStyles().setBgColor(0x1a1a1a);
        } else if (listKeys.contains(txt)) {
            this.getAllStyles().setBgColor(0x505545);
        } else if (stackKeys.contains(txt)) {
            this.getAllStyles().setBgColor(0x8c9bac);
        } else {
            // advanced button
            this.getAllStyles().setBgColor(0x1a1a1a);
        }
        this.getSelectedStyle().setBgColor(0xE74C3C);
        this.getAllStyles().setMargin(40, 0, 7,7);

        this.getUnselectedStyle().setBorder(RoundRectBorder.create().
                strokeColor(0).
                strokeOpacity(120).
                stroke(borderStroke));

        Font smallBoldSystemFont = Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_SMALL);
        this.getAllStyles().setFont(smallBoldSystemFont);
    }


    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public void longPointerPress(int x, int y) {
        System.out.println("LONG PRESS");
    }
}
