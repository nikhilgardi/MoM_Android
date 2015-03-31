package com.mom.apps.model.pbxpl.lic;

/**
 * Created by vaibhavsinha on 11/1/14.
 */
public class LicPolicy {
    public String PolicyNo;
    public Float TotalAmount;
    public String FrUnpaidPremiumDate;
    public String ToUnpaidPremiumDate;

    public String getFrUnpaidPremiumDate() {
        return FrUnpaidPremiumDate;
    }

    public void setFrUnpaidPremiumDate(String frUnpaidPremiumDate) {
        FrUnpaidPremiumDate = frUnpaidPremiumDate;
    }

    public String getToUnpaidPremiumDate() {
        return ToUnpaidPremiumDate;
    }

    public void setToUnpaidPremiumDate(String toUnpaidPremiumDate) {
        ToUnpaidPremiumDate = toUnpaidPremiumDate;
    }
}
