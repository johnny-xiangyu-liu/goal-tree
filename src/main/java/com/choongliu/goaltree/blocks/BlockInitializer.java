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

package com.choongliu.goaltree.blocks;

import com.choongliu.goaltree.blocks.geometry.Point;
import com.choongliu.goaltree.blocks.geometry.Size;
import java.util.ArrayList;
import java.util.List;

public class BlockInitializer {

    public static BlockInfo init() {
        List<Block> blocks = new ArrayList<>();
        int id = 1;
        blocks.add(make(id, 0, 100, 100, 80));
        id ++;
        blocks.add(make(id, 100, 60, 200, 40));
        id++;
        blocks.add(make(id, 0, 120, 100, 20));
        id++;
        blocks.add(make(id, 300, 40, 100, 20));
        return new BlockInfo(blocks, initTable());
    }

    private static Block initTable() {
        return make(0, 0, 20, 800, 20);
    }

    private static Block make(long id, int left, int top, int width, int height) {
        return new Block(id, new Point(left, top), new Size(width, height));
    }
}
