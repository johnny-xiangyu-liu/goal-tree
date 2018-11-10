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

package com.choongliu.goaltree.blocks.geometry;

import com.choongliu.goaltree.blocks.Movable;
import java.awt.Dimension;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@AllArgsConstructor
public class Rectangle implements Movable {
    /**
     * Center of the rectangle
     */
    private Point topLeft;
    private Size size;

    @Override
    public void moveTo(int x, int y) {
        topLeft.moveTo(x,y);
    }

    @Override
    public void moveBy(int x, int y) {
        topLeft.moveBy(x,y);
    }

    public boolean canSupport(Rectangle another) {
        return this.getSize().getWidth() > another.getSize().getWidth();
    }

    /**
     * Return the center point where the given rectangle will be.
     * @param another
     * @return
     */
    public Point support(Rectangle another) {
        if (!canSupport(another)) return null;
        return new Point(getTopLeft().getX(), getTopLeft().getY() + another.getSize().getHeight());
    }

    public Projection getProjection() {
        int leftTopX = topLeft.getX();
        return new Projection(leftTopX, leftTopX + size.getWidth());
    }

    public java.awt.Rectangle toAwtRect() {
        return new java.awt.Rectangle(topLeft.getX(), topLeft.getY(), size.getWidth(), size.getHeight());
    }

    public Dimension toAwtDimention() {
        return new Dimension(size.getWidth(), size.getHeight());
    }

}
