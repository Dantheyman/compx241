/**
 * This class implemints a Queue of strings. 
 * <br>
 * It uses a FIFO order for dequeing.
 * 
 * 
 * @author Daniel Jensen 
 * @since 19/5/2023
 * @version 1.0
 */
//1576516
public class Queue{

    private Node head;
    private int length = 0;

    /**
     * This method adds nodes to the queue.
     * 
     * @param x  String - The string value of the added Node  
     */
    public void enqueue(String x){
         
        x = x.trim();
        if (x.equals(""))
        {
            System.err.println("Usage: String must contain non-Whitespace Characters");
            return; 
        }
        Node insert = new Node(x);
        if (head == null){
            head = insert;
            length = 1;
            return;
        }

        Node end =findEnd(head);
        end.setNext(insert);
        length += 1;
    }

    /**
     * This method returns the Node from the Head of the queue and then removes it from the queue
     * @return Node - The Node at the head of the queue 
     */
    public Node dequeue(){
        if(head != null){
            Node result = head;
            head = head.getNext();
            length -= 1;
            return result;
        }
        return null;
    }

    /**
     * This method returns the string value of the head node. 
     * @return String - Strinf from head node 
     */
    public String  peek(){
        if (head==null)
        {
            return "";
        }
        return head.getString();
    }

    /**
     * Determines if the queue is empty or not.
     * @return boolean - whether queue is empty
     */
    public boolean isEmpty(){
        if (head == null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Finds the length of the queue
     * @return int - length of the queue 
     */
    public int length(){
        return length;
    }

    /**
     * Prints out all the string values for all the nodes to the standard output
     */
    public void dump(){
        Node cur = head;
        while (cur != null){
            System.out.println(cur.getString());
            cur = cur.getNext();
        }
    }

    private Node findEnd(Node cur){

        if (cur.getNext() != null){
            return findEnd(cur.getNext());
        }
        return cur;
    }
}