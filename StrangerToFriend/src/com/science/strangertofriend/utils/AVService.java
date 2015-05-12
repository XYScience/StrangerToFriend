package com.science.strangertofriend.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
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

	// 个推ClientID
	public static void saveClientID(String username, String clientID) {
		AVObject avObject = new AVObject("ClientID");
		avObject.put("username", username);
		avObject.put("clientID", clientID);
		avObject.saveInBackground();
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

	public static void myLocation(String userObjectId, String username,
			String gender, double latitude, double longititude) {

		AVGeoPoint point = new AVGeoPoint(latitude, longititude);
		AVObject myPlace = new AVObject("MyLocation");
		myPlace.put("location", point);
		myPlace.put("userObjectId", userObjectId);
		myPlace.put("username", username);
		myPlace.put("gender", gender);
		myPlace.saveInBackground();

		// AVObject myLocation = new AVObject("MyLocation");
		// myLocation.put("userObjectId", userObjectId);
		// myLocation.put("username", username);
		// myLocation.put("gender", gender);
		// myLocation.put("latitude", latitude);
		// myLocation.put("longtitude", longititude);
		// myLocation.saveInBackground();
	}
}
