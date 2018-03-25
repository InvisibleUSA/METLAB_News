package com.metlab.frontend.view;

import com.metlab.frontend.ICallbackFunction;
import com.metlab.frontend.view.forms.SysAdminLoginForm;



public interface IView
{
	void registerCallbackFunctions(
			ICallbackFunction userLoginEnterForm,
			ICallbackFunction userLogin,
			ICallbackFunction userRegisterEnterForm,
			ICallbackFunction userRegister,
			ICallbackFunction userLogout,
			ICallbackFunction sysAdminLoginEnterForm,
			ICallbackFunction sysAdminLogin,
			ICallbackFunction sysAdminLogout,
			ICallbackFunction createProfile
	);

	void showUserLoginForm();
	void showUserLoginInfo_NotRegistered();
	void showUserLoginInfo_PasswordIncorrect();
	void showUserLoginInfo_UnknownError();
	void showUserRegisterForm();
	void showUserRegisterInfo_RegistrationFailed();
	void showDashboardForm(String username, Boolean isAdmin);
	void showCreateProfileInfo_ProfileCreated();
	void showCreateProfileInfo_ProfileNotCreated();
	void showSysAdminLoginForm();
	void showSysAdminForm(String username);
}
