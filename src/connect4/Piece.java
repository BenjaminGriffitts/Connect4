package connect4;

import java.awt.*;
public class Piece {
    private Color color;
    Piece(Color _color)
    {
        color = _color;
    }
    public Color getColor()
    {
        return(color);
    }
    public void setColor(Color _color)
    {
        color = _color;
    }
}
