package Exceptions;

public class SubAlreadyInSubListException extends Exception{
    public SubAlreadyInSubListException(int subId, int parentId) {
        System.out.println(String.format("Саб id: %d уже представлен в саблисте эпика id:%d", subId, parentId));
    }
}
