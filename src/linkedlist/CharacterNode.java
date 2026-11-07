package linkedlist;

import model.Character;

/**
 *
 * @author Minh Tien Tran
 */
public class CharacterNode {
    Character info;
    CharacterNode next;

    public CharacterNode() {}

    public CharacterNode(Character x, CharacterNode p) {
        info = x;
        next = p;
    }

    public CharacterNode(Character x) {
        this(x, null);
    }
}
