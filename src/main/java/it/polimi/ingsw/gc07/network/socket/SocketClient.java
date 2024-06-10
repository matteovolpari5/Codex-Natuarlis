package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.network.PingSender;
import it.polimi.ingsw.gc07.network.SocketCommunication;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.utils.SafePrinter;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.tui.Tui;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class representing a client using Socket.
 */
public class SocketClient implements Client, PingSender {
    /**
     * Nickname of the player associated to the SocketClient.
     */
    private String nickname;
    /**
     * Socket used for communication.
     */
    private final Socket mySocket;
    /**
     * Player's local copy of the game model.
     */
    private GameView gameView;
    /**
     * Input channel used for communication.
     */
    private final ObjectInputStream input;
    /**
     * Abstraction of the server used in communication.
     */
    private VirtualSocketServer myServer;
    /**
     * Boolean that is true if the client connection is functioning.
     */
    private boolean clientAlive;
    /**
     * Player's ui.
     */
    private Ui ui;
    /**
     * Number of missed pongs to detect a disconnection.
     */
    private static final int maxMissedPongs = 3;
    /**
     * Boolean that is true if the server is on.
     */
    private boolean pong;

    /**
     * Constructor of SocketClient
     * @param mySocket socket connected to the server
     * @param interfaceType player's interface type
     * @throws IOException I/O exception
     */
    public SocketClient(Socket mySocket, boolean interfaceType) throws IOException {
        InputStream temp_input;
        OutputStream temp_output;

        this.mySocket = mySocket;

        this.ui = null;

        temp_output = this.mySocket.getOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(temp_output);
        output.flush();

        temp_input = this.mySocket.getInputStream();
        this.input = new ObjectInputStream(temp_input);

        this.clientAlive = true;
        this.pong = true;

        this.myServer = new VirtualSocketServer(output);

        manageSetUp(output, interfaceType);
    }

    /**
     * Getter method for gameView.
     * @return client's game views
     */
    @Override
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Method used to finalize the setup of the SocketClient
     * @param output output channel used for communication
     * @param interfaceType player's interface type
     */
    private void manageSetUp(ObjectOutputStream output, boolean interfaceType){
        Scanner scan = new Scanner(System.in);
        String nickname;
        NicknameCheck check = null;
        do{
            do {
                SafePrinter.println("Insert nickname: ");
                SafePrinter.print("> ");
                nickname = scan.nextLine();
            }while(nickname == null || nickname.isEmpty());
            try {
                output.writeObject(nickname);
            } catch (IOException e) {
                closeConnection();
                break;
            }
            try {
                check = (NicknameCheck) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                closeConnection();
                break;
            }
        }while(check.equals(NicknameCheck.EXISTING_NICKNAME));
        if(isClientAlive()){
            this.nickname = nickname;
            this.gameView = new GameView(nickname);
            if(interfaceType) {
                // run application on new thread
                new Thread(() -> {
                    Application.launch(Gui.class);
                    System.exit(0);
                }).start();
                this.ui = Gui.getGuiInstance();
                this.ui.setNickname(nickname);
                this.ui.setClient(this);
            } else {
                this.ui = new Tui(nickname, this);
            }
            this.gameView.addViewListener(ui);
            if(check != null && check.equals(NicknameCheck.NEW_NICKNAME)){
                try {
                    myServer.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, false, interfaceType));
                } catch (IOException e) {
                    //Network error during the initial communication for the set-up
                    closeConnection();
                    System.exit(-1);
                }
                ui.runJoinGameInterface();
                runJoinGameInterface();
            }else{
                try {
                    output.writeBoolean(interfaceType);
                    output.reset();
                    output.flush();
                } catch (IOException e) {
                    //Network error during the initial communication for the set-up
                    closeConnection();
                    System.exit(-1);
                }
                new Thread(this::manageReceivedUpdate).start();
                new Thread(this::startGamePing).start();
                new Thread(this::checkPong).start();
                runGameInterface();
            }
        }else{
            //Network error during the initial communication for the set-up
            System.exit(-1);
        }
    }

    /**
     * Method used to run the lobby ui.
     */
    @Override
    public void runJoinGameInterface() {
        if(isClientAlive()){
            SocketCommunication result;
            try {
                result = (SocketCommunication) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                closeConnection();
                result = null;
            }
            if(result!=null){
                if(result.equals(SocketCommunication.GAME_JOINED)){
                    // game joined
                    new Thread(this::manageReceivedUpdate).start();
                    new Thread(this::startGamePing).start();
                    new Thread(this::checkPong).start();
                    runGameInterface();
                }else{
                    if(result.equals(SocketCommunication.DISPLAY_SUCCESSFUL)){
                        Update update;
                        try {
                            update = (Update) input.readObject();
                            update.execute(gameView);
                            runJoinGameInterface();
                        } catch (IOException | ClassNotFoundException e) {
                            closeConnection();
                            //ask for reconnection
                            ui.runJoinGameInterface();
                        }
                    }else{
                        SafePrinter.println("\nCould not add you to the game, retry.\n");
                        ui.runJoinGameInterface();
                        runJoinGameInterface();
                    }
                }
            }else{
                //ask for reconnection
                ui.runJoinGameInterface();
            }
        }else{
            //ask for reconnection
            ui.runJoinGameInterface();
        }
    }

    /**
     * Method used to run the game ui.
     */
    @Override
    public void runGameInterface() {
        assert(ui != null);
        ui.runGameInterface();
    }

    /**
     * Method used to receive and execute updates form the server
     */
    private void manageReceivedUpdate() {
        Update update;
        while (true){
            if(!isClientAlive()){
                break;
            }
            try {
                update = (Update) input.readObject();
                update.execute(gameView);
                synchronized (this){
                    pong = true;
                }
            } catch (IOException | ClassNotFoundException e) {
                closeConnection();
                break;
            }
        }
    }

    /**
     * Method used to close the socket connection when a disconnection is detected
     */
    //TODO se la ui aspetta il comando e cade la connessione il client rimane vivo perché la ui sta ancora spettando l'input
    private synchronized void closeConnection(){
        if(isClientAlive()){
            while(!mySocket.isClosed()){
                try{
                    input.close();
                    myServer.closeConnection();
                    mySocket.close();
                }catch (IOException e){
                    e.printStackTrace(); //TODO da togliere prima della consegna
                    //throw new RuntimeException();
                }
            }
            SafePrinter.println("you lost the connection");
            this.clientAlive = false;

            // stop the Ui
            ui.stopUi();
        }
    }

    /**
     * Method to execute a games manager command.
     * @param gamesManagerCommand command to execute
     */
    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
        try {
            myServer.setAndExecuteCommand(gamesManagerCommand);
        } catch (IOException e) {
            closeConnection();
        }
    }

    /**
     * Method to execute a game command.
     * @param gameControllerCommand command to execute
     */
    @Override
    public void setAndExecuteCommand(GameControllerCommand gameControllerCommand) {
        try {
            myServer.setAndExecuteCommand(gameControllerCommand);
        } catch (IOException e) {
            closeConnection();
        }
    }

    /**
     * Setter method for clientAlive.
     * @param isAlive value of clientAlive
     */
    @Override
    public synchronized void setClientAlive(boolean isAlive) {
        if(isAlive){
            this.clientAlive = isAlive;
        }else{
            closeConnection();
        }
    }

    /**
     * Getter method for clientAlive.
     * @return value of clientAlive
     */
    @Override
    public synchronized boolean isClientAlive() {
        return clientAlive;
    }

    /**
     * Method used to periodically send pings to the server.
     */
    @Override
    public void startGamePing() {
        while(true) {
            if (!isClientAlive()) {
                break;
            } else {
                try {
                    myServer.setAndExecuteCommand(new SendPingCommand(nickname));
                } catch (IOException e) {
                    closeConnection();
                }
            }
            try {
                Thread.sleep(1000); // wait one second between two ping
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method that periodically checks if the client is receiving pongs from server.
     */
    private void checkPong() {
        int missedPong = 0;
        while(true){
            if(!isClientAlive()){
                break;
            }
            synchronized(this) {
                if(pong) {
                    missedPong = 0;
                }else {
                    missedPong ++;
                    if(missedPong >= maxMissedPongs) {
                        closeConnection();
                        break;
                    }
                }
                pong = false;

            }
            try {
                Thread.sleep(1000); // wait one second between two pong checks
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }
}
