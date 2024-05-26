package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.conditions.CornerCoverageCondition;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.conditions.LayoutCondition;
import it.polimi.ingsw.gc07.enumerations.GameObject;
import it.polimi.ingsw.gc07.enumerations.GameResource;

import java.util.List;

public interface PlayerTui {
    String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    String ANSI_RED_BACKGROUND = "\u001B[41m";
    String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    /**
     * Method used to print the starter card.
     * @param card    starter card
     * @param cardway starter card way
     */
    static void printStarterCard(PlaceableCard card, boolean cardway) {
        // first row //
        System.out.print(ANSI_BLACK_BACKGROUND + "+---------+");
        System.out.println();
        // second row //
        if (cardway) {
            if (card.getBackCorners()[0]) {
                if (card.getBackCornersContent()[0] == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "       ");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|        ");
            }
            if (card.getBackCorners()[1]) {
                if (card.getBackCornersContent()[1] == null) {
                    System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + " |");
            }
        } else {
            if (card.getFrontCorners()[0]) {
                if (card.getFrontCornersContent()[0] == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (card.getFrontCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            System.out.print(ANSI_BLACK_BACKGROUND + "     ");
            if (card.getFrontCorners()[1]) {
                if (card.getFrontCornersContent()[1] == null) {
                    System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        // third row //
        System.out.println();
        if (cardway) {
            List<GameResource> permanentResources = card.getPermanentResources();
            if (permanentResources == null) {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|   ");
                for (GameResource permanentResource : permanentResources) {
                    if (permanentResource.equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F");
                    } else if (permanentResource.equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A");
                    } else if (permanentResource.equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P");
                    } else if (permanentResource.equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I");
                    }
                }
                for (int i = permanentResources.size(); i < 3; i++) {
                    System.out.print(ANSI_BLACK_BACKGROUND + " ");
                }
                System.out.print(ANSI_BLACK_BACKGROUND + "   |");
            }
        } else {
            System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        System.out.println();
        // fouth row //
        if (cardway) {
            if (card.getBackCorners()[3]) {
                if (card.getBackCornersContent()[3] == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_WHITE_BACKGROUND + "|" + ANSI_RED_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (card.getBackCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "       ");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|        ");
            }
            if (card.getBackCorners()[2]) {
                if (card.getBackCornersContent()[2] == null) {
                    System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getBackCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + " |");
            }
        } else {
            if (card.getFrontCorners()[3]) {
                if (card.getFrontCornersContent()[3] == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getFrontCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + " ");
                }
            }
            System.out.print(ANSI_BLACK_BACKGROUND + "      ");
            if (card.getFrontCorners()[2]) {
                if (card.getFrontCornersContent()[2] == null) {
                    System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameResource.FUNGI)) {
                    System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameResource.ANIMAL)) {
                    System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameResource.PLANT)) {
                    System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameResource.INSECT)) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameObject.QUILL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameObject.INKWELL)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (card.getFrontCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        // last row //
        System.out.println();
        System.out.print(ANSI_BLACK_BACKGROUND + "+---------+");
        System.out.println();
    }

    /**
     * Method used to print objective card.
     * @param objectiveCard objective card.
     */
    static void printOnlyObjectiveCard(ObjectiveCard objectiveCard)
    {
        // first row //
        System.out.println(ANSI_YELLOW_BACKGROUND + "+---------+");
        // second row //
        if (objectiveCard == null) {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        } else {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|     ");
            if (objectiveCard.getScoringCondition() instanceof LayoutCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + " ");
                    }
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "   ");
            }
        }
        System.out.println(ANSI_YELLOW_BACKGROUND + " |");
        // third row //
        if (objectiveCard == null) {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        } else {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|  " + ANSI_BLACK_BACKGROUND + objectiveCard.getPoints() + ANSI_YELLOW_BACKGROUND + "  ");
            if (objectiveCard.getScoringCondition() instanceof ItemsCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().size() > i) {
                        if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.MANUSCRIPT)) {
                            System.out.print(ANSI_YELLOW_BACKGROUND + "M");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.QUILL)) {
                            System.out.print(ANSI_YELLOW_BACKGROUND + "Q");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.INKWELL)) {
                            System.out.print(ANSI_YELLOW_BACKGROUND + "I");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                            System.out.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                            System.out.print(ANSI_RED_BACKGROUND + "F");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                            System.out.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                            System.out.print(ANSI_PURPLE_BACKGROUND + "I");
                        } else {
                            System.out.print(ANSI_BLACK_BACKGROUND + " ");
                        }
                    } else {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                }
            } else if (objectiveCard.getScoringCondition() instanceof CornerCoverageCondition) {
                System.out.print(ANSI_YELLOW_BACKGROUND + " C ");
            } else if (objectiveCard.getScoringCondition() instanceof LayoutCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + " ");
                    }
                }
            }
        }
        System.out.println(ANSI_YELLOW_BACKGROUND + " |");
        // fourth row //
        if (objectiveCard == null) {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        } else {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|     ");
            if (objectiveCard.getScoringCondition() instanceof LayoutCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + " ");
                    }
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "   ");
            }
        }
        System.out.println(ANSI_YELLOW_BACKGROUND + " |");
        // fifth (and last row) //
        System.out.println(ANSI_YELLOW_BACKGROUND + "+---------+");

    }

    /**
     * Method used to print the hand of a player.
     * @param hand list of card that the player has.
     * @param personalObjective personal objective card.
     * @param cardway choice the face that we want to print.
     */
    static void printPlayerHand(List<DrawableCard> hand, ObjectiveCard personalObjective, boolean cardway) {
        if (!cardway) {
            printFirstLastRow();
            printSecondRow(hand, personalObjective);
            printThirdRow(personalObjective);
            printFourthRow(hand, personalObjective);
            printFirstLastRow();
        } else {
            printFirstLastRow();
            printSecondRowBack(hand);
            printThirdRowBack(hand);
            printFourthRowBack(hand);
            printFirstLastRow();
        }
    }

    /**
     * Method used to print the first and last row of the player hand.
     */
    private static void printFirstLastRow() {
        System.out.println(ANSI_YELLOW_BACKGROUND + "+---------+" + ANSI_BLACK_BACKGROUND + " +---------+ +---------+ +---------+");
    }

    /**
     * Method used to print the second row of the player front hand.
     * @param hand list of card that the player has.
     * @param objectiveCard personal objective card.
     */
    private static void printSecondRow(List<DrawableCard> hand, ObjectiveCard objectiveCard) {
        if (objectiveCard == null) {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        } else {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|     ");
            if (objectiveCard.getScoringCondition() instanceof LayoutCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + " ");
                    }
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "   ");
            }
        }
        System.out.print(ANSI_YELLOW_BACKGROUND + " |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        //print hand//
        DrawableCard card;
        for (int i = 0; i < 3; i++) {
            if (i < hand.size())
                card = hand.get(i);
            else
                card = null;
            if (card != null) {
                if (card.getFrontCorners()[0]) {
                    if (card.getFrontCornersContent()[0] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
                }
                if (card.getPoints() > 0)
                    System.out.print(ANSI_BLACK_BACKGROUND + card.getPoints());
                else
                    System.out.print(ANSI_BLACK_BACKGROUND + " ");
                if (card.getScoringCondition() == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + " ");
                } else if (card.getScoringCondition() instanceof CornerCoverageCondition) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "C");
                } else if (card.getScoringCondition() instanceof ItemsCondition) {
                    if (((ItemsCondition) card.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q");
                    } else if (((ItemsCondition) card.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I");
                    } else if (((ItemsCondition) card.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "  ");
                }
                System.out.print(ANSI_BLACK_BACKGROUND + "   ");
                if (card.getFrontCorners()[1]) {
                    if (card.getFrontCornersContent()[1] == null) {
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " |");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND + " ");
        }
        System.out.println();
    }

    /**
     * Method used to print the third row of the player front hand.
     * @param objectiveCard personal objective card.
     */
    private static void printThirdRow(ObjectiveCard objectiveCard) {
        if (objectiveCard == null) {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        } else {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|  " + ANSI_BLACK_BACKGROUND + objectiveCard.getPoints() + ANSI_YELLOW_BACKGROUND + "  ");
            if (objectiveCard.getScoringCondition() instanceof ItemsCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().size() > i) {
                        if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.MANUSCRIPT)) {
                            System.out.print(ANSI_YELLOW_BACKGROUND + "M");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.QUILL)) {
                            System.out.print(ANSI_YELLOW_BACKGROUND + "Q");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.INKWELL)) {
                            System.out.print(ANSI_YELLOW_BACKGROUND + "I");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                            System.out.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                            System.out.print(ANSI_RED_BACKGROUND + "F");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                            System.out.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                            System.out.print(ANSI_PURPLE_BACKGROUND + "I");
                        } else {
                            System.out.print(ANSI_BLACK_BACKGROUND + " ");
                        }
                    } else {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                }
            } else if (objectiveCard.getScoringCondition() instanceof CornerCoverageCondition) {
                System.out.print(ANSI_YELLOW_BACKGROUND + " C ");
            } else if (objectiveCard.getScoringCondition() instanceof LayoutCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + " ");
                    }
                }
            }
        }
        System.out.print(ANSI_YELLOW_BACKGROUND + " |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        System.out.print(ANSI_BLACK_BACKGROUND + "|         | ");
        System.out.print(ANSI_BLACK_BACKGROUND + "|         | ");
        System.out.println(ANSI_BLACK_BACKGROUND + "|         | ");
    }

    /**
     * Method used to print the fourth row of the player front hand.
     * @param hand list of card that the player has.
     * @param objectiveCard personal objective card.
     */
    private static void printFourthRow(List<DrawableCard> hand, ObjectiveCard objectiveCard) {
        if (objectiveCard == null) {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        } else {
            System.out.print(ANSI_YELLOW_BACKGROUND + "|     ");
            if (objectiveCard.getScoringCondition() instanceof LayoutCondition) {
                for (int i = 0; i < 3; i++) {
                    if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + " ");
                    } else if (((LayoutCondition) objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + " ");
                    }
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "   ");
            }
        }
        System.out.print(ANSI_YELLOW_BACKGROUND + " |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        DrawableCard card;
        for (int i = 0; i < 3; i++) {
            if (i < hand.size())
                card = hand.get(i);
            else
                card = null;
            if (card != null) {
                if (card.getFrontCorners()[3]) {
                    if (card.getFrontCornersContent()[3] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getFrontCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "  ");
                }
                if (card.getPlacementCondition() == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "    ");
                } else if (card.getPlacementCondition() instanceof ItemsCondition) {
                    for (int j = 0; j < 5; j++) {
                        if (j < ((ItemsCondition) card.getPlacementCondition()).getNeededItems().size()) {
                            if (((ItemsCondition) card.getPlacementCondition()).getNeededItems().get(j).equals(GameResource.PLANT)) {
                                System.out.print(ANSI_GREEN_BACKGROUND + "P");
                            } else if (((ItemsCondition) card.getPlacementCondition()).getNeededItems().get(j).equals(GameResource.ANIMAL)) {
                                System.out.print(ANSI_BLUE_BACKGROUND + "A");
                            } else if (((ItemsCondition) card.getPlacementCondition()).getNeededItems().get(j).equals(GameResource.INSECT)) {
                                System.out.print(ANSI_PURPLE_BACKGROUND + "I");
                            } else if (((ItemsCondition) card.getPlacementCondition()).getNeededItems().get(j).equals(GameResource.FUNGI)) {
                                System.out.print(ANSI_RED_BACKGROUND + "F");
                            }
                        } else {
                            System.out.print(ANSI_BLACK_BACKGROUND + " ");
                        }
                    }
                }
                System.out.print(ANSI_BLACK_BACKGROUND + " ");
                if (card.getFrontCorners()[2]) {
                    if (card.getFrontCornersContent()[2] == null) {
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getFrontCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " |");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND + " ");
        }
        System.out.println();
    }

    /**
     * Method used to print the second row of the player back hand.
     * @param hand list of card that the player has.
     */
    private static void printSecondRowBack(List<DrawableCard> hand) {
        System.out.print(ANSI_YELLOW_BACKGROUND + "|         |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        //print hand//
        DrawableCard card;
        for (int i = 0; i < 3; i++) {
            if (i < hand.size())
                card = hand.get(i);
            else
                card = null;
            if (card != null) {
                if (card.getBackCorners()[0]) {
                    if (card.getBackCornersContent()[0] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
                }
                System.out.print(ANSI_BLACK_BACKGROUND + "     ");
                if (card.getBackCorners()[1]) {
                    if (card.getBackCornersContent()[1] == null) {
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " |");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND + " ");
        }
        System.out.println();
    }

    /**
     * Method used to print the third row of the player back hand.
     * @param hand list of card that the player has.
     */
    private static void printThirdRowBack(List<DrawableCard> hand) {
        System.out.print(ANSI_YELLOW_BACKGROUND + "|        ");
        System.out.print(ANSI_YELLOW_BACKGROUND + " |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        DrawableCard card;
        for (int i = 0; i < 3; i++) {
            if (i < hand.size())
                card = hand.get(i);
            else
                card = null;
            if (card != null) {
                List<GameResource> permanentResources = card.getPermanentResources();
                if (permanentResources == null) {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|   ");
                    for (GameResource permanentResource : permanentResources) {
                        if (permanentResource.equals(GameResource.FUNGI)) {
                            System.out.print(ANSI_RED_BACKGROUND + "F");
                        } else if (permanentResource.equals(GameResource.ANIMAL)) {
                            System.out.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (permanentResource.equals(GameResource.PLANT)) {
                            System.out.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (permanentResource.equals(GameResource.INSECT)) {
                            System.out.print(ANSI_PURPLE_BACKGROUND + "I");
                        }
                    }
                    for (int k = permanentResources.size(); k < 3; k++) {
                        System.out.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                    System.out.print(ANSI_BLACK_BACKGROUND + "   |");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND + " ");
        }
        System.out.println();
    }

    /**
     * Method used to print the fourth row of the player back hand.
     * @param hand list of card that the player has.
     */
    private static void printFourthRowBack(List<DrawableCard> hand){
        System.out.print(ANSI_YELLOW_BACKGROUND + "|         |");
        System.out.print(ANSI_BLACK_BACKGROUND + " ");
        DrawableCard card;
        for (int i = 0; i < 3; i++) {
            if (i < hand.size())
                card = hand.get(i);
            else
                card = null;
            if (card != null) {
                if (card.getBackCorners()[3]) {
                    if (card.getBackCornersContent()[3] == null) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                    } else if (card.getBackCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "  ");
                }
                System.out.print(ANSI_BLACK_BACKGROUND + "    ");
                System.out.print(ANSI_BLACK_BACKGROUND + " ");
                if (card.getBackCorners()[2]) {
                    if (card.getBackCornersContent()[2] == null) {
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameResource.FUNGI)) {
                        System.out.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameResource.ANIMAL)) {
                        System.out.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameResource.PLANT)) {
                        System.out.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameResource.INSECT)) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameObject.QUILL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameObject.INKWELL)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                    } else if (card.getBackCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                        System.out.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                    }
                } else {
                    System.out.print(ANSI_BLACK_BACKGROUND + " |");
                }
            } else {
                System.out.print(ANSI_BLACK_BACKGROUND + "|         |");
            }
            System.out.print(ANSI_BLACK_BACKGROUND + " ");
        }
        System.out.println();
    }
}
