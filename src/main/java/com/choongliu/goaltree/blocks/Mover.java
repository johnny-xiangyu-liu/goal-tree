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
import com.choongliu.goaltree.blocks.geometry.Projection;
import com.choongliu.goaltree.datastructure.GraphFactory;
import com.choongliu.goaltree.datastructure.Node;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mover {

    @ToString
    private static class SpaceInfo {
        Block b;
        Point p;
    }

    private GraphFactory<MoveAction, String> graphFactory;

    public Mover(GraphFactory factory) {
        graphFactory = factory;
    }

    public Mover() {
        this(GraphFactory.Instance());
    }

    public Node<MoveAction> move(Block a, Block b, BlockInfo info) {
        log.debug("move " + a + " on top of " + b);
        return putOnTop(a, b, new ArrayList<>(), info);
    }

    private Node<MoveAction> putOnTop(Block a, Block b, List<Projection> toAvoid,
                                      BlockInfo blockInfo) {
        if (a.getSize().getWidth() > b.getSize().getWidth()) {
            log.debug("cannot place " + a + " on to " + b);
            return null;
        }

        Node<MoveAction> n = makeNode("Put " + a.getId() + " on to " + b.getId(), null, null);

        List<Projection> aAvoids = Projection.merge(a.getProjection(), toAvoid);
        Node<MoveAction> clearBTopNode = clearTop(b, aAvoids, blockInfo);
        n.addChild(clearBTopNode);

        List<Projection> bAvoids = Projection.merge(b.getProjection(), toAvoid);
        // clear everything on top of a.
        Node<MoveAction> clearATopNode = clearTop(a, bAvoids, blockInfo);
        n.addChild(clearATopNode);

        // move a on top of b.
        SpaceInfo info = new SpaceInfo();
        info.b = b;
        info.p = getTop(a,b);
        Node<MoveAction> moveNode = move(a, info);

        n.addChild(moveNode);
        return n;
    }

    private Point getTop(Block a, Block b) {
        int x = b.getTopLeft().getX();
        int y = b.getTopLeft().getY() + a.getSize().getHeight();
        return new Point(x, y);
    }

    private Node<MoveAction> clearTop(Block a, List<Projection> toAvoid,
                                      BlockInfo blockInfo) {
        log.debug("clear top for " + a);
        Node<MoveAction> n = makeNode("clear top on " + a.getId(), a, null);

        Block boxOnTop = findTopBlockAt(a.getTopLeft().getX(), blockInfo);
        List<Projection> newAvoid = Projection.merge(a.getProjection(), toAvoid);

        while (boxOnTop != null && boxOnTop != a) {
            log.debug("current box on top:" + boxOnTop);
            SpaceInfo info = findSpace(boxOnTop, newAvoid, blockInfo);
            Node<MoveAction> child = move(boxOnTop, info);
            n.addChild(child);
            boxOnTop = findTopBlockAt(a.getTopLeft().getX(), blockInfo);
        }

        return n;
    }

    private SpaceInfo findSpace(Block a, List<Projection> toAvoid, BlockInfo info) {
        Projection table = info.getTable().getProjection();

        Projection required = a.getProjection();

        List<Projection> available = Projection.exclude(table, toAvoid);
        log.debug("found " + available.size() +" available for " + required);
        int x = -1;
        for (Projection p : available) {
            boolean canSupport = p.canSupport(required);
            log.debug(p + " contains " + required + " ?: " + canSupport);
            if (canSupport) {
                x =  p.getStart();
                break;
            }
        }

        if (x == -1) return null;

        SpaceInfo result = new SpaceInfo();
        Block top = findTopBlockAt(x, info);
        int y;
        if (top != null) {
            y = top.getTopLeft().getY();
        } else {
            y = 0;
        }
        y += a.getSize().getHeight();

        Point p = new Point(x, y);
        result.b = top;
        result.p = p;
        return result;
    }


    private Block findTopBlockAt(int x, BlockInfo info) {
        Block result = null;
        for (Block b : info.getBlocks()) {
            if (b.getProjection().contains(x)) {
                if (result == null){
                    result = b;
                } else {
                    if (result.getTopLeft().getY() < b.getTopLeft().getY()) {
                        result = b;
                    }
                }
            }
        }

        if (result == null)
            result = info.getTable();

        return result;
    }

    private Node<MoveAction> move(Block a, SpaceInfo info) {
        log.debug("moving block " + a + " to " + info);
        Node<MoveAction> n = makeNode("Moving " + a.getId() + " to " + info,
                a, info);
        a.getTopLeft().moveTo(info.p);
        return n;
    }

    private Node<MoveAction> makeNode(String msg, Block a, SpaceInfo info) {
        Node<MoveAction> n = graphFactory.newNode();
        n.setData(new MoveAction(msg, a, info == null? null: info.p));
        return n;
    }

}
