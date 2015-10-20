package connect4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Connect4 extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 496;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 528;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    final int numRows = 8;
    final int numColumns = 8;
    int columnWidth;
    int rowHeight;
    int currentRow;
    int currentColumn;
    int endRow;
    int connectWin;
    boolean teamRed = true;
    boolean moveFinished = true;
    int timeCount;
    enum Win{redWin, blackWin, none}
    Win winType;
    
    Piece board[][];

    static Connect4 frame1;
    public static void main(String[] args) {
        frame1 = new Connect4();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
        
    }

    
    public Connect4() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    int xpos = e.getX() - getX(0);
                    int ypos = e.getY() - getY(0);
                    
                    int row = ypos/rowHeight;
                    int column = xpos/columnWidth;
                    
                    if(winType == Win.none)
                    {
                        if(moveFinished)
                        {
                            for(int i = numRows-1;i >= 0; i--)
                            {
                                if(row >= numRows || e.getX() < getX(0) || column >= numColumns || e.getY() < getY(0))
                                    return;
                                    if(teamRed)
                                    {
                                        if(board[i][column] == null)
                                        {
                                            endRow = i;
                                        }
                                        if(board[0][column] == null)
                                        board[0][column] = new Piece(Color.RED);
                                        moveFinished = false;
                                        currentRow = 0;
                                        currentColumn = column;
                                        teamRed = false;
                                        return;
                                    }
                                    else
                                    {
                                        if(board[i][column] == null)
                                        {
                                            endRow = i;
                                        }
                                        if(board[0][column] == null)
                                        board[0][column] = new Piece(Color.BLACK);
                                        teamRed = true;
                                        moveFinished = false;
                                        currentRow = 0;
                                        currentColumn = column;
                                        return;
                                    }
                            }
                        }

                    }
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_RIGHT == e.getKeyCode())
                {

                }
                if (e.VK_LEFT == e.getKeyCode())
                {

                }
                if (e.VK_UP == e.getKeyCode())
                {

                }
                if (e.VK_DOWN == e.getKeyCode())
                {

                }
                if (e.VK_E == e.getKeyCode())
                {
                    reset();
                }

                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);
        
        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        g.setColor(Color.black);
//horizontal lines
        for (int zi=1;zi<numRows;zi++)
        {
            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
        }
//vertical lines
        for (int zi=1;zi<numColumns;zi++)
        {
            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
        }

        
        for (int zrow=0;zrow<numRows;zrow++)
        {
            for (int zcolumn=0;zcolumn<numColumns;zcolumn++)
            {
                if (board[zrow][zcolumn] != null)
                {
                    g.setColor(board[zrow][zcolumn].getColor());

                    g.fillOval(getX(0)+zcolumn*getWidth2()/numColumns,
                    getY(0)+zrow*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }
            }
        }
        
        if(winType == Win.blackWin)
        {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Impact",Font.BOLD,60));
            g.drawString("Black Wins", 140, 250);
        }
        if(winType == Win.redWin)
        {
            g.setColor(Color.RED);
            g.setFont(new Font("Impact",Font.BOLD,60));
            g.drawString("Red Wins", 140, 250);
        }
        
        gOld.drawImage(image, 0, 0, null);
    }


////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.015;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
        board = new Piece[numRows][numColumns];
        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
            {
                board[zrow][zcolumn] = null;
            }
        }
                columnWidth = getWidth2()/numColumns;
                rowHeight = getHeight2()/numRows;
                timeCount = 0;
                connectWin = 4;
                winType = Win.none;
//        board[1][3] = new Piece(Color.RED);

    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();
        }
        if(winType == Win.none)
        {
            if(timeCount % 1 == 0)
            {
                boolean movedOnce = false;
                    for(int zrow = 0; zrow < numRows; zrow++)
                    {
                        if(zrow+1<numRows && board[zrow][currentColumn] != null && board[zrow+1][currentColumn] == null && !movedOnce)
                        {
                            board[zrow+1][currentColumn] = new Piece(board[zrow][currentColumn].getColor());
                            board[zrow][currentColumn] = null;
                            movedOnce = true;
                            return;
                        }
                        else if(zrow == endRow)
                        {
                            currentRow = zrow;
                            moveFinished = true;
                        }
                    }
                    movedOnce = false;
                    if(moveFinished)
                    checkWin(board[currentRow][currentColumn]);
            }
            timeCount++;
        }
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
    public void checkWin(Piece _piece)
    {
        int currentConnected = 0;
        
            for(int zcolumn = 0; zcolumn < numColumns; zcolumn++)
                    {
                        for(int zrow = 0; zrow < numRows-(connectWin-1); zrow++)
                        {
                                if(board[zrow][zcolumn] != null)
                                {
                                    for(int i = 0;i<connectWin;i++)
                                    {
                                        if(board[zrow+1][zcolumn] != null && 
                                           board[zrow][zcolumn].getColor() == board[zrow+i][zcolumn].getColor())
                                            currentConnected++;
                                    }
                                }
                                if(currentConnected>=connectWin)
                                {
                                    for(int i = 0;i<connectWin;i++)
                                        {
                                        if(board[zrow+i][zcolumn].getColor() == Color.RED)
                                            winType = Win.redWin;
                                        else
                                            winType = Win.blackWin;
                                        board[zrow+i][zcolumn].setColor(Color.BLUE);
                                        }
                                }
                                currentConnected = 0;
                        }
                    }
            for(int zcolumn = 0; zcolumn < numColumns-3; zcolumn++)
                    {
                        for(int zrow = 0; zrow < numRows; zrow++)
                        {
                            if(board[zrow][zcolumn] != null)
                            {
                                for(int i = 0;i<connectWin;i++)
                                    {
                                    if(board[zrow][zcolumn+i] != null &&
                                       board[zrow][zcolumn].getColor() == board[zrow][zcolumn+i] .getColor())
                                        currentConnected++;
                                    }
                            }
                            if(currentConnected>=connectWin)
                                {
                                    for(int i = 0;i<connectWin;i++)
                                        {
                                        if(board[zrow][zcolumn+i].getColor() == Color.RED)
                                            winType = Win.redWin;
                                        else
                                            winType = Win.blackWin;
                                        board[zrow][zcolumn+i].setColor(Color.BLUE);
                                        }
                                }
                                currentConnected = 0;
                        }
                        for(int zrow = 0; zrow < numRows-3; zrow++)
                        {
                            if(board[zrow][zcolumn] != null)
                            {
                                for(int i = 0;i<connectWin;i++)
                                    {
                                    if(board[zrow+i][zcolumn+i] != null && 
                                       board[zrow][zcolumn].getColor() == board[zrow+i][zcolumn+i].getColor())
                                       currentConnected++;
                                    }
                            }
                            if(currentConnected>=connectWin)
                                {
                                    for(int i = 0;i<connectWin;i++)
                                    {
                                        if(board[zrow+i][zcolumn+i].getColor() == Color.RED)
                                            winType = Win.redWin;
                                        else
                                            winType = Win.blackWin;
                                        board[zrow+i][zcolumn+i].setColor(Color.BLUE);
                                    }
                                }
                                currentConnected = 0;
                        }
                        for(int zrow = 3; zrow < numRows; zrow++)
                        {
                            if(board[zrow][zcolumn] != null)
                            {
                                for(int i = 0;i<connectWin;i++)
                                    {
                                    if(board[zrow-i][zcolumn+i] != null && 
                                           board[zrow][zcolumn].getColor() == board[zrow-i][zcolumn+i] .getColor())
                                            currentConnected++;
                                    }
                            }
                            if(currentConnected>=connectWin)
                                {
                                    for(int i = 0;i<connectWin;i++)
                                    {
                                        if(board[zrow-i][zcolumn+i].getColor() == Color.RED)
                                            winType = Win.redWin;
                                        else
                                            winType = Win.blackWin;
                                        board[zrow-i][zcolumn+i].setColor(Color.BLUE);
                                    }
                                }
                                currentConnected = 0;
                        }
                    }
    }
}
