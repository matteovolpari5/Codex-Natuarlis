package it.polimi.ingsw.gc07.view.tui;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.conditions.CornerCoverageCondition;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.conditions.LayoutCondition;
import it.polimi.ingsw.gc07.model.GameObject;
import it.polimi.ingsw.gc07.model.GameResource;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.util.List;

public interface DeckTui {
    String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    String ANSI_RED_BACKGROUND = "\u001B[41m";
    String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    /**
     * Method used to print the front deck view.
     * @param commonObjective common objective
     * @param faceUpGoldCard face up gold cards
     * @param faceUpResourceCard face up resource card
     * @param topGoldDeck top gold deck
     * @param topResourceDeck top resource deck
     */
    static void printDeck(List<ObjectiveCard> commonObjective, List<GoldCard> faceUpGoldCard, List<DrawableCard> faceUpResourceCard, GoldCard topGoldDeck, DrawableCard topResourceDeck) {
        ObjectiveCard commonObjective1;
        ObjectiveCard commonObjective2;
        if(commonObjective == null || commonObjective.size() == 0) {
            commonObjective1 = null;
            commonObjective2 = null;
        } else if (commonObjective.size() == 1) {
            commonObjective1 = commonObjective.get(0);
            commonObjective2 = null;
        }else {
            commonObjective1 = commonObjective.get(0);
            commonObjective2 = commonObjective.get(1);
        }
        GoldCard goldCard1;
        GoldCard goldCard2;
        if(faceUpGoldCard == null || faceUpGoldCard.size() == 0) {
            goldCard1 = null;
            goldCard2 = null;
        } else if (faceUpGoldCard.size() == 1) {
            goldCard1 = faceUpGoldCard.get(0);
            goldCard2 = null;
        }else {
            goldCard1 = faceUpGoldCard.get(0);
            goldCard2 = faceUpGoldCard.get(1);
        }
        DrawableCard resourceCard1;
        DrawableCard resourceCard2;
        if(faceUpResourceCard == null || faceUpResourceCard.size() == 0) {
            resourceCard1 = null;
            resourceCard2 = null;
        } else if (faceUpResourceCard.size() == 1) {
            resourceCard1 = faceUpResourceCard.get(0);
            resourceCard2 = null;
        }else {
            resourceCard1 = faceUpResourceCard.get(0);
            resourceCard2 = faceUpResourceCard.get(1);
        }
        printFirstLastRowDeck();
        printSecondRowDeck(commonObjective1, goldCard1, goldCard2, topGoldDeck);
        printThirdRowDeck(commonObjective1, topGoldDeck);
        printFourthRowDeck(commonObjective1, goldCard1, goldCard2, topGoldDeck);
        printFirstLastRowDeck();
        printFirstLastRowDeck();
        printSecondRowDeck(commonObjective2, resourceCard1, resourceCard2, topResourceDeck);
        printThirdRowDeck(commonObjective2,topResourceDeck);
        printFourthRowDeck(commonObjective2, resourceCard1, resourceCard2, topResourceDeck);
        printFirstLastRowDeck();
    }

    /**
     * Method that print the first and last row of the front deck.
     */
    private static void printFirstLastRowDeck() {
        SafePrinter.println(ANSI_YELLOW_BACKGROUND + "+---------+" +ANSI_BLACK_BACKGROUND + "             +---------+ +---------+ +---------+" );
    }

    /**
     * Method that print the second row of the front deck.
     * @param objectiveCard common objective card1.
     * @param goldCard1 gold card1.
     * @param goldCard2 gold card2.
     * @param topGoldDeck top of the gold card deck.
     */
    private static void printSecondRowDeck(ObjectiveCard objectiveCard,DrawableCard goldCard1,DrawableCard goldCard2, DrawableCard topGoldDeck) {
        if(objectiveCard==null){
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|      ");
        }
        else{
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|     ");
            if(objectiveCard.getScoringCondition() instanceof LayoutCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i]==null)
                    {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.INSECT))
                    {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.ANIMAL))
                    {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.PLANT))
                    {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.FUNGI))
                    {
                        SafePrinter.print(ANSI_RED_BACKGROUND+" ");
                    }
                }
            }
            else
            {
                SafePrinter.print(ANSI_BLACK_BACKGROUND+"   ");
            }
        }
        SafePrinter.print(ANSI_YELLOW_BACKGROUND+" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "             ");
        // print first gold card
        if(goldCard1!=null) {
            if (goldCard1.getFrontCorners()[0]) {
                if (goldCard1.getFrontCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getFrontCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + goldCard1.getPoints());
            if (goldCard1.getScoringCondition() == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
            } else if (goldCard1.getScoringCondition() instanceof CornerCoverageCondition) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "C");
            } else if (goldCard1.getScoringCondition() instanceof ItemsCondition) {
                if (((ItemsCondition) goldCard1.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q");
                } else if (((ItemsCondition) goldCard1.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I");
                } else if (((ItemsCondition) goldCard1.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "  ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "   ");
            if (goldCard1.getFrontCorners()[1]) {
                if (goldCard1.getFrontCornersContent()[1] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
        //print second gold card//
        if(goldCard2!=null)
        {
            if (goldCard2.getFrontCorners()[0]) {
                if (goldCard2.getFrontCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + goldCard2.getPoints());
            if (goldCard2.getScoringCondition() == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
            } else if (goldCard2.getScoringCondition() instanceof CornerCoverageCondition) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "C");
            } else if (goldCard2.getScoringCondition() instanceof ItemsCondition) {
                if (((ItemsCondition) goldCard2.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q");
                } else if (((ItemsCondition) goldCard2.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I");
                } else if (((ItemsCondition) goldCard2.getScoringCondition()).getNeededItems().getFirst().equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "  ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "   ");
            if (goldCard2.getFrontCorners()[1]) {
                if (goldCard2.getFrontCornersContent()[1] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(" ");
        //print top of gold deck
        if (topGoldDeck!=null) {
            if (topGoldDeck.getBackCorners()[0]) {
                if (topGoldDeck.getBackCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "       ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|        ");
            }
            if (topGoldDeck.getBackCorners()[1]) {
                if (topGoldDeck.getBackCornersContent()[1] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.println("");
    }

    /**
     * Method that print the third row of the front deck.
     * @param objectiveCard common objective card1.
     * @param topGoldDeck top of the gold card deck.
     */
    private static void printThirdRowDeck(ObjectiveCard objectiveCard, DrawableCard topGoldDeck) {
        if(objectiveCard==null){
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|      ");
        }
        else {
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|  "+ANSI_BLACK_BACKGROUND+objectiveCard.getPoints()+ANSI_YELLOW_BACKGROUND+"  ");
            if(objectiveCard.getScoringCondition() instanceof ItemsCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().size()>i)
                    {
                        if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.MANUSCRIPT)) {
                            SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.QUILL)) {
                            SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.INKWELL)) {
                            SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                            SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                            SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                            SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                            SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                        } else {
                            SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                        }
                    } else {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                }
            }
            else if (objectiveCard.getScoringCondition() instanceof CornerCoverageCondition)
            {
                SafePrinter.print(ANSI_YELLOW_BACKGROUND +" C ");
            }
            else if (objectiveCard.getScoringCondition() instanceof LayoutCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i]==null)
                    {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.INSECT))
                    {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.ANIMAL))
                    {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.PLANT))
                    {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.FUNGI))
                    {
                        SafePrinter.print(ANSI_RED_BACKGROUND+" ");
                    }
                }
            }
        }
        SafePrinter.print(ANSI_YELLOW_BACKGROUND+" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "             ");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         | ");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         | ");
        if(topGoldDeck== null)
        {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        else {
            List<GameResource> permanentResources = topGoldDeck.getPermanentResources();
            if (permanentResources == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|   ");
                for (GameResource permanentResource : permanentResources) {
                    if (permanentResource.equals(GameResource.FUNGI)) {
                        SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                    } else if (permanentResource.equals(GameResource.ANIMAL)) {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                    } else if (permanentResource.equals(GameResource.PLANT)) {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                    } else if (permanentResource.equals(GameResource.INSECT)) {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                    }
                }
                for (int i = permanentResources.size(); i < 3; i++) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                }
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "   |");
            }
        }
        SafePrinter.println("");
    }

    /**
     * Method that print the fourth row of the front deck.
     * @param objectiveCard common objective card1.
     * @param goldCard1 gold card1.
     * @param goldCard2 gold card2.
     * @param topGoldDeck top of the gold card deck.
     */
    private static void printFourthRowDeck(ObjectiveCard objectiveCard,DrawableCard goldCard1,DrawableCard goldCard2, DrawableCard topGoldDeck) {
        if(objectiveCard==null){
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|      ");
        }
        else {
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|     ");
            if(objectiveCard.getScoringCondition() instanceof LayoutCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i]==null)
                    {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.INSECT))
                    {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.ANIMAL))
                    {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.PLANT))
                    {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.FUNGI))
                    {
                        SafePrinter.print(ANSI_RED_BACKGROUND+" ");
                    }
                }
            }
            else
            {
                SafePrinter.print(ANSI_BLACK_BACKGROUND+"   ");
            }
        }
        SafePrinter.print(ANSI_YELLOW_BACKGROUND+" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "             ");
        // print first gold card
        if(goldCard1!=null) {
            if (goldCard1.getFrontCorners()[3]) {
                if (goldCard1.getFrontCornersContent()[3] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + " ");
                } else if (goldCard1.getFrontCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + " ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "  ");
            }
            if (goldCard1.getPlacementCondition() == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "     ");
            } else if (goldCard1.getPlacementCondition() instanceof ItemsCondition) {
                for (int i = 0; i < 5; i++) {
                    if (i < ((ItemsCondition) goldCard1.getPlacementCondition()).getNeededItems().size()) {
                        if (((ItemsCondition) goldCard1.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                            SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (((ItemsCondition) goldCard1.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                            SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (((ItemsCondition) goldCard1.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                            SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                        } else if (((ItemsCondition) goldCard1.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                            SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                        }
                    } else {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                }
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
            if (goldCard1.getFrontCorners()[2]) {
                if (goldCard1.getFrontCornersContent()[2] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getFrontCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(" ");
        //print second gold card//
        if(goldCard2!=null) {
            if (goldCard2.getFrontCorners()[3]) {
                if (goldCard2.getFrontCornersContent()[3] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getFrontCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            if (goldCard2.getPlacementCondition() == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "     ");
            } else if (goldCard2.getPlacementCondition() instanceof ItemsCondition) {
                for (int i = 0; i < 5; i++) {
                    if (i < ((ItemsCondition) goldCard2.getPlacementCondition()).getNeededItems().size()) {
                        if (((ItemsCondition) goldCard2.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                            SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (((ItemsCondition) goldCard2.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                            SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (((ItemsCondition) goldCard2.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                            SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                        } else if (((ItemsCondition) goldCard2.getPlacementCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                            SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                        }
                    } else {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                    }

                }
            }
            if (goldCard2.getFrontCorners()[2]) {
                if (goldCard2.getFrontCornersContent()[2] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getFrontCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
        //print top of gold deck
        if (topGoldDeck!=null) {
            if (topGoldDeck.getBackCorners()[3]) {
                if (topGoldDeck.getBackCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "       ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|        ");
            }
            if (topGoldDeck.getBackCorners()[2]) {
                if (topGoldDeck.getBackCornersContent()[2] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.println("");
    }

    /**
     * Method used to print the back deck view.
     * @param commonObjective common objective
     * @param faceUpGoldCard face up gold cards
     * @param faceUpResourceCard face up resource card
     * @param topGoldDeck top gold deck
     * @param topResourceDeck top resource deck
     */
    static void printBackDeck(List<ObjectiveCard> commonObjective, List<GoldCard> faceUpGoldCard, List<DrawableCard> faceUpResourceCard, GoldCard topGoldDeck,DrawableCard topResourceDeck) {
        ObjectiveCard commonObjective1;
        ObjectiveCard commonObjective2;
        if(commonObjective == null || commonObjective.size() == 0) {
            commonObjective1 = null;
            commonObjective2 = null;
        } else if (commonObjective.size() == 1) {
            commonObjective1 = commonObjective.get(0);
            commonObjective2 = null;
        }else {
            commonObjective1 = commonObjective.get(0);
            commonObjective2 = commonObjective.get(1);
        }
        GoldCard goldCard1;
        GoldCard goldCard2;
        if(faceUpGoldCard == null || faceUpGoldCard.size() == 0) {
            goldCard1 = null;
            goldCard2 = null;
        } else if (faceUpGoldCard.size() == 1) {
            goldCard1 = faceUpGoldCard.get(0);
            goldCard2 = null;
        }else {
            goldCard1 = faceUpGoldCard.get(0);
            goldCard2 = faceUpGoldCard.get(1);
        }
        DrawableCard resourceCard1;
        DrawableCard resourceCard2;
        if(faceUpResourceCard == null || faceUpResourceCard.size() == 0) {
            resourceCard1 = null;
            resourceCard2 = null;
        } else if (faceUpResourceCard.size() == 1) {
            resourceCard1 = faceUpResourceCard.get(0);
            resourceCard2 = null;
        }else {
            resourceCard1 = faceUpResourceCard.get(0);
            resourceCard2 = faceUpResourceCard.get(1);
        }
        printFirstLastRowDeck();
        printBackSecondRowDeck(commonObjective1,goldCard1,goldCard2,topGoldDeck);
        printBackThirdRowDeck(commonObjective1,goldCard1,goldCard2,topGoldDeck);
        printBackFourthRowDeck(commonObjective1,goldCard1,goldCard2,topGoldDeck);
        printFirstLastRowDeck();
        printFirstLastRowDeck();
        printBackSecondRowDeck(commonObjective2,resourceCard1,resourceCard2,topResourceDeck);
        printBackThirdRowDeck(commonObjective2,resourceCard1,resourceCard2,topResourceDeck);
        printBackFourthRowDeck(commonObjective2,resourceCard1,resourceCard2,topResourceDeck);
        printFirstLastRowDeck();
    }

    /**
     * Method used to print the second row of the back deck.
     * @param objectiveCard common objective
     * @param goldCard1 face up gold cards
     * @param goldCard2 face up resource card
     * @param topGoldDeck top gold deck
     */
    private static void printBackSecondRowDeck(ObjectiveCard objectiveCard,DrawableCard goldCard1,DrawableCard goldCard2, DrawableCard topGoldDeck) {
        if(objectiveCard==null){
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|      ");
        }
        else {
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|     ");
            if(objectiveCard.getScoringCondition() instanceof LayoutCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i]==null)
                    {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.INSECT))
                    {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.ANIMAL))
                    {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.PLANT))
                    {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[0][i].equals(GameResource.FUNGI))
                    {
                        SafePrinter.print(ANSI_RED_BACKGROUND+" ");
                    }
                }
            }
            else
            {
                SafePrinter.print(ANSI_BLACK_BACKGROUND+"   ");
            }
        }
        SafePrinter.print(ANSI_YELLOW_BACKGROUND+" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "             ");
        // print first gold card
        if(goldCard1!=null) {
            if (!goldCard1.getBackCorners()[0])
            {
                if (goldCard1.getBackCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "     ");
            if (goldCard1.getBackCorners()[1]) {
                if (goldCard1.getBackCornersContent()[1] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
        //print second gold card//
        if(goldCard2!=null)
        {
            if (goldCard2.getBackCorners()[0]) {
                if (goldCard2.getBackCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "     ");
            if (goldCard2.getBackCorners()[1]) {
                if (goldCard2.getBackCornersContent()[1] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(" ");
        //print top of gold deck
        if (topGoldDeck!=null) {
            if (topGoldDeck.getBackCorners()[0]) {
                if (topGoldDeck.getBackCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[0].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "       ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|        ");
            }
            if (topGoldDeck.getBackCorners()[1]) {
                if (topGoldDeck.getBackCornersContent()[1] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[1].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.println("");
    }

    /**
     * Method used to print the third row of the back deck.
     * @param objectiveCard common objective
     * @param goldCard1 face up gold cards
     * @param goldCard2 face up resource card
     * @param topGoldDeck top gold deck
     */
    private static void printBackThirdRowDeck(ObjectiveCard objectiveCard,DrawableCard goldCard1,DrawableCard goldCard2, DrawableCard topGoldDeck) {
        if(objectiveCard==null){
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|      ");
        }
        else {
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|  "+ANSI_BLACK_BACKGROUND+objectiveCard.getPoints()+ANSI_YELLOW_BACKGROUND+"  ");
            if(objectiveCard.getScoringCondition() instanceof ItemsCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().size()>i)
                    {
                        if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.MANUSCRIPT)) {
                            SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.QUILL)) {
                            SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameObject.INKWELL)) {
                            SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.PLANT)) {
                            SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.FUNGI)) {
                            SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.ANIMAL)) {
                            SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                        } else if (((ItemsCondition) objectiveCard.getScoringCondition()).getNeededItems().get(i).equals(GameResource.INSECT)) {
                            SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                        } else {
                            SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                        }
                    } else {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                    }
                }
            }
            else if (objectiveCard.getScoringCondition() instanceof CornerCoverageCondition)
            {
                SafePrinter.print(ANSI_YELLOW_BACKGROUND +" C ");
            }
            else if (objectiveCard.getScoringCondition() instanceof LayoutCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i]==null)
                    {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.INSECT))
                    {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.ANIMAL))
                    {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.PLANT))
                    {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[1][i].equals(GameResource.FUNGI))
                    {
                        SafePrinter.print(ANSI_RED_BACKGROUND+" ");
                    }
                }
            }
        }
        SafePrinter.print(ANSI_YELLOW_BACKGROUND+" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "             ");
        if(goldCard1==null)
        {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        else {
            List<GameResource> permanentResources = goldCard1.getPermanentResources();
            if (permanentResources == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|   ");
                for (GameResource permanentResource : permanentResources) {
                    if (permanentResource.equals(GameResource.FUNGI)) {
                        SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                    } else if (permanentResource.equals(GameResource.ANIMAL)) {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                    } else if (permanentResource.equals(GameResource.PLANT)) {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                    } else if (permanentResource.equals(GameResource.INSECT)) {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                    }
                }
                for (int i = permanentResources.size(); i < 3; i++) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                }
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "   |");
            }

        }
        SafePrinter.print(" ");
        if(goldCard2==null)
        {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        else {
            List<GameResource> permanentResources = goldCard2.getPermanentResources();
            if (permanentResources == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|   ");
                for (GameResource permanentResource : permanentResources) {
                    if (permanentResource.equals(GameResource.FUNGI)) {
                        SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                    } else if (permanentResource.equals(GameResource.ANIMAL)) {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                    } else if (permanentResource.equals(GameResource.PLANT)) {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                    } else if (permanentResource.equals(GameResource.INSECT)) {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                    }
                }
                for (int i = permanentResources.size(); i < 3; i++) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                }
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "   |");
            }

        }
        SafePrinter.print(" ");
        if(topGoldDeck==null)
        {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        else {
            List<GameResource> permanentResources = topGoldDeck.getPermanentResources();
            if (permanentResources == null) {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|   ");
                for (GameResource permanentResource : permanentResources) {
                    if (permanentResource.equals(GameResource.FUNGI)) {
                        SafePrinter.print(ANSI_RED_BACKGROUND + "F");
                    } else if (permanentResource.equals(GameResource.ANIMAL)) {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND + "A");
                    } else if (permanentResource.equals(GameResource.PLANT)) {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND + "P");
                    } else if (permanentResource.equals(GameResource.INSECT)) {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I");
                    }
                }
                for (int i = permanentResources.size(); i < 3; i++) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + " ");
                }
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "   |");
            }

        }
        SafePrinter.println("");
    }

    /**
     * Method used to print the fourth row of the back deck.
     * @param objectiveCard common objective
     * @param goldCard1 face up gold cards
     * @param goldCard2 face up resource card
     * @param topGoldDeck top gold deck
     */
    private static void printBackFourthRowDeck(ObjectiveCard objectiveCard,DrawableCard goldCard1,DrawableCard goldCard2, DrawableCard topGoldDeck) {
        if(objectiveCard==null){
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|      ");
        }
        else {
            SafePrinter.print(ANSI_YELLOW_BACKGROUND+"|     ");
            if(objectiveCard.getScoringCondition() instanceof LayoutCondition)
            {
                for (int i = 0; i < 3; i++) {
                    if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i]==null)
                    {
                        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.INSECT))
                    {
                        SafePrinter.print(ANSI_PURPLE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.ANIMAL))
                    {
                        SafePrinter.print(ANSI_BLUE_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.PLANT))
                    {
                        SafePrinter.print(ANSI_GREEN_BACKGROUND+" ");
                    }
                    else if(((LayoutCondition)objectiveCard.getScoringCondition()).getCardsColor()[2][i].equals(GameResource.FUNGI))
                    {
                        SafePrinter.print(ANSI_RED_BACKGROUND+" ");
                    }
                }
            }
            else
            {
                SafePrinter.print(ANSI_BLACK_BACKGROUND+"   ");
            }
        }
        SafePrinter.print(ANSI_YELLOW_BACKGROUND+" |");
        SafePrinter.print(ANSI_BLACK_BACKGROUND + "             ");
        // print first gold card
        if(goldCard1!=null) {
            if (goldCard1.getBackCorners()[3]) {
                if (goldCard1.getBackCornersContent()[3] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard1.getBackCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "  ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "     ");
            if (goldCard1.getBackCorners()[2]) {
                if (goldCard1.getBackCornersContent()[2] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard1.getBackCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(" ");
        //print second gold card//
        if(goldCard2!=null) {
            if (goldCard2.getBackCorners()[3]) {
                if (goldCard2.getBackCornersContent()[3] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "  ");
                } else if (goldCard2.getBackCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "  ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLACK_BACKGROUND + "   ");
            }
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "     ");
            if (goldCard2.getBackCorners()[2]) {
                if (goldCard2.getBackCornersContent()[2] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (goldCard2.getBackCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.print(ANSI_BLACK_BACKGROUND+" ");
        //print top of gold deck
        if (topGoldDeck!=null) {
            if (topGoldDeck.getBackCorners()[3]) {
                if (topGoldDeck.getBackCornersContent()[0] == null) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "       ");
                } else if (topGoldDeck.getBackCornersContent()[3].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_BLACK_BACKGROUND + "|" + ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "       ");
                }
            } else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + "|        ");
            }
            if (topGoldDeck.getBackCorners()[2]) {
                if (topGoldDeck.getBackCornersContent()[2] == null) {
                    SafePrinter.print(ANSI_WHITE_BACKGROUND + " " + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.FUNGI)) {
                    SafePrinter.print(ANSI_RED_BACKGROUND + "F" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.ANIMAL)) {
                    SafePrinter.print(ANSI_BLUE_BACKGROUND + "A" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.PLANT)) {
                    SafePrinter.print(ANSI_GREEN_BACKGROUND + "P" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameResource.INSECT)) {
                    SafePrinter.print(ANSI_PURPLE_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameObject.QUILL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "Q" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameObject.INKWELL)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "I" + ANSI_BLACK_BACKGROUND + "|");
                } else if (topGoldDeck.getBackCornersContent()[2].equals(GameObject.MANUSCRIPT)) {
                    SafePrinter.print(ANSI_YELLOW_BACKGROUND + "M" + ANSI_BLACK_BACKGROUND + "|");
                }
            }
            else {
                SafePrinter.print(ANSI_BLACK_BACKGROUND + " |");
            }
        }
        else {
            SafePrinter.print(ANSI_BLACK_BACKGROUND + "|         |");
        }
        SafePrinter.println("");
    }
}

