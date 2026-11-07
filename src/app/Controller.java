package app;

import gamemanager.CharacterManager;
import gamemanager.GameManager;
import gamemanager.TeamManager;
import java.util.Scanner;

/**
 *
 * @author Minh Tien Tran
 */
public class Controller {

    static final String CHARACTER_FILE = "characters.txt";
    static final String TEAM_FILE = "teams.txt";

    private final Scanner sc = new Scanner(System.in);
    private final TeamManager tm = new TeamManager();
    private final CharacterManager cm = new CharacterManager();
    private final GameManager gm = new GameManager(tm, cm);

    public void run() {
        gm.loadFromFile(CHARACTER_FILE, TEAM_FILE);

        int choice;
        do {
            mainMenu();
            choice = readChoice();
            switch (choice) {
                case 1:
                    characterMenu();
                    break;
                case 2:
                    teamMenu();
                    break;
                case 3:
                    cm.showHistory();
                    break;
                case 4:
                    gm.undoAction();
                    break;
                case 5:
                    gm.saveToFile(CHARACTER_FILE, TEAM_FILE);
                    break;
                case 6:
                    if (!gm.loadFromFile(CHARACTER_FILE, TEAM_FILE)) {
                        System.out.println("  Khong doc duoc file '" + CHARACTER_FILE + "' / '" + TEAM_FILE + "'");
                    }
                    break;
                case 0:
                    gm.saveToFile(CHARACTER_FILE, TEAM_FILE);
                    System.out.println("Tam biet!");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);

        sc.close();
    }

    private void mainMenu() {
        System.out.println("\n===== GAME CHARACTER MANAGEMENT =====");
        System.out.println("1. Quan ly nhan vat");
        System.out.println("2. Quan ly team");
        System.out.println("3. Xem lich su hanh dong");
        System.out.println("4. Hoan tac hanh dong gan nhat");
        System.out.println("5. Luu du lieu ra file");
        System.out.println("6. Doc du lieu tu file");
        System.out.println("0. Thoat");
        System.out.print("Chon: ");
    }

    private void characterMenu() {
        int choice;
        do {
            System.out.println("\n----- QUAN LY NHAN VAT -----");
            System.out.println("1. Them nhan vat");
            System.out.println("2. Xoa nhan vat");
            System.out.println("3. Cap nhat level");
            System.out.println("4. Tim nhan vat theo ten");
            System.out.println("5. Xem danh sach nhan vat theo ten A-Z");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = readChoice();

            switch (choice) {
                case 1: {
                    String name = readLine("  Ten: ");
                    Integer lv = readInt("  Level: ");
                    if (lv != null) {
                        cm.addCharacter(name, lv);
                    }
                    break;
                }
                case 2:
                    cm.removeCharacter(readLine("  Ten can xoa: "));
                    break;
                case 3: {
                    String name = readLine("  Ten nhan vat: ");
                    Integer lv = readInt("  Level moi: ");
                    if (lv != null) {
                        cm.levelUp(name, lv);
                    }
                    break;
                }
                case 4:
                    cm.searchCharacter(readLine("  Ten can tim: "));
                    break;
                case 5:
                    cm.showSorted();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    private void teamMenu() {
        int choice;
        do {
            System.out.println("\n----- QUAN LY TEAM -----");
            System.out.println("1. Tao team moi");
            System.out.println("2. Xoa team");
            System.out.println("3. Xem cac team (so luong + tong level)");
            System.out.println("4. Xem chi tiet 1 team");
            System.out.println("5. Them nhan vat vao team");
            System.out.println("6. Go nhan vat khoi team");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = readChoice();

            switch (choice) {
                case 1:
                    gm.createTeam(readLine("  Ten team moi: "));
                    break;
                case 2:
                    gm.removeTeam(readLine("  Ten team can xoa: "));
                    break;
                case 3:
                    tm.showTeams();
                    break;
                case 4:
                    tm.showTeamDetail(readLine("  Ten team: "));
                    break;
                case 5: {
                    tm.showTeams();
                    String teamName = readLine("  Ten team: ");
                    String charName = readLine("  Ten nhan vat: ");
                    gm.addCharacterInTeam(teamName, charName);
                    break;
                }
                case 6:
                    gm.removeCharacterFromTeam(readLine("  Ten nhan vat can go khoi team: "));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    private int readChoice() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private Integer readInt(String prompt) {
        try {
            return Integer.parseInt(readLine(prompt));
        } catch (NumberFormatException e) {
            System.out.println("  Level phai la so nguyen!");
            return null;
        }
    }
}
