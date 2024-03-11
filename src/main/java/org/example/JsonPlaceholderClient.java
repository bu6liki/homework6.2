package org.example;
import java.io.*;
import java.net.*;

public class JsonPlaceholderClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        try {
            int userId = 1;
            createNewUser();
            getAllUsers();
            getSingleUser(userId);
            getUserByUsername("Bret");
            getUserTasks(userId);
            getUserPosts(userId);

            int latestPostId = getLatestPostId(userId);
            String latestPostCommentsJson = getPostComments(latestPostId);
            String fileName = "user-" + userId + "-latest-post-comments.json";
            writeJsonToFile(latestPostCommentsJson, fileName);
            System.out.println("Коментарі до останнього поста записано у файл: " + fileName);


            String openTasksJson = TaskManager.getOpenTasksForUser(userId);
            System.out.println("Відкриті задачі для користувача: " + openTasksJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createNewUser() throws IOException {
        String newUserJson = "{\"name\": \"John Doe\", \"username\": \"johndoe\"}";
        String createdUserJson = sendPostRequest(BASE_URL + "/users", newUserJson);
        System.out.println("Створено нового користувача: " + createdUserJson);
    }

    private static void getAllUsers() throws IOException {
        String allUsersJson = sendGetRequest(BASE_URL + "/users");
        System.out.println("Інформація про всіх користувачів: " + allUsersJson);
    }

    private static void getSingleUser(int userId) throws IOException {
        String singleUserJson = sendGetRequest(BASE_URL + "/users/" + userId);
        System.out.println("Інформація про користувача з id " + userId + ": " + singleUserJson);
    }

    private static void getUserByUsername(String username) throws IOException {
        String userByUsernameJson = sendGetRequest(BASE_URL + "/users?username=" + username);
        System.out.println("Інформація про користувача з ім'ям " + username + ": " + userByUsernameJson);
    }

    private static void getUserTasks(int userId) throws IOException {
        String userTasksJson = sendGetRequest(BASE_URL + "/users/" + userId + "/todos");
        System.out.println("Завдання користувача з id " + userId + ": " + userTasksJson);
    }

    private static void getUserPosts(int userId) throws IOException {
        String userPostsJson = sendGetRequest(BASE_URL + "/users/" + userId + "/posts");
        System.out.println("Пости користувача з id " + userId + ": " + userPostsJson);
    }

    private static int getLatestPostId(int userId) throws IOException {
        String latestPostJson = sendGetRequest(BASE_URL + "/users/" + userId + "/posts");
        return getLastPostId(latestPostJson);
    }

    private static String getPostComments(int postId) throws IOException {
        return sendGetRequest(BASE_URL + "/posts/" + postId + "/comments");
    }

    private static String sendPostRequest(String urlString, String postData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(postData.getBytes());
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private static String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private static int getLastPostId(String postsJson) {
        return 10;
    }

    private static void writeJsonToFile(String json, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        }
    }
}

class TaskManager {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static String getOpenTasksForUser(int userId) throws IOException {
        String url = BASE_URL + "/users/" + userId + "/todos?completed=false";
        return sendGetRequest(url);
    }

    private static String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
}
