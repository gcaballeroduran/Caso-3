import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Repetidor{


	public static final int PUERTO = 3401; //Puerto del servidor
	public static final int PUERTO2 = 3400; //Puerto del repetidor
	public static final String SERVIDOR = "localhost";
	
	public static void main(String[] args) throws IOException {
		ServerSocket ss = null;
		boolean seguir = true;
		
		try {
			ss = new ServerSocket(PUERTO2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(seguir)
		{
			Socket socketCliente = ss.accept();
			
			try {
				PrintWriter escritorCliente = new PrintWriter(socketCliente.getOutputStream(), true);
				BufferedReader lectorCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
				
				escritorCliente.println(); //Flujo de salida al cliente
				lectorCliente.readLine(); //Flujo de entrada del cliente
				
				Socket socketServidor = null;
				PrintWriter escritorServidor = null;
				BufferedReader lectorServidor = null;
				
				try {
					socketServidor = new Socket(SERVIDOR, PUERTO);
					escritorServidor = new PrintWriter(socketServidor.getOutputStream(), true);
					lectorServidor = new BufferedReader(new InputStreamReader(socketServidor.getInputStream()));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				escritorServidor.println(); //Flujo de salida al servidor
				lectorServidor.readLine(); //Flujo de entrada del servidor
				
				
				
				escritorCliente.close();
				lectorCliente.close();
				socketCliente.close();
				escritorServidor.close();
				lectorServidor.close();
				socketServidor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
