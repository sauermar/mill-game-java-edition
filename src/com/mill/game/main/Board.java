package com.mill.game.main;

import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.models.Coordinates;

import java.awt.*;
import java.awt.Color;
import java.util.List;

public class Board {

    private static final int radius = 10;

    private List<Coordinates> boardColumns = List.of(
            //left first column
            new Coordinates(150,80),
            new Coordinates(150,290),
            new Coordinates(150,500),
            //left second column
            new Coordinates(230,130),
            new Coordinates(230,290),
            new Coordinates(230,450),
            //left third column
            new Coordinates(310,180),
            new Coordinates(310,290),
            new Coordinates(310,400),
            //middle upper column
            new Coordinates(390,80),
            new Coordinates(390,130),
            new Coordinates(390,180),
            //middle lower column
            new Coordinates(390,500),
            new Coordinates(390,450),
            new Coordinates(390,400),
            //right first column
            new Coordinates(470,180),
            new Coordinates(470,290),
            new Coordinates(470,400),
            //right second column
            new Coordinates(550,130),
            new Coordinates(550,290),
            new Coordinates(550,450),
            //right third column
            new Coordinates(630,80),
            new Coordinates(630,290),
            new Coordinates(630,500)
    );

    private List<Coordinates> boardRows = List.of(
            //first row
            new Coordinates(150,80),
            new Coordinates(390,80),
            new Coordinates(630,80),
            //second row
            new Coordinates(230,130),
            new Coordinates(390,130),
            new Coordinates(550,130),
            //third row
            new Coordinates(310,180),
            new Coordinates(390,180),
            new Coordinates(470,180),
            //forth left row
            new Coordinates(150,290),
            new Coordinates(230,290),
            new Coordinates(310,290),
            //forth right row
            new Coordinates(470,290),
            new Coordinates(550,290),
            new Coordinates(630,290),
            //fifth row
            new Coordinates(310,400),
            new Coordinates(390,400),
            new Coordinates(470,400),
            //sixth row
            new Coordinates(230,450),
            new Coordinates(390,450),
            new Coordinates(550,450),
            //seventh row
            new Coordinates(150,500),
            new Coordinates(390,500),
            new Coordinates(630,500)
    );

    public List<Coordinates> getBoardColumns(){
        return boardColumns;
    }

    public List<Coordinates> getBoardRows(){
        return boardRows;
    }

    public void renderBoard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g.setColor(Color.gray);
        Coordinates coordinates, nextCoordinates;
        for (int i = 1; i <= boardColumns.size(); i++ ) {
            coordinates = boardColumns.get(i - 1);
            Helpers.drawCenteredCircle(g, coordinates.getX(), coordinates.getY(), radius );
            if (i % 3 != 0) {
                //draw column line
                nextCoordinates = boardColumns.get(i);
                g.drawLine(coordinates.getX(), coordinates.getY(), nextCoordinates.getX(), nextCoordinates.getY());
                //draw row line
                coordinates = boardRows.get(i - 1);
                nextCoordinates = boardRows.get(i);
                g.drawLine(coordinates.getX(), coordinates.getY(), nextCoordinates.getX(), nextCoordinates.getY());
            }
        }
    }

}

