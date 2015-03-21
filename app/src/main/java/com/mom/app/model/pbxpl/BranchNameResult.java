package com.mom.app.model.pbxpl;

import com.mom.app.model.Beneficiary;
import com.mom.app.model.BranchName;

/**
 * Created by akanksha
 */
public class BranchNameResult {
    public String branchName;
    public String IFSCCode;

    public BranchNameResult(){}
    public BranchNameResult(String IFSCCode, String branchName){
        this.branchName   = branchName;
        this.IFSCCode   = IFSCCode;
    }

    public String getCode() {
        return branchName;
    }

    public String getName() {
        return IFSCCode;
    }

    public String toString(){
        return branchName;
    }


    public BranchName getBranch (){
        BranchName branchname = new BranchName();
        branchname.IFSCCode  = IFSCCode;
        branchname.branchName = branchName;

        return branchname;
    }
}
