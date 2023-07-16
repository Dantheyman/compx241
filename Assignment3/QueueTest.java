import java.io.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;

/**
 * This Class is designed to test the Queue class
 * @author Daniel Jensen 
 * @since 19/5/2023
 * @version 1.0
 */
//1576516
public class QueueTest {


    /**
     * This test tests that enqueue works properly when the queue is empty
     */
    @Test 
    @DisplayName("Test enqueue, When queue empty, dependent on peek()")
    public void emptyAddTest()
    {
        Queue queue = new Queue();
        queue.enqueue("hello");
        Assertions.assertEquals(queue.peek(),"hello"); 
    }

    /**
     * This method tests that enqueue correctly increments length 
     */
    @Test 
    @DisplayName("test enqueue,corectly increments length, dependent on length()")
    public void AddTestLength(){
        Queue queue = new Queue();
        queue.enqueue("hello");
        queue.enqueue("john");
        Assertions.assertEquals(queue.length(),2);
    }
    

    /**
     * This method tests that enqueue puts nodes in the correct order
     */
    @Test 
    @DisplayName("test enqueue,Puts nodes in correct order, dependent on dump")
    public void addTestNodes(){
        //Sets out system.out to go to the outCont variable 
        ByteArrayOutputStream outCont = new ByteArrayOutputStream();
        PrintStream originalOut=System.out;
        System.setOut(new PrintStream(outCont));

        Queue queue = new Queue();
        queue.enqueue("hello");
        queue.enqueue("john");
        queue.enqueue("please work");
        queue.enqueue("help");
        queue.dump();
        String expectedString="hello"+System.lineSeparator()+"john"+System.lineSeparator()+"please work"+System.lineSeparator()+"help";
       
        Assertions.assertEquals(expectedString,outCont.toString().trim());

        System.setOut(originalOut);
    }
    

    /**
     * This method tests that enqueue does not add an empty string to the queue 
     */
    @Test
    @DisplayName("test enqueue, when an empty string is given, dependent on length()")
    public void addTestEmptyString(){
        Queue queue = new Queue();
        queue.enqueue("    ");
        Assertions.assertEquals(queue.length(),0);
    }

    /**
     * This method tests that special string characters are not added to the queue  
     */
    @Test
    @DisplayName("test enqueue,when a special charcter is given as the string, dependent on length()")
    public void addSpecialStringTest(){
        Queue queue = new Queue();
        queue.enqueue("\n");
        Assertions.assertEquals(queue.length(),0);
    }

    /**
     * This method tests that dequeue does not decrement length when queue is already empty 
     */
    @Test 
    @DisplayName("test dequeue.  empty queue, make sure length remains zero")
    public void emptyRemoveTest(){
        Queue queue = new Queue();
        queue.dequeue();
        Assertions.assertEquals(queue.length(),0);
    }

    /**
     * This method tests that dequeue decrements length as expected when there are nodes in the queue 
     */
    @Test 
    @DisplayName("test dequeue,make sure length is decremented as expected , dependent on length()")
    public void removeHeadTestLength(){
        Queue queue = new Queue();
        queue.enqueue("john");
        queue.enqueue("harvey");
        queue.enqueue("bob");
        queue.dequeue();
        Assertions.assertEquals(queue.length(),2);
    }

    /**
     * This method tests that dequeue returns the correct value 
     */
    @Test
    @DisplayName("test dequeue, make sure it returns the correct value")
    public void returnDequeueTest(){
        Queue queue = new Queue();
        queue.enqueue("hello");
        queue.enqueue("mom");
        Node expectedNode = queue.dequeue();
        Assertions.assertEquals(expectedNode.getString(),"hello");
    }

    /**
     * This method tests that head is the expected value after dequeue is called  
     */
    @Test 
    @DisplayName("test dequeue,make sure head is the expected value, dependent on peek")
    public void removeHeadTest(){
        Queue queue = new Queue();
        queue.enqueue("john");
        queue.enqueue("harvey");
        queue.enqueue("ben");
        queue.dequeue();
        Assertions.assertEquals(queue.peek(),"harvey");
    }

    /**
     * This method tests that the head node is null after all nodes removed
     * /// need to fix this method 
     */
    @Test 
    @DisplayName("test dequeue, remove last node,dependent on peek")
    public void removeLastTest(){    
        ByteArrayOutputStream outCont = new ByteArrayOutputStream();
        PrintStream originalOut=System.out;
        System.setOut(new PrintStream(outCont));

    
        Queue queue = new Queue();
        queue.enqueue("david");
        queue.dequeue();
        queue.peek();
        Assertions.assertEquals(outCont.toString(),"");
        System.setOut(originalOut);
    }

    /**
     * This method tests that is empty returns true when queue is empty 
     */
    @Test
    @DisplayName("test isEmpty empty")
    public void emptyIsEmptyTest(){
        Queue q = new Queue();
        Assertions.assertEquals(q.isEmpty(),true);
    }

     /**
     * This method tests that is empty returns flase when queue is  not empty 
     */
    @Test 
    @DisplayName("test isEmpty not empty")
    public void notEmptyIsEmptyTest(){
        Queue q = new Queue();
        q.enqueue("head");
        Assertions.assertEquals(q.isEmpty(),false);
    }

    /**
     * This method tests that is empty returns true after modes have been added and then removed 
     */
    @Test
    @DisplayName("test isEmpty after node added then removed")
    public  void addThenRemoveIsEmptyTest(){

        Queue queue = new Queue();
        queue.enqueue("hi, mom");
        queue.dequeue();
        Assertions.assertEquals(queue.isEmpty(),true);
    }


    /**
     * This method tests that dump prints nothing when the queue is empty
     */
    @Test 
    @DisplayName("Test dump, empty queue")
    public void testEmptyDump(){
        ByteArrayOutputStream outCont = new ByteArrayOutputStream();
        PrintStream originalOut=System.out;
        System.setOut(new PrintStream(outCont));

        Queue q = new Queue();
        q.dump();
        Assertions.assertEquals("",outCont.toString().trim());

        System.setOut(originalOut);
    }

    /**
     * This method Tests that dump prints correctly when there are nodes in the queue 
     */
    @Test 
    @DisplayName("Test dump, nodes in queue")
    public void testDump(){
        ByteArrayOutputStream outCont = new ByteArrayOutputStream();
        PrintStream originalOut=System.out;
        System.setOut(new PrintStream(outCont));

        Queue q = new Queue();
        q.enqueue("hello");
        q.enqueue("world");
        q.dump();
        String newline = System.lineSeparator();
        String Expected = "hello"+newline+"world";
        Assertions.assertEquals(Expected,outCont.toString().trim());

        System.setOut(originalOut);
    }
    

    /**
     * This method tests length returns the correct length when queue empty
    */
    @Test 
    @DisplayName("test length, empty queue")
    public void emptyLengthTest(){
        Queue queue = new Queue();
        Assertions.assertEquals(queue.length(),0);
    }

    /**
     * This method test length when there are nodes in the queue 
     */
    @Test 
    @DisplayName("test length, nodes in queue")
    public void LengthTest(){
        Queue queue = new Queue();
        queue.enqueue("help");
        queue.enqueue("this is ");
        queue.enqueue("boring");
        Assertions.assertEquals(queue.length(),3);
    }

    /**
     * This method tests peek when queue is empty
     */
    @Test 
    @DisplayName("test peek, when empty")
    public void emptyPeekTest(){
        Queue queue = new Queue();
        Assertions.assertEquals(queue.peek(),"");

    }
    //This method Tests peek when it is not empty
    @Test 
    @DisplayName("test peek, when not empty")
    public void peekTest(){

        Queue queue = new Queue();
        queue.enqueue("i");
        queue.enqueue("think");
        queue.enqueue("im done");
        Assertions.assertEquals(queue.peek(),"i");
    }
}
