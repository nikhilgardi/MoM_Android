package com.mom.app.model.pbxpl;

import com.mom.app.model.StateName;

/**
 * Created by akanksha
 */
public class StateNameResult {

    public String stateName;

    public StateNameResult(){}
    public StateNameResult(String name){

        this.stateName   = name;
    }



    public String getName() {
        return stateName;
    }

    public String toString(){
        return stateName;
    }


    public StateName getState (){
        StateName statename = new StateName();

        statename.name = stateName;

        return statename;
    }
}
