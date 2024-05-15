package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.main.ClientMain;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.view.Ui;

import java.util.*;

public class Tui implements Ui, ChatTui, DeckTui, GameFieldTui, PlayerTui, BoardTui {
    private final String nickname;
    private final Client client;
    private final static int minPlayersNumber = 2;
    private final static int maxPlayersNumber = 4;

    public Tui(String nickname, Client client) {
        this.nickname = nickname;
        this.client = client;
    }

    @Override
    public void runCliJoinGame() {
        Timer timeout = new Timer();
        new Thread(()->{
            timeout.schedule(new TimerTask() {
                @Override
                public void run() {
                    client.setAndExecuteCommand(new RemoveFromPendingCommand(nickname));
                    System.exit(0);
                }
            }, 5*1000); //timer of 5 minutes
        }).start();

        Scanner scan = new Scanner(System.in);
        System.out.println("Insert a character to perform an action:");
        System.out.println("- q to join an existing game"); // JoinExistingGameCommand
        System.out.println("- w to create an new game"); // JoinNewGameCommand
        System.out.println("- e to see existing games"); // DisplayGamesCommand
        System.out.print("> ");
        String command = scan.nextLine();

        String tokenColorString;
        TokenColor tokenColor;
        boolean correctInput;
        switch(command){
            case "q":
                // join existing game
                do {
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    tokenColorString = scan.nextLine();
                    tokenColor = parseTokenColor(tokenColorString);
                    if(tokenColor == null) {
                        correctInput = false;
                        System.out.println("No such token color");
                    }else {
                        correctInput = true;
                    }
                }while(!correctInput);

                int gameId;
                do {
                    System.out.println("Insert game id: ");
                    gameId = -1;
                    correctInput = true;
                    try {
                        gameId = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        correctInput = false;
                        System.out.println("No such game id, insert a number.");
                    }
                    if(gameId < 0) {
                        System.out.println("No such game id.");
                        correctInput = false;
                    }
                }while(!correctInput);
                synchronized (this){
                    timeout.cancel();
                    timeout.purge();

                }
                client.setAndExecuteCommand(new JoinExistingGameCommand(nickname, tokenColor, gameId));
                break;

            case "w":
                // join new game
                do {
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    tokenColorString = scan.nextLine();
                    tokenColor = parseTokenColor(tokenColorString);
                    correctInput = true;
                    if(tokenColor == null) {
                        correctInput = false;
                        System.out.println("No such token color");
                    }
                }while(!correctInput);

                int playersNumber = -1;
                do {
                    correctInput = true;
                    System.out.println("Insert the number of players for the game: ");
                    System.out.print("> ");
                    try {
                        playersNumber = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        correctInput = false;
                        scan.nextLine();
                    }
                    if(playersNumber < minPlayersNumber || playersNumber > maxPlayersNumber) {
                        correctInput = false;
                    }
                }while(!correctInput);

                synchronized (this){
                    timeout.cancel();
                    timeout.purge();

                }
                client.setAndExecuteCommand(new JoinNewGameCommand(nickname, tokenColor, playersNumber));
                break;

            case "e":
                // display existing games
                client.setAndExecuteCommand(new DisplayGamesCommand(nickname));
                break;

            default:
                System.out.println("The provided character doesn't refer to any action");
                runCliJoinGame();
        }
    }

    @Override
    public void runCliGame() {
        Scanner scan = new Scanner(System.in);
        while(client.isClientAlive()) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to write a private message"); // AddChatPrivateMessage
            System.out.println("- w to write a public message"); // AddChatPublicMessage
            System.out.println("- e to disconnect from the game"); // DisconnectPlayerControllerCommand
            System.out.println("- r to draw a card from a deck"); // DrawDeckCardControllerCommand
            System.out.println("- t to draw a face up card"); // DrawFaceUpCardControllerCommand
            System.out.println("- y to place a card"); // PlaceCardControllerCommand
            System.out.println("- u to place the starter card"); // PlaceStarterCardControllerCommand
            System.out.print("> ");
            String command = scan.nextLine();

            String content;
            String cardTypeString;
            CardType cardType;
            int wayInput;
            boolean way;
            switch(command) {
                case "q":
                    System.out.println("Insert the receiver nickname: ");
                    System.out.print("> ");
                    String receiver = scan.nextLine();
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    // check sender not equals receiver
                    if(receiver.equals(nickname)) {
                        System.out.println("CLIENT CHECK - You can't send a private message to yourself.");
                        break;
                    }
                    // check existing receiver
                    if(!client.getGameView().checkPlayerPresent(receiver)) {
                        System.out.println("CLIENT CHECK - Provided receiver doesn't exist.");
                        System.out.println("You: -"+nickname+"-");
                        System.out.println("Nickname: -"+receiver+"-");
                        break;
                    }
                    client.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand(content, nickname, receiver));
                    break;
                case "w":
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    client.setAndExecuteCommand(new AddChatPublicMessageControllerCommand(content, nickname));
                    break;
                case "e":
                    client.setAndExecuteCommand(new DisconnectPlayerControllerCommand(nickname));
                    client.setClientAlive(false);
                    break;
                case "r":
                    System.out.println("Select a card type ('g' for gold or 'r' for resource): ");
                    System.out.print("> ");
                    cardTypeString = scan.nextLine();
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        System.out.println("CLIENT CHECK - No such card type");
                        continue;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("CLIENT CHECK - Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("CLIENT CHECK - This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawDeckCardControllerCommand(nickname, cardType));
                    break;
                case "t":
                    System.out.println("Select a card type ('g' for gold or 'r' for resource): ");
                    System.out.print("> ");
                    cardTypeString = scan.nextLine();
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        System.out.println("CLIENT CHECK - No such card type");
                        continue;
                    }
                    System.out.println("Select the position of the card to draw: ");
                    System.out.print("> ");
                    int pos = scan.nextInt();
                    scan.nextLine();
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("CLIENT CHECK - Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("CLIENT CHECK - This is not your turn, try later.");
                        break;
                    }
                    // check valid position
                    if(pos < 0 || pos > client.getGameView().getNumFaceUpCards(cardType)) {
                        System.out.println("CLIENT CHECK - Wrong cards position.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawFaceUpCardControllerCommand(nickname, cardType, pos));
                    break;
                case "y":
                    // pos
                    System.out.println("Select the position of the card you want to place: ");
                    System.out.print("> ");
                    int cardPos = scan.nextInt();
                    scan.nextLine();
                    if(cardPos < 0 || cardPos >= client.getGameView().getCurrHardHandSize()) {
                        System.out.println("CLIENT CHECK - Wrong card hand position.");
                        break;
                    }
                    System.out.println("Insert a position of the game field where you want to place the card.");
                    // x
                    System.out.println("Insert x: ");
                    System.out.print("> ");
                    int x = scan.nextInt();
                    scan.nextLine();
                    if(x < 0 || x >= client.getGameView().getGameFieldDim()) {
                        System.out.println("CLIENT CHECK - GameField position out of bound.");
                        break;
                    }
                    // y
                    System.out.println("Insert y: ");
                    System.out.print("> ");
                    int y = scan.nextInt();
                    scan.nextLine();
                    if(y < 0 || y >= client.getGameView().getGameFieldDim()) {
                        System.out.println("CLIENT CHECK - GameField position out of bound.");
                        break;
                    }
                    // way
                    System.out.println("Select 0 to place the card face up, 1 to place the card face down: ");
                    System.out.print("> ");
                    wayInput = scan.nextInt();
                    scan.nextLine();
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("CLIENT CHECK - The provided value for way is not correct");
                        continue;
                    }
                    // create and execute command
                    client.setAndExecuteCommand(new PlaceCardControllerCommand(nickname, cardPos, x, y, way));
                    break;
                case "u":
                    System.out.println("Select 0 to place the starter card face up, 1 to place the starter card face down: ");
                    System.out.print("> ");
                    wayInput = scan.nextInt();
                    scan.nextLine();
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("CLIENT CHECK - The provided value is not correct");
                        continue;
                    }
                    client.setAndExecuteCommand(new PlaceStarterCardControllerCommand(nickname, way));
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }

        System.out.println("\n\nDo you want to reconnect (1 = yes, other = no)?");
        int reconnect = scan.nextInt();
        scan.nextLine();
        if(reconnect == 1) {
            ClientMain.main(new String[0]);
        }else {
            System.exit(0);
        }
    }

    private TokenColor parseTokenColor(String tokenColorString) {
        return switch (tokenColorString) {
            case "green" -> TokenColor.GREEN;
            case "red" -> TokenColor.RED;
            case "yellow" -> TokenColor.YELLOW;
            case "blue" -> TokenColor.BLUE;
            default -> null;
        };
    }

    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      GAME CHAT                         ");
        System.out.println("--------------------------------------------------------");
        ChatTui.printMessage(chatMessage);
    }

    @Override
    public void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                   PLAYER GAME FIELD                    ");
        System.out.println("--------------------------------------------------------");
        GameFieldTui.printGameField(cardsContent, cardsFace, cardsOrder);
    }

    @Override
    public void receiveStarterCardUpdate(PlaceableCard starterCard) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     STARTER CARD                       ");
        System.out.println("--------------------------------------------------------");
        System.out.println("PRINTING");
        PlayerTui.printStarterCard(starterCard, true);
        PlayerTui.printStarterCard(starterCard, false);
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      PLAYER HAND                       ");
        System.out.println("--------------------------------------------------------");
        PlayerTui.printPlayerHand(hand, personalObjective);
    }

    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      SCORE BOARD                       ");
        System.out.println("--------------------------------------------------------");
        BoardTui.printScoreTrackBoard(playerScores, playerTokenColors);
    }

    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      FRONT DECK                        ");
        System.out.println("--------------------------------------------------------");
        DeckTui.printDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
        System.out.println("--------------------------------------------------------");
        System.out.println("                       BACK DECK                        ");
        System.out.println("--------------------------------------------------------");
        DeckTui.printBackDeck(commonObjective, faceUpGoldCard, faceUpResourceCard, topGoldDeck, topResourceDeck);
    }

    @Override
    public void receiveGeneralModelUpdate(GameState gameState, String currPlayer) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     GAME MODEL UPDATE                  ");
        System.out.println("--------------------------------------------------------");
        System.out.println("Game state: " + gameState);
        if(currPlayer == null) {
            currPlayer = "none";
        }
        System.out.println("Current player: " + currPlayer);
    }

    @Override
    public void receivePenultimateRoundUpdate() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     PENULTIMATE ROUND                  ");
        System.out.println("--------------------------------------------------------");
    }

    @Override
    public void receiveAdditionalRoundUpdate() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                     ADDITIONAL ROUND                   ");
        System.out.println("--------------------------------------------------------");
    }

    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                      COMMAND RESULT                    ");
        System.out.println("--------------------------------------------------------");
        System.out.println(commandResult.getResultMessage());
    }

    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGames) {
        for(Integer id: existingGames.keySet()) {
            System.out.println("Id: " + id + " - " + "Number of players: " + existingGames.get(id));
        }
    }
}