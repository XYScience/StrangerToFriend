package com.science.strangertofriend.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * @description avos云服务操作
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class AVService {

	// 注册
	public static void signUp(String username, String password, String email,
			String gender, SignUpCallback signUpCallback) {
		AVUser user = new AVUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.put("gender", gender);
		user.signUpInBackground(signUpCallback);
	}

	public static void requestPasswordReset(String email,
			RequestPasswordResetCallback callback) {
		AVUser.requestPasswordResetInBackground(email, callback);
	}

	// 上传图片或头像
	public static void uploadImage(String usernameString, String emailString,
			Bitmap genderPhoto) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		genderPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] data = stream.toByteArray();
		AVFile imageFile = new AVFile("gender", data);
		try {
			imageFile.save();
		} catch (AVException e) {
			e.printStackTrace();
		}
		// Associate image with AVOS Cloud object
		AVObject po = new AVObject("Gender");
		po.put("username", usernameString);
		po.put("email", emailString);
		po.put("gender", imageFile);
		po.saveInBackground();
	}
}
