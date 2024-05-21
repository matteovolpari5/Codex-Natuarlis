package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
import it.polimi.ingsw.gc07.game_commands.*;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.network.PingSender;
import it.polimi.ingsw.gc07.updates.*;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.tui.Tui;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClient implements Client, PingSender {
    private String nickname;
    private final Socket mySocket;
    private GameView gameView;
    private final ObjectInputStream input;
    private VirtualSocketServer myServer;
    private boolean clientAlive;
    private Ui ui;
    private static final int maxMissedPongs = 3;
    private boolean pong;
    public SocketClient(Socket mySocket, boolean interfaceType) throws IOException {
        System.out.println("SC> costruttore");
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

        manageSetUp(output, interfaceType);

    }

    @Override
    public GameView getGameView() {
        return gameView;
    }

    private void manageSetUp(ObjectOutputStream output, boolean interfaceType){
        Scanner scan = new Scanner(System.in);
        String nickname;
        NicknameCheck check = null;
        do{
            System.out.println("Insert nickname: ");
            System.out.print("> ");
            nickname = scan.nextLine();
            try {
                output.writeObject(nickname);
            } catch (IOException e) {
                System.out.println("\nConnection failed.\n");
                setClientAlive(false);
                closeConnection();
                break;
            }
            try {
                check = (NicknameCheck) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("\nConnection failed.\n");
                setClientAlive(false);
                closeConnection();
                break;
            }
        }while(check.equals(NicknameCheck.EXISTING_NICKNAME));

        this.nickname = nickname;
        this.gameView = new GameView(nickname);
        this.myServer = new VirtualSocketServer(output);

        if(interfaceType)
            this.ui = new Gui();
        else
            this.ui = new Tui(nickname, this);
        this.gameView.addViewListener(ui);

        if(check.equals(NicknameCheck.NEW_NICKNAME)){
            connectToGamesManagerServer(interfaceType);
        } else{
            try {
                output.writeBoolean(interfaceType);
                output.reset();
                output.flush();
            } catch (IOException e) {
                System.out.println("\nConnection failed.\n");
                setClientAlive(false);
                closeConnection();
            }
            new Thread(this::manageReceivedUpdate).start();
            new Thread(this::startGamePing).start();
            new Thread(this::checkPong).start();
            runCliGame();
        }
    }

    private void connectToGamesManagerServer(boolean interfaceType) {
        System.out.println("SC> connectToGMS");
        try {
            myServer.setAndExecuteCommand(new AddPlayerToPendingCommand(nickname, false, interfaceType));
        } catch (RemoteException e) {
            System.out.println("\nConnection failed.\n");
            setClientAlive(false);
            closeConnection();
        }
        this.runCliJoinGame();
    }

    public void runCliJoinGame() {
        assert(ui != null);
        ui.runCliJoinGame();
        String result = null;
        try {
            result = (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nConnection failed.\n");
            setClientAlive(false);
            closeConnection();
        }
        if(result.equals("Game joined.")){
            new Thread(this::manageReceivedUpdate).start();
            // game joined
            new Thread(this::startGamePing).start();
            new Thread(this::checkPong).start();
            runCliGame();
        }else{
            if(result.equals("Display successful.")){
                Update update;
                try {
                    update = (Update) input.readObject();
                    update.execute(gameView);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("\nConnection failed.\n");
                    setClientAlive(false);
                    closeConnection();
                }
            }
            runCliJoinGame();
        }
    }

    private void manageReceivedUpdate() {
        System.out.println("SC-T> manageReceivedUpdate");
        Update update;
        while (true){ //TODO dalla documentazione non trovo un modo di utilizzare il risultato di readObject() come condizione del while, chiedere se così va bene
            try {
                //System.out.println("SC-T> ascolto");
                update = (Update) input.readObject();
                //System.out.println("SC-T> ho letto un update, lo eseguo");
                update.execute(gameView);
                synchronized (this){
                    pong = true;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("\nConnection failed.\n");
                setClientAlive(false);
                closeConnection();
            }
        }
    }

    private void closeConnection(){
        //TODO system exit (?)
        try{
            input.close();
            myServer.closeConnection();
            mySocket.close();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    public void runCliGame() {
        assert(ui != null);
        ui.runCliGame();
    }

    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) {
        try {
            myServer.setAndExecuteCommand(gamesManagerCommand);
        } catch (RemoteException e) {
            System.out.println("\nConnection failed.\n");
            setClientAlive(false);
            closeConnection();
        }
    }

    @Override
    public void setAndExecuteCommand(GameControllerCommand gameControllerCommand) {
        try {
            myServer.setAndExecuteCommand(gameControllerCommand);
        } catch (RemoteException e) {
            System.out.println("\nConnection failed.\n");
            setClientAlive(false);
            closeConnection();
        }
    }

    @Override
    public synchronized void setClientAlive(boolean isAlive) {
        this.clientAlive = isAlive;
    }

    @Override
    public synchronized boolean isClientAlive() {
        return clientAlive;
    }

    @Override
    public void startGamePing() {
        System.out.println("SC-T2> startGamePing");
        while(true) {
            boolean isAlive;
            synchronized (this) {
                isAlive = clientAlive;
            }
            if (!isAlive) {
                break;
            } else {
                try {
                    myServer.setAndExecuteCommand(new SendPingCommand(nickname));
                } catch (RemoteException e) {
                    System.out.println("\nConnection failed.\n");
                    setClientAlive(false);
                    closeConnection();
                    /*// connection failed
                    System.out.println("Connection failed. Press enter. - ping");
                    synchronized (this) {
                        clientAlive = false;
                    }*/
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

    /**
     * Method that checks if the client is receiving pongs from server.
     */
    private void checkPong() {
        int missedPong = 0;
        while(true){
            synchronized(this) {
                if(pong) {
                    missedPong = 0;
                }else {
                    missedPong ++;
                    if(missedPong >= maxMissedPongs) {
                        System.out.println("you lost the connection :(");
                        clientAlive = false;
                        break;
                    }
                }
                pong = false;

            }
            try {
                Thread.sleep(1000); // wait one second between two pong checks
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
