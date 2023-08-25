package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> taskHistory = new CustomLinkedList<>();
    HashMap<Integer, Node<Task>> historyHash = new HashMap<>();

    public class CustomLinkedList<T> {
        public Node<T> head;
        public Node<T> tail;
        private int size = 0;
        public CustomLinkedList()
        {
            head=null;
            tail=null;
        }
        public void add (Task task)
        {
           if(!historyHash.isEmpty()){
                if(historyHash.containsKey(task.getId()))
                {
                    remove(task.getId());
                    taskHistory.linkLast(task);
                }
            else
                taskHistory.linkLast(task);
            } else {
                taskHistory.linkLast(task);
            }


        }
        public void linkLast(T task){
            if (head == null) {
                head = new Node<>(null, task, tail);
            }
            else if (taskHistory.size==1)
            {
                tail= new Node<>(head, task, null);
                head.next=tail;
            }
            else
            {
                Node<T> oldTail =tail;
                tail= new Node<>(oldTail, task, null);
                oldTail.next=tail;
            }
                size++;// Реализуйте метод
        };
        public ArrayList<Task> getTasks(){
            ArrayList<Task> history = new ArrayList<>();
            Node<Task>element =taskHistory.head;
            for(int i=0; i<size; i++) {
               history.add(element.data);
               element=element.next;
            }
            return history;
        };
        private void removeNode(Node<T> task) {
            if (taskHistory.size==2) {
                if (task == head){
                    tail.prev = null;
                    head= tail;
                }
                else if (task==tail) {
                    head.next = null;
                }
            }
            else if (taskHistory.size>2)
            {
                Node<T>element = head;
                for(int i=0; i<taskHistory.size; i++)
                {
                    if(element.equals(task)) {
                        if (element.prev != null && element.next != null) {
                            element.prev.next = element.next;
                            element.next.prev=element.prev;
                            break;
                        }
                        else if (element.prev == null) {
                            head.next.prev=null;
                            head = head.next;
                            break;
                        }
                        else if (element.next==null)
                        {
                            tail.prev.next=null;
                            tail=tail.prev;
                            break;
                        }
                    }
                    else element = element.next;
                }
            }
            else if(task.equals(head))
            {
                head=null;
            }

        };
    }
    class Node <T> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(data, node.data) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data, next, prev);
        }
    }

    @Override
    public void remove(int id) {
        if (historyHash.containsKey(id)) {
            if (historyHash.get(id).data instanceof EpicTask) {
                EpicTask epic = (EpicTask) historyHash.get(id).data;
                taskHistory.removeNode(historyHash.get(id));
                historyHash.remove(id);
                taskHistory.size--;
                for (SubTask sub : epic.getSubtasksList().values()) {
                    if (historyHash.containsKey(sub.getId())) {
                        taskHistory.removeNode(historyHash.get(sub.getId()));
                        historyHash.remove(sub.getId());
                        taskHistory.size--;
                    }
                }
            } else {
                taskHistory.removeNode(historyHash.get(id));
                historyHash.remove(id);
                taskHistory.size--;
            }
        }
        else System.out.println("Такого таска нет в истории");
    }

    @Override
    public ArrayList<Task> getHistory() {
            return taskHistory.getTasks();
    }

    @Override
    public void addToHistory(Task task) {
        if(taskHistory.size<1){
            taskHistory.add(task);
            historyHash.put(task.getId(), taskHistory.head);
        }
        else {
            taskHistory.add(task);
            historyHash.put(task.getId(), taskHistory.tail);
        }

    }
}
