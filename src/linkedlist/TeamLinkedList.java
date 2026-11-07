package linkedlist;

import model.Team;

/**
 *
 * @author Minh Tien Tran
 */
public class TeamLinkedList {

    private TeamNode head, tail;
    private int size;

    public TeamLinkedList() {
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

    public boolean addTeam(Team t) {
        if (findTeam(t.getTeamName()) != null) {
            return false;              
        }
        TeamNode q = new TeamNode(t);
        if (isEmpty()) {
            head = tail = q;
        } else {
            tail.next = q;
            tail = q;
        }
        size++;
        return true;
    }

    /** Chen team vao vi tri index (0 = dau). index >= size thi chen cuoi. */
    public boolean insertAt(int index, Team t) {
        if (findTeam(t.getTeamName()) != null) {
            return false;
        }
        if (isEmpty() || index >= size) {
            return addTeam(t);
        }
        TeamNode q = new TeamNode(t);
        if (index <= 0) {
            q.next = head;
            head = q;
        } else {
            TeamNode f = head;
            for (int i = 0; i < index - 1; i++) {
                f = f.next;
            }
            q.next = f.next;
            f.next = q;
        }
        size++;
        return true;
    }

    /** Vi tri cua team trong danh sach (tu 0), -1 neu khong co. */
    public int indexOf(String name) {
        int i = 0;
        for (TeamNode p = head; p != null; p = p.next, i++) {
            if (p.info.getTeamName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public Team findTeam(String name) {
        for (TeamNode p = head; p != null; p = p.next) {
            if (p.info.getTeamName().equalsIgnoreCase(name)) {
                return p.info;
            }
        }
        return null;
    }

    public Team findTeamOfCharacter(String charName) {
        for (TeamNode p = head; p != null; p = p.next) {
            if (p.info.findCharacter(charName) != null) {
                return p.info;
            }
        }
        return null;
    }

    public Team removeTeam(String name) {
        TeamNode f = null, p = head;
        while (p != null && !p.info.getTeamName().equalsIgnoreCase(name)) {
            f = p;
            p = p.next;
        }
        if (p == null) {
            return null;
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
        return p.info;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("  (Chua co team nao)");
            return;
        }
        int i = 1;
        for (TeamNode p = head; p != null; p = p.next, i++) {
            System.out.println("  " + i + ". " + p.info);
        }
    }

    public Team[] toArray() {
        Team[] arr = new Team[size];
        int i = 0;
        for (TeamNode p = head; p != null; p = p.next) {
            arr[i++] = p.info;
        }
        return arr;
    }
}
