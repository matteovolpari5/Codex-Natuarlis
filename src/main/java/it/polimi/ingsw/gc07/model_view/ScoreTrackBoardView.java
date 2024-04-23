package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreTrackBoardView {
    /**
     * Map that link each player with his personal score.
     */
    private final Map<String, Integer> playersScore;
    private final Map<String, TokenColor> playersToken;

    private final List<String> playersNickname;

    /**
     * constant for color bg
     */
    public final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    /**
     * Constructor method for an empty ScoreTrackBoard.
     */
    public ScoreTrackBoardView() {
        playersNickname = new ArrayList<>();
        playersScore = new HashMap<>();
        playersToken = new HashMap<>();
    }

    /**
     * Method that allows to insert a new Player to the ScoreTrackBoard,
     * initializing it's score to 0.
     * @param nickname player to add
     * @param color token color choose by the player
     */
    public void addPlayer(String nickname, TokenColor color) {
        assert(!playersScore.containsKey(nickname)): "The player is already present";
        playersScore.put(nickname, 0);
        playersToken.put(nickname, color);
        playersNickname.add(nickname);
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param nickname player
     * @param newScore new score to set
     */
    public void setNewScore(String nickname, int newScore) {
        assert(playersScore.containsKey(nickname)): "The player is not present";
        playersScore.put(nickname, newScore);
    }

    /**
     * method that print in the console the complete track board
     */
    public void printScoreTrackBoard()
    {
        List<Integer> score = new ArrayList<>();
        List<String> color = new ArrayList<>();
        for (String s : playersNickname) {
            score.add(playersScore.get(s));
            if (playersToken.get(s).equals(TokenColor.GREEN)) {
                color.add(ANSI_GREEN_BACKGROUND);
            } else if (playersToken.get(s).equals(TokenColor.BLUE)) {
                color.add(ANSI_BLUE_BACKGROUND);
            } else if (playersToken.get(s).equals(TokenColor.RED)) {
                color.add(ANSI_RED_BACKGROUND);
            } else if (playersToken.get(s).equals(TokenColor.YELLOW)) {
                color.add(ANSI_YELLOW_BACKGROUND);
            }
        }
        int i = 29;
        for (int x = 0; x < 7; x++) {
            System.out.println(ANSI_BLACK_BACKGROUND + "+---+ +---+ +---+ +---+");
            for (int y = 0; y < 4; y++) {
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        System.out.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        System.out.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        System.out.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        System.out.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND);
                }
                System.out.print("|");
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        System.out.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        System.out.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        System.out.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        System.out.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND);
                }
                System.out.print(" ");
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        System.out.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        System.out.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        System.out.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        System.out.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND);
                }
                System.out.print(i);
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        System.out.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        System.out.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        System.out.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        System.out.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND);
                }
                if (i > 9) {
                    System.out.print("|");
                    i--;
                } else {
                    System.out.print(" |");
                    i--;
                }
                System.out.print(ANSI_BLACK_BACKGROUND + " ");
            }
            System.out.println();
            System.out.println(ANSI_BLACK_BACKGROUND + "+---+ +---+ +---+ +---+");
        }
        System.out.println(ANSI_BLACK_BACKGROUND + "      +---+ +---+      ");
        System.out.print(ANSI_BLACK_BACKGROUND + "      ");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print("|");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print(" ");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print(0);
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print(" |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print("|");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print(" ");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print(1);
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                System.out.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                System.out.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                System.out.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                System.out.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND);
        }
        System.out.print(" |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        System.out.println(ANSI_BLACK_BACKGROUND + "        ");
        System.out.println(ANSI_BLACK_BACKGROUND + "      +---+ +---+      ");
    }
}
