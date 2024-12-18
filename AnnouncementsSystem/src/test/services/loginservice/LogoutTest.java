package test.services.loginservice;

import com.google.gson.JsonObject;
import server.services.LoginService;

public class LogoutTest {

    public static void main(String[] args) {
        testSuccessfulLogout();
        testAlreadyLoggedOut();
        testMissingTokenField();
        testIncorrectToken();
        testNullToken();
    }

    public static void testSuccessfulLogout() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", "2459582");

        LoginService loginService = LoginTest.testSuccessfulCommonUserLogin();
        String responseJson = loginService.logout(jsonObject);

        assert(loginService.getLoggedInUserId() == null);
        assert(loginService.getLoggedInUserToken() == null);

        System.out.println("Ao tentar fazer logout, recebi: " + responseJson);
    }

    public static void testAlreadyLoggedOut() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", "2459582");

        LoginService loginService = LoginTest.testSuccessfulCommonUserLogin();
        loginService.logout(jsonObject);
        String responseJson = loginService.logout(jsonObject);
        System.out.println("Ao tentar fazer logout já deslogado, recebi: " + responseJson);
    }

    public static void testMissingTokenField() {
        JsonObject jsonObject = new JsonObject();

        LoginService loginService = LoginTest.testSuccessfulCommonUserLogin();
        String responseJson = loginService.logout(jsonObject);
        System.out.println("Ao tentar fazer logout sem token, recebi: " + responseJson);
    }

    public static void testIncorrectToken() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", "");

        LoginService loginService = LoginTest.testSuccessfulCommonUserLogin();
        String responseJson = loginService.logout(jsonObject);
        System.out.println("Ao tentar fazer logout com token incorreto, recebi: " + responseJson);
    }

    public static void testNullToken() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", (String) null);

        LoginService loginService = LoginTest.testSuccessfulCommonUserLogin();
        String responseJson = loginService.logout(jsonObject);
        System.out.println("Ao tentar fazer logout com token nulo, recebi: " + responseJson);
    }
}
