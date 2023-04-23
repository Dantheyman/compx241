

class UnOrdStrList 
{
    ///// Fields //////
    
    private int length;
    private StrNode head;

   /////// constructor///////
    public  UnOrdStrList()
    {
        length= 0;
    }




    ///// Methods  /////

    //prints each value  to console
    public void dump()
    {
        StrNode next = head;
        while(!(next==null))
        {
            System.out.print(next.key+" ");
            next = next.next; 
        }
        System.out.print("\n");
        
    }

    


    // adds the string to the front of the list after checking if there is room for it 
    public void  add(String s)
    {
        if (head==null)
        {
            head=new StrNode(s);
            length=1;
            return;
            
        }
        StrNode temp= new StrNode(s);
        temp.next=head;
        head=temp;
        length+=1;

        
        
        
    }
   

    
    // checks the list to see if that string is present in it
    public boolean has(String s)
    {
        StrNode next = head;
        while(!(next==null))
        {
            
            if (next.key.equals(s))
            {
                return true;
            }
            next = next.next; 
             
        }
        return false;
        
    }
    // removes  node with key value equal to the string then makes the prevoius node point to
    // the node after it 
    public void remove(String s)
    {
        if (head.key.equals(s))
        {
            head=head.next;
            length-=1;
            return;
        }
        StrNode next = head.next;
        StrNode prev = head;
        while(!(next==null))
        {
            
            if (next.key.equals(s))
            {
               
                prev.next=next.next;
                length-=1;
                return;
            }
            prev=next;
            next = next.next; 
            
             
        }
    }
    // returns length value of list object
    public int length()
    {
       return length;
    }
    // returns true if no nodes are part of the list
    public boolean isEmpty()
    {
        int i = length();
        if (i==0)
        {
            return true;
        }
        return false; 
    }
    
    
    // node class for holding strings and refference to next node in list
    private class StrNode
    {
        private String key;
        private StrNode next; 

        public StrNode(String s )
        {
            key= s;
            next=null;
            
        }

    }
}

