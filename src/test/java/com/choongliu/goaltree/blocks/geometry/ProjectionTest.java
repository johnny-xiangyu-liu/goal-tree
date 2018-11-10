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
import jdk.internal.jline.internal.TestAccessible;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectionTest {

    @Test
    public void TestExcludeSimple() {
        List<Projection> base = new ArrayList<>();
        Projection table = new Projection(0, 600);
        base.add(table);

        List<Projection> toExclude = new ArrayList<>();
        Projection toExclude1 = new Projection(0, 200);
        toExclude.add(toExclude1);

        List<Projection> result = Projection.exclude(base, toExclude);
        assertEquals(1, result.size());
        Projection expected = new Projection(200, 600);
        assertEquals(expected, result.get(0));
    }

    @Test
    public void TestExcludeSimpleInMiddle() {
        List<Projection> base = new ArrayList<>();
        Projection table = new Projection(0, 600);
        base.add(table);

        List<Projection> toExclude = new ArrayList<>();
        Projection toExclude1 = new Projection(100, 200);
        toExclude.add(toExclude1);

        List<Projection> result = Projection.exclude(base, toExclude);
        assertEquals(2, result.size());

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 100));
        expected.add(new Projection(200, 600));

        ensure(expected, result);
    }

    @Test
    public void TestExcludeSimpleNoOverlap() {
        List<Projection> base = new ArrayList<>();
        Projection table = new Projection(0, 600);
        base.add(table);

        List<Projection> toExclude = new ArrayList<>();
        Projection toExclude1 = new Projection(600, 700);
        toExclude.add(toExclude1);

        List<Projection> result = Projection.exclude(base, toExclude);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 600));

        ensure(expected, result);
    }

    @Test
    public void TestExcludeTwoBaseSimple() {
        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 100));
        base.add(new Projection(200, 300));

        List<Projection> toExclude = new ArrayList<>();
        toExclude.add(new Projection(50, 250));

        List<Projection> result = Projection.exclude(base, toExclude);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 50));
        expected.add(new Projection(250, 300));

        ensure(expected, result);
    }

    @Test
    public void TestExcludeTwoExcludeSimple() {

        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 600));

        List<Projection> toExclude = new ArrayList<>();
        toExclude.add(new Projection(50, 100));
        toExclude.add(new Projection(200, 300));

        List<Projection> result = Projection.exclude(base, toExclude);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 50));
        expected.add(new Projection(100, 200));
        expected.add(new Projection(300, 600));

        ensure(expected, result);
    }

    @Test
    public void TestExcludeTwoBaseAndExcludeSimple() {

        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 300));
        base.add(new Projection(500, 600));

        List<Projection> toExclude = new ArrayList<>();
        toExclude.add(new Projection(50, 100));
        toExclude.add(new Projection(200, 300));

        List<Projection> result = Projection.exclude(base, toExclude);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 50));
        expected.add(new Projection(100, 200));
        expected.add(new Projection(500, 600));

        ensure(expected, result);
    }

    @Test
    public void testMergeSimple() {
        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 300));

        List<Projection> toMerge = new ArrayList<>();
        toMerge.add(new Projection(50, 100));
        toMerge.add(new Projection(200, 300));


        List<Projection> result = Projection.merge(base, toMerge);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 300));
        ensure(expected, result);
    }

    @Test
    public void TestMergeSimple2() {
        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 300));

        List<Projection> toMerge = new ArrayList<>();
        toMerge.add(new Projection(300, 400));


        List<Projection> result = Projection.merge(base, toMerge);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 400));
        ensure(expected, result);
    }


    @Test
    public void TestMergeSimple3() {
        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 300));
        base.add(new Projection(301, 400));
        base.add(new Projection(401, 490));

        List<Projection> toMerge = new ArrayList<>();
        toMerge.add(new Projection(340, 450));


        List<Projection> result = Projection.merge(base, toMerge);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 300));
        expected.add(new Projection(301, 490));
        ensure(expected, result);
    }

    @Test
    public void TestMergeSimple4() {
        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 300));
        base.add(new Projection(301, 400));
        base.add(new Projection(401, 490));

        List<Projection> toMerge = new ArrayList<>();
        toMerge.add(new Projection(240, 450));
        toMerge.add(new Projection(340, 450));


        List<Projection> result = Projection.merge(base, toMerge);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 490));
        ensure(expected, result);
    }


    @Test
    public void TestMergeSimple5() {
        List<Projection> base = new ArrayList<>();
        base.add(new Projection(0, 300));
        base.add(new Projection(301, 400));
        base.add(new Projection(401, 490));

        List<Projection> toMerge = new ArrayList<>();
        toMerge.add(new Projection(0, 900));

        List<Projection> result = Projection.merge(base, toMerge);

        List<Projection> expected = new ArrayList<>();
        expected.add(new Projection(0, 900));
        ensure(expected, result);
    }


    private void ensure(List<Projection> expected, List<Projection> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i<expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }


}
