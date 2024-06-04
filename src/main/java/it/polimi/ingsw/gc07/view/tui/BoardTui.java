package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.TokenColor;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BoardTui {
    /**
     * Method used to print in the console the complete track board.
     */
    static void printScoreTrackBoard(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        String ANSI_BLACK_BACKGROUND = "\u001B[40m";
        String ANSI_RED_BACKGROUND = "\u001B[41m";
        String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
        String ANSI_BLUE_BACKGROUND = "\u001B[44m";

        List<Integer> score = new ArrayList<>();
        List<String> color = new ArrayList<>();
        for (String s : playerScores.keySet()) {
            score.add(playerScores.get(s));
            if (playerTokenColors.get(s).equals(TokenColor.GREEN)) {
                color.add(ANSI_GREEN_BACKGROUND);
            } else if (playerTokenColors.get(s).equals(TokenColor.BLUE)) {
                color.add(ANSI_BLUE_BACKGROUND);
            } else if (playerTokenColors.get(s).equals(TokenColor.RED)) {
                color.add(ANSI_RED_BACKGROUND);
            } else if (playerTokenColors.get(s).equals(TokenColor.YELLOW)) {
                color.add(ANSI_YELLOW_BACKGROUND);
            }
        }
        int i = 29;
        for (int x = 0; x < 7; x++) {
            SafePrinter.println(ANSI_BLACK_BACKGROUND + "+---+ +---+ +---+ +---+");
            for (int y = 0; y < 4; y++) {
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        SafePrinter.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        SafePrinter.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        SafePrinter.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        SafePrinter.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND);
                }
                SafePrinter.print("|");
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        SafePrinter.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        SafePrinter.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        SafePrinter.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        SafePrinter.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND);
                }
                SafePrinter.print(" ");
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        SafePrinter.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        SafePrinter.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        SafePrinter.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        SafePrinter.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND);
                }
                SafePrinter.print(Integer.toString(i));
                if (score.contains(i)) {
                    if (i == score.get(0)) {
                        SafePrinter.print(color.get(0));
                        score.set(0,-1);
                    } else if (i == score.get(1)) {
                        SafePrinter.print(color.get(1));
                        score.set(1,-1);
                    } else if (i == score.get(2)) {
                        SafePrinter.print(color.get(2));
                        score.set(2,-1);
                    } else if (i == score.get(3)) {
                        SafePrinter.print(color.get(3));
                        score.set(3,-1);
                    }
                } else {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND);
                }
                if (i > 9) {
                    SafePrinter.print("|");
                    i--;
                } else {
                    SafePrinter.print(" |");
                    i--;
                }
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
            }
            SafePrinter.println("");
            SafePrinter.println(ANSI_BLACK_BACKGROUND + "+---+ +---+ +---+ +---+");
        }
        SafePrinter.println(ANSI_BLACK_BACKGROUND + "      +---+ +---+      ");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "      ");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print("|");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print(" ");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print("0");
        if (score.contains(0)) {
            if (0 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (0 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (0 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (0 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print(" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print("|");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print(" ");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print("1");
        if (score.contains(1)) {
            if (1 == score.get(0)) {
                SafePrinter.print(color.get(0));
                score.set(0,-1);
            } else if (1 == score.get(1)) {
                SafePrinter.print(color.get(1));
                score.set(1,-1);
            } else if (1 == score.get(2)) {
                SafePrinter.print(color.get(2));
                score.set(2,-1);
            } else if (1 == score.get(3)) {
                SafePrinter.print(color.get(3));
                score.set(3,-1);
            }
        } else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND);
        }
        SafePrinter.print(" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
        SafePrinter.println(ANSI_BLACK_BACKGROUND + "        ");
        SafePrinter.println(ANSI_BLACK_BACKGROUND + "      +---+ +---+      ");
    }
}

