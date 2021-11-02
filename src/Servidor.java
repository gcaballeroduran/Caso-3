import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{

	public static final int PUERTO = 3401; //Puerto del servidor
	
	public static void main(String[] args) throws IOException {
		ServerSocket ss = null;
		boolean seguir = true;
		
		try {
			ss = new ServerSocket(PUERTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(seguir)
		{
			Socket socket = ss.accept();
			
			try {
				PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				escritor.println(); //Flujo de salida al repetidor
				lector.readLine(); //Flujo de entrada del repetidor
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}