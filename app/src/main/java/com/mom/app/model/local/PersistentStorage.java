package com.mom.app.model.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.mom.app.utils.MiscUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhavsinha on 7/25/14.
 */
public class PersistentStorage extends LocalStorage {

    SharedPreferences _pref             = null;
    SharedPreferences.Editor _prefEditor = null;
    static PersistentStorage _instance;
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

    private PersistentStorage(Context context){
        _context    = context;
        _pref       = _context.getSharedPreferences(
                "com.mom.app.pref",
                Context.MODE_PRIVATE
        );
        _prefEditor = _pref.edit();
    }

    public static IStorage getInstance(Context context){
        if(_instance == null){
            _instance       = new PersistentStorage(context);
        }

        return _instance;
    }

    @Override
    public void clear(){
        _pref.edit().clear();
    }

    @Override
    public void storeBoolean(String psKey, boolean pbValue){
        _prefEditor.putBoolean(psKey, pbValue);
        _prefEditor.commit();
    }

    @Override
    public boolean getBoolean(String psKey, boolean pbDefault){
        return _pref.getBoolean(psKey, pbDefault);
    }

    @Override
    public void storeString(String psKey, String psValue){
        _prefEditor.putString(psKey, psValue);
        _prefEditor.commit();
    }

    @Override
    public String getString(String psKey, String psDefault){
        return _pref.getString(psKey, psDefault);
    }

    @Override
    public void storeFloat(String psKey, float pValue){
        _prefEditor.putFloat(psKey, pValue);
        _prefEditor.commit();
    }

    @Override
    public float getFloat(String psKey, float pfDefault) {
        return _pref.getFloat(psKey, pfDefault);
    }

    @Override
    public int getInt(String psKey, int pnDefault) {
        return _pref.getInt(psKey, pnDefault);
    }

    @Override
    public void storeInt(String psKey, int pnValue) {
        _prefEditor.putInt(psKey, pnValue);
        _prefEditor.commit();
    }

    @Override
    public void storeObject(String psKey, Object obj) {
        storeString(psKey, MiscUtils.toJson(obj));
    }

    @Override
    public Object getObject(String psKey, Object pDefault) {
        String sObj = getString(psKey, null);

        if(sObj == null || pDefault == null){
            return null;
        }

        return MiscUtils.fromJson(sObj, pDefault.getClass());
    }

    @Override
    public void storeBeneficiaryList(String psKey ,HashMap<String, String> beneficiaryListDetail){
        beneficiaryList = beneficiaryListDetail;
    }
    @Override
    public  String getBeneficiaryList( String beneficiaryId) {
        return beneficiaryList.get(beneficiaryId);

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
    public void storeBankList(String psKey ,List<String> bankList){
        bankList = bankList;
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
    public void storeBranchList(String psKey ,HashMap<String, String> branchListDetail){
        branchListMap = branchListDetail;
    }
    @Override
    public  String getBranchList( String branchName) {
        return branchListMap.get(branchName);

    }
    @Override
    public void storeBranchListDetail(String psKey ,List<String> branchListDetail){
        branchList = branchListDetail;
    }
    @Override
    public  List<String> getBranchListDetail( ) {
        return branchList;

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
}
