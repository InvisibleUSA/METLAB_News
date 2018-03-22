package com.metlab.view;

import com.metlab.controller.ICallbackFunction;



public interface IView
{
	void registerCallbackFunctions(ICallbackFunction userLogin,
	                               ICallbackFunction userLogout);

	void showUserLoginForm();
	void showDashboardForm(String username);
}
