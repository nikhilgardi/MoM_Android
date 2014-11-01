package com.mom.app.model.pbxpl;

/**
 * Created by Pbx12 on 11/1/2014.
 */
public class Lic {

    TXLifeResponse response;
    TXLife responsex;
    public class TXLife {

        public TXLife() {

            TXLifeResponse tXLifeResponse=new TXLifeResponse(response);
        }
    }

    public class TXLifeResponse {

        public  TXLifeResponse(TXLifeResponse response )
        {

        }
        public Float TransInvAmount;
        public String TransType;

        public Float getTransInvAmount() {
            return TransInvAmount;
        }

        public void setTransInvAmount(Float transInvAmount) {
            TransInvAmount = transInvAmount;
        }

        public String toString(){
            return  Double.toString(TransInvAmount) ;
        }
    }
}