package com.mom.app.model.local;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public interface IStorage {

    void storeBoolean(String psKey, boolean pbValue);
    boolean getBoolean(String psKey, boolean pbDefault);
    void storeString(String psKey, String psValue);
    void storeBeneficiaryList(String psKey , HashMap<String, String> beneficiaryListDetail);
    void storeBeneficiaryListDetail(String psKey ,List<String> beneficiaryListDetail);
    List<String> getBeneficiaryListDetail();
    String getString(String psKey, String psDefault);
    String getBeneficiaryList( String beneficiaryId);
    void storeFloat(String psKey, float pValue);
    float getFloat(String psKey, float pfDefault);
    void storeInt(String psKey, int pnValue);
    int getInt(String psKey, int pnDefault);
    void storeObject(String psKey, Object obj);
    Object getObject(String psKey, Object pDefault);
    void clear();
    void storeBankList(String psKey ,List<String> bankList);
    List<String> getBankList();
    void storeStateList(String psKey ,List<String> stateNameList);
    List<String> getStateList();
    void storeCityList(String psKey ,List<String> cityNameList);
    List<String> getCityList();
    void storeBranchList(String psKey ,HashMap<String, String> branchListDetail);
    String getBranchList( String branchName);
    void storeBranchListDetail(String psKey ,List<String> branchListDetail);
    List<String> getBranchListDetail();
    void storeRefundReceiptIdMap(String psKey ,HashMap<String, String> refundListDetail);
    String getRefundReceiptIdMap( String TransactionDescription);
    void storeRefundAmountMap(String psKey ,HashMap<String, String> refundAmountListDetail);
    String getRefundMapAmount( String TransactionDescription);
    void storeRefundTransactionDescriptionList(String psKey ,List<String> refundTransactionDescriptionList);
    List<String> getRefundTransactionDescriptionList();
}
