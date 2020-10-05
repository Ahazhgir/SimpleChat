import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {
	
	ArrayList clientOutputStream;
	
	public class ClientHandler implements Runnable {
		
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			String message;
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("Прочитать " + message);
					tellEveryone(message);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args){
		new VerySimpleChatServer().go();

	}
	
	public void go() {
		clientOutputStream = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStream.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("есть связь");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tellEveryone(String message) {
		Iterator it = clientOutputStream.iterator();
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
