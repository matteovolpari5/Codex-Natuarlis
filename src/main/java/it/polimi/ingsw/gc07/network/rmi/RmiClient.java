package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements VirtualView {
    private final String nickname;
    private final VirtualServerGamesManager serverGamesManager;
    private VirtualServerGame serverGame;

    public RmiClient(VirtualServerGamesManager serverGamesManager, String nickname) throws RemoteException {
        this.nickname = nickname;
        this.serverGamesManager = serverGamesManager;
        this.serverGame = null;
    }

    @Override
    public void setServerGame(VirtualServerGame serverGame) throws RemoteException {
        this.serverGame = serverGame;
    }

    @Override
    public String getNickname() throws RemoteException {
        return nickname;
    }

    public void connectToGamesManager(boolean connectionType, boolean interfaceType) {
        try {
            serverGamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
        } catch (RemoteException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }


    //TODO
    // 1 probabilmente AddPlayerToPending non è qua (?)
    // 3 fare display games
    // 4 creare dei metodi / classi per richiedere le cose, così è un pastrugno

    public void runCliJoinGame() {
        boolean joiningGame = true;
        Scanner scan = new Scanner(System.in);
        while(joiningGame) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to join an existing game"); // JoinExistingGameCommand
            System.out.println("- w to join an new game"); // JoinNewGameCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "q":
                    // join existing game

                    // TODO display existing games

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
                    int gameId = scan.nextInt();
                    scan.nextLine();
                    try {
                        serverGamesManager.setAndExecuteCommand(new JoinExistingGameCommand(nickname, tokenColor, gameId));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    joiningGame = false;
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
                    joiningGame = false;
                    break;
                default:
                    System.out.println("The provided character doesn't refer to any action");
            }
        }
        // game joined
        connectToGameServer();
        runCliGame();
    }

    private void connectToGameServer() {
        try {
            serverGame.connect(this);
        }catch(RemoteException e) {
            //TODO manager remote exception
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void runCliGame() {
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("Insert a character to perform an action:");
            System.out.println("- q to write a private message"); // AddChatPrivateMessage
            System.out.println("- w to write a public message"); // AddChatPublicMessage
            System.out.println("- e to disconnect from the game"); // DisconnectPlayerCommand
            System.out.println("- r to draw a card from a deck"); // DrawDeckCardCommand
            System.out.println("- t to draw a face up card"); // DrawFaceUpCardCommand
            System.out.println("- y to place a card"); // PlaceCardCommand
            System.out.println("- u to place the starter card"); // PlaceStarterCardCommand
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
                        serverGame.setAndExecuteCommand(new AddChatPrivateMessageCommand(content, nickname, receiver));
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
                        serverGame.setAndExecuteCommand(new AddChatPublicMessageCommand(content, nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "e":
                    try {
                        serverGame.setAndExecuteCommand(new DisconnectPlayerCommand(nickname));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
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
                        serverGame.setAndExecuteCommand(new DrawDeckCardCommand(nickname, cardType));
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
                        serverGame.setAndExecuteCommand(new DrawFaceUpCardCommand(nickname, cardType, pos));
                    }catch (RemoteException e) {
                        // TODO gestire
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    break;
                case "y":
                    //TODO
                    // non posso conoscere la carta!
                    // possiamo modificare PlaceCardCommand per prendere la posizione della carta in mano per esempio
                    break;
                case "u":
                    System.out.println("Select 0 to place the starter card face up, 1 to place the starter card face down: ");
                    System.out.print("> ");
                    int wayInput = scan.nextInt();
                    scan.nextLine();
                    boolean way;
                    if(wayInput == 1) {
                        way = true;
                    }else if(wayInput == 0) {
                        way = false;
                    }else {
                        System.out.println("The provided value is not correct");
                        continue;
                    }
                    try {
                        serverGame.setAndExecuteCommand(new PlaceStarterCardCommand(nickname, way));
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
    }
}
