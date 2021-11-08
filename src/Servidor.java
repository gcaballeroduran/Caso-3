import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Servidor extends Thread{

	private static final int PUERTO = 3401; //Puerto del servidor
	
	@Override
	public void run() {
		
		Map<String, String> map = new HashMap<String, String>();

		map.put("00", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa1");
		map.put("01", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa2");
		map.put("02", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa3");
		map.put("03", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa4");
		map.put("04", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa5");
		map.put("05", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa6");
		map.put("06", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa7");
		map.put("07", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa8");
		map.put("08", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa9");
		map.put("09", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa10");
		
		try {
			FileOutputStream archivo;
			ObjectOutputStream oos;
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			KeyPair keypair = generator.generateKeyPair();
			PublicKey publica = keypair.getPublic();
			PrivateKey privada = keypair.getPrivate();
			
			new File("PublicaServidor").delete();
			archivo = new FileOutputStream("PublicaServidor");
			oos = new ObjectOutputStream(archivo);
			oos.writeObject(publica);
			
			archivo.close();
			oos.close();
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
				PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String algoritmo = lector.readLine();
				if(algoritmo.equals("Simetrico"))
				{
					//Por hacer
				}
				else
				{
					
					String cifradoRepetidor = lector.readLine();
					byte[] AsimRepetidor = Encapsulamiento.Desencapsular(cifradoRepetidor);
					byte[] mensajeRepetidorByte = Asimetrico.descifrar(privada, AsimRepetidor);
					System.out.println(mensajeRepetidorByte);
					String mensajeRepetidor = new String(mensajeRepetidorByte);

					String envio = map.get(mensajeRepetidor);
					
					FileInputStream input = new FileInputStream("PublicaRepetidor");
					ObjectInputStream ois = new ObjectInputStream(input);
					PublicKey publicaRepetidor = null;
					while(input.available() > 0)
					{
				        publicaRepetidor = (PublicKey) ois.readObject();
					}
					input.close();
					ois.close();
					
					
					byte[] AsimParaRepetidor = Asimetrico.cifrar(publicaRepetidor, envio);
					String CapsulaParaRepetidor = Encapsulamiento.Encapsular(AsimParaRepetidor);
					escritor.println(CapsulaParaRepetidor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}