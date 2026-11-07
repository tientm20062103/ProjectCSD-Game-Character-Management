package gamemanager;

import linkedlist.TeamLinkedList;
import model.Character;
import model.Team;

/**
 *
 * @author Minh Tien Tran
 */
public class TeamManager {

    private final TeamLinkedList teams = new TeamLinkedList();

    public boolean createTeam(String name) {
        if (name == null || name.trim().isEmpty() || name.indexOf('|') >= 0) {
            return false;     
        }
        return teams.addTeam(new Team(name.trim()));
    }

    public Team removeTeam(String name) {
        Team t = teams.removeTeam(name);
        if (t != null) {
            for (Character c : t.toArray()) {
                c.setTeamName(null);
            }
        }
        return t;
    }

    public Team findTeam(String name) {
        return teams.findTeam(name);
    }

    public boolean removeCharacterFromTeam(String teamName, String charName) {
        Team t = teams.findTeam(teamName);
        return t != null && t.removeCharacter(charName);
    }

    public int indexOfTeam(String name) {
        return teams.indexOf(name);
    }

    public int indexOfCharacter(String teamName, String charName) {
        Team t = teams.findTeam(teamName);
        return (t == null) ? -1 : t.indexOfCharacter(charName);
    }

    public boolean restoreTeam(String name, int index) {
        return teams.insertAt(index, new Team(name));
    }

    public void showTeams() {
        System.out.println("  --- Danh sach team (" + teams.getSize() + ") ---");
        teams.display();
    }

    public void showTeamDetail(String teamName) {
        Team t = teams.findTeam(teamName);
        if (t == null) {
            System.out.println("  Khong tim thay team '" + teamName + "'");
            return;
        }
        System.out.println("  --- " + t + " ---");
        t.display();
    }

    public int getSize() {
        return teams.getSize();
    }

    public boolean isEmpty() {
        return teams.isEmpty();
    }

    public Team[] toArray() {
        return teams.toArray();
    }

    public void clear() {
        teams.clear();
    }
}
