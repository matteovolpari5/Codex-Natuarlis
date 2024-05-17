package it.polimi.ingsw.gc07.main;

import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.socket.SocketClient;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);

            String ip;
            do {
                System.out.println("Insert server IP: ");
                System.out.print("> ");
                ip = scan.nextLine();
            }while(!checkValidIp(ip));

            boolean wrongInput = true;
            boolean connectionType = false;
            while(wrongInput) {
                System.out.println("Insert connection type (1 = RMI, 0 = Socket)");
                System.out.print("> ");
                int connectionTypeInt;
                try {
                    connectionTypeInt = scan.nextInt();
                    scan.nextLine();
                    if(connectionTypeInt == 1) {
                        wrongInput = false;
                        connectionType = true;
                    }else if(connectionTypeInt == 0) {
                        wrongInput = false;
                        connectionType = false;
                    }else {
                        // wrong input already true
                        System.out.println("No such connection type");
                    }
                }catch(InputMismatchException e) {
                    scan.nextLine();
                    // wrong input already true
                }
            }

            wrongInput = true;
            boolean interfaceType = false;
            while(wrongInput) {
                System.out.println("Insert interface type(1 = GUI, 0 = Tui)");
                System.out.print("> ");
                int interfaceTypeInt;
                try {
                    interfaceTypeInt = scan.nextInt();
                    scan.nextLine();
                    if(interfaceTypeInt == 1) {
                        wrongInput = false;
                        interfaceType = true;
                    }else if(interfaceTypeInt == 0) {
                        wrongInput = false;
                        interfaceType = false;
                    }else {
                        // wrong input already true
                        System.out.println("No such interface type");
                    }
                }catch(InputMismatchException e) {
                    scan.nextLine();
                    // wrong input already true
                }
            }

            if(connectionType) {
                // RMI connection
                Registry registry = null;
                try {
                    registry = LocateRegistry.getRegistry(ip, 1234);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                VirtualServerGamesManager rmiServerGamesManager = (VirtualServerGamesManager) registry.lookup("VirtualServerGamesManager");

                String nickname;
                NicknameCheck check;
                do{
                    System.out.println("Insert nickname: ");
                    System.out.print("> ");
                    nickname = scan.nextLine();
                    check = rmiServerGamesManager.checkNickname(nickname);
                }while(nickname == null || nickname.isEmpty() || check.equals(NicknameCheck.EXISTING_NICKNAME));
                if(check.equals(NicknameCheck.NEW_NICKNAME)) {
                    RmiClient newRmiClient = new RmiClient(nickname, interfaceType, rmiServerGamesManager);
                    // add virtual view to rmiServerGamesManager
                    newRmiClient.connectToGamesManagerServer(connectionType, interfaceType);
                    newRmiClient.runCliJoinGame();
                }else {
                    // nickname of a reconnected player
                    RmiClient newRmiClient = new RmiClient(nickname, interfaceType, rmiServerGamesManager);
                    newRmiClient.reconnectPlayer(nickname, connectionType, interfaceType);
                }
            }else {
                // Socket connection
                System.out.println("SOCKET>");
                int port = 65000;
                Socket sc = new Socket(ip, port);
                new SocketClient(sc, interfaceType);
            }
        } catch (NotBoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkValidIp(String ip) {
        String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
