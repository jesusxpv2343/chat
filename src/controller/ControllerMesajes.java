package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import model.UsuarioDao;
import model.UsuarioVo;
import view.ViewLogin;
import view.ViewMensajes;

public class ControllerMesajes implements ActionListener, Runnable{
	 private Socket cliente;
	 private DataInputStream in;
	 private DataOutputStream out;
	 private int puerto = 2027;
	 private String host = "localhost";
	 private String mensajes = "";
	 
	 private ViewMensajes viewMensajes = new ViewMensajes();
	 private UsuarioVo usuarioVo = new UsuarioVo();
	 private UsuarioDao usuarioDao = new UsuarioDao();
	 
	 public ControllerMesajes(ViewMensajes viewMensajes, UsuarioDao usuarioDao){
		 this.viewMensajes = viewMensajes;
		 this.usuarioDao = new UsuarioDao();
		 
		 this.viewMensajes.setBtnAddMessage(this);
		 this.viewMensajes.setBtnLogOut(this);
		 this.viewMensajes.setBtnSalir(this);
		 this.viewMensajes.setTitle(usuarioVo.getNomUserArea());
		 viewMensajes();
		 
		 try {
	        cliente = new Socket(host,puerto);
	        in = new DataInputStream(cliente.getInputStream());
	        out = new DataOutputStream(cliente.getOutputStream());
	    } catch (Exception e) {
	        System.out.println(e.getMessage()+" error contructor ControllerMensajes");
	    }
		 
		 viewMensajes();
	 }

	@Override
	public void run() {
		try{
            while(true){
            	mensajes = in.readUTF();
               viewMensajes.setTxtAreapull(mensajes);
            }
        }catch(Exception e){
            System.out.println(e.getMessage()+" error run controllerMensajes");
        }		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object btn = arg0.getSource();
		if (btn == viewMensajes.getBtnAddMessage()) {
			String mensaje = viewMensajes.getTxtAreaMess();
			if(mensaje.equals("")){
				viewMensajes.setError("Mensaje vacio");
			}else {
				usuarioVo.setMensaje(mensaje);
				usuarioDao.hora(usuarioVo);
				usuarioDao.fecha(usuarioVo);
				if (usuarioDao.addMessaje(usuarioVo)) {
					enviarMsg(String.format("%s %s %s \n %s", usuarioVo.getNomUserArea(),usuarioVo.getFecha(),usuarioVo.getHora(),usuarioVo.getMensaje()));
				}
			}
		}else if (btn == viewMensajes.getBtnLogOut()){
			viewMensajes.dispose();
			ViewLogin viewLogin = new ViewLogin();
			ControllerLogin controllerLogin = new ControllerLogin(viewLogin, usuarioDao);
			viewLogin.setVisible(true);
			
		} else if (btn == viewMensajes.getBtnSalir()) {
			System.exit(0);		
		}
		
	}
	
	public void enviarMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void viewMensajes(){
		ArrayList<UsuarioVo> bd = usuarioDao.mensajesbd();
		for (int i = 0; i <  bd.size(); i++) {
			viewMensajes.setTxtAreapull(String.format("%s %s %s \n %s", bd.get(i).getNomUser(),bd.get(i).getFecha(),bd.get(i).getHora(),bd.get(i).getMensaje()));
		}
	}
}
