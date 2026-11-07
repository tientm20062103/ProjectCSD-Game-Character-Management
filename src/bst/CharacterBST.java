package bst;

import java.util.ArrayList;
import model.Character;

/**
 *
 * @author Minh Tien Tran
 */

public class CharacterBST {

    private BSTNode root;

    public CharacterBST() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean insert(Character character) {
        if (root == null) {
            root = new BSTNode(character);
            return true;
        }
        BSTNode f = null, p = root;
        while (p != null) {
            int cmp = character.getName().compareToIgnoreCase(p.info.getName());
            if (cmp == 0) {
                return false;
            }
            f = p;
            if (cmp < 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        if (character.getName().compareToIgnoreCase(f.info.getName()) < 0) {
            f.left = new BSTNode(character);
        } else {
            f.right = new BSTNode(character);
        }
        return true;
    }

    // search de quy (giong slide)
    private BSTNode search(BSTNode p, String name) {
        if (p == null) {
            return null;
        }
        int cmp = name.compareToIgnoreCase(p.info.getName());
        if (cmp == 0) {
            return p;
        }
        if (cmp < 0) {
            return search(p.left, name);
        } else {
            return search(p.right, name);
        }
    }

    public Character search(String name) {
        BSTNode p = search(root, name);
        return (p == null) ? null : p.info;
    }

    private BSTNode delete(BSTNode p, String name) {
        if (p == null) {
            return null;
        }
        int cmp = name.compareToIgnoreCase(p.info.getName());
        if (cmp < 0) {
            p.left = delete(p.left, name);
        } else if (cmp > 0) {
            p.right = delete(p.right, name);
        } else {
            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }
            BSTNode m = p.right;
            while (m.left != null) {
                m = m.left;
            }
            p.info = m.info;
            p.right = delete(p.right, m.info.getName());
        }
        return p;
    }

    public void remove(String name) {
        root = delete(root, name);
    }

    private void inOrder(BSTNode p) {
        if (p == null) {
            return;
        }
        inOrder(p.left);
        System.out.println("  " + p.info);
        inOrder(p.right);
    }

    public void inorder() {
        if (root == null) {
            System.out.println("  (Cay rong)");
        } else {
            inOrder(root);
        }
    }

    private void collect(BSTNode p, ArrayList<Character> list) {
        if (p == null) {
            return;
        }
        collect(p.left, list);
        list.add(p.info);
        collect(p.right, list);
    }

    public Character[] toArray() {
        ArrayList<Character> list = new ArrayList<Character>();
        collect(root, list);
        return list.toArray(new Character[0]);
    }

    public void clear() {
        root = null;
    }
}
