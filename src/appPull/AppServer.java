package appPull;

import controller.Servidor;

public class AppServer {
    public static void main(String[] args) {
        Servidor servidor= new Servidor();
        servidor.escuchar();
    }
}
