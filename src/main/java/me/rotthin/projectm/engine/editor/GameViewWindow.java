package me.rotthin.projectm.engine.editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import me.rotthin.projectm.engine.input.MouseListener;
import me.rotthin.projectm.engine.rendering.Window;
import org.joml.Vector2f;

public final class GameViewWindow {
    private static float left, right, top, bottom;

    private GameViewWindow() {}

    public static void imgui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 _winSize = getLargestSizeForViewport();
        ImVec2 _winPos = getCenteredPosForViewport(_winSize);

        ImGui.setCursorPos(_winPos.x, _winPos.y);

        ImVec2 _topLeft = new ImVec2();
        ImGui.getCursorScreenPos(_topLeft);
        _topLeft.x -= ImGui.getScrollX();
        _topLeft.y -= ImGui.getScrollY();
        left = _topLeft.x;
        bottom = _topLeft.y;
        right = _topLeft.x + _winSize.x;
        top = _topLeft.y + _winSize.y;

        MouseListener.setGameViewportPos(new Vector2f(_topLeft.x, _topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(_winSize.x, _winSize.y));

        int _texID = Window.getFrameBuffer().getTextureID();
        ImGui.image(_texID, _winSize.x, _winSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private static ImVec2 getLargestSizeForViewport(){
        ImVec2 _winSize = getWindowSize();

        float _aspectWidth = _winSize.x;;
        float _aspectHeight = _aspectWidth / Window.getTargetAspectRatio();

        // We need to switch to pillarbox mode
        if(_aspectHeight > _winSize.y){
            _aspectHeight = _winSize.y;
            _aspectWidth = _aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(_aspectWidth, _aspectHeight);
    }

    private static ImVec2 getCenteredPosForViewport(ImVec2 a_aspectSize){
        ImVec2 _winSize = getWindowSize();

        float _viewportX = (_winSize.x/2f) - (a_aspectSize.x/2f);
        float _viewportY = (_winSize.y/2f) - (a_aspectSize.y/2f);

        return new ImVec2(_viewportX + ImGui.getCursorPosX(), _viewportY + ImGui.getCursorPosY());
    }

    private static ImVec2 getWindowSize(){
        ImVec2 _winSize = new ImVec2();
        ImGui.getContentRegionAvail(_winSize);

        _winSize.x -= ImGui.getScrollX();
        _winSize.y -= ImGui.getScrollY();

        return _winSize;
    }

    public static boolean getWantCaptureMouse() {
        return MouseListener.getX() >= left && MouseListener.getX() <= right && MouseListener.getY() >= bottom && MouseListener.getY() <= top;
    }
}
