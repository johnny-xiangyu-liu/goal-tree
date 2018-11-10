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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import lombok.Getter;

public class BlockLayoutManager implements LayoutManager {
    @Getter
    private int preferredWidth = 0;
    @Getter
    private int preferredHeight = 0;
    private boolean measured = false;
    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        measure(parent);
        return new Dimension(preferredWidth, preferredHeight);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        if (!measured) {
            measure(parent);
        }

        int nComps = parent.getComponentCount();
        for (int i = 0; i < nComps; i++) {
            BlockPanel c = (BlockPanel) parent.getComponent(i);
            int left = c.getTopLeft().getX();
            int top = preferredHeight - c.getTopLeft().getY();
            c.setBounds(left, top, c.getBlockSize().getWidth(), c.getBlockSize().getHeight());
        }
    }

    private void measure(Container parent) {
        int nComps = parent.getComponentCount();
        //Reset preferred/minimum width and height.
        preferredWidth = 0;
        preferredHeight = 0;

        for (int i = 0; i < nComps; i++) {
            BlockPanel c = (BlockPanel) parent.getComponent(i);
            preferredWidth = Math.max(preferredWidth, c.getBlockSize().getWidth() + c.getTopLeft().getX());
            preferredHeight = Math.max(preferredHeight, c.getBlockSize().getHeight() + c.getTopLeft().getY());
        }

        preferredHeight += 200; // height offset.
        measured = true;
    }
}
