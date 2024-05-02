package server.Handlers;

import Exceptions.TimeCrossException;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import model.EpicTask;
import model.SubTask;
import model.Task;
import service.FileBackedTasksManager;
import service.Managers;

 public class BaseHttpHandler implements HttpHandler {
    protected final static String NOT_FOUND ="Указанынй объект не был найден";
    protected final static String CROSS_ERROR ="Обнаруженно пересечение с существующим таском";

    protected final static String REQUEST_SUCCESS ="Запрос успешно обработан";

    protected final static String UNKNOWN_REQUEST ="Неизвестный запрос: ";


    protected Gson responseJson;

    FileBackedTasksManager manager;

    public BaseHttpHandler(FileBackedTasksManager manager) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setPrettyPrinting();
        this.responseJson = gsonBuilder.create();
        this.manager = manager;
    }

    @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            Type classType = null;

        switch(exchange.getRequestURI().getPath().split("/")[1]) {
            case "tasks":
                classType = Task.class;
                break;
            case "subtasks":
                classType = SubTask.class;
                break;
            case "epics":
                classType = EpicTask.class;
                break;
            default:
                sendText(exchange, UNKNOWN_REQUEST+exchange.getRequestURI().getPath(), 404);
        }

            switch(method) {
                case "POST":
                    try {
                        callPostMethod(exchange, classType);
                    } catch (TimeCrossException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "GET":
                        callGetMethod(exchange, classType );
                    break;
                case "DELETE":
                    callDeleteMethod(exchange, classType);
                    break;
            default:
                sendText(exchange, "Данный метод не поддерживается", 405);
            }
            try {
                manager.save();
            }
            catch (Exception e)
            {
                sendText(exchange,"Произошла ошибка обработки изменений"+e.getMessage(), 500);
            }

        }
    public void callPostMethod(HttpExchange exchange,  Type classType) throws IOException, TimeCrossException {
        String path = exchange.getRequestURI().getPath();
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (path.split("/").length==2) {
            manager.setNewTaskOfType(classType, body);
            sendText(exchange,REQUEST_SUCCESS, 201);
        }
        else
        sendText(exchange, UNKNOWN_REQUEST+":   "+exchange.getRequestURI().getPath(), 404);
    }



    public void callGetMethod(HttpExchange exchange, Type classType) throws IOException {

                String path = exchange.getRequestURI().getPath();
        if (path.split("/").length==2) {
            Optional<ArrayList<Task>> tasksMap = Optional.ofNullable(manager.getSpecifiedByTypeTaskList(classType));
            if (tasksMap.isEmpty()||!tasksMap.isPresent())
               sendNotFound(exchange);
            else {
                sendText(exchange, responseJson.toJson(tasksMap.get()), 200);
            }
        }
        else if (path.split("/").length==3)
        {
            int id = Integer.parseInt(path.split("/")[2]);
            sendTaskByID(exchange,id);
        }
        else if (path.split("/").length==4)
        {

            int id = Integer.parseInt(path.split("/")[2]);

            if (classType.equals(manager.getTaskByID(id).getClass())) {
                Optional<List<SubTask>> subTasksMap = Optional.ofNullable(((EpicTask) (manager.getTaskByID(id)))
                        .getSubtasksMap().values()
                        .stream()
                        .collect(Collectors.toList()));
                if (subTasksMap.isEmpty()||!subTasksMap.isPresent())
                    sendNotFound(exchange);
                else
                    sendText(exchange,responseJson.toJson(subTasksMap.get()), 200);
            }
        }
        else {
            sendText(exchange, UNKNOWN_REQUEST + exchange.getRequestURI().getPath(), 404);
        }
    }
    public void callDeleteMethod(HttpExchange exchange, Type classType) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.split("/").length==2) {

            try {
                int id = Integer.parseInt(exchange.getRequestHeaders().getFirst("id").toString());
                if (manager.getTasksList().containsKey(id)) {
                    if (classType.equals(manager.getTaskByID(id).getClass())) {
                        manager.removeTaskByID(id);
                        sendText(exchange, REQUEST_SUCCESS, 201);
                    }
                    else sendNotFound(exchange);
                }
                else sendNotFound(exchange);
            }
            catch (NullPointerException e)
            {
               sendNotFound(exchange);
            }

        }
        else
            sendText(exchange, UNKNOWN_REQUEST+exchange.getRequestURI().getPath(), 404);

    }
    public  void sendTaskByID(HttpExchange exchange, int id) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            String responseString = responseJson.toJson(manager.getTaskByID(id));
            exchange.sendResponseHeaders(200, responseString.getBytes().length);
            os.write(responseString.getBytes());
        }
        exchange.close();
    }
  public  void sendText (HttpExchange exchange, String responseString, int responseCode) throws IOException {
     if(responseCode==200) {
         try (OutputStream os = exchange.getResponseBody()) {
             exchange.sendResponseHeaders(responseCode, responseString.getBytes().length);
             os.write(responseString.getBytes());
         }
         exchange.close();
     }
     else
     {
         try (OutputStream os = exchange.getResponseBody()) {
             exchange.sendResponseHeaders(responseCode, responseString.getBytes().length);
             os.write(responseString.getBytes());
         }
         exchange.close();
     }
  }
    public  void sendNotFound (HttpExchange exchange) throws IOException {
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.sendResponseHeaders(404, NOT_FOUND.getBytes().length+exchange.getRequestURI().getPath().length()+1);
            os.write((NOT_FOUND+" "+exchange.getRequestURI().getPath()).getBytes());
        }
        exchange.close();
    }
    public void  sendHasInteractions (HttpExchange exchange) throws IOException{
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(406, CROSS_ERROR.getBytes().length+exchange.getRequestURI().getPath().length()+1);
            os.write((CROSS_ERROR+" "+exchange.getRequestURI().getPath()).getBytes());
        }
        exchange.close();
    }
}
