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
import com.choongliu.goaltree.blocks.MoveAction;
import com.choongliu.goaltree.blocks.Mover;
import com.choongliu.goaltree.datastructure.Node;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControlPanel extends JFrame {
    private BlockInfo blockInfo;
    private JPanel top;
    private JPanel bottom;
    private JPanel main;
    private BlockMoverPanel moverPanel;
    private Block a;
    private Block b;
    private Mover mover;
    private ActionListener topAction = e -> {
        long id = Long.valueOf(e.getActionCommand());
        log.debug("pressing on top " + id);
        for (Block block : blockInfo.getBlocks()) {
            if (block.getId() == id) {
                this.a = block;
                break;
            }
        }
        tryMove();
    };

    private ActionListener bottomAction = e -> {
        long id = Long.valueOf(e.getActionCommand());
        log.debug("pressing on bottom " + id);

        for (Block block : blockInfo.getBlocks()) {
            if (block.getId() == id) {
                this.b = block;
                break;
            }
        }
        tryMove();
    };

    public ControlPanel(BlockInfo blockInfo, Mover mover) {
        super();
        this.blockInfo = blockInfo;
        this.mover = mover;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        top = newPanel();
        bottom = newPanel();
        main = new JPanel();
        add(top);
        add(bottom);
        add(main);
        populate(blockInfo);
        moverPanel =new BlockMoverPanel(blockInfo);
        main.add(moverPanel);
    }

    private JPanel newPanel() {
        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));
        return result;
    }

    private void populate(BlockInfo state) {
        for (Block b : state.getBlocks()) {
            JButton button = new JButton();
            button.setText("Block: " + b.getId());
            button.setActionCommand(String.valueOf(b.getId()));
            button.addActionListener(topAction);
            top.add(button);
        }

        for (Block b : state.getBlocks()) {
            JButton button = new JButton();
            button.setText("Block: " + b.getId());
            button.setActionCommand(String.valueOf(b.getId()));
            button.addActionListener(bottomAction);
            bottom.add(button);
        }
    }

    private void tryMove() {
        log.debug("try move " + a +" on to " + b);
        if (a != null && b != null) {
            Node<MoveAction> root = mover.move(a, b, blockInfo);
            a = null;
            b = null;

            /* TODO uncomment this to see the goal tree hierarchy
            JFrame tree = new JFrame();
            tree.add(new TreePanel(root));
            tree.pack();
            tree.setVisible(true);
            */
            new Thread(new BlockAnimator(moverPanel, root)).start();
        }
    }

}
