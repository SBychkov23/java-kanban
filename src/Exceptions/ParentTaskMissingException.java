package Exceptions;


public class ParentTaskMissingException extends Exception{
    public ParentTaskMissingException() {
        super("EpicTask не объявлен");
    }
}
