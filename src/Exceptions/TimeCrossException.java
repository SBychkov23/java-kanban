package Exceptions;

public class TimeCrossException extends Exception{
    public TimeCrossException(String message) {
        super("Ошибка при добавлении Тask-а, пересечение времени с другим таском id-"+message);
    }
}
