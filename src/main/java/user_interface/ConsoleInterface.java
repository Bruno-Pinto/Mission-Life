package user_interface;

import core.GameMode;
import patterns.Pattern;
import patterns.PatternType;

import java.util.List;
import java.util.Scanner;

public class ConsoleInterface implements UserInterface {

    @Override
    public Pattern getChosenPattern(List<Pattern> patterns) {
        System.out.println("Choose a pattern:");
        for (Pattern pattern : patterns) {
            System.out.println("["+patterns.indexOf(pattern)+"] = "+pattern.getName());
        }
        Pattern pattern = null;
        try (Scanner scanner = new Scanner(System.in)) {
            while (pattern == null) {
                int choice = scanner.nextInt();
                if (choice >= 0 && choice < patterns.size()) {
                    pattern = patterns.get(choice);
                } else {
                    System.out.println("Invalid choice, please try again");
                }
            }
        }
        return pattern;
    }

    @Override
    public PatternType getChosenPatternType() {
        System.out.println("Choose a pattern type:");
        for (PatternType patternType : PatternType.values()) {
            System.out.println("["+patternType.ordinal()+"] = "+patternType.getName());
        }
        PatternType patternType = null;
        try (Scanner scanner = new Scanner(System.in)) {
            while (patternType == null) {
                int choice = scanner.nextInt();
                if (choice >= 0 && choice < PatternType.values().length) {
                    patternType = PatternType.values()[choice];
                } else {
                    System.out.println("Invalid choice, please try again");
                }
            }
        }
        return patternType;
    }

    @Override
    public GameMode getGameMode() {
        System.out.println("Choose a game mode:");
        for (GameMode gameMode : GameMode.values()) {
            System.out.println("["+gameMode.getId()+"] = "+gameMode.getName());
        }
        GameMode gameMode = null;
        try (Scanner scanner = new Scanner(System.in)) {
            while (gameMode == null) {
                int choice = scanner.nextInt();
                if (choice >= 0 && choice < GameMode.values().length) {
                    gameMode = GameMode.values()[choice];
                } else {
                    System.out.println("Invalid choice, please try again");
                }
            }
        }
        return gameMode;
    }
}
