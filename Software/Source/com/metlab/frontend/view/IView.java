package com.metlab.frontend.view;

import com.metlab.frontend.ICallbackFunction;



public interface IView
{
	void registerCallbackFunctions(
			ICallbackFunction userEnterLoginForm,
			ICallbackFunction userLogin,
			ICallbackFunction userEnterRegisterForm,
			ICallbackFunction userRegister,
			ICallbackFunction userLogout
	);

	void showUserLoginForm();
	void showUserRegisterForm();
	void showDashboardForm(String username);
}
