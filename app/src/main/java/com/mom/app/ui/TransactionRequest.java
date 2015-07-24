package com.mom.app.ui;

import android.text.TextUtils;

import com.mom.app.identifier.PinType;
import com.mom.app.model.Operator;
import com.mom.app.utils.MiscUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * Created by vaibhavsinha on 10/9/14.
 */
public class TransactionRequest<T> implements Serializable {
    public static enum RequestStatus {
        PENDING(1), SUCCESSFUL(0), FAILED(-1), REPEAT_RECHARGE(-2),
        INVALID_NUMBER(-3), INVALID_AMOUNT(-4), INVALID_SYNTAX(-5),
        NOT_AUTHORIZED(-6), NOT_REGISTERED(-7);

        public int code;

        private RequestStatus(int code) {
            this.code = code;
        }

        public static RequestStatus getStatus(int code) {
            switch (code) {
                case 1:
                    return PENDING;
                case 0:
                    return SUCCESSFUL;
                case -1:
                    return FAILED;
                case -2:
                    return REPEAT_RECHARGE;
                case -3:
                    return INVALID_NUMBER;
                case -4:
                    return INVALID_SYNTAX;
                case -5:
                    return INVALID_SYNTAX;
                case -6:
                    return NOT_AUTHORIZED;
                case -7:
                    return NOT_REGISTERED;
            }

            return null;
        }
    }

    public Long id;
    String _type;
    float amount;
    String placeholder;
    String consumerId;
    String oldPin;
    String newPin;
    PinType pinType;
    String NewConfirmPin;
    String customerMobile;
    Operator operator;
    Date dateStarted;
    Date dateCompleted;
    RequestStatus status = RequestStatus.PENDING;
    boolean isCompleted = false;
    int responseCode;
    String stubButton_rb;
    String remoteResponse;

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    String tpin;


    String acMonth;
    String dueDate;
    String sdCode;
    String SOP;
    String FSA;
    String formatDueDate;

    T custom;
    String remoteId;

    //IMPS CUSTOMER CREATION
    String consumerNumber;
    String consumerName;
    String consumerDOB;
    String consumerEmailAddress;


    //IMPS ADD BENEFICIARY
    String beneficiaryName;
    String accountNumber;
    String ifscCode;
    String beneficiaryMobNo;


    //IMPS BENEFICIARY DETAILS
    String beneficiaryId;


    //IMPS ADD BENEFICIARY
    String sBeneficiaryName;

    //IMPS VERIFY PAYMENT

    String OTP;

    String customerNumber;


    public void setId(Long id) {
        this.id = id;
    }


    /**
     * This is only used in methods which are not operator or transactionType specific.
     * e.g. getBalance
     */
    public TransactionRequest() {
        this.id = new Random().nextLong();
    }

    /**
     * @param type   String of transaction type
     * @param amount amount of transaction
     */
    public TransactionRequest(String type, String consumerId, float amount) {
        _type = type;
        this.id = MiscUtils.getRandomLong();
        this.consumerId = consumerId;
        this.amount = amount;
        this.dateStarted = new Date();
    }

    public TransactionRequest(PinType pinType, String oldPin, String NewPin) {

        this.pinType = pinType;
        this.oldPin = oldPin;
        this.newPin = NewPin;
        this.dateStarted = new Date();
    }

    public TransactionRequest(String type, String consumerId, String customerMobile, float amount, Operator operator) {
        this.id = MiscUtils.getRandomLong();
        _type = type;
        this.consumerId = consumerId;
        this.customerMobile = customerMobile;
        this.amount = amount;
        this.operator = operator;
        this.dateStarted = new Date();
    }
    public TransactionRequest(String type, String consumerId, String customerMobile, float amount,String sOccasion,
                              String sDescription ,
                              String sSentTo ,
                              String sSentFrom,
                              String sEmailId,
                              Operator operator) {
        this.id = MiscUtils.getRandomLong();
        _type = type;
        this.consumerId = consumerId;
        this.customerMobile = customerMobile;
        this.amount = amount;
        this.operator = operator;
        this.dateStarted = new Date();
    }


    public TransactionRequest(String type, String sConsumerNumber, String sConsumerName, String sConsumerDOB, String sConsumerEmailAddress) {
        this.id = MiscUtils.getRandomLong();
        _type = type;
        this.consumerNumber = sConsumerNumber;
        this.consumerName = sConsumerName;
        this.consumerDOB = sConsumerDOB;
        this.consumerEmailAddress = sConsumerEmailAddress;

    }

    public TransactionRequest(String type, String sBeneficiaryName, String sAccountNumber, String sIfscCode, String sBeneficiaryMobNo, String sAmount) {
        this.id = MiscUtils.getRandomLong();
        _type = type;
        this.beneficiaryName = sBeneficiaryName;
        this.accountNumber = sAccountNumber;
        this.ifscCode = sIfscCode;
        this.beneficiaryMobNo = sBeneficiaryMobNo;

    }

    public TransactionRequest(String consumerId) {

        this.consumerId = consumerId;
        this.dateStarted = new Date();
    }

    public TransactionRequest(String type, String consumerId) {
        _type = type;
        this.id = MiscUtils.getRandomLong();
        this.consumerId = consumerId;
        this.dateStarted = new Date();
    }

    public TransactionRequest(String type, String sOTP, String sAccountNumber ,String sifscCode ) {
        _type = type;
        this.id = MiscUtils.getRandomLong();
        this.OTP           = OTP;
        this.accountNumber = accountNumber;
        this.ifscCode      = ifscCode;
        this.dateStarted   = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder(_type);

        if (operator != null) {
            sb.append(" / ").append(operator.name);
        }

        if (!TextUtils.isEmpty(consumerId)) {
            sb.append(" (").append(consumerId).append(") ");
        }

        return sb.toString();
    }

    public float getAmount() {
        return amount;
    }

    public String getPlaceHolder() {
        return placeholder;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setPin(PinType pin) {
        this.pinType = pinType;
    }

    public PinType getPin() {
        return pinType;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public String getOldPin() {
        return oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public boolean setStatus(int code) {
        RequestStatus statusFromCode = RequestStatus.getStatus(code);
        if (statusFromCode != null) {
            setStatus(statusFromCode);
            return true;
        }
        return false;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getRemoteResponse() {
        return remoteResponse;
    }

    public void setRemoteResponse(String remoteResponse) {
        this.remoteResponse = remoteResponse;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setPlaceHolder(String placeholder) {
        this.placeholder = placeholder;
    }

    public T getCustom() {
        return custom;
    }

    public void setCustom(T custom) {
        this.custom = custom;
    }

    public String getACMonth() {
        return acMonth;
    }

    public void setACMonth(String ACMonth) {
        this.acMonth = ACMonth;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        dueDate = dueDate;
    }

    public String getSDCode() {
        return sdCode;
    }

    public void setSDCode(String SDCode) {
        this.sdCode = SDCode;
    }

    public String getSOP() {
        return SOP;
    }

    public void setSOP(String SOP) {
        this.SOP = SOP;
    }

    public String getFSA() {
        return FSA;
    }

    public void setFSA(String FSA) {
        this.FSA = FSA;
    }

    public String getFormatDueDate() {
        return formatDueDate;
    }

    public void setFormatDueDate(String formatDueDate) {
        formatDueDate = formatDueDate;
    }

    public void setStubType(String Radiobutton_rb) {
        stubButton_rb = Radiobutton_rb;
    }

    public String getStubType() {
        return stubButton_rb;

    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String getConsumerEmailAddress() {
        return consumerEmailAddress;
    }

    public void setConsumerEmailAddress(String consumerEmailAddress) {
        this.consumerEmailAddress = consumerEmailAddress;
    }

    public String getConsumerDOB() {
        return consumerDOB;
    }

    public void setConsumerDOB(String consumerDOB) {
        this.consumerDOB = consumerDOB;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBeneficiaryMobNo() {
        return beneficiaryMobNo;
    }

    public void setBeneficiaryMobNo(String beneficiaryMobNo) {
        this.beneficiaryMobNo = beneficiaryMobNo;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getsBeneficiaryName() {
        return sBeneficiaryName;
    }

    public void setsBeneficiaryName(String sBeneficiaryName) {
        this.sBeneficiaryName = sBeneficiaryName;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }


}
