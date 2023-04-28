/*
Daniel Jensen 
1576516
Date Modified:28/4/23
XProcess
This class reads from a file and uses that info to carry out operations on a BST
too simulate bank account withdraws/depsoits/etc. 
*/

import java.io.BufferedReader;
import java.io.FileReader;

public class XProcess {

    private static BankBST bank=new BankBST(); 

    public static void main(String[] args)
    {
        //Prints and error if an incorect amount of agruments have been supplied 
        if (args.length!=1)
        {
            System.err.println("Usage: Xprocess <Filename>");
            return;
        }

        try
        {
            //creates a buffered reader and processes each line invidually 
            BufferedReader br = new BufferedReader(new FileReader(args[0].trim()));
            String line;
            while ((line = br.readLine()) != null)
            {
                process(line);
            }
            //once the processing is done prints the whole tree
            bank.printTree();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        
    }
    
    //this method processes each line and makes adjustments to the BankBST
    private static void process(String line)
    {
        //splits the line between spaces and if not 
        //what expected returns without doing anything
        String[] parts = line.split(" ");
        if (parts.length!=3)
        {
            return;
        }

        int key;
        String process;
        double amount;
        //parses the key, process and  balance values 
        try
        {
            key = Integer.parseInt(parts[0].trim());
            process= parts[1].trim();
            amount= Double.parseDouble(parts[2].trim());
        }
        //if we cant parse the strings return without doing anything 
        catch(Exception e )
        {
            return;
        }
   

        //calls the correct method depending on the process value else 
        // it does nothing 
        if(process.equals("d"))
        {
            deposit(key,amount);
        }
        else if (process.equals("w"))
        {
            withdraw(key,amount);
        }
        else if(process.equals("c"))
        {
            close(key);
        }

    }

    /*  this method adds to an accounts balance
     or creates one and appends to BST if it 
     doesint exit */
    private static void deposit(int key, double amount)
    {
        //finds accounnt or creates a new one. 
        Account a=bank.find(key);
        if (a==null)
        {
            a= new Account(key,0);
            bank.addAccount(a);
        }
        //increments balnce by stated amount
        double value=a.GetBalance();
        value+=amount;
        a.SetBalance(value);

        //prints the path to the Node then adds DEPOSIT to the end 
        bank.printPath(key);
        System.out.print("DEPOSIT\n");
    }

     /*  this method removes from an accounts balance
     or creates one and appends to BST if it 
     doesint exit */
    private static void withdraw(int key, double amount)
    {
        Account a=bank.find(key);
        if (a==null)
        {
            a= new Account(key,0);
            bank.addAccount(a);
        }
        
       
        //decrements the balance of the account
        double value=a.GetBalance();
        value-=amount;
        a.SetBalance(value);

        //prints the path to the Node then adds WITHDRAW to the end 
        bank.printPath(key);
        System.out.print("WITHDRAW\n");
    }

    //this method closes accounts 
    private static  void close(int key)
    {   
        //if no account exists dont do anything 
        if (bank.find(key)==null)
        {
            return;
        }

        //print path to Node and then add CLOSE 
        bank.printPath(key);
        System.out.print("CLOSE\n");

        //removes account from BST 
        try 
        {
            bank.removeAccount(key);
        }
        catch (Exception e )
        {
            System.err.println(e);
        }
        
    }
}
