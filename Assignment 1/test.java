public class test {

    

    public static void main(String[] args)
    {
        OrdStrList list = new OrdStrList();
        list.insert("a");
        list.insert("b");
        list.insert("c");
        list.insert("f");
        list.insert("e");
        list.insert("d");
        
        list.dump();
        list.remove("yo");
        list.remove("e");
        list.dump();
        System.out.println(list.length());
        

      
    
        
     

     
        

        



    }


    
}
