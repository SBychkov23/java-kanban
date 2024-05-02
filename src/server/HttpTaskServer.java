package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import server.Handlers.*;
import service.FileBackedTasksManager;
import service.Managers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer httpServer;
    public FileBackedTasksManager manager;
    public HttpTaskServer() throws IOException {
        manager = new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));

    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    public void startServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("C:\\Users\\Sebatian Piererro\\FirstHomework\\third\\java-kanban\\data", "ManagerHistory.txt"));
        httpServer.createContext("/tasks", new BaseHttpHandler(manager));
        httpServer.createContext("/epics", new BaseHttpHandler(manager));
        httpServer.createContext("/subtasks", new BaseHttpHandler(manager));
        httpServer.createContext("/history", new historyOrPrioritizedHandler(manager));
        httpServer.createContext("/prioritized", new historyOrPrioritizedHandler(manager));
        httpServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
    public void stopServer()
    {
        httpServer.stop(0);
    }




}
