package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.controller.GameState;
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
import it.polimi.ingsw.gc07.model_view.GameFieldView;
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
                    System.exit(0);
                }
            }, 30*1000); //timer of 5 minutes
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
                    if(correctInput && gameId < 0) {
                        System.out.println("No such game id.");
                        correctInput = false;
                    }
                }while(!correctInput);

                // cancel timer
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

                // cancel timer
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
            System.out.println("- e to disconnect from the game"); // DisconnectPlayerCommand
            System.out.println("- r to draw a card from a deck"); // DrawDeckCardCommand
            System.out.println("- t to draw a face up card"); // DrawFaceUpCardCommand
            System.out.println("- y to place a card"); // PlaceCardCommand
            System.out.println("- u to place the starter card"); // PlaceStarterCardCommand
            System.out.println("- i to see another player's game field");
            System.out.println("- o to see the whole chat");
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

                    // check sender not equals receiver
                    if(receiver.equals(nickname)) {
                        System.out.println("You can't send a private message to yourself.");
                        break;
                    }
                    // check existing receiver
                    if(!client.getGameView().checkPlayerPresent(receiver)) {
                        System.out.println("Provided receiver doesn't exist.");
                        break;
                    }
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    client.setAndExecuteCommand(new AddChatPrivateMessageCommand(content, nickname, receiver));
                    break;
                case "w":
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    client.setAndExecuteCommand(new AddChatPublicMessageCommand(content, nickname));
                    break;
                case "e":
                    client.setAndExecuteCommand(new DisconnectPlayerCommand(nickname));
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
                        System.out.println("No such card type");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawDeckCardCommand(nickname, cardType));
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
                        System.out.println("No such card type");
                        break;
                    }
                    System.out.println("Select the position of the card to draw: ");
                    System.out.print("> ");
                    int pos;
                    try {
                        pos = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    // check valid position
                    if(pos < 0 || pos > client.getGameView().getNumFaceUpCards(cardType)) {
                        System.out.println("Wrong cards position.");
                        break;
                    }
                    // check game state
                    if(!client.getGameView().getGameState().equals(GameState.PLAYING)) {
                        System.out.println("Wrong game state.");
                        break;
                    }
                    // check current player
                    if(!client.getGameView().isCurrentPlayer(nickname)) {
                        System.out.println("This is not your turn, try later.");
                        break;
                    }
                    client.setAndExecuteCommand(new DrawFaceUpCardCommand(nickname, cardType, pos));
                    break;
                case "y":
                    // pos
                    System.out.println("Select the position of the card you want to place: ");
                    System.out.print("> ");
                    int cardPos;
                    try {
                        cardPos = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(cardPos < 0 || cardPos >= client.getGameView().getCurrHardHandSize()) {
                        System.out.println("Wrong card hand position.");
                        break;
                    }
                    System.out.println("Insert a position of the game field where you want to place the card.");
                    // x
                    System.out.println("Insert x: ");
                    System.out.print("> ");
                    int x;
                    try {
                        x = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(x < 0 || x >= client.getGameView().getGameFieldDim()) {
                        System.out.println("GameField position out of bound.");
                        break;
                    }
                    // y
                    System.out.println("Insert y: ");
                    System.out.print("> ");
                    int y;
                    try {
                        y = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(y < 0 || y >= client.getGameView().getGameFieldDim()) {
                        System.out.println("GameField position out of bound.");
                        break;
                    }
                    // way
                    System.out.println("Select 0 to place the card face up, 1 to place the card face down: ");
                    System.out.print("> ");
                    try {
                        wayInput = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value for way is not correct");
                        break;
                    }
                    // create and execute command
                    client.setAndExecuteCommand(new PlaceCardCommand(nickname, cardPos, x, y, way));
                    break;
                case "u":
                    System.out.println("Select 0 to place the starter card face up, 1 to place the starter card face down: ");
                    System.out.print("> ");
                    try {
                        wayInput = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("Insert a number.");
                        break;
                    }
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value is not correct");
                        break;
                    }
                    if(!client.getGameView().getGameState().equals(GameState.PLACING_STARTER_CARDS)) {
                        System.out.println("Wrong game state.");
                        break;
                    }
                    client.setAndExecuteCommand(new PlaceStarterCardCommand(nickname, way));
                    break;
                case "i":
                    System.out.println("Insert other player's nickname");
                    System.out.print("> ");
                    String nickname = scan.nextLine();
                    // check existing players
                    if(!client.getGameView().checkPlayerPresent(nickname)) {
                        System.out.println("Provided nickname doesn't exist in the game.");
                        break;
                    }
                    System.out.println();
                    System.out.println("--------------------------------------------------------");
                    System.out.println("                     " + nickname + "'s GAME FIELD");
                    System.out.println("--------------------------------------------------------");
                    printGameField(nickname);
                    System.out.println("\n\n");
                    break;
                case "o":
                    System.out.println();
                    System.out.println("--------------------------------------------------------");
                    System.out.println("                            CHAT                        ");
                    System.out.println("--------------------------------------------------------");
                    client.getGameView().printChat();
                    System.out.println();
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }

        System.out.println("\n\nDo you want to reconnect (1 = yes, other = no)?");
        int reconnect = 0;
        try {
            reconnect = scan.nextInt();
            scan.nextLine();
        }catch(InputMismatchException e) {
            System.exit(0);
        }
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

    private void printGameField(String nickname) {
        GameFieldView gameField = client.getGameView().getGameField(nickname);
        receiveGameFieldUpdate(gameField.getCardsContent(), gameField.getCardsFace(), gameField.getCardsOrder());
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
        PlayerTui.printStarterCard(starterCard, true);
        PlayerTui.printStarterCard(starterCard, false);
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        if(!(client.getGameView().getGameState().equals(GameState.GAME_STARTING) && (hand.size() < 3 || personalObjective == null))) {
            System.out.println();
            System.out.println("--------------------------------------------------------");
            System.out.println("                      PLAYER HAND                       ");
            System.out.println("--------------------------------------------------------");
            PlayerTui.printPlayerHand(hand, personalObjective);
        }
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
        // don't want to print deck update every time a card changes
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

        // if new current player, print deck update
        GameState state = client.getGameView().getGameState();
        String currPlayerNickname = client.getGameView().getCurrentPlayerNickname();
        String ownerNickname = client.getGameView().getOwnerNickname();
        DrawableCard topResourceDeck = client.getGameView().getTopResourceDeck();
        GoldCard topGoldDeck = client.getGameView().getTopGoldDeck();
        List<DrawableCard> faceUpResourceCard = client.getGameView().getFaceUpResourceCard();
        List<GoldCard> faceUpGoldCard = client.getGameView().getFaceUpGoldCard();
        List<ObjectiveCard> commonObjective = client.getGameView().getCommonObjective();
        if(state.equals(GameState.PLAYING) && currPlayerNickname != null && currPlayerNickname.equals(ownerNickname)) {
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
        runCliJoinGame();
    }

    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                         WINNERS                        ");
        System.out.println("--------------------------------------------------------");
        for(String winner: winners) {
            System.out.println(winner);
        }
        System.out.println();
        client.setClientAlive(false);
    }
}