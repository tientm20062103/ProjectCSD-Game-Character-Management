package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Character;
import model.Team;

/**
 *
 * @author Minh Tien Tran
 */
public class FileHandler {

    public static void saveCharacters(String path, Character[] chars, String[] actions)
            throws IOException {
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Character c : chars) {
                bw.write("C|" + c.getName() + "|" + c.getLevel());
                bw.newLine();
            }
            for (String a : actions) {
                bw.write("A|" + a);
                bw.newLine();
            }
        }
    }

    public static void loadCharacters(String path, ArrayList<Character> chars,
            ArrayList<String> actions, ArrayList<String> warnings) throws IOException {
        try ( BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() < 3) {
                    continue;
                }
                char type = line.charAt(0);
                String rest = line.substring(2);

                if (type == 'C') {
                    int first = rest.indexOf('|');
                    int last = rest.lastIndexOf('|');
                    if (first <= 0) {
                        continue;
                    }
                    String name = (first == last) ? rest.substring(0, first) : rest.substring(first + 1, last);
                    try {
                        chars.add(new Character(name, Integer.parseInt(rest.substring(last + 1).trim())));
                    } catch (NumberFormatException e) {
                        warnings.add("Bo qua dong loi level: " + line);
                    }
                } else if (type == 'A') {
                    actions.add(rest);
                }
            }
        }
    }

    public static void saveTeams(String path, Team[] teams) throws IOException {
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Team t : teams) {
                bw.write("T|" + t.getTeamName());
                bw.newLine();
            }
            for (Team t : teams) {
                for (Character c : t.toArray()) {
                    bw.write("M|" + t.getTeamName() + "|" + c.getName());
                    bw.newLine();
                }
            }
        }
    }

    public static void loadTeams(String path, ArrayList<String> teamNames,
            ArrayList<String[]> members) throws IOException {
        try ( BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() < 3) {
                    continue;
                }
                char type = line.charAt(0);
                String rest = line.substring(2);

                if (type == 'T') {
                    addTeamName(teamNames, rest.trim());

                } else if (type == 'M') {
                    int idx = rest.indexOf('|');
                    if (idx > 0) {
                        members.add(new String[]{rest.substring(0, idx), rest.substring(idx + 1)});
                    }

                } else if (type == 'C') {        
                    int first = rest.indexOf('|');
                    int last = rest.lastIndexOf('|');
                    if (first > 0 && first != last) {
                        String teamName = rest.substring(0, first);
                        addTeamName(teamNames, teamName);
                        members.add(new String[]{teamName, rest.substring(first + 1, last)});
                    }
                }
            }
        }
    }

    private static void addTeamName(ArrayList<String> teamNames, String name) {
        if (!name.isEmpty() && !teamNames.contains(name)) {
            teamNames.add(name);
        }
    }
}
