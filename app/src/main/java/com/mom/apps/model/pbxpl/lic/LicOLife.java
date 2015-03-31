package com.mom.apps.model.pbxpl.lic;

/**
 * Created by vaibhavsinha on 11/1/14.
 */
public class LicOLife {
    public LicParty Party;
    public LicPolicy Policy;


    public LicOLife()
    {

    }

    public LicParty getParty() {
        return Party;
    }

    public void setParty(LicParty party) {
        Party = party;
    }

    public LicPolicy getPolicy() {
        return Policy;
    }

    public void setPolicy(LicPolicy policy) {
        Policy = policy;
    }
}
