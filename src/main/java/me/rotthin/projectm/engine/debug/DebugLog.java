package me.rotthin.projectm.engine.debug;

import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public final class DebugLog {
    private static List<String> logs = new ArrayList<>();
    private static final int MAX_LOGS = 1000;
    private static boolean scrollDown;

    private DebugLog() {}

    public static void log(Object a_text){
        logs.add(a_text.toString());
        scrollDown = true;
    }

    public static void imgui(){
        ImGui.begin("Log");

        if(ImGui.button("Clear")){
            logs.clear();
        }

        ImGui.beginChild("logs");
        for(String _l : logs){
            while(logs.size() >= MAX_LOGS){
                logs.remove(0);
            }
            ImGui.text(_l);
        }

        if(scrollDown){
            scrollDown = false;
            ImGui.setScrollHereY();
        }

        ImGui.endChild();

        ImGui.end();
    }
}
