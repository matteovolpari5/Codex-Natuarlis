package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.controller.JoinExistingGameCommand;
import it.polimi.ingsw.gc07.controller.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualView {
    private final String nickname;
    private final VirtualServerGamesManager serverGamesManager;
    private VirtualServerGame serverGame;

    public RMIClient(VirtualServerGamesManager serverGamesManager, String nickname) throws RemoteException {
        this.nickname = nickname;
        this.serverGamesManager = serverGamesManager;
        this.serverGame = null;
    }

    public void setServerGame(VirtualServerGame serverGame) {
        this.serverGame = serverGame;
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
            System.out.println("- q to connect to games manager"); // AddPlayerToPendingCommand
            System.out.println("- w to join an existing game"); // JoinExistingGameCommand
            System.out.println("- e to join an new game"); // JoinNewGameCommand
            System.out.print("> ");
            String command = scan.nextLine();
            switch(command){
                case "q":
                    // connect to games manager
                    System.out.println("Insert connection type (1 = RMI, 0 = Socket)");
                    System.out.print("> ");
                    int connectionTypeInt = scan.nextInt();
                    scan.nextLine();
                    boolean connectionType;
                    if(connectionTypeInt == 1) {
                        connectionType = true;
                    }else if(connectionTypeInt == 0) {
                        connectionType = false;
                    }else {
                        System.out.println("No such connection type");
                        break;
                    }
                    System.out.println("Insert interface type(1 = GUI, 0 = TUI)");
                    System.out.print("> ");
                    int interfaceTypeInt = scan.nextInt();
                    scan.nextLine();
                    boolean interfaceType;
                    if(interfaceTypeInt == 1) {
                        interfaceType = true;
                    }else if(interfaceTypeInt == 0) {
                        interfaceType = false;
                    }else {
                        System.out.println("No such interface type");
                        break;
                    }
                    try {
                        serverGamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, connectionType, interfaceType));
                    } catch (RemoteException e) {
                        // TODO
                        throw new RuntimeException(e);
                    }
                    break;


                case "w":
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


                case "e":
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
        // TODO
    }
}
