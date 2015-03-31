package com.mom.apps;

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
	 private static String RMNAccountBal;

	public static void SetMyRechargeMobileNumber(String InputmobileNumber) {
		Recharge_txtfield_mobileNumber = InputmobileNumber;
	}

	public static String GetMyRechargeMobileNumber() {
		return Recharge_txtfield_mobileNumber;
	}

	public static void SetMyRelianceConsumerNumber(String InputconsumerNumber) {
		Recharge_txtfield_mobileNumber = InputconsumerNumber;
	}

	public static String GetMyRelianceConsumerNumber() {
		return Recharge_txtfield_mobileNumber;
	}

	public static void SetMyRechargeAmount(String txtfield_amount) {
		Recharge_txtfield_amount = txtfield_amount;
	}

	public static String GetMyRechargeAmount() {
		return Recharge_txtfield_amount;
	}

	public static void SetMyRechargeOperator(String cb_operators) {
		Recharge_cb_operators = cb_operators;
	}

	public static String GetMyRechargeOperator() {
		return Recharge_cb_operators.toString();
	}

	public static void SetMyRechargeType(String Radiobutton_rb) {
		RechargeRadiobutton_rb = Radiobutton_rb;
	}

	public static String GetMyRechargeType() {
		return RechargeRadiobutton_rb;

	}

	public static void SetMyLoginMobileNumber(String InputLoginmobileNumber) {
 		Login_txtfield_mobileNumber = InputLoginmobileNumber;
	}

	public static String GetMyLoginMobileNumber() {
		return Login_txtfield_mobileNumber;
	}

	public static void SetMyCustomerId(String CustomerId) {
		UserCustomerId = CustomerId;
	}

	public static String GetMyCustomerId() {

		return UserCustomerId;
	}

	public static void SetMyCompanyId(String CompanyId) {
		UserCompanyId = CompanyId;
	}

	public static String GetMyCompanyId() {
		return UserCompanyId;
	}

	public static void SetMyRoleID(String RoleId) {
		UserRoleID = RoleId;
	}

	public static String GetMyRoleID() {
		return UserRoleID;
	}

	public static void SetMyUserAuthID(String AuthId) {
		UserAuthID = AuthId;
	}

	public static String GetMyUserAuthID() {
		return UserAuthID;
	}

	public static void SetMyUserId(String UserId) {
		UserID = UserId;
	}

	public static String GetMyUserId() {
		return UserID;
	}

	public static void SetMyUserAreaDist(String UserAreaDistId) {
		UserAreaDist = UserAreaDistId;
	}

	public String GetMyUserAreaDist() {
		return UserAreaDist;
	}

	public static void SetMyUserWalletID(String WalletId) {
		UserWalletID = WalletId;
	}

	public String GetMyUserWalletID() {
		return UserWalletID;
	}

	public static void SetMyUserVAS01(String VAS01) {
		UserVAS01 = VAS01;
	}

	public String GetMyUserVAS01() {
		return UserVAS01;
	}

	public static void SetMyUserVAS02(String VAS02) {
		UserVAS02 = VAS02;
	}

	public String GetMyUserVAS02() {
		return UserVAS02;
	}

	public static void SetMyUserFranchID(String FranchId) {
		UserFranchID = FranchId;
	}

	public String GetMyUserFranchID() {
		return UserFranchID;
	}

	public static void SetMyUserMastDist(String MastdisId) {
		UserMastDist = MastdisId;
	}

	public String GetMyUserMastDist() {
		return UserMastDist;
	}

	public static void SetMyUserStatus(String UserStatusId) {
		UserStatus = UserStatusId;
	}

	public static String GetMyUserStatus() {
		return UserStatus;
	}

	public static boolean SetLogoutVariable(boolean b) {
		LogoutVariable = b;
		return b;
	}

	public static boolean GetLogoutVariable() {
		return LogoutVariable;
	}

	public static void SetCustomerMobileNumber(String InputmobileNumber) {
		Customer_txtfield_mobileNumber = InputmobileNumber;
	}

	public static String GetCustomerMobileNumber() {
		return Customer_txtfield_mobileNumber;
	}

	public static void SetConsumerNumber(String InputconsumerNumber) {
		Consumer_txtfield_Number = InputconsumerNumber;
	}

	public static String GetConsumerNumber() {
		return Consumer_txtfield_Number;
	}

	public static void SetConsumerName(String InputconsumerName) {
		Consumer_txtfield_name = InputconsumerName;
	}

	public static String GetConsumerName() {
		return Consumer_txtfield_name;
	}

	public static void SetMySpecialOperatorNBE(String cb_sploperators) {
		Recharge_cb_sploperatorsNBE = cb_sploperators;
	}

	public static String GetMySpecialOperatorNBE() {
		return Recharge_cb_sploperatorsNBE.toString();
	}

	public static void SetMySpecialOperator(String cb_sploperators) {
		Recharge_cb_sploperators = cb_sploperators;
	}

	public static String GetMySpecialOperator() {
		return Recharge_cb_sploperators.toString();
	}

	public static void SetMyREL_SBE_NBE(String InputREL_SBE_NBE_number) {
		Recharge_InputREL_SBE_NBE_number = InputREL_SBE_NBE_number;
	}

	public static String GetMyREL_SBE_NBE() {
		return Recharge_InputREL_SBE_NBE_number;
	}

	public static void SetUserFingerData(String FingerData) {
		Finger_Print_Data = FingerData;
	}

	public String GetUserFingerData() {
		return Finger_Print_Data;
	}
	 public static void SetMyUserPartyName(String PartyTypeName) {
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
	public static void SetRMNAccountBal(String AccountBal) {
		RMNAccountBal = AccountBal;
	}

	public String GetRMNAccountBal() {
		return RMNAccountBal;
	}
}
