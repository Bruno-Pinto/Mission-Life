package user_interface;

import core.GameMode;
import patterns.Pattern;
import patterns.PatternType;

import java.util.List;

public interface UserInterface {

    Pattern getChosenPattern(List<Pattern> patterns);

    PatternType getChosenPatternType();

    GameMode getGameMode();
}
