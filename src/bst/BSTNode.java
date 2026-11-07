package bst;

/**
 *
 * @author Minh Tien Tran
 */

import model.Character;

public class BSTNode {
    Character info;
    BSTNode left, right;

    public BSTNode(Character x) {
        info = x;
        left = right = null;
    }
}
