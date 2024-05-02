package server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import model.EpicTask;
import model.SubTask;
import model.Task;
import service.FileBackedTasksManager;

import java.io.IOException;
import java.io.OutputStream;

public class historyOrPrioritizedHandler extends BaseHttpHandler{

    public historyOrPrioritizedHandler(FileBackedTasksManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch(exchange.getRequestURI().getPath().split("/")[1]) {
            case "history":
                try (OutputStream os = exchange.getResponseBody()) {
                    String responseString = responseJson.toJson(manager.historyManager.getHistory());
                    exchange.sendResponseHeaders(200, responseString.getBytes().length);
                    os.write(responseString.getBytes());
                }
                catch (NullPointerException e){
                    sendNotFound(exchange);
                }
                exchange.close();
                break;
            case "prioritized":
                try (OutputStream os = exchange.getResponseBody()) {
                    String responseString = responseJson.toJson(manager.getPrioritizedTasks());
                    exchange.sendResponseHeaders(200, responseString.getBytes().length);
                    os.write(responseString.getBytes());
                }
                catch (NullPointerException e){
                    sendNotFound(exchange);
                }
                exchange.close();

                break;
            default:
                sendText(exchange, UNKNOWN_REQUEST+exchange.getRequestURI().getPath(), 404);
        }
    }
}
