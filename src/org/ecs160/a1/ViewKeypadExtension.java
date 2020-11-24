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

public class ViewKeypadExtension extends Container {
    private String[] buttonNames =
            { "MAP", "DUP", "SAVE", "◎" //---------| List
                    , "<", "CREATE", "DELETE", ">" //----| List
                    , "PASTE", "UP", "DOWN" //----------| Stack
                    , "ENT","COPY", "POP", "X<->Y"}; //-----| Stack

    private Hashtable<String, CustomizedButton> buttons = new Hashtable<String, CustomizedButton>();
    public ViewKeypadExtension() {
        super();
        this.setLayout(new GridLayout(2, 4));
        this.getAllStyles().setBgColor(0x000000);//0xF4ECF7); // background is pinkish
        this.getAllStyles().setBgTransparency(255);
//        for (String buttonName : buttonNames) {
//            CustomizedButton button = new CustomizedButton(buttonName);
//            buttons.put(buttonName, button);
//            this.add(button);
//        }
        Container cell1 = new Container(new GridLayout(2, 1));
        CustomizedButton bName = new CustomizedButton(buttonNames[0]);
        buttons.put(buttonNames[0], bName);
        CustomizedButton bLeft = new CustomizedButton(buttonNames[4]);
        buttons.put(buttonNames[4], bLeft);
        cell1.add(bName); cell1.add(bLeft);
        this.add(cell1); // asuming cell overwrites data upon reaching capacity, don't redeclare

        Container cell2 = new Container(new GridLayout(2, 1));
        CustomizedButton bDup = new CustomizedButton(buttonNames[1]);
        buttons.put(buttonNames[1], bDup);
        CustomizedButton bCreate = new CustomizedButton(buttonNames[5]);
        buttons.put(buttonNames[5], bCreate);
        cell2.add(bDup); cell2.add(bCreate);
        this.add(cell2);

        Container cell3 = new Container(new GridLayout(2, 1));
        CustomizedButton bSave = new CustomizedButton(buttonNames[2]);
        buttons.put(buttonNames[2], bSave);
        CustomizedButton bDelete = new CustomizedButton(buttonNames[6]);
        buttons.put(buttonNames[6], bDelete);
        cell3.add(bSave); cell3.add(bDelete);
        this.add(cell3);

        Container cell4 = new Container(new GridLayout(2, 1));
        CustomizedButton bGenesis = new CustomizedButton(buttonNames[3]);
        buttons.put(buttonNames[3], bGenesis);
        CustomizedButton bRight = new CustomizedButton(buttonNames[7]);
        buttons.put(buttonNames[7], bRight);
        cell4.add(bGenesis); cell4.add(bRight);
        this.add(cell4);

        CustomizedButton bEnter = new CustomizedButton(buttonNames[11]);
        buttons.put(buttonNames[11], bEnter);
        this.add(bEnter);

        Container cell5 = new Container(new GridLayout(2, 1));
        CustomizedButton bPaste = new CustomizedButton(buttonNames[8]);
        buttons.put(buttonNames[8], bPaste);
        CustomizedButton bCopy = new CustomizedButton(buttonNames[12]);
        buttons.put(buttonNames[12], bCopy);
        cell5.add(bPaste); cell5.add(bCopy);
        this.add(cell5);

        Container cell6 = new Container(new GridLayout(2, 1));
        CustomizedButton bUp = new CustomizedButton(buttonNames[9]);
        buttons.put(buttonNames[9], bUp);
        CustomizedButton bPop = new CustomizedButton(buttonNames[13]);
        buttons.put(buttonNames[13], bPop);
        cell6.add(bUp); cell6.add(bPop);
        this.add(cell6);

        Container cell7 = new Container(new GridLayout(2, 1));
        CustomizedButton bDown = new CustomizedButton(buttonNames[10]);
        buttons.put(buttonNames[10], bDown);
        CustomizedButton bXToY = new CustomizedButton(buttonNames[14]);
        buttons.put(buttonNames[14], bXToY);
        cell7.add(bDown); cell7.add(bXToY);
        this.add(cell7);




//        CustomizedButton bSave = new CustomizedButton(buttonNames[1]);
//        buttons.put(buttonNames[1], bDup);
//        CustomizedButton bCreate = new CustomizedButton(buttonNames[5]);
//        buttons.put(buttonNames[5], bCreate);
//        cell.add(bDup); cell.add(bCreate);
//        this.add(cell);

        //this.getAllStyles().setPadding(10, 10, 10, 10);
        // this.getAllStyles().setPadding(0, 100, 0, 0);

    }

    public CustomizedButton getButton(String buttonName) {
        return buttons.get(buttonName);
    }
}