package com.mmpl.app;

public class Helpz {
	private static String Recharge_txtfield_mobileNumber;
	private static String Recharge_txtfield_amount;
	private static String Recharge_cb_operators;
	private static String RechargeRadiobutton_rb;
	// private static String Field_CustomerId;
	// private static String Field_CompanyId;
	private static String UserID;
	private static String UserCustomerId;
	private static String UserCompanyId;
	private static String UserRoleID;
	private static String UserAuthID;
	private static String UserWalletID;
	private static String UserFranchID;
	private static String UserMastDist;
	private static String UserAreaDist;
	private static String UserVAS01;
	private static String UserVAS02;
	private static String UserStatus;
	private static String Login_txtfield_mobileNumber;
	private static String Customer_txtfield_mobileNumber;
	private static String Consumer_txtfield_name;
	private static String Consumer_txtfield_Number;
	private static String Recharge_cb_sploperators;
	private static String Recharge_cb_sploperatorsNBE;
	private static String Recharge_InputREL_SBE_NBE_number;
	private static boolean LogoutVariable;
	private static String Finger_Print_Data;
	 private static String UserPartyTypeName;

	public void SetMyRechargeMobileNumber(String InputmobileNumber) {
		Recharge_txtfield_mobileNumber = InputmobileNumber;
	}

	public String GetMyRechargeMobileNumber() {
		return Recharge_txtfield_mobileNumber;
	}

	public void SetMyRelianceConsumerNumber(String InputconsumerNumber) {
		Recharge_txtfield_mobileNumber = InputconsumerNumber;
	}

	public String GetMyRelianceConsumerNumber() {
		return Recharge_txtfield_mobileNumber;
	}

	public void SetMyRechargeAmount(String txtfield_amount) {
		Recharge_txtfield_amount = txtfield_amount;
	}

	public String GetMyRechargeAmount() {
		return Recharge_txtfield_amount;
	}

	public void SetMyRechargeOperator(String cb_operators) {
		Recharge_cb_operators = cb_operators;
	}

	public String GetMyRechargeOperator() {
		return Recharge_cb_operators.toString();
	}

	public void SetMyRechargeType(String Radiobutton_rb) {
		RechargeRadiobutton_rb = Radiobutton_rb;
	}

	public String GetMyRechargeType() {
		return RechargeRadiobutton_rb;

	}

	public void SetMyLoginMobileNumber(String InputLoginmobileNumber) {
 		Login_txtfield_mobileNumber = InputLoginmobileNumber;
	}

	public String GetMyLoginMobileNumber() {
		return Login_txtfield_mobileNumber;
	}

	public void SetMyCustomerId(String CustomerId) {
		UserCustomerId = CustomerId;
	}

	public String GetMyCustomerId() {

		return UserCustomerId;
	}

	public void SetMyCompanyId(String CompanyId) {
		UserCompanyId = CompanyId;
	}

	public String GetMyCompanyId() {
		return UserCompanyId;
	}

	public void SetMyRoleID(String RoleId) {
		UserRoleID = RoleId;
	}

	public String GetMyRoleID() {
		return UserRoleID;
	}

	public void SetMyUserAuthID(String AuthId) {
		UserAuthID = AuthId;
	}

	public String GetMyUserAuthID() {
		return UserAuthID;
	}

	public void SetMyUserId(String UserId) {
		UserID = UserId;
	}

	public String GetMyUserId() {
		return UserID;
	}

	public void SetMyUserAreaDist(String UserAreaDistId) {
		UserAreaDist = UserAreaDistId;
	}

	public String GetMyUserAreaDist() {
		return UserAreaDist;
	}

	public void SetMyUserWalletID(String WalletId) {
		UserWalletID = WalletId;
	}

	public String GetMyUserWalletID() {
		return UserWalletID;
	}

	public void SetMyUserVAS01(String VAS01) {
		UserVAS01 = VAS01;
	}

	public String GetMyUserVAS01() {
		return UserVAS01;
	}

	public void SetMyUserVAS02(String VAS02) {
		UserVAS02 = VAS02;
	}

	public String GetMyUserVAS02() {
		return UserVAS02;
	}

	public void SetMyUserFranchID(String FranchId) {
		UserFranchID = FranchId;
	}

	public String GetMyUserFranchID() {
		return UserFranchID;
	}

	public void SetMyUserMastDist(String MastdisId) {
		UserMastDist = MastdisId;
	}

	public String GetMyUserMastDist() {
		return UserMastDist;
	}

	public void SetMyUserStatus(String UserStatusId) {
		UserStatus = UserStatusId;
	}

	public String GetMyUserStatus() {
		return UserStatus;
	}

	public boolean SetLogoutVariable(boolean b) {
		LogoutVariable = b;
		return b;
	}

	public boolean GetLogoutVariable() {
		return LogoutVariable;
	}

	public void SetCustomerMobileNumber(String InputmobileNumber) {
		Customer_txtfield_mobileNumber = InputmobileNumber;
	}

	public String GetCustomerMobileNumber() {
		return Customer_txtfield_mobileNumber;
	}

	public void SetConsumerNumber(String InputconsumerNumber) {
		Consumer_txtfield_Number = InputconsumerNumber;
	}

	public String GetConsumerNumber() {
		return Consumer_txtfield_Number;
	}

	public void SetConsumerName(String InputconsumerName) {
		Consumer_txtfield_name = InputconsumerName;
	}

	public String GetConsumerName() {
		return Consumer_txtfield_name;
	}

	public void SetMySpecialOperatorNBE(String cb_sploperators) {
		Recharge_cb_sploperatorsNBE = cb_sploperators;
	}

	public String GetMySpecialOperatorNBE() {
		return Recharge_cb_sploperatorsNBE.toString();
	}

	public void SetMySpecialOperator(String cb_sploperators) {
		Recharge_cb_sploperators = cb_sploperators;
	}

	public String GetMySpecialOperator() {
		return Recharge_cb_sploperators.toString();
	}

	public void SetMyREL_SBE_NBE(String InputREL_SBE_NBE_number) {
		Recharge_InputREL_SBE_NBE_number = InputREL_SBE_NBE_number;
	}

	public String GetMyREL_SBE_NBE() {
		return Recharge_InputREL_SBE_NBE_number;
	}

	public void SetUserFingerData(String FingerData) {
		Finger_Print_Data = FingerData;
	}

	public String GetUserFingerData() {
		return Finger_Print_Data;
	}
	 public void SetMyUserPartyName(String PartyTypeName) {
	        UserPartyTypeName = PartyTypeName;
	    }
	 public String GetMyUserPartyName() {

	        return UserPartyTypeName;
	    }

	// ////////........................ Urls For MOM Application.................................... /////////

	public String GetLoginUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getLoggedIn";
	}

	public String GetRMNDetails() {
		return "http://180.179.67.72/nokiaservice/DetailsByUserRMNCompID.aspx?UserRMN=";
	}

	public String GetBalanceAmount() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/getBalanceByCustomerId";
	}

	public String GetAllOperatorsMobile() {
		return "http://180.179.67.72/nokiaservice/getProductByProductType.aspx?OperatorType=1";
	}

	public String GetAllOperatorsDTH() {
		return "http://180.179.67.72/nokiaservice/getProductByProductType.aspx?OperatorType=2";
	}

	public String GetAllOperatorsBill() {
		return "http://180.179.67.72/nokiaservice/getProductByProductType.aspx?OperatorType=3";
	}

	public String Get_OperatorID() {
		return "http://180.179.67.72/nokiaservice/getOperatorIdByOperatorName.aspx?OperatorName=";
	}

	public String GetMobileRechargeUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/DoMobRecharge";
	}

	public String GetDTHRechargeUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/DoDTHRechargeV2";
	}

	public String GetBillPaymentUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Trans.asmx/doBillPayment";
	}

	public String GetBillAmountDataBestPayments() {
		return "http://180.179.67.72/bestpayments/billInquiry.ashx?AccountNumber=";
	}

	public String GetBillAmountDataRelianceEnergy() {
		return "http://180.179.67.72/RelianceEnergy/billEnquiry.ashx?CANumber=";
	}

	public String GetBillAmountDataMahaNagarGas() {
		return "http://180.179.67.72/mgl/billInquiry.aspx?CANumber=";
	}

	public String GetCheckValidateTPinUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/checkVallidTpin";
	}

	public String GetHistoryUrl() {
		return "http://180.179.67.72/nokiaservice/Lastfivetransactions.aspx?UserID=";
	}

	public String GetChangeMPinUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/ChangeM_Pin";
	}

	public String GetChangeTPinUrl() {
		return "http://msvc.money-on-mobile.net/WebServiceV3Client.asmx/ChangeT_Pin";
	}

}
