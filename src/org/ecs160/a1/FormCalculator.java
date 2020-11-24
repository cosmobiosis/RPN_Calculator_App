
package org.ecs160.a1;

import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;

public class FormCalculator extends Form {

    public FormCalculator(ViewScreen screenRef, ViewKeypad keypadRef, ViewKeypadExtension keypadExRef) {
        super("A1 Final");
        this.setLayout(new BorderLayout());

        //keypadRef.add(listStackRef);

        this.add(BorderLayout.NORTH, screenRef);
        this.add(BorderLayout.CENTER, keypadExRef);
        this.add(BorderLayout.SOUTH, keypadRef);
    }
}