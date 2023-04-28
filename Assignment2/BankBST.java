/*
Daniel Jensen 
1576516
Date Modified:28/4/23
BankBST
This class implements a binary search tree. Each Node has an account class attached to it with 
a key value and balance. 
*/
public class BankBST {
    private Node root;

    public BankBST() {
    }

    // method used to add nodes to the BST
    public void addAccount(Account account) throws NullPointerException
     {
        //if account is null throw an exception
        if (account==null)
        {
            throw new NullPointerException("Value for account is null");   
        }
        //create a new node 
        Node insert = new Node(account);

        //if root is null make root eqaul to node 
        if (root == null) 
        {
            root = insert;
            return;
        }

        //else find node to insert 'under'
        Node parent = findPlace(account, root);
    
        //if parent is null than account already is in BST so return 
        if (parent==null)
        {
            return;
        }
        //insert node in to correct 'side' of parent node 
        else if (parent.account.GetKey() > account.GetKey()) 
        {
            parent.LeftNode = insert;
        } else if (parent.account.GetKey() < account.GetKey()) 
        {
            parent.RightNode = insert;
        } 
    }

   
    //public method used to remove Node from BST
    public void removeAccount(int key) throws Exception
    {
        //find Node 
        Node cur = find(key, root);
        //if node doesint exist throw exception
        if (cur==null)
        {
            throw new Exception("Value for key does not exist in BST");
        }
        // else call the private method
        else 
        {
           removeAccount(cur);
        }
       
    }

    //private method used to remove account 
    private void removeAccount(Node cur) 
    {   //finds parent node of current node 
        Node parent = findParent(cur, root);
        
        //if current node has no children set parents reference to null
        if ((cur.LeftNode == null) && (cur.RightNode == null))
        {
            if (parent.RightNode==cur)
            {
                parent.RightNode=null;
            }
            else 
            {
                parent.LeftNode=null;
            }     
        }
        //if current node has a child on "left" side 
        //ser parents reference to that node 
        else if  (cur.RightNode==null)
        {
            if (parent.RightNode==cur)
            {
                parent.RightNode=cur.LeftNode;
            }
            else 
            {
                parent.LeftNode=cur.LeftNode;

            }
        }
        //if current node has a child on "right" side 
        //ser parents reference to that node 
        else if (cur.LeftNode==null)
        {
           
            if (parent.RightNode==cur)
            {
                parent.RightNode=cur.RightNode;
            }
            else 
            {
                parent.LeftNode=cur.RightNode;

            }
        }
        // find suitable replacement 
        else
        {
            //find replacement node 
            Node replace = replacement(cur.RightNode);
            //remove replacement node from BST
            removeAccount(replace);
            //make currents node acount to the replacements account 
            cur.account=replace.account;
           
        }
       
       

    }

    //Finds the left most node of current 
    private Node replacement(Node cur)
    {
        // if current has no left node return current 
        if (cur.LeftNode==null)
        {
            return cur;
        }
        //call method again 
        else 
        {
           Node replace= replacement(cur.LeftNode);
           return replace;
        }
    }

    //private method used to determine the node that should be the parent node of the node 
    //that we are inserting  
    private Node findPlace(Account Node, Node cur) {
        // if key for the node we want to insert is greater than current node goto the
        // right node
        if (Node.GetKey() > cur.account.GetKey()) 
        {
            if (cur.RightNode == null) {
                return cur;
            } else {
                Node place = findPlace(Node, cur.RightNode);
                return place;
            }
        } 
        // if key for the node we want to insert is less than current node goto the
        // left node
        else if (Node.GetKey() < cur.account.GetKey()) 
        {
            if (cur.LeftNode == null) {
                return cur;
            } else {
                Node place = findPlace(Node, cur.LeftNode);
                return place;
            }
        } 
        //if equal to node we have duplicate to return null
        else 
        {
            return null;
        }
    }

    //public method that finds Node in BST 
    public Account find(int key) 
    {

        Node n = find(key, root);
        if (n==null)
        {
            return null;
        }
        return n.account;
    }

    //private method that finds Node in BST 
    private Node find(int key, Node cur) 
    {
        Node temp;
        if (cur==null)
        {
            return null;
        }
        if (cur.account.GetKey()==key)
        {
            return cur;
        }
        
        else if (cur.account.GetKey()>key)
        {
            temp=find(key,cur.LeftNode);
            if (temp!=null)
            {
                return temp;
            }
        }
        else if (cur.account.GetKey()<key)
        {
            temp=find (key,cur.RightNode);
            if (temp!=null)
            {
                return temp;
            }
        }
        return null;
    }

    //private method that finds the parent of a given Node    
    private Node findParent(Node child,Node cur) 
    {
        //if cuurent node equals null or child return null
        if (cur==null||cur==child)
        {
           return null;
        }
        //if either left or right node of current equals child return current 
        else if (cur.RightNode==child)
        {
            return cur;
        }
        else if (cur.LeftNode==child)
        {
            return cur; 
        }
        //else call findParent method again
        else
        {
             if (cur.account.GetKey()>child.account.GetKey())
            {
                return findParent(child,cur.LeftNode);
            }
            else 
            {
              return findParent(child,cur.RightNode);
            }
        }
    }  

    //public method that prints all key and balance values for all nodes in ascending order
   public void printTree()
   {
        //if tree has values else do nothing 
        if (root !=null)
        {
            System.out.println("RESULT");
            printTree(root);
            System.out.print("\n");
        }
   }

   //private method that prints all key and balance values for all nodes in ascending order
   private void printTree(Node cur)
   {
        //if current node is null do nothing 
       if (cur!=null)
       {
        //call method on left node first 
        printTree(cur.LeftNode);
        //print out values 
        System.out.print(cur.account.GetKey()+" "+cur.account.GetBalance()+"\n");
        //call method on right node 
        printTree(cur.RightNode);

       }
   } 

   //public method that prints path from root to selected key value
   public void printPath(int key)
   {
        //if BST has values continue 
        if (root !=null)
        {
            printPath(key,root);
        }
   } 
 
   //private method that prints path from root to selected node 
   private void printPath(int key,Node cur)
   {
        //if current is null then node does not exist so return null
        if (cur==null)
        {
            return;
        }
        //if current node key is equal to desired key print key value
        if (cur.account.GetKey()==key)
        {
            System.out.print(cur.account.GetKey()+" ");
        }
        //if current node key greater than desired key print key value 
        //then call method on left node 
        else if (cur.account.GetKey()>key)
        {
            System.out.print(cur.account.GetKey()+" ");
            printPath(key,cur.LeftNode);
        }
        //if current node key less than desired key print key value 
        //then call method on right node 
        else if (cur.account.GetKey()<key)
        {
            System.out.print(cur.account.GetKey()+" ");
            printPath(key,cur.RightNode);
        }
   }
   
   //Private class used as nodes for Bank BST
   private class Node 
   {
        //Fields 
        private Account account;
        private Node LeftNode;
        private Node RightNode;
        //Conctructor 
        public Node(Account a) 
        {
            account = a;
        }
    }

}