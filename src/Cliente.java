import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente{
	public static final int PUERTO = 3400; //Puerto del repetidor
	public static final String SERVIDOR = "localhost";
	
	public static void main(String[] args) throws IOException {
		Socket socket = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		
		try {
			socket = new Socket(SERVIDOR, PUERTO);
			escritor = new PrintWriter(socket.getOutputStream(), true);
			lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		escritor.println(); //Flujo de salida al repetidor
		lector.readLine(); //Flujo de entrada del repetidor
		
		
		escritor.close();
		lector.close();
		socket.close();
	}
}