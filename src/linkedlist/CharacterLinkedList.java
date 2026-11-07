package linkedlist;

import model.Character;

/**
 *
 * @author Minh Tien Tran
 */
public class CharacterLinkedList {

    private CharacterNode head, tail;
    private int size;

    public CharacterLinkedList() {
        head = tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public void addCharacter(Character c) {
        if (isEmpty()) {
            head = tail = new CharacterNode(c);
        } else {
            CharacterNode q = new CharacterNode(c);
            tail.next = q;
            tail = q;
        }
        size++;
    }

    /** Chen nhan vat vao vi tri index (0 = dau). index >= size thi chen cuoi. */
    public void insertAt(int index, Character c) {
        if (isEmpty() || index >= size) {
            addCharacter(c);
            return;
        }
        if (index <= 0) {
            head = new CharacterNode(c, head);
        } else {
            CharacterNode q = head;
            for (int i = 0; i < index - 1; i++) {
                q = q.next;
            }
            q.next = new CharacterNode(c, q.next);
        }
        size++;
    }

    /** Vi tri cua nhan vat trong danh sach (tu 0), -1 neu khong co. */
    public int indexOf(String name) {
        int i = 0;
        for (CharacterNode p = head; p != null; p = p.next, i++) {
            if (p.info.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("  (Doi hinh trong)");
            return;
        }
        CharacterNode p = head;
        int i = 1;
        while (p != null) {
            System.out.println("  " + i + ". " + p.info);
            p = p.next;
            i++;
        }
    }

    private CharacterNode findNode(String name) {
        CharacterNode p = head;
        while (p != null) {
            if (p.info.getName().equalsIgnoreCase(name)) {
                return p;
            }
            p = p.next;
        }
        return null;
    }

    public Character findCharacter(String name) {
        CharacterNode p = findNode(name);
        return (p == null) ? null : p.info;
    }

    public boolean updateCharacter(String name, int newLevel) {
        CharacterNode p = findNode(name);
        if (p == null) {
            return false;
        }
        p.info.setLevel(newLevel);
        return true;
    }

    public boolean removeCharacter(String name) {
        CharacterNode f = null;
        CharacterNode p = head;
        while (p != null && !p.info.getName().equalsIgnoreCase(name)) {
            f = p;
            p = p.next;
        }
        if (p == null) {
            return false;
        }
        if (f == null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            f.next = p.next;
            if (p == tail) {
                tail = f;
            }
        }
        size--;
        return true;
    }

    public Character[] toArray() {
        Character[] arr = new Character[size];
        CharacterNode p = head;
        int i = 0;
        while (p != null) {
            arr[i++] = p.info;
            p = p.next;
        }
        return arr;
    }

    public int getTotalLevel() {
        int sum = 0;
        for (CharacterNode p = head; p != null; p = p.next) {
            sum += p.info.getLevel();
        }
        return sum;
    }
}
