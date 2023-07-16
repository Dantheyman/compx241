/**
 * This class implemints a Node Holding a String value for use in a linked list/Queue  
 * 
 * @author Daniel Jensen 
 * @since 19/5/2023
 * @version 1.0
 */
//1576516
public class Node 
{
    private String value;
    private Node next;

    /**
     * Creates a Node Instance
     * @param s String value for the node to hold
     */
    public Node (String s)
    {
        value=s;
    }

    /**
     * Returns the string value of the Node
     * @return String - string value of the Node
     */
    public String getString(){
        return value;
    }

    /**
     * Returns the Node the current Node is pointing at 
     * @return Node - Node being pointed too
     */
    public Node getNext(){
        return next;
    }
    
    /**
     * Sets the Node the current Node is pointing too
     * @param n Node - Node to point to 
     */
    public void setNext(Node n ){
        next=n;
    }

}