package gamemanager;

import bst.CharacterBST;
import model.Action;
import model.Character;
import stack.Stack;

/**
 *
 * @author Minh Tien Tran
 */
public class CharacterManager {

    private final CharacterBST bst = new CharacterBST();
    private final Stack history = new Stack();

    private boolean validName(String s) {
        return s != null && !s.trim().isEmpty() && s.indexOf('|') < 0;
    }

    public void addCharacter(String name, int level) {
        if (!validName(name) || level <= 0) {
            System.out.println("  Ten hoac level khong hop le!");
            return;
        }

        name = name.trim();

        if (bst.search(name) != null) {
            System.out.println("  Nhan vat da ton tai!");
            return;
        }

        Character c = new Character(name, level);

        if (bst.insert(c)) {
            performAction(Action.addChar(name, level));
            System.out.println("  Da them: " + c);
        }
    }

    public void removeCharacter(String name) {
        Character c = bst.search(name);
        if (c == null) {
            System.out.println("  Khong tim thay nhan vat '" + name + "'");
            return;
        }

        if (c.getTeamName() != null) {
            System.out.println("  '" + c.getName() + "' dang o team " + c.getTeamName()
                    + ", hay go khoi team truoc!");
            return;
        }

        bst.remove(name);
        performAction(Action.removeChar(c.getName(), c.getLevel()));
        System.out.println("  Da xoa: " + c.getName());
    }

    public void levelUp(String name, int newLevel) {
        Character c = bst.search(name);

        if (newLevel <= 0) {
            System.out.println("  Level phai lon hon 0!");
            return;
        }

        if (c == null) {
            System.out.println("  Khong tim thay nhan vat '" + name + "'");
            return;
        }

        int old = c.getLevel();
        if (old == newLevel) {
            System.out.println("  Level khong thay doi (van la " + old + ")");
            return;
        }

        c.setLevel(newLevel);
        performAction(Action.levelChange(c.getName(), old, newLevel));
        System.out.println("  Da cap nhat: " + c);
    }

    public void searchCharacter(String name) {
        Character c = bst.search(name);

        if (c == null) {
            System.out.println("  Khong tim thay '" + name + "'");
            return;
        }

        System.out.println("  Tim thay: " + c + (c.getTeamName() == null
                ? " (chua co team)" : " (team " + c.getTeamName() + ")"));
    }

    public void showSorted() {
        System.out.println("  --- Danh sach theo ten A-Z ---");
        bst.inorder();
    }

    public Character find(String name) {
        return bst.search(name);
    }

    public boolean add(Character c) {
        return bst.insert(c);
    }

    public Character[] toArray() {
        return bst.toArray();
    }

    public void performAction(Action a) {
        history.push(a.encode());               
    }

    public String popAction() {
        return history.pop();                    
    }

    public void showHistory() {
        System.out.println("  --- Lich su hanh dong  ---");
        String[] arr = history.toArray();        
        if (arr.length == 0) {
            System.out.println("  (Chua co hanh dong nao)");
            return;
        }
        for (int i = arr.length - 1; i >= 0; i--) {   
            Action a = Action.parse(arr[i]);
            String text = (a == null) ? arr[i] : a.describe();
            System.out.println("  " + (i == arr.length - 1 ? "-> " : "   ") + text);
        }
    }

    public String[] historyToArray() {
        return history.toArray();
    }

   
    public boolean restore(String name, int level) {
        return bst.insert(new Character(name, level));
    }

    public boolean removeSilently(String name) {
        if (bst.search(name) == null) {
            return false;
        }
        bst.remove(name);
        return true;
    }

    public boolean setLevelSilently(String name, int level) {
        Character c = bst.search(name);
        if (c == null) {
            return false;
        }
        c.setLevel(level);
        return true;
    }

    public void clear() {
        bst.clear();
        history.clear();
    }
}
