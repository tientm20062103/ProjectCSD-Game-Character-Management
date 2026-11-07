package model;

/**
 *
 * @author Minh Tien Tran
 */
public class Character {

    private String name;
    private int level;
    private String teamName;

    public Character(String name, int level) {
        this.name = name;
        this.level = level;
        this.teamName = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "[" + name + " - Level " + level + "]";
    }
}
