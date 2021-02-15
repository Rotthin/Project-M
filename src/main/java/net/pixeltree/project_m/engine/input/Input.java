package net.pixeltree.project_m.engine.input;

public class Input {
    private Input() { }

    public static boolean isKeyDown(int a_key){
        return KeyListener.isKeyDown(a_key);
    }

    public static boolean isMouseBtnDown(int a_btn){
        return MouseListener.isButtonDown(a_btn);
    }
}
