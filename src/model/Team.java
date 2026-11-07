package model;

import linkedlist.CharacterLinkedList;

/**
 *
 * @author Minh Tien Tran
 */
public class Team {

    private String teamName;
    private CharacterLinkedList members;
    public static final int MAX_SIZE = 4;

    public String getTeamName() {
        return teamName;
    }

    public int getSize() {
        return members.getSize();
    }

    public boolean isFull() {
        return members.getSize() >= MAX_SIZE;
    }

    public int getTotalLevel() {
        return members.getTotalLevel();
    }

    public Team(String teamName) {
        this.teamName = teamName;
        this.members = new CharacterLinkedList();
    }

    public boolean addCharacter(Character c) {
        if (isFull()) {
            return false;
        }

        members.addCharacter(c);
        return true;
    }

    public boolean addCharacterAt(int index, Character c) {
        if (isFull()) {
            return false;
        }
        members.insertAt(index, c);
        return true;
    }

    public int indexOfCharacter(String name) {
        return members.indexOf(name);
    }

    public Character findCharacter(String name) {
        return members.findCharacter(name);
    }

    public boolean removeCharacter(String name) {
        return members.removeCharacter(name);
    }

    public Character[] toArray() {
        return members.toArray();
    }

    public void display() {
        members.display();
    }

    @Override
    public String toString() {
        return teamName + " | " + getSize() + "/" + MAX_SIZE
                + " nhan vat | Tong level: " + getTotalLevel();
    }
}
