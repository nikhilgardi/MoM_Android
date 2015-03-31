package com.mom.apps.identifier;

import com.mom.apps.R;

/**
 * Created by vaibhavsinha on 7/21/14.
 */

public enum OperatorIdentifier {
    AIRTEL("AIRTEL", "2", R.drawable.airtel);

    public String name;
    public String id;
    public int logoId;

    private OperatorIdentifier(String name, String id, int logoId){
        this.name   = name;
        this.id     = id;
        this.logoId = logoId;
    }
}
