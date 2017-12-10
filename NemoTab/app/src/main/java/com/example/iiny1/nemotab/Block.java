package com.example.iiny1.nemotab;

import java.util.Random;

/**
 * Created by iiny1 on 2017-11-22.
 */

public class Block {
    private int rows;
    private int cols;
    private int[][] blockArr;
    private int colorCode;

    public Block(int blockCode) {
        if (blockCode == 1)
        {
            rows = 3;
            cols = 2;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = colorCode; blockArr[0][1] = 0;
            blockArr[1][0] = colorCode; blockArr[1][1] = colorCode;
            blockArr[2][0] = 0; blockArr[2][1] = colorCode;
        }
        else if(blockCode == 2)
        {
            rows = 3;
            cols = 2;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = 0; blockArr[0][1] = colorCode;
            blockArr[1][0] = colorCode; blockArr[1][1] = colorCode;
            blockArr[2][0] = colorCode; blockArr[2][1] = 0;
        }
        else if(blockCode == 3)
        {
            rows = 3;
            cols = 2;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = colorCode; blockArr[0][1] = 0;
            blockArr[1][0] = colorCode; blockArr[1][1] = colorCode;
            blockArr[2][0] = colorCode; blockArr[2][1] = 0;
        }
        else if(blockCode == 4)
        {
            rows = 3;
            cols = 2;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = colorCode; blockArr[0][1] = 0;
            blockArr[1][0] = colorCode; blockArr[1][1] = 0;
            blockArr[2][0] = colorCode; blockArr[2][1] = colorCode;
        }
        else if(blockCode == 5)
        {
            rows = 3;
            cols = 2;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = 0; blockArr[0][1] = colorCode;
            blockArr[1][0] = 0; blockArr[1][1] = colorCode;
            blockArr[2][0] = colorCode; blockArr[2][1] = colorCode;
        }
        else if(blockCode == 6)
        {
            rows = 4;
            cols = 1;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = colorCode;
            blockArr[1][0] = colorCode;
            blockArr[2][0] = colorCode;
            blockArr[3][0] = colorCode;
        }
        else
        {
            rows = 2;
            cols = 2;
            colorCode = blockCode;
            blockArr = new int[rows][cols];
            blockArr[0][0] = colorCode; blockArr[0][1] = colorCode;
            blockArr[1][0] = colorCode; blockArr[1][1] = colorCode;
        }
        Random random = new Random();

        for(int cnt = random.nextInt(4); cnt > 0; cnt--)
            TurnBlock();
    }

    public int[][] getBlock()
    {
        return blockArr;
    }

    public void TurnBlock()
    {
        int[][] turnArr = new int[cols][rows];
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                turnArr[j][i] = blockArr[rows - 1 - i][j];
            }
        }
        blockArr = turnArr;
        int temp = rows;
        rows = cols;
        cols = temp;
    }

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public int getColorCode() { return colorCode; }
}
