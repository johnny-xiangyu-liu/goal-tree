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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
@EqualsAndHashCode
public class Projection {
    private int start, end;

    public Projection(Point start, Point end) {
        this.start = Math.min(start.getX(), end.getX());
        this.end = Math.max(start.getX(), end.getX());
    }

    public Projection(int start, int end) {
        this.start = Math.min(start, end);
        this.end = Math.max(start, end);
    }

    public int getWidth() {
        return getEnd() - getStart();
    }


    public boolean startLeftOf(Projection another) {
        return getStart() < another.getStart();
    }

    public boolean startRightOf(Projection another) {
        return getStart() > another.getStart();
    }

    public boolean endLeftOf(Projection p){
        return getEnd() < p.getEnd();
    }

    public boolean endRightOf(Projection p) {
        return getEnd() > p.getEnd();
    }

    public boolean overlap(Projection another) {
        return getStart() < another.getEnd() && getEnd() > another.getStart();
    }

    public boolean connects(Projection p) {
        return getStart() == p.getEnd() || getEnd() == p.getStart();
    }

    public Projection merge(Projection p) {
        if (!overlap(p) && !connects(p)) throw new IllegalArgumentException();

        return new Projection(
                Math.min(getStart(), p.getStart()),
                Math.max(getEnd(), p.getEnd())
        );
    }

    public boolean canSupport(Projection p) {
        return getWidth() >= p.getWidth();
    }

    public boolean contains(Projection p) {
        return getStart() <= p.getStart() && getEnd() >= p.getEnd();
    }

    public boolean contains(int x) {
        return getStart() <= x && getEnd() > x;
    }

    public static List<Projection> merge(Projection base, List<Projection> newList) {
        List<Projection> baseList = new ArrayList<>();
        baseList.add(base);
        return merge(baseList, newList);
    }

    public static List<Projection> merge(List<Projection> base, List<Projection> newList) {
        List<Projection> result = new ArrayList<>();
        if (base.isEmpty()) return newList;
        if (newList.isEmpty()) return base;

        int i = 0;
        int j = 0;
        Projection curA = null;
        Projection curB = null;
        while (i < base.size() || j < newList.size()) {
            if (curA == null && i < base.size()) {
                curA = base.get(i);
            }

            if (curB == null && j < newList.size()) {
                curB = newList.get(j);
            }

            log.debug(curA +":" + curB);

            if (curA == null && curB == null) {
                break;
            }

            if (curA == null) {
                result.add(curB);
                curB = null;
                j++;
                continue;
            }

            if (curB == null) {
                result.add(curA);
                curA = null;
                i ++;
                continue;
            }

            boolean canMerge = curA.overlap(curB) || curA.connects(curB);
            boolean aLeftB = curA.endLeftOf(curB);

            log.debug("canMerge: " + canMerge +". a on the left? " + aLeftB);
            if (canMerge) {
                if (aLeftB) {
                  curB = curB.merge(curA);
                  curA = null;
                  i++;
                } else {
                    curA = curA.merge(curB);
                    curB = null;
                    j++;
                }
            } else {
                if (aLeftB) {
                    result.add(curA);
                    curA = null;
                    i ++;
                } else {
                    result.add(curB);
                    curB = null;
                    j++;
                }
            }
            log(result);
        }

        if (curA != null) result.add(curA);
        if (curB != null) result.add(curB);


        log.debug("final:");
        log(result);
        return result;
    }


    public static List<Projection> exclude(Projection base, Projection excludes) {
        List<Projection> excludeList = new ArrayList<>();
        excludeList.add(excludes);
        return exclude(base, excludeList);
    }

    public static List<Projection> exclude(Projection base, List<Projection> excludeList) {
        List<Projection> baseList = new ArrayList<>();
        baseList.add(base);
        return exclude(baseList, excludeList);
    }
    /**
     * TODO Need to use binary search instead of linear search.
     * TODO Need to use
     * @param base must be a sorted, non-overlapping list
     * @param projections must be a sorted, non-overlapping list
     *
     * @return
     */
    public static List<Projection> exclude(List<Projection> base, List<Projection> projections) {
        if (base.isEmpty()) return base;
        if (projections.isEmpty()) return base;

        List<Projection> result = new ArrayList<>(base);

        boolean changed = true;
        while (changed) {
            changed = false;
            List<Projection> working = new ArrayList<>();
            ListIterator<Projection> it = result.listIterator();
            while(it.hasNext()) {
                boolean itemChanged = false;
                Projection l = it.next();
                log.debug(">checking " + l);
                ListIterator<Projection> it2 = projections.listIterator();
                while (it2.hasNext()) {
                    Projection l2 = it2.next();
                    log.debug(">>checking " + l2);
                    boolean startLeft = l.startLeftOf(l2);
                    boolean startRight = l.startRightOf(l2);
                    boolean startSame = !startLeft && !startRight;
                    boolean endLeft = l.endLeftOf(l2);
                    boolean endRight = l.endRightOf(l2);

                    if (l.getEnd() <= l2.getStart()) {
                        log.debug("l end left of l2 start");
                        break;
                    }
                    if (l.getStart() >= l2.getEnd()) {
                        log.debug("l start right of l2 end");
                        continue;
                    }

                    if (startLeft) {
                        addTo(working, new Projection(l.getStart(), l2.getStart()));
                        if (endRight) {
                            addTo(working, new Projection(l2.getEnd(), l.getEnd()));
                        }
                        itemChanged = true;
                        changed = true;
                    } else if (startSame) {
                        if (endRight) {
                            addTo(working, new Projection(l2.getEnd(), l.getEnd()));
                            itemChanged = true;
                            changed = true;
                        }
                    } else {
                        if (endRight) {
                            addTo(working, new Projection(l2.getEnd(), l.getEnd()));
                            itemChanged = true;
                            changed = true;
                        }
                    }

                    break;
                }

                if (!itemChanged) {
                    addTo(working, l);
                }
            }

            log(working);
            result = working;
        }

        return result;
    }

    private static void log(List<Projection> l) {
        log.debug("=====Logging result: " + l.size());
        for (Projection p : l)
            log.debug("---" + p);
        log.debug("=========");
    }
    private static void addTo(List<Projection> it, Projection p) {
        log.debug("adding: " + p);
        it.add(p);
    }

}
