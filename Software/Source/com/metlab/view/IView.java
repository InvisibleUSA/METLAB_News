package com.metlab.view;

import com.metlab.controller.ICallbackFunction;



public interface IView
{
	static IView getInstance() {return null;}

	void registerCallbackFunctions(ICallbackFunction userLogin,
	                               ICallbackFunction userLogout);

	void showUserLoginForm();
	void showDashboardForm(String username);
}
