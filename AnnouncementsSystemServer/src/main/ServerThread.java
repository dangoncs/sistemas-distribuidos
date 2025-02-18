package main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import responses.Response;
import services.AccountService;
import services.CategoryService;
import services.LoginService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private final Socket clientSocket;
	private final LoginService loginService;
	private PrintWriter out;
	private BufferedReader in;

	public ServerThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.loginService = new LoginService();
	}

	public ServerThread() {
		this.loginService = new LoginService();
		this.clientSocket = null;
		this.out = null;
		this.in = null;
	}
	
	public void run() {
		System.out.println("[INFO] Conexão estabelecida com novo cliente.");
	    try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
	    	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	
	    	String inputLine;
	    	while ((inputLine = in.readLine()) != null) {
				System.out.printf("[INFO] Recebido: %s%n", inputLine);
				Response response = processJson(inputLine);
				String responseJson = new Gson().toJson(response);
                System.out.printf("[INFO] Enviando: %s%n", responseJson);
				out.println(responseJson);

				if(loginService.isNoLongerLoggedIn())
					break;
	    	}
			
	    	closeConnection();
	    }
	    catch (IOException e) {
            System.err.printf("[AVISO] Problema na comunicação com um cliente (%s)%n", e.getLocalizedMessage());
	    	closeConnection();
	    }
	}

	public Response processJson(String inputLine) {
		JsonObject receivedJson;

		try {
			receivedJson = JsonParser.parseString(inputLine).getAsJsonObject();
		} catch (Exception e) {
			return new Response("400", "Invalid JSON");
		}

		JsonElement opElement = receivedJson.get("op");

		if(opElement == null || opElement.isJsonNull()) {
			return new Response("401","Op missing");
		}

		String operationCode = opElement.getAsString();

        return switch (operationCode) {
			case "1" -> AccountService.create(receivedJson);
			case "2" -> AccountService.read(receivedJson, loginService);
			case "3" -> AccountService.update(receivedJson, loginService);
			case "4" -> AccountService.delete(receivedJson, loginService);
			case "5" -> loginService.login(receivedJson);
			case "6" -> loginService.logout(receivedJson);
            case "7" -> CategoryService.create(receivedJson, loginService);
            case "8" -> CategoryService.read(receivedJson, loginService);
            case "9" -> CategoryService.update(receivedJson, loginService);
            case "10" -> CategoryService.delete(receivedJson, loginService);
			default -> new Response("402", "Invalid op");
		};
	}

	public void closeConnection() {
		try {
			if(out != null) out.close();
			if(in != null) in.close();
			if(clientSocket != null) clientSocket.close();
			System.out.println("[INFO] Conexão com um cliente fechada.");
		} catch (Exception e) {
            System.err.printf("[AVISO] Não foi possível fechar conexão corretamente com um cliente (%s)%n", e.getLocalizedMessage());
			System.err.println("[AVISO] Forçando encerramento da conexão.");
		}
	}
}
