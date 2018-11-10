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

package com.choongliu.goaltree.datastructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @param <T> as the datastructure type held by the node
 */
@Slf4j
public class Node<T> extends GraphEntity {
    @Getter private T data;
    @Getter private Edge<?> parent;
    @Getter private List<Edge> children = new ArrayList<>();

    public Node(GraphFactory factory) {
        super(factory);
    }

    public void addChild(Node<T> child){
        if (child.hasParent())
            throw new IllegalStateException("Child has parent");

        Edge e = getGraphFactory().newEdge(this, child);
        children.add(e);
        child.parent = e;
    }

    public void removeChild(Node<T> child) {
        Iterator<Edge> iterator = children.iterator();
        while(iterator.hasNext()) {
            Edge e = iterator.next();
            if (e.getTo() == child) {
                iterator.remove();
                child.parent = null;
                return;
            }
        }
    }

    public boolean hasChild() { return children.size() > 0; }
    public boolean hasParent() {
        return parent != null;
    }

    public void setData(T data) {
        log.debug("setting data: " + data);
        this.data = data;
    }
}
