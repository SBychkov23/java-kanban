package server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class ServerEndpointsTests {
    HttpTaskServer testServer = new HttpTaskServer();

    HttpClient serverClient = HttpClient.newHttpClient();

    public ServerEndpointsTests() throws IOException {
    }

    @BeforeEach
     public  void ServerStartTest() throws IOException {
         testServer.startServer();
     }

     @Test
     public void getAllTasksHeaderTest() throws IOException, InterruptedException {
         URI uri = URI.create("http://localhost:8080/tasks");
         HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                 .GET()    // указываем HTTP-метод запроса
                 .uri(uri) // указываем адрес ресурса
                 .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                 .header("Accept", "application/json") // указываем заголовок Accept
                 .build(); // заканчиваем настройку и создаём ("строим") http-запрос
         HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
         HttpResponse<String> response = serverClient.send(request, handler);
         assertEquals(200, response.statusCode());
         System.out.println("Код ответа: " + response.statusCode());
         System.out.println("Тело ответа: " + response.body());
     }
    @Test
    public void getAllEpicTasksHeaderTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);
        assertEquals(200, response.statusCode());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
    @Test
    public void getAllSubsTasksHeaderTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);
        assertEquals(200, response.statusCode());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
    @Test
    public void getOneSubTaskHeaderTest() throws IOException, InterruptedException {

        URI uri = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);
        assertEquals(200, response.statusCode());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
    @Test
    public void getOneEpicTaskHeaderTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);

        assertEquals(200, response.statusCode());

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
    @Test
    public void getOneTaskHeaderTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);
        assertEquals(200, response.statusCode());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }
@Test void postOneTaskOfAllTypesNoIdHeaderTest() throws IOException, InterruptedException {

       testServer.manager.removeAllTasks();
    Task testTask = new Task("Обычный таск POST", String.format("Должен попасть в cохранение под номером %d", 1), Status.NEW, 60);

    EpicTask testEpicTask = new EpicTask("Проверочный эпик",String.format("Должен попасть в cохранение под номером %d и статус InProgress ", 2),
            Status.NEW);

    testTask.setStartTime("00:00 01.12.2013");
    testEpicTask.setStartTime("00:00 01.12.2014");

    Gson gson = new Gson();
    String taskJson = gson.toJson(testTask);
    String epicJson = gson.toJson(testEpicTask);

    URI url = URI.create("http://localhost:8080/tasks");
    HttpRequest request = HttpRequest.
            newBuilder().
            uri(url).
            POST(HttpRequest.BodyPublishers.ofString(taskJson)).
            build();
    HttpResponse<String> response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(201, response.statusCode());

    url = URI.create("http://localhost:8080/epics");
    request = HttpRequest.
            newBuilder().
            uri(url).
            POST(HttpRequest.BodyPublishers.ofString(epicJson)).
            build();
   response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(201, response.statusCode());

    SubTask testSubTask1 = new SubTask("Проверочный саб",
            String.format("Должен попасть в cохранение под номером %d", 3),
            Status.NEW, 60);
    SubTask testSubTask2 = new SubTask("Проверочный саб",
            String.format("Должен попасть в cохранение под номером %d", 4),
            Status.NEW, 60);
    testSubTask1.setParentId(2);
    testSubTask2.setParentId(2);
    testSubTask1.setStartTime("00:00 01.12.2015");
    testSubTask2.setStartTime("00:00 01.12.2016");
    String subJson = gson.toJson(testSubTask1);
    String subJson2 = gson.toJson(testSubTask2);
    url = URI.create("http://localhost:8080/subtasks");
    request = HttpRequest.
            newBuilder().
            uri(url).
            POST(HttpRequest.BodyPublishers.ofString(subJson)).
            build();
    response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(201, response.statusCode());
    url = URI.create("http://localhost:8080/subtasks");
    request = HttpRequest.
            newBuilder().
            uri(url).
            POST(HttpRequest.BodyPublishers.ofString(subJson2)).
            build();
    response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(201, response.statusCode());
}

@Test void postOneTaskOfAllTypesWithIdHeaderTest() throws IOException, InterruptedException {

        testServer.manager.removeAllTasks();
        Task testTask = new Task("Обычный таск POST", String.format("Должен попасть в cохранение под номером %d", 1), Status.NEW, 60);

        EpicTask testEpicTask = new EpicTask("Проверочный эпик",String.format("Должен попасть в cохранение под номером %d и статус InProgress ", 2),
                Status.NEW);

        testTask.setStartTime("00:00 01.12.2013");
        testEpicTask.setStartTime("00:00 01.12.2014");
        testTask.setId(testServer.manager.getNewID());
        testEpicTask.setId(testServer.manager.getNewID());

        Gson gson = new Gson();
        String taskJson = gson.toJson(testTask);
        String epicJson = gson.toJson(testEpicTask);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.
                newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(taskJson)).
                build();
        HttpResponse<String> response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        url = URI.create("http://localhost:8080/epics");
        request = HttpRequest.
                newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(epicJson)).
                build();
        response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        SubTask testSubTask1 = new SubTask("Проверочный саб",
                String.format("Должен попасть в cохранение под номером %d", 3),
                Status.NEW, 60);
        SubTask testSubTask2 = new SubTask("Проверочный саб",
                String.format("Должен попасть в cохранение под номером %d", 4),
                Status.NEW, 60);
        testSubTask1.setParentId(2);
        testSubTask2.setParentId(2);
        testSubTask1.setId(testServer.manager.getNewID());
        testSubTask2.setId(testServer.manager.getNewID());
        testSubTask1.setStartTime("00:00 01.12.2015");
        testSubTask2.setStartTime("00:00 01.12.2016");
        String subJson = gson.toJson(testSubTask1);
        String subJson2 = gson.toJson(testSubTask2);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.
                newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(subJson)).
                build();
        response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.
                newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(subJson2)).
                build();
        response = serverClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }
    @Test
    public void getAllSubsOfEpicTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/2/subtasks");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);

        assertEquals(200, response.statusCode());

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

    }
    @Test
    public void getDeleteOneTaskTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("id", "2")
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);

        assertEquals(201, response.statusCode());

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        postOneTaskOfAllTypesWithIdHeaderTest();
    }

    @Test
    public void getHistoryOfTasksTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/history");
        
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);

        assertEquals(200, response.statusCode());

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        postOneTaskOfAllTypesWithIdHeaderTest();
    }

    @Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/prioritized");

        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                .header("Accept", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = serverClient.send(request, handler);

        assertEquals(200, response.statusCode());

        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        postOneTaskOfAllTypesWithIdHeaderTest();
    }



    @AfterEach
     public void ServerStopTest() throws IOException {
         testServer.stopServer();
     }
}
