public class OrdStrList
{
 ///// Fields //////
    
 private int length;
 private StrNode head;

/////// constructor///////
 public  OrdStrList()
 {
    length= 0;
 }




/////////// Methods  /////////

    //prints each value 
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

    // inserts nodes so that the key values are in alpabetical order 
    public void insert(String s)
    {
        if (head==null)
        {
            head=new StrNode(s);
            length=1;
            return;   
        }
        

        StrNode insert = new StrNode(s);
        StrNode current=head;
        StrNode prev= null; 
        for (int i=0; i<length;i++)
        {
            
            int compare=current.key.compareTo(s);
            //if the current key value is before the new string alphabetically
            if (compare<0)
            {
                if(current.next==null)
                {
                    current.next=insert;
                    length+=1;
                    return;
                }
                prev=current;
                current=current.next;
                continue;
                
            }
            //if new string comes before current key 
         
                
            
            if (prev==null)
            {
                insert.next=head;
                head=insert;
                length+=1;
                return;
            }
            else 
            {
                prev.next=insert;
                insert.next=current;
                length+=1;
                return;
            }

            

        }


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
        if (head.key==s)
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

    //returns length value of list object
    public int length()
    {
        return length;
    }

    //returns true if no nodes have been added to the list
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