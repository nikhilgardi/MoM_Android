package com.mom.app.model.pbxpl;


import com.mom.app.model.BankName;

/**
 * Created by akanksha
 */
public class BankNameResult {

    public String bankName;

    public BankNameResult(){}
    public BankNameResult( String name){

        this.bankName   = name;
    }



    public String getName() {
        return bankName;
    }

    public String toString(){
        return bankName;
    }


    public BankName getBank (){
        BankName bankname = new BankName();

        bankname.name = bankName;

        return bankname;
    }
}
