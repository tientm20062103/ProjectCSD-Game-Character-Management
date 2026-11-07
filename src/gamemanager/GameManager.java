package gamemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import model.Action;
import model.Character;
import model.Team;
import storage.FileHandler;

/**
 *
 * @author Minh Tien Tran
 */
public class GameManager {

    private final TeamManager teamManager;
    private final CharacterManager charManager;

    public GameManager(TeamManager teamManager, CharacterManager charManager) {
        this.teamManager = teamManager;
        this.charManager = charManager;
    }
    
    public void createTeam(String name) {
        name = (name == null) ? "" : name.trim();
        if (!teamManager.createTeam(name)) {
            System.out.println("  Ten khong hop le (rong, co '|') hoac team da ton tai!");
            return;
        }
        charManager.performAction(Action.createTeam(name));
        System.out.println("  Da tao team: " + name);
    }

    public void removeTeam(String name) {
        Team t = teamManager.findTeam(name);
        if (t == null) {
            System.out.println("  Khong tim thay team '" + name + "'");
            return;
        }

        int index = teamManager.indexOfTeam(t.getTeamName());
        Character[] members = t.toArray();
        String[] memberNames = new String[members.length];
        
        for (int i = 0; i < members.length; i++) {
            memberNames[i] = members[i].getName();
        }

        teamManager.removeTeam(name);          
        charManager.performAction(Action.removeTeam(t.getTeamName(), index, memberNames));
        System.out.println("  Da xoa team: " + t.getTeamName()
                + " (cac thanh vien chua co team)");
    }

    public void addCharacterInTeam(String teamName, String charName) {
        Character c = charManager.find(charName);
        if (c == null) {
            System.out.println("  Khong tim thay nhan vat '" + charName + "'");
            return;
        }

        if (c.getTeamName() != null) {
            System.out.println("  '" + c.getName() + "' da o team " + c.getTeamName()
                    + ", hay go khoi team do truoc!");
            return;
        }

        Team t = teamManager.findTeam(teamName);

        if (t == null) {
            System.out.println("  Khong tim thay team '" + teamName + "'");
            return;
        }

        if (t.isFull()) {
            System.out.println("  Team '" + t.getTeamName() + "' da du " + Team.MAX_SIZE + " nguoi!");
            return;
        }

        t.addCharacter(c);
        c.setTeamName(t.getTeamName());
        charManager.performAction(Action.addToTeam(c.getName(), t.getTeamName()));
        System.out.println("  Da them " + c.getName() + " vao team " + t.getTeamName());
    }

    public void removeCharacterFromTeam(String charName) {
        Character c = charManager.find(charName);

        if (c == null) {
            System.out.println("  Khong tim thay nhan vat '" + charName + "'");
            return;
        }

        String teamName = c.getTeamName();

        if (teamName == null) {
            System.out.println("  '" + c.getName() + "' chua thuoc team nao!");
            return;
        }

        int index = teamManager.indexOfCharacter(teamName, c.getName());  
        teamManager.removeCharacterFromTeam(teamName, c.getName());
        c.setTeamName(null);
        charManager.performAction(Action.removeFromTeam(c.getName(), teamName, index));
        System.out.println("  Da go " + c.getName() + " khoi team " + teamName);
    }

    public void undoAction() {
        String raw = charManager.popAction();
        if (raw == null) {
            System.out.println("  Khong co hanh dong de hoan tac!");
            return;
        }

        Action a = Action.parse(raw);
        if (a == null) {
            System.out.println("  Bo qua hanh dong khong hop le trong lich su: " + raw);
            return;
        }

        String err = undo(a);
        if (err == null) {
            System.out.println("  " + a.describeUndo());
        } else {
            System.out.println("  Khong the hoan tac '" + a.describe() + "': " + err);
        }
    }

    private String undo(Action a) {
        String type = a.getType();

        if (type.equals(Action.ADD_CHAR)) {
            Character c = charManager.find(a.getArg(0));
            if (c == null) {
                return "khong tim thay nhan vat " + a.getArg(0);
            }
            if (c.getTeamName() != null) {
                return "nhan vat dang o team " + c.getTeamName();
            }
            charManager.removeSilently(c.getName());
            return null;
        }

        if (type.equals(Action.REMOVE_CHAR)) {
            if (!charManager.restore(a.getArg(0), a.getInt(1))) {
                return "nhan vat " + a.getArg(0) + " da ton tai";
            }
            return null;
        }

        if (type.equals(Action.LEVEL)) {
            if (!charManager.setLevelSilently(a.getArg(0), a.getInt(1))) {
                return "khong tim thay nhan vat " + a.getArg(0);
            }
            return null;
        }

        if (type.equals(Action.ADD_TO_TEAM)) {
            Character c = charManager.find(a.getArg(0));
            Team t = teamManager.findTeam(a.getArg(1));
            if (c == null) {
                return "khong tim thay nhan vat " + a.getArg(0);
            }
            if (t == null) {
                return "khong tim thay team " + a.getArg(1);
            }
            if (!t.getTeamName().equalsIgnoreCase(c.getTeamName())) {
                return a.getArg(0) + " khong con o team " + t.getTeamName();
            }
            teamManager.removeCharacterFromTeam(t.getTeamName(), c.getName());
            c.setTeamName(null);
            return null;
        }

        if (type.equals(Action.REMOVE_FROM_TEAM)) {
            Character c = charManager.find(a.getArg(0));
            Team t = teamManager.findTeam(a.getArg(1));
            if (c == null) {
                return "khong tim thay nhan vat " + a.getArg(0);
            }
            if (t == null) {
                return "khong tim thay team " + a.getArg(1);
            }
            if (c.getTeamName() != null) {
                return a.getArg(0) + " dang o team " + c.getTeamName();
            }
            if (!t.addCharacterAt(a.getInt(2), c)) {      
                return "team " + t.getTeamName() + " da day";
            }
            c.setTeamName(t.getTeamName());
            return null;
        }

        if (type.equals(Action.CREATE_TEAM)) {
            Team t = teamManager.findTeam(a.getArg(0));
            if (t == null) {
                return "khong tim thay team " + a.getArg(0);
            }
            if (t.getSize() > 0) {
                return "team dang co " + t.getSize() + " thanh vien";
            }
            teamManager.removeTeam(t.getTeamName());
            return null;
        }

        if (!teamManager.restoreTeam(a.getArg(0), a.getInt(1))) {
            return "team " + a.getArg(0) + " da ton tai";
        }
        Team t = teamManager.findTeam(a.getArg(0));
        for (int i = 2; i < a.getArgCount(); i++) {
            Character c = charManager.find(a.getArg(i));
            if (c == null || c.getTeamName() != null || !t.addCharacter(c)) {
                System.out.println("  Canh bao: khong khoi phuc duoc thanh vien " + a.getArg(i));
            } else {
                c.setTeamName(t.getTeamName());
            }
        }
        return null;
    }

    public void saveToFile(String charPath, String teamPath) {
        try {
            FileHandler.saveCharacters(charPath, charManager.toArray(), charManager.historyToArray());
            FileHandler.saveTeams(teamPath, teamManager.toArray());
            System.out.println("  Da luu nhan vat vao: " + new File(charPath).getAbsolutePath());
            System.out.println("  Da luu team vao    : " + new File(teamPath).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("  Loi ghi file: " + e.getMessage());
        }
    }

    public boolean loadFromFile(String charPath, String teamPath) {
        File cf = new File(charPath);
        File tf = new File(teamPath);

        if (!cf.exists() && !tf.exists()) {
            return false;
        }

        ArrayList<Character> chars = new ArrayList<Character>();
        ArrayList<String> acts = new ArrayList<String>();
        ArrayList<String> teamNames = new ArrayList<String>();
        ArrayList<String[]> members = new ArrayList<String[]>();
        ArrayList<String> warns = new ArrayList<String>();
        try {
            if (cf.exists()) {
                FileHandler.loadCharacters(charPath, chars, acts, warns);
            }

            File teamSource = tf.exists() ? tf : cf;

            if (teamSource.exists()) {
                FileHandler.loadTeams(teamSource.getPath(), teamNames, members);
            }

        } catch (IOException e) {
            System.out.println("  Loi doc file: " + e.getMessage());
            return false;
        }

        teamManager.clear();
        charManager.clear();

        for (Character c : chars) {
            if (!charManager.add(c)) {
                warns.add("Trung ten nhan vat '" + c.getName() + "', bo qua");
            }
        }

        for (String name : teamNames) {
            teamManager.createTeam(name);
        }

        for (String[] m : members) {
            Character c = charManager.find(m[1]);
            Team t = teamManager.findTeam(m[0]);
            if (c == null || t == null) {
                warns.add("Bo qua thanh vien " + m[1] + " cua team " + m[0]);
            } else if (c.getTeamName() != null) {
                warns.add(m[1] + " da o team " + c.getTeamName() + ", bo qua");
            } else if (!t.addCharacter(c)) {
                warns.add("Team '" + m[0] + "' da du, bo qua " + m[1]);
            } else {
                c.setTeamName(t.getTeamName());
            }
        }

        int legacy = 0;
        for (String a : acts) {
            Action act = Action.parse(a);
            if (act == null) {
                legacy++;
            } else {
                charManager.performAction(act);
            }
        }
        if (legacy > 0) {
            warns.add("Bo qua " + legacy + " dong lich su o dinh dang cu (khong the hoan tac)");
        }

        for (String w : warns) {
            System.out.println("  Canh bao: " + w);
        }

        System.out.println("  Da doc " + chars.size() + " nhan vat, " + teamManager.getSize() + " team");
        return true;
    }
}
