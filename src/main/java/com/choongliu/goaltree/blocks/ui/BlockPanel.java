/**
 * MIT License
 * <p>
 * Copyright (c) 2018 Xiangyu Liu
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.choongliu.goaltree.blocks.ui;


import com.choongliu.goaltree.blocks.Block;
import com.choongliu.goaltree.blocks.Mover;
import com.choongliu.goaltree.blocks.geometry.Point;
import com.choongliu.goaltree.blocks.geometry.Size;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class BlockPanel extends JPanel {

    private static Color[] sColors = {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.CYAN,
            Color.PINK,
            Color.YELLOW
    };

    @Getter
    private Block block;

    @Getter
    private Size blockSize;

    @Getter
    private Point topLeft;

    public BlockPanel(Block block){
        this.block = block;
        blockSize = Size.from(block.getSize());
        topLeft = Point.from(block.getTopLeft());
        setLayout(null);
        setSize(blockSize.getWidth(), blockSize.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBlock(block, g);
    }

    private Color fromBlock(Block b) {
        return sColors[(int) b.getId() % sColors.length];
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(block.toAwtDimention());
    }

    public void drawBlock(Block b, Graphics g) {

        int fontSize = 12;
        Font font = new Font("TimesRoman", Font.PLAIN, fontSize);

        g.setColor(fromBlock(b));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);

        String text = "id:"+ String.valueOf(b.getId());
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    public void updateTopLeft(Point newTopLeft, Size parentSize) {

        this.topLeft = newTopLeft;

        int left = getTopLeft().getX();
        int top = parentSize.getHeight() - getTopLeft().getY();
        setLocation(left, top);
    }
}