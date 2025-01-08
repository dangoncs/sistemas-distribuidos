package test.services.loginservice;

import com.google.gson.JsonObject;
import server.services.LoginService;

public class LoginTest {

    public static void main(String[] ignoredArgs) {
        testSuccessfulCommonUserLogin();
        testSuccessfulAdminLogin();
        testUserAlreadyLoggedIn();
        testMissingFields();
        testInvalidCredentials();
    }

    public static LoginService testSuccessfulCommonUserLogin() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user", "2459582");
        jsonObject.addProperty("password", "1122");

        LoginService loginService = new LoginService();
        String responseJson = loginService.login(jsonObject);
        System.out.printf("Ao tentar logar como usuário comum, recebi: %s%n", responseJson);
        return loginService;
    }

    public static void testSuccessfulAdminLogin() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user", "admin01");
        jsonObject.addProperty("password", "pass");

        String responseJson = new LoginService().login(jsonObject);
        System.out.printf("Ao tentar logar como admin, recebi: %s%n", responseJson);
    }

    public static void testUserAlreadyLoggedIn() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user", "2459582");
        jsonObject.addProperty("password", "1122");

        LoginService loginService = new LoginService();
        loginService.login(jsonObject);
        String responseJson = loginService.login(jsonObject);
        System.out.printf("Ao tentar logar com alguém já logado, recebi: %s%n", responseJson);
    }

    public static void testMissingFields() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("password", "1234");

        LoginService loginService = new LoginService();
        String responseJson = loginService.login(jsonObject);
        System.out.printf("Ao tentar logar com campos faltando, recebi: %s%n", responseJson);
    }

    public static void testInvalidCredentials() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user", "2459582");
        jsonObject.addProperty("password", "password123");

        String responseJson = new LoginService().login(jsonObject);
        System.out.printf("Ao tentar logar com credenciais inválidas, recebi: %s%n", responseJson);
    }
}
