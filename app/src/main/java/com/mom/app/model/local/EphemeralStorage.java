package com.mom.app.model.local;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public class EphemeralStorage extends LocalStorage {
    HashMap map = new HashMap();
    HashMap<String, String> beneficiaryList = new HashMap<String, String>();
    HashMap<String, String> branchListMap = new HashMap<String, String>();
    HashMap<String, String> hashMapRefundReceiptID = new HashMap<String, String>();
    HashMap<String, String> hashMapRefundAmount = new HashMap<String, String>();
    List<String> refundTxnDescriptionList = new ArrayList<String>();
    List<String> list = new ArrayList<String>();
    List<String> bankList = new ArrayList<String>();
    List<String> stateList = new ArrayList<String>();
    List<String> cityList  = new ArrayList<String>();
    List<String> branchList  = new ArrayList<String>();
    long id = -1;
    private EphemeralStorage(Context context){
        _context    = context;
    }
    private static EphemeralStorage _instance;

    public static EphemeralStorage getInstance(Context context){
        if(_instance == null){
            _instance       = new EphemeralStorage(context);
        }

        return _instance;
    }

    @Override
    public void clear(){
        map.clear();
    }
    @Override
    public void storeBoolean(String psKey, boolean pbValue){
        map.put(psKey, pbValue);
    }

    @Override
    public boolean getBoolean(String psKey, boolean pbDefault){
        Object val = map.get(psKey);
        return val == null ? pbDefault : (Boolean) val;
    }

    @Override
    public void storeString(String psKey, String psValue){
        map.put(psKey, psValue);
    }

    @Override
    public String getString(String psKey, String psDefault){
        Object val = map.get(psKey);
        return val == null ? psDefault : (String) val;
    }

    @Override
    public void storeBeneficiaryList(String psKey ,HashMap<String, String> beneficiaryListDetail){
       beneficiaryList = beneficiaryListDetail;
    }
    @Override
    public  String getBeneficiaryList( String beneficiaryAlias) {
        return beneficiaryList.get(beneficiaryAlias);

    }

    @Override
    public void storeRefundReceiptIdMap(String psKey ,HashMap<String, String> refundListDetail){
        hashMapRefundReceiptID = refundListDetail;
    }
    @Override
    public  String getRefundReceiptIdMap( String TransactionDescription) {
        return hashMapRefundReceiptID.get(TransactionDescription);

    }
    @Override
    public void storeRefundAmountMap(String psKey ,HashMap<String, String> refundAmountListDetail){
        hashMapRefundAmount = refundAmountListDetail;
    }

    @Override
    public  String getRefundMapAmount( String TransactionDescription) {
        return hashMapRefundAmount.get(TransactionDescription);

    }
    @Override
    public void storeRefundTransactionDescriptionList(String psKey ,List<String> refundTransactionDescriptionList){
        refundTxnDescriptionList = refundTransactionDescriptionList;
    }
    @Override
    public  List<String> getRefundTransactionDescriptionList() {
        return refundTxnDescriptionList;

    }

    @Override
    public void storeBankList(String psKey ,List<String> bankNameList){
        bankList = bankNameList;
    }
    @Override
    public  List<String> getBankList() {
        return bankList;

    }
    @Override
    public void storeStateList(String psKey ,List<String> stateNameList){
        stateList = stateNameList;
    }
    @Override
    public  List<String> getStateList() {
        return stateList;


    }
    @Override
    public void storeCityList(String psKey ,List<String> cityNameList){
        cityList = cityNameList;
    }
    @Override
    public  List<String> getCityList() {
        return cityList;
    }
    @Override
    public void storeBeneficiaryListDetail(String psKey ,List<String> beneficiaryListDetail){
        list = beneficiaryListDetail;
    }
    @Override
    public  List<String> getBeneficiaryListDetail( ) {
        return list;

    }

    @Override
    public void storeBranchList(String psKey ,HashMap<String, String> branchListDetail){
        branchListMap = branchListDetail;
    }
    @Override
    public  String getBranchList( String branchName) {
//        if(!branchName.equals(""))
//        {
//        return branchListMap.get(branchName);}
//        return "";
        return branchListMap.get(branchName);

    }
    @Override
    public void storeBranchListDetail(String psKey ,List<String> branchListDetail){
        branchList = branchListDetail;
    }
    @Override
    public  List<String> getBranchListDetail() {
        return branchList;

    }


    @Override
    public void storeFloat(String psKey, float pValue){
        map.put(psKey, pValue);
    }

    @Override
    public float getFloat(String psKey, float pfDefault){
        Object val = map.get(psKey);
        return val == null ? pfDefault : (Float) val;
    }

    @Override
    public int getInt(String psKey, int pnDefault) {
        Object val = map.get(psKey);
        return val == null ? pnDefault : (Integer) val;
    }

    @Override
    public void storeInt(String psKey, int pnValue) {
        map.put(psKey, pnValue);
    }

    @Override
    public void storeObject(String psKey, Object obj) {
        map.put(psKey, obj);
    }

    @Override
    public Object getObject(String psKey, Object pDefault) {
        Object val = map.get(psKey);
        return val == null ? pDefault : val;
    }
}
