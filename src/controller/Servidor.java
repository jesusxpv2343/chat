package controller;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Servidor {
    private final int puerto = 2027;
    private final int noConexiones = 20;
    private LinkedList<Socket> usuarios = new LinkedList<Socket>();
       
    public void escuchar(){
        try {
            ServerSocket servidor = new ServerSocket(puerto,noConexiones);
            while(true){
                System.out.println("Escuchando....");
                Socket cliente = servidor.accept();
                usuarios.add(cliente);
                Runnable  run = new HiloServidor(cliente,usuarios);
                Thread hilo = new Thread(run);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
   
}
