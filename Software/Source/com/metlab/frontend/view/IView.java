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
			ICallbackFunction sysAdminLogout
	);

	void showUserLoginForm();
	void showUserRegisterForm();
	void showDashboardForm(String username);
	void showSysAdminLoginForm();
	void showSysAdminForm(String username);
}
