package me.rotthin.projectm.engine.components;

import me.rotthin.projectm.engine.debug.DebugDraw;
import me.rotthin.projectm.engine.main.Camera;
import me.rotthin.projectm.engine.rendering.Window;
import me.rotthin.projectm.engine.utils.Constants;
import org.joml.Vector2f;

public class Grid extends Component {
    @Override
    public void update(float a_dt) {
        Vector2f _camPos = Window.getCurrentScene().getCamera().position;

        int _firstX = ((int)(_camPos.x / Constants.GRID_WIDTH)-1) * Constants.GRID_HEIGHT;
        int _firstY = ((int)(_camPos.y / Constants.GRID_HEIGHT)-1) * Constants.GRID_WIDTH;

        int _vLines = (int) (Camera.PROJ_SIZE.x / Constants.GRID_WIDTH)+2;
        int _hLines = (int) (Camera.PROJ_SIZE.y / Constants.GRID_HEIGHT)+2;

        int _width = Camera.PROJ_SIZE.x + Constants.GRID_WIDTH*2;
        int _height = Camera.PROJ_SIZE.y + Constants.GRID_WIDTH*2;

        int _maxlines = Math.max(_vLines, _hLines);
        for(int i=0; i<_maxlines; i++){
            int _x = _firstX + (Constants.GRID_WIDTH * i);
            int _y = _firstY + (Constants.GRID_HEIGHT * i);

            if(i < _vLines){
                DebugDraw.addLine(new Vector2f(_x, _firstY), new Vector2f(_x, _y + _height), Constants.COLOR3_GRAY, 1, DebugDraw.LifeTimeType.frames);
            }

            if(i < _hLines){
                DebugDraw.addLine(new Vector2f(_firstX, _y), new Vector2f(_firstX + _width, _y), Constants.COLOR3_GRAY, 1, DebugDraw.LifeTimeType.frames);
            }
        }
    }
}
