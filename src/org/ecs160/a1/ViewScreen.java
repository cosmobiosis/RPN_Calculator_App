package org.ecs160.a1;
import static com.codename1.ui.CN.*;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.*;

import java.io.IOException;

public class ViewScreen extends Container {

    private CustomizedRegister metaHolder;
    private CustomizedRegister[] registers;
    private String[] registerNames = { "X: ", "Y: ", "Z: ", "T: " };

    public ViewScreen() {
        super();
        metaHolder = new CustomizedRegister("ID: ");
        this.add(metaHolder);
        registers = new CustomizedRegister[registerNames.length];
        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        this.getAllStyles().setBgColor(0x000000);//0xD4E6F1); //top screen
        this.getAllStyles().setBgTransparency(255);
        for (int i = registers.length - 1; i >= 0 ; i--) {
            CustomizedRegister register = new CustomizedRegister(registerNames[i]);
            registers[i] = register;
            // add view in REVERSE order
            this.add(register);
        }
    }

    public void updateAllRegisters(String[] txts, String metaData) {
        if (txts.length != registers.length) {
            System.out.println("Parameter size does not match with the registers!");
            throw new IllegalArgumentException(Integer.toString(txts.length)
                    .concat(" vs ")
                    .concat(Integer.toString(registers.length)));
        }
        for (int i = 0; i < txts.length; i++) {
            registers[i].updateView(txts[i]);
        }
        metaHolder.updateView(metaData);
    }

    public void updateOneRegister(int i, String txt, String metaData) {
        if (i < 0 || i >= registers.length) {
            System.out.println("There're only four registers to be displayed!");
            return;
        }
        registers[i].updateView(txt);
    }

    public void updateTopView(String txt) {
        registers[0].updateView(txt);
    }

    public void updateTopView(String txt, String color) {
        registers[0].updateColorView(txt, color);
    }

    public int numRegisters() {
        return registers.length;
    }
}