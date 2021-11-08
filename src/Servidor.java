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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Servidor extends Thread{

	private static final int PUERTO = 3401; //Puerto del servidor
	
	@Override
	public void run() {
		
		Map<String, String> map = new HashMap<String, String>();

		map.put("00", "Código oculto número 100000000");
		map.put("01", "Código oculto número 100000001");
		map.put("02", "Código oculto número 100000002");
		map.put("03", "Código oculto número 100000003");
		map.put("04", "Código oculto número 100000004");
		map.put("05", "Código oculto número 100000005");
		map.put("06", "Código oculto número 100000006");
		map.put("07", "Código oculto número 100000007");
		map.put("08", "Código oculto número 100000008");
		map.put("09", "Código oculto número 100000009");
		
		try {
			
			// ASIMETRICO
			
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
			
			/// SIMETRICO
			
			new File("SimetricoServidor").delete();

			FileOutputStream archivoSimetrica;
			ObjectOutputStream ooss;
			archivoSimetrica = new FileOutputStream("SimetricoServidor");
			ooss = new ObjectOutputStream(archivoSimetrica);
			
			
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(128);
			SecretKey secretKey = keygen.generateKey();
			
		
			ooss.writeObject(secretKey);
			
			archivoSimetrica.close();
			ooss.close();
			
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
					long startTime = System.nanoTime();
					String cifradoRepetidor = lector.readLine();
					byte[] SimRepetidor = Encapsulamiento.Desencapsular(cifradoRepetidor);
					byte[] mensajeRepetidorByte = Simetrico.descifrar(secretKey, SimRepetidor);
					String mensajeRepetidor = new String(mensajeRepetidorByte);

					String envio = map.get(mensajeRepetidor);
					
					byte[] SimParaRepetidor = Simetrico.cifrar(secretKey, envio);
					String CapsulaParaRepetidor = Encapsulamiento.Encapsular(SimParaRepetidor);
					escritor.println(CapsulaParaRepetidor);
					
					long endTime = System.nanoTime();
					// Calcular tiempo 
					System.out.println("Mensaje descifrado por servidor "+( endTime -startTime)+ " nanosegundos");
					
				}
				else
				{
					long startTime = System.nanoTime();
					String cifradoRepetidor = lector.readLine();
					byte[] AsimRepetidor = Encapsulamiento.Desencapsular(cifradoRepetidor);
					byte[] mensajeRepetidorByte = Asimetrico.descifrar(privada, AsimRepetidor);
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
					
					long endTime = System.nanoTime();
					// Calcular tiempo 
					System.out.println("Mensaje descifrado por servidor "+( endTime -startTime)+ " nanosegundos");
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}