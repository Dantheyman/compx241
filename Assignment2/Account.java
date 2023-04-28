/*
Daniel Jensen 
1576516
Date Modified:28/4/23
Account
This class implements a account used in BankBST. 
*/
public class Account {
    //fields 
    private int key;
    private double balance;


    //public constructor 
    public Account(int key, double balance)
    {
        this.balance=balance;
        this.key=key;
    }

    //public Get and Sets 
    public int GetKey(){return key;}
    public double GetBalance(){return balance;}
    public void SetBalance(double balance){this.balance=balance;}
    
    
    
}
