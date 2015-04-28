package com.science.strangertofriend.utils;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

/**
 * @description avos�Ʒ������
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class AVService {

	// ע��
	public static void signUp(String username, String password, String email,
			SignUpCallback signUpCallback) {
		AVUser user = new AVUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.signUpInBackground(signUpCallback);
	}
}
