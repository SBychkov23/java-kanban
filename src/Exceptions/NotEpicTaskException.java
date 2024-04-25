package Exceptions;

public class NotEpicTaskException extends Exception{
    public NotEpicTaskException()  {
        super("Заданный таск не относится к типу Epic");
    }


}
