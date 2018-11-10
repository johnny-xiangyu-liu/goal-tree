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
import com.choongliu.goaltree.blocks.MoveAction;
import com.choongliu.goaltree.blocks.geometry.Point;
import com.choongliu.goaltree.blocks.geometry.Size;
import com.choongliu.goaltree.datastructure.Edge;
import com.choongliu.goaltree.datastructure.Node;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlockAnimator implements Runnable {

    private BlockMoverPanel container;
    private Map<Long, BlockPanel> panels;
    private Node<MoveAction> root;
    private int highestY;

    /* The number of pixels to shift the label on each cycle */
    private static final int DELTA_X = 2;

    /* The number of milliseconds to pause on each cycle */
    private static final int PAUSE_TIME = 15;

    public BlockAnimator(BlockMoverPanel panel, Node<MoveAction> root) {
        container = panel;
        this.root = root;
        panels = new HashMap<>();
        int size = panel.getComponentCount();
        highestY = 0;
        for (int i = 0; i<size; i++ ){
            BlockPanel c = (BlockPanel) panel.getComponent(i);
            log.debug(c.getName() +" is mapped to " + c.getBlock());
            panels.put(c.getBlock().getId(), c);
            highestY = Math.max(highestY, c.getTopLeft().getY());
        }

        highestY = Math.min(container.getHeight(), highestY + 80);
    }

    public void run() {
        List<MoveAction> actions = findActionableMoves(root);
        if (actions.isEmpty()) return;

        for (int i = 0; i< actions.size(); i++) {
            MoveAction a = actions.get(i);

            performAction(a, panels);
        }
    }

    private void performAction(MoveAction a, Map<Long, BlockPanel> panels) {
        log.debug("performing action :" + a);
        Block target = a.getBlock();
        BlockPanel ui = panels.get(target.getId());
        if (ui == null) {
            // huh?
            log.error("Cannot find the ui for " + target);
            return;
        }
        if (a.getTopLeft() == null) return;

        Point first = new Point(ui.getTopLeft().getX(), highestY);
        Point second = new Point(a.getTopLeft().getX(), highestY);
        animate(ui, first);
        animate(ui, second);
        animate(ui, a.getTopLeft());
    }

    private void animate(BlockPanel ui, Point topLeft) {
        Point cur = ui.getTopLeft();
        log.debug("ui :" + ui +", to new top left:" + topLeft);
        while(!cur.equals(topLeft)) {
            int dx = topLeft.getX() - cur.getX();
            if (Math.abs(dx) > DELTA_X) {
                dx = DELTA_X * (dx / Math.abs(dx));
            }

            int dy = topLeft.getY() - cur.getY();
            if (Math.abs(dy) > DELTA_X) {
                dy = DELTA_X * (dy / Math.abs(dy));
            }
            Point newPoint = new Point(cur.getX() + dx, cur.getY() + dy);

            ui.updateTopLeft(newPoint,
                    new Size(container.getWidth(), container.getHeight()));

            cur = ui.getTopLeft();
            try { Thread.sleep(PAUSE_TIME); }
            catch (InterruptedException e) { }
        }
    }


    private List<MoveAction> findActionableMoves(Node<MoveAction> root) {
        List<MoveAction> result = new ArrayList<>();
        LinkedList<Node<MoveAction>> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()) {
            Node<MoveAction> n = q.poll();
            if (n.hasChild()) {
                List<Node<MoveAction>> children = new ArrayList<>();
                for (Edge e : n.getChildren()) {
                    children.add(e.getTo());
                }
                q.addAll(0, children);
            } else {
                result.add(n.getData());
            }
        }
        return result;

    }
}
