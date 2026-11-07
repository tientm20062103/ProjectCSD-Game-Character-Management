package model;

import java.util.Arrays;

/**
 *
 * @author Minh Tien Tran
 */
public class Action {

    public static final String ADD_CHAR = "ADD_CHAR";
    public static final String REMOVE_CHAR = "REMOVE_CHAR";
    public static final String LEVEL = "LEVEL";
    public static final String ADD_TO_TEAM = "ADD_TO_TEAM";
    public static final String REMOVE_FROM_TEAM = "REMOVE_FROM_TEAM";
    public static final String CREATE_TEAM = "CREATE_TEAM";
    public static final String REMOVE_TEAM = "REMOVE_TEAM";

    private final String type;
    private final String[] args;

    private Action(String type, String... args) {
        this.type = type;
        this.args = args;
    }

    public static Action addChar(String name, int level) {
        return new Action(ADD_CHAR, name, String.valueOf(level));
    }

    public static Action removeChar(String name, int level) {
        return new Action(REMOVE_CHAR, name, String.valueOf(level));
    }

    public static Action levelChange(String name, int oldLevel, int newLevel) {
        return new Action(LEVEL, name, String.valueOf(oldLevel), String.valueOf(newLevel));
    }

    public static Action addToTeam(String charName, String teamName) {
        return new Action(ADD_TO_TEAM, charName, teamName);
    }

    public static Action removeFromTeam(String charName, String teamName, int index) {
        return new Action(REMOVE_FROM_TEAM, charName, teamName, String.valueOf(index));
    }

    public static Action createTeam(String teamName) {
        return new Action(CREATE_TEAM, teamName);
    }

    public static Action removeTeam(String teamName, int index, String[] members) {
        String[] a = new String[2 + members.length];
        a[0] = teamName;
        a[1] = String.valueOf(index);
        for (int i = 0; i < members.length; i++) {
            a[2 + i] = members[i];
        }
        return new Action(REMOVE_TEAM, a);
    }

    public String getType() {
        return type;
    }

    public String getArg(int i) {
        return args[i];
    }

    public int getInt(int i) {
        return Integer.parseInt(args[i]);
    }

    public int getArgCount() {
        return args.length;
    }

    public String encode() {
        StringBuilder sb = new StringBuilder(type);
        for (String a : args) {
            sb.append('|').append(a);
        }
        return sb.toString();
    }

    public static Action parse(String s) {
        if (s == null) {
            return null;
        }
        String[] p = s.split("\\|", -1);
        if (p.length < 2) {
            return null;
        }
        String type = p[0];
        String[] args = Arrays.copyOfRange(p, 1, p.length);
        for (String x : args) {
            if (x.trim().isEmpty()) {
                return null;
            }
        }
        try {
            if (type.equals(ADD_CHAR) || type.equals(REMOVE_CHAR)) {
                if (args.length != 2 || Integer.parseInt(args[1]) <= 0) {
                    return null;
                }
            } else if (type.equals(LEVEL)) {
                if (args.length != 3 || Integer.parseInt(args[1]) <= 0
                        || Integer.parseInt(args[2]) <= 0) {
                    return null;
                }
            } else if (type.equals(ADD_TO_TEAM)) {
                if (args.length != 2) {
                    return null;
                }
            } else if (type.equals(REMOVE_FROM_TEAM)) {
                if (args.length != 3 || Integer.parseInt(args[2]) < 0) {
                    return null;
                }
            } else if (type.equals(CREATE_TEAM)) {
                if (args.length != 1) {
                    return null;
                }
            } else if (type.equals(REMOVE_TEAM)) {
                if (args.length < 2 || args.length > 2 + Team.MAX_SIZE
                        || Integer.parseInt(args[1]) < 0) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return new Action(type, args);
    }

    public String describe() {
        if (type.equals(ADD_CHAR)) {
            return "Them nhan vat " + args[0] + " (Level " + args[1] + ")";
        }
        if (type.equals(REMOVE_CHAR)) {
            return "Xoa nhan vat " + args[0] + " (Level " + args[1] + ")";
        }
        if (type.equals(LEVEL)) {
            return "Doi level " + args[0] + ": " + args[1] + " -> " + args[2];
        }
        if (type.equals(ADD_TO_TEAM)) {
            return "Them " + args[0] + " vao team " + args[1];
        }
        if (type.equals(REMOVE_FROM_TEAM)) {
            return "Go " + args[0] + " khoi team " + args[1];
        }
        if (type.equals(CREATE_TEAM)) {
            return "Tao team " + args[0];
        }
        return "Xoa team " + args[0] + " (" + (args.length - 2) + " thanh vien)";
    }

    public String describeUndo() {
        if (type.equals(ADD_CHAR)) {
            return "Da xoa lai nhan vat " + args[0] + " (hoan tac viec them)";
        }
        if (type.equals(REMOVE_CHAR)) {
            return "Da khoi phuc nhan vat " + args[0] + " - Level " + args[1] + " (hoan tac viec xoa)";
        }
        if (type.equals(LEVEL)) {
            return "Da tra level cua " + args[0] + " ve " + args[1] + " (hoan tac viec doi level)";
        }
        if (type.equals(ADD_TO_TEAM)) {
            return "Da go " + args[0] + " khoi team " + args[1] + " (hoan tac viec them vao team)";
        }
        if (type.equals(REMOVE_FROM_TEAM)) {
            return "Da dua " + args[0] + " tro lai team " + args[1] + " (hoan tac viec go)";
        }
        if (type.equals(CREATE_TEAM)) {
            return "Da xoa team " + args[0] + " (hoan tac viec tao team)";
        }
        return "Da khoi phuc team " + args[0] + " cung " + (args.length - 2)
                + " thanh vien (hoan tac viec xoa team)";
    }
}
