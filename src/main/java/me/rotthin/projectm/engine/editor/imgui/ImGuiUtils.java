package me.rotthin.projectm.engine.editor.imgui;

import imgui.ImGui;
import imgui.ImString;
import imgui.enums.ImGuiInputTextFlags;
import me.rotthin.projectm.engine.components.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ImGuiUtils {
    private ImGuiUtils(){}

    public static void showIntField(Field a_f, Object a_sender){
        try {
            int _val = (int) a_f.get(a_sender);
            String _name = a_f.getName();
            int[] _imInt = {_val};
            if (ImGui.dragInt(_name + ": ", _imInt)) {
                a_f.set(a_sender, _imInt[0]);
            }
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void showFloatField(Field a_f, Object a_sender){
        try {
            float _val = (float) a_f.get(a_sender);
            String _name = a_f.getName();
            float[] _imInt = {_val};
            if (ImGui.dragFloat(_name + ": ", _imInt)) {
                a_f.set(a_sender, _imInt[0]);
            }
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void showBoolField(Field a_f, Object a_sender){
        try {
            if(Modifier.isStatic(a_f.getModifiers()) || Modifier.isFinal(a_f.getModifiers())){
                return;
            }

            String _name = a_f.getName();
            boolean _val = (boolean)a_f.get(a_sender);
            if(ImGui.checkbox(_name + ": ", _val)){
                a_f.set(a_sender, !_val);
            }
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void showVec2Field(Field a_f, Object a_sender) {
        try {
            Vector2f _val = (Vector2f)a_f.get(a_sender);
            String _name = a_f.getName();

            float[] _imVec = {_val.x, _val.y};

            if(ImGui.dragFloat2(_name + ": ", _imVec)){
                _val.set(_imVec[0], _imVec[1]);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void showVec3Field(Field a_f, Object a_sender) {
        try {
            Vector3f _val = (Vector3f)a_f.get(a_sender);
            String _name = a_f.getName();

            float[] _imVec = {_val.x, _val.y, _val.z};

            if(ImGui.dragFloat3(_name + ": ", _imVec)){
                _val.set(_imVec[0], _imVec[1], _imVec[2]);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void showStringField(Field a_f, Object a_sender){
        try {
            String _val = (String) a_f.get(a_sender);
            String _name = a_f.getName();
            ImString _imString = new ImString(_val);

            if(ImGui.inputText(_name + ": ", _imString, ImGuiInputTextFlags.CallbackResize)){
                System.out.println(_imString.get());
                a_f.set(a_sender, _imString.get());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean isHeaderNotCollapsed(Class a_type){
        return ImGui.collapsingHeader(a_type.getSimpleName());
    }

    public static void showFields(Object a_sender, boolean a_header){
        Class _type = a_sender.getClass();

        if(!a_header || isHeaderNotCollapsed(_type)){
            Field[] _fields = _type.getDeclaredFields();
            for(Field _f : _fields){
                boolean _private = Modifier.isPrivate(_f.getModifiers());
                boolean  _transient = Modifier.isTransient(_f.getModifiers());

                if(_transient) continue;
                if(_private) _f.setAccessible(true);

                Class _t = _f.getType();
                showField(_t, _f, a_sender);

                if(_private) _f.setAccessible(false);
            }
        }
    }

    private static void showField(Class a_type, Field a_f, Object a_sender){
        if(a_type == int.class)              ImGuiUtils.showIntField(a_f, a_sender);
        else if(a_type == float.class)       ImGuiUtils.showFloatField(a_f, a_sender);
        else if(a_type == boolean.class)     ImGuiUtils.showBoolField(a_f, a_sender);
        else if(a_type == Vector2f.class)    ImGuiUtils.showVec2Field(a_f, a_sender);
        else if(a_type == Vector3f.class)    ImGuiUtils.showVec3Field(a_f, a_sender);
        else if(a_type == String.class)      ImGuiUtils.showStringField(a_f, a_sender);
    }
}
