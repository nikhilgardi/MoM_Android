package com.mom.app.model.pbxpl;

import com.mom.app.model.Beneficiary;
import com.mom.app.model.Transaction;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class BeneficiaryResult {
    public String id;
    public String name;

    public BeneficiaryResult(){}
    public BeneficiaryResult(String id, String name){
        this.id   = id;
        this.name   = name;
    }

    public String getCode() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return name;
    }


    public Beneficiary getBeneficiary (){
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.id   = id;
        beneficiary.name = name;

        return beneficiary;
    }
}
