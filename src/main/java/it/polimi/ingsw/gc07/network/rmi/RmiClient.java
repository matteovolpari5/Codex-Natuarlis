package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.PingSender;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements VirtualView, PingSender {
    /**
     * Nickname of the player associated to the RmiClient.
     */
    private final String nickname;
    /**
     * Reference to RmiServerGamesManager, the general server.
     */
    private final VirtualServerGamesManager serverGamesManager;
    /**
     * Reference to RmiServerGame, the game specific server.
     */
    private VirtualServerGame serverGame;
    /**
     * Player's local copy of the game model.
     */
    private final GameView gameView;
    /**
     * Send ping attribute.
     */
    private boolean viewAlive;

    /**
     * Constructor of RmiClient.
     * @param serverGamesManager general server
     * @param nickname nickname
     * @throws RemoteException remote exception
     */
    public RmiClient(String nickname, VirtualServerGamesManager serverGamesManager) throws RemoteException {
        this.nickname = nickname;
        this.serverGamesManager = serverGamesManager;
        this.serverGame = null;
        this.gameView = new GameView(nickname);
        this.viewAlive = true;
    }

    /**
     * Method that allows the client to connect with RMIServerGamesManager, the general server.
     * @param connectionType connection type
     * @param interfaceType interface type
     */
    public void connectToGamesManagerServer(boolean connectionType, boolean interfaceType) {
        try {
            serverGamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
        } catch (RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void reconnectPlayer(String nickname, boolean connectionType, boolean interfaceType) {
        try {
            serverGamesManager.setAndExecuteCommand(new ReconnectPlayerCommand(this, nickname, connectionType, interfaceType));
        } catch (RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that allows to set a RmiServerGame, the game specific server, for the client.
     * @param gameId game id
     * @throws RemoteException remote exception
     */
    @Override
    public void setServerGame(int gameId) throws RemoteException {
        try {
            this.serverGame = serverGamesManager.getGameServer(gameId);
        }catch(RemoteException e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException();
        }
        // game joined
        connectToGameServer();
        new Thread(this::startGamePing).start();
        new Thread(this::runCliGame).start();
    }

    /**
     * Method that allows the client to connect with RMIServerGame, the game specific server.
     */
    private void connectToGameServer() {
        try {
            serverGame.connect(nickname, this);
        }catch(RemoteException e) {
            //TODO manager remote exception
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Method used from RmiServerGamesManager to restart the cli if the joining was not successful.
     */
    public void notifyJoinNotSuccessful() throws RemoteException {
        System.out.println("Action not successful.");
        new Thread(this::runCliJoinGame).start();
    }

    /**
     * Getter method for nickname
     * @return nickname
     * @throws RemoteException remote exception
     */
    @Override
    public String getNickname() throws RemoteException {
        return nickname;
    }

    @Override
    public void startGamesManagerPing() {
        //TODO
        // start the ping for the first phase, in the games manager
    }

    @Override
    public void startGamePing() {
        boolean runThread = true;
        while(runThread) {
            synchronized (this) {
                if (viewAlive) {
                    try {
                        System.out.println("Ho inviato un ping");
                        serverGame.setAndExecuteCommand(new SendPingControllerCommand(nickname));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    runThread = false;
                }
            }
            try {
                Thread.sleep(1000); // wait one second between two ping
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    //TODO creare dei metodi / classi per richiedere le cose, così è un pastrugno
    public void runCliJoinGame() {
        boolean triedJoining = false;
        Scanner scan = new Scanner(System.in);
        while(!triedJoining) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to join an existing game"); // JoinExistingGameCommand
            System.out.println("- w to join an new game"); // JoinNewGameCommand
            System.out.println("- e to see existing games"); // DisplayGamesCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "q":
                    // join existing game
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    String tokenColorString = scan.nextLine();
                    TokenColor tokenColor;
                    switch(tokenColorString) {
                        case "green":
                            tokenColor = TokenColor.GREEN;
                            break;
                        case "red":
                            tokenColor = TokenColor.RED;
                            break;
                        case "yellow":
                            tokenColor = TokenColor.YELLOW;
                            break;
                        case "blue":
                            tokenColor = TokenColor.BLUE;
                            break;
                        default:
                            System.out.println("No such token color");
                            continue;
                    }
                    System.out.println("Insert game id: ");
                    int gameId;
                    try {
                        gameId = scan.nextInt();
                        scan.nextLine();
                    }catch(InputMismatchException e) {
                        scan.nextLine();
                        System.out.println("No such game id, insert a number");
                        continue;
                    }
                    if(gameId < 0) {
                        System.out.println("No such game id");
                        continue;
                    }
                    try {
                        serverGamesManager.setAndExecuteCommand(new JoinExistingGameCommand(nickname, tokenColor, gameId));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    triedJoining = true;
                    break;

                case "w":
                    // join new game
                    System.out.println("Insert token color (green, red, yellow or blue): ");
                    System.out.print("> ");
                    tokenColorString = scan.nextLine();
                    switch(tokenColorString) {
                        case "green":
                            tokenColor = TokenColor.GREEN;
                            break;
                        case "red":
                            tokenColor = TokenColor.RED;
                            break;
                        case "yellow":
                            tokenColor = TokenColor.YELLOW;
                            break;
                        case "blue":
                            tokenColor = TokenColor.BLUE;
                            break;
                        default:
                            System.out.println("No such token color");
                            continue;
                    }
                    System.out.println("Insert the number of players for the game: ");
                    int playersNumber = scan.nextInt();
                    scan.nextLine();
                    // TODO potremmo fare già qua il controllo su players number per efficienza
                    try {
                        serverGamesManager.setAndExecuteCommand(new JoinNewGameCommand(nickname, tokenColor, playersNumber));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    triedJoining = true;
                    break;

                case "e":
                    // display existing games
                    try {
                        serverGamesManager.setAndExecuteCommand(new DisplayGamesCommand(nickname));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    break;

                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
    }

    public void runCliGame() {
        Scanner scan = new Scanner(System.in);
        while(viewAlive) {
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
            switch(command){
                case "q":
                    System.out.println("Insert the receiver nickname: ");
                    System.out.print("> ");
                    String receiver = scan.nextLine();
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    String content = scan.nextLine();
                    try {
                        serverGame.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand(content, nickname, receiver));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "w":
                    System.out.println("Insert the message content:");
                    System.out.print("> ");
                    content = scan.nextLine();
                    try {
                        serverGame.setAndExecuteCommand(new AddChatPublicMessageControllerCommand(content, nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "e":
                    try {
                        serverGame.setAndExecuteCommand(new DisconnectPlayerControllerCommand(nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    System.out.println("\nYou successfully disconnected !");
                    viewAlive = false;
                    break;
                case "r":
                    System.out.println("Select a card type ('g' for gold or 'r' for resource): ");
                    System.out.print("> ");
                    String cardTypeString = scan.nextLine();
                    CardType cardType;
                    if(cardTypeString.equals("r")) {
                        cardType = CardType.RESOURCE_CARD;
                    }else if(cardTypeString.equals("g")) {
                        cardType = CardType.GOLD_CARD;
                    }else {
                        System.out.println("No such card type");
                        continue;
                    }
                    try {
                        serverGame.setAndExecuteCommand(new DrawDeckCardControllerCommand(nickname, cardType));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                        continue;
                    }
                    System.out.println("Select the position of the card to draw: ");
                    System.out.print("> ");
                    int pos = scan.nextInt();
                    scan.nextLine();
                    //TODO possiamo introdurre un controllo per evitare una chiamata
                    // inutile se la posizione eccede il range possibile
                    try {
                        serverGame.setAndExecuteCommand(new DrawFaceUpCardControllerCommand(nickname, cardType, pos));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "y":
                    // String nickname, int pos, int x, int y, boolean way) {
                    // pos
                    System.out.println("Select the position of the card you want to place: ");
                    System.out.print("> ");
                    int cardPos = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Insert a position of the game field where you want to place the card.");
                    // x
                    System.out.println("Insert x: ");
                    System.out.print("> ");
                    int x = scan.nextInt();
                    scan.nextLine();
                    // y
                    System.out.println("Insert y: ");
                    System.out.print("> ");
                    int y = scan.nextInt();
                    scan.nextLine();
                    // way
                    System.out.println("Select 0 to place the card face up, 1 to place the card face down: ");
                    System.out.print("> ");
                    int wayInput = scan.nextInt();
                    scan.nextLine();
                    boolean way;
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value for way is not correct");
                        continue;
                    }
                    // create and execute command
                    try {
                        serverGame.setAndExecuteCommand(new PlaceCardControllerCommand(nickname, cardPos, x, y, way));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                        System.out.println("The provided value is not correct");
                        continue;
                    }
                    try {
                        serverGame.setAndExecuteCommand(new PlaceStarterCardControllerCommand(nickname, way));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
        System.exit(0);
    }

    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessageUpdate chat message update
     */
    @Override
    public void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) {
        chatMessageUpdate.execute(gameView);
    }

    /**
     * Method used to show the client his starter card.
     * @param starterCardUpdate starter card update
     */
    @Override
    public void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) {
        starterCardUpdate.execute(gameView);
    }

    /**
     * Method used to notify players that a card has been placed.
     * @param placedCardUpdate placed card update
     */
    @Override
    public void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) {
        placedCardUpdate.execute(gameView);
    }

    /**
     * Method used to notify a game model update.
     * @param gameModelUpdate game model update
     */
    @Override
    public void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) {
        gameModelUpdate.execute(gameView);
    }

    /**
     * Method used to notify a player joined update.
     * @param playerJoinedUpate player joined update
     * @throws RemoteException remote exception
     */
    @Override
    public void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpate) throws RemoteException {
        playerJoinedUpate.execute(gameView);
    }

    /**
     * Method used to notify a command result update.
     * @param commandResultUpdate command result update
     */
    @Override
    public void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) {
        commandResultUpdate.execute(gameView);
    }

    /**
     * Method used to send a stall update.
     * @param stallUpdate stall update
     */
    @Override
    public void receiveStallUpdate(StallUpdate stallUpdate) {
        stallUpdate.execute(gameView);
    }

    /**
     * Method used to send a connection update.
     * @param connectionUpdate connection update
     */
    @Override
    public void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) {
        connectionUpdate.execute(gameView);
    }

    /**
     * Method used to send a card hand update.
     * @param cardHandUpdate card hand update
     */
    @Override
    public void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) {
        cardHandUpdate.execute(gameView);
    }

    /**
     * Method used to show the client an updated score.
     * @param scoreUpdate score update
     */
    @Override
    public void receiveScoreUpdate(ScoreUpdate scoreUpdate) {
        scoreUpdate.execute(gameView);
    }

    /**
     * Method used to show the client existing games.
     * @param existingGamesUpdate existing games update
     */
    @Override
    public void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) {
        existingGamesUpdate.execute(gameView);
    }

    /**
     * Method used to show the client a deck update.
     * @param deckUpdate deck update
     */
    @Override
    public void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException {
        deckUpdate.execute(gameView);
    }
}
