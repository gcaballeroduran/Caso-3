import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Cliente extends Thread{
	private static final int PUERTO = 3400; //Puerto del repetidor
	private static final String SERVIDOR = "localhost";
	private int id;
	private String algoritmo;
	private SecretKey llave;
	
	
	public Cliente(int pid, String palgoritmo)
	{
		id=pid;
		algoritmo=palgoritmo;
		this.start();
	}
	
	@Override
	public void run(){
		Socket socket = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		try {
			
			try {
				socket = new Socket(SERVIDOR, PUERTO);
				escritor = new PrintWriter(socket.getOutputStream(), true);
				lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			escritor.println();
			lector.readLine();
			
			escritor.close();
			lector.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws IOException {
		
	}
}