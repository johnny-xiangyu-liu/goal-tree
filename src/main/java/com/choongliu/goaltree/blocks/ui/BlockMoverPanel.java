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
import com.choongliu.goaltree.blocks.BlockInfo;
import com.choongliu.goaltree.blocks.Mover;
import com.choongliu.goaltree.blocks.geometry.Size;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BlockMoverPanel extends JPanel {

    public BlockMoverPanel(BlockInfo mover) {
        setLayout(new BlockLayoutManager());

        Block table = mover.getTable();
        List<Block> blocks = mover.getBlocks();

        for (Block b : blocks) {
            add(String.valueOf(b.getId()), new BlockPanel(b));
        }
        add(String.valueOf(table.getId()), new BlockPanel(table));

        setPreferredSize(getLayout().preferredLayoutSize(this));
    }
}
