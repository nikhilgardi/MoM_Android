package com.mom.app.identifier;

import com.mom.app.R;

/**
* Created by vaibhavsinha on 10/15/14.
*/
public enum TransactionType {
    MOBILE (R.string.action_mobileRecharge),
    DTH (R.string.action_dthRecharge),
    BILL_PAYMENT (R.string.action_billPayment),
    BALANCE_TRANSFER(R.string.action_balanceTransfer);

    public int transactionTypeStringId;

    TransactionType(int id){
        this.transactionTypeStringId = id;
    }
}
