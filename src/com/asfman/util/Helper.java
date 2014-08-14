package com.asfman.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Helper {
	private static boolean bShowLog = true;
	public static final String TAG = "info";

	public static String getMetaData(Context context, String name) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = applicationInfo.metaData;
			String value = bundle.getString(name);
			showLog("meta", name + ": " + value);
			return value;
		} catch (NameNotFoundException e) {
			showErrorLog(e);
		}
		return "";
	}

	public static String getApplicationVersionString(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			return pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS).versionName;
		} catch (NameNotFoundException e) {
			showErrorLog(e);
			return "";
		}
	}

	public static void closeLog() {
		bShowLog = false;
	}

	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, String str, boolean longTime) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	public static void showToast(final Activity activity, final String str,
			String ui) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showToast(activity, str);

			}
		});
	}

	public static void showErrorToast(Activity activity, String retString,
			String ui) {
		try {
			JSONObject jsonObject = new JSONObject(retString);
			String message = jsonObject.getString("message");
			if (!TextUtils.isEmpty(message))
				showToast(activity, message, "ui");
		} catch (Exception e) {
			showErrorLog("showErrorToast:" + e.toString());
		}
	}

	public static void showErrorToast(Context context, String retString) {
		try {
			JSONObject jsonObject = new JSONObject(retString);
			String message = jsonObject.getString("message");
			if (!TextUtils.isEmpty(message))
				showToast(context, message);
		} catch (Exception e) {
			showErrorLog("showErrorToast:" + e.toString());
		}
	}

	public static String getStackTraceString(Exception e) {
		StackTraceElement[] stackTraceElement = e.getStackTrace();
		StringBuilder stringBuilder = new StringBuilder();
		for (StackTraceElement stackTraceElement2 : stackTraceElement) {
			stringBuilder.append(stackTraceElement2.toString() + "\n");
		}
		return stringBuilder.toString();
	}

	public static String getErrorLog(Context paramContext) {
		StringBuffer stringBuffer = new StringBuffer();
		String str1 = paramContext.getPackageName();
		ArrayList<String> localArrayList = new ArrayList<String>();
		localArrayList.add("logcat");
		localArrayList.add("-d");
		localArrayList.add("-v");
		localArrayList.add("raw");
		localArrayList.add("-s");
		localArrayList.add("AndroidRuntime:E");
		localArrayList.add("-p");
		localArrayList.add(str1);
		String[] adb = (String[]) localArrayList
				.toArray(new String[localArrayList.size()]);
		try {

			Process localProcess = Runtime.getRuntime().exec(adb);
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localProcess.getInputStream()), 1024);
			for (String str3 = localBufferedReader.readLine(); str3 != null; str3 = localBufferedReader
					.readLine()) {
				stringBuffer.append(str3);
			}
		} catch (Exception e) {
		}
		return stringBuffer.toString();
	}

	public static String showAdbLog() {
		String ret = "";
		try {
			Process mLogcatProc = null;
			BufferedReader reader = null;
			mLogcatProc = Runtime.getRuntime().exec(
					new String[] { "logcat", "-d" });

			reader = new BufferedReader(new InputStreamReader(
					mLogcatProc.getInputStream()));

			String line;
			final StringBuilder log = new StringBuilder();
			String separator = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				log.append(line);
				log.append(separator);
			}
			ret = log.toString();
		} catch (Exception e) {
		}
		return ret;
	}

	public static void showLog(String content) {
		showLog(TAG, content);
	}

	public static void showErrorLog(String content) {
		showErrorLog(TAG, content);
	}

	public static void showErrorLog(Exception e) {
		showErrorLog(TAG, e);
	}

	public static void showLog(String tag, String content) {
		if (!bShowLog || content == null)
			return;
		Log.i(tag, content);
	}

	public static void showErrorLog(String TAG, Exception e) {
		if (!bShowLog)
			return;
		// String stringBuilder = getStackTraceString(e);
		String stringBuilder = Log.getStackTraceString(e);
		showErrorLog(TAG, stringBuilder);
	}

	public static void showErrorLog(String tag, String content) {
		if (!bShowLog || content == null)
			return;
		Log.e(tag, content);
	}

	// final boolean scanAvailable =
	// isIntentAvailable(this,"com.google.zxing.client.android.SCAN");
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	// isAppInstalled(activity, "com.aizheke.aizheked");
	public static boolean isAppInstalled(Context context, String packageName) {
		Boolean installed = false;
		List<PackageInfo> packs = context.getPackageManager()
				.getInstalledPackages(PackageManager.PERMISSION_GRANTED);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if (p.versionName == null) {
				continue;
			}

			String pName = p.packageName;
			if (pName.contains(packageName)) {
				installed = true;
				showLog(TAG, p.packageName + " has been found~");
				break;
			}
		}
		return installed;
	}

	public static boolean isTopRunningApp(String packageName, Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if (packageName.equals(cn.getPackageName())) {
			return true;
		}
		return false;
	}

	public static boolean isAppInstalled2(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName,
					PackageManager.PERMISSION_GRANTED);
			return true;
		} catch (NameNotFoundException e) {
			showLog(TAG, "NameNotFoundException:" + e.getMessage());
		}
		return false;
	}

	public static void showRunningApps(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo2 : runningAppProcessInfo) {
			String[] arr = runningAppProcessInfo2.pkgList;
			for (String str : arr) {
				showLog(TAG, "packageName: " + str);
			}
		}

	}

	public static boolean isAppRunning(String packageName, Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo2 : runningAppProcessInfo) {
			String[] arr = runningAppProcessInfo2.pkgList;
			for (String str : arr) {
				if (str.contains(packageName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getShowDistance(String businessDistance) {
		if (businessDistance.compareTo("1.0") < 0) {
			int distance = (int) (Float.parseFloat(businessDistance) * 1000);
			if (distance == 0)
				businessDistance = "未知";
			else
				businessDistance = distance + "米";
		} else {
			DecimalFormat df = new DecimalFormat("0.0");
			businessDistance = df.format(Double.parseDouble(businessDistance))
					+ "公里";
		}
		return businessDistance;
	}

	public static String getShowDistance(Double businessDistance) {
		String retString = "";
		if (businessDistance < 0) {
			int distance = (int) (businessDistance * 1000);
			retString = distance + "米";
		} else {
			DecimalFormat df = new DecimalFormat("0.0");
			retString = df.format(businessDistance) + "公里";
		}
		return retString;
	}

	public static int getIntPref(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				key, 0);
	}

	public static void setIntPref(Context context, String key, int val) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt(key, val).commit();
	}

	public static String getStringPref(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, "");
	}

	public static void setStringPref(Context context, String key, String val) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString(key, val).commit();
	}

	public static boolean getBooleanPref(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, false);
	}

	public static boolean getBooleanPref(Context context, String key,
			boolean def) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, def);
	}

	public static void setBooleanPref(Context context, String key, Boolean val) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean(key, val).commit();
	}

	public static void removePref(Context context, String key) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.remove(key).commit();
	}

	public static void goIntent(Context fromContext, Class<?> goClass) {
		Intent intent = new Intent(fromContext, goClass);
		fromContext.startActivity(intent);
	}

	public static void goSingleTopIntent(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
	}

	public static boolean isNetworkAvailable(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getNetworkType(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return info.getTypeName();
				}
			}
		}
		return null;
	}

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

	public static void networkNotAvailable(WebView webView, String failingUrl) {
		String page = String
				.format("<html>"
						+ "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
						+ "<bod><br/><br/></br/>"
						+ "<center><div>"
						+ "<a href=\"%s\" style=\"font-size: 20pt;\">刷新</a><br/><br/>"
						+ "<span>可能是您的手机无网络连接，现在暂时无法连接爱折客服务器。请确认网络连接正常后点击“刷新”重试。</span>"
						+ "</div></center>" + "</bod></html>", failingUrl);
		webView.loadData(page, "text/html", "UTF-8");
		webView.clearHistory();
	}

	public static void networkNotAvailable(WebView webView) {
		String page = String
				.format("<html>"
						+ "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
						+ "<bod><br/><br/></br/>"
						+ "<center><div>"
						+ "<a href=\"%s\" style=\"font-size: 20pt;\">刷新</a><br/><br/>"
						+ "<span>可能是您的手机无网络连接，现在暂时无法连接爱折客服务器。请确认网络连接正常后点击“刷新”重试。</span>"
						+ "</div></center>" + "</bod></html>", "");
		webView.loadData(page, "text/html", "UTF-8");
		webView.clearHistory();
	}

	public static Intent emailIntent(String address, String subject, String body) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.setType("message/rfc822");
		return intent;
	}

	public static Intent smsIntent(String message) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body", message);
		sendIntent.setType("vnd.android-dir/mms-sms");
		return sendIntent;
	}

	public static Intent smsIntent(String message, String phoneNumber) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body", message);
		sendIntent.setType("vnd.android-dir/mms-sms");
		sendIntent.putExtra("address", phoneNumber);
		return sendIntent;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static void tel(Context context, String number) {
		try {
			Uri url = Uri.parse("tel:" + number);
			Intent dialIntent = new Intent(Intent.ACTION_DIAL, url);
			context.startActivity(dialIntent);
		} catch (Exception e) {
			showLog("tel parse:" + e.getMessage());
		}
	}

	public static void hideImm(View view, Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	public static void showImm(View view, Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		} catch (Exception e) {
		}
	}

	public static String getCheckInDate() {
		Calendar ctime = Calendar.getInstance();
		SimpleDateFormat fymd = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Date date = ctime.getTime();
		return fymd.format(date);
	}

	public static void linksHighlight(String couponText, TextView textView,
			Context context) {
		if (TextUtils.isEmpty(couponText))
			return;
		if (textView == null)
			return;
		try {
			SpannableStringBuilder couponTextBuilder = new SpannableStringBuilder(
					couponText);
			Pattern pattern = Pattern
					.compile("(https?)://([^/:\\s]+)(:\\d*)?([^#\\s]*)");
			Matcher matcher = pattern.matcher(couponText);
			while (matcher.find()) {
				couponTextBuilder.setSpan(new ForegroundColorSpan(0xff5c5cff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				couponTextBuilder.setSpan(new StyleSpan(Typeface.BOLD),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			textView.setText(couponTextBuilder);
		} catch (Exception e) {
			textView.setText(couponText);
		}
	}

	public static void displayGuideMask(Context context, int imageResourceId) {
		final WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		final ImageView imageView = new ImageView(context);
		imageView.setBackgroundResource(imageResourceId);
		WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
		localLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		localLayoutParams.format = PixelFormat.RGBA_8888;
		localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		localLayoutParams.width = LayoutParams.MATCH_PARENT;
		localLayoutParams.height = LayoutParams.MATCH_PARENT;

		windowManager.addView(imageView, localLayoutParams);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					windowManager.removeView(imageView);
				} catch (Exception e) {
				}
			}
		});
	}

	public static SparseArray<Long> dateHashMap;

	public static void time(String tag) {
		if (dateHashMap == null)
			dateHashMap = new SparseArray<Long>();
		dateHashMap.put(tag.hashCode(), System.currentTimeMillis());
	}

	public static void timeEnd(String tag) {
		int key = tag.hashCode();
		if (dateHashMap != null) {
			Long startTime = dateHashMap.get(key);
			if (startTime != null) {
				showLog("time", tag + ": 花费时间 "
						+ (System.currentTimeMillis() - startTime) + "ms");
				dateHashMap.remove(key);
			}
		}
	}

	public static String md5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int[] getLocationInWindow(View view) {
		int[] location = new int[2];
		view.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
		return location;
	}

	public static int[] getLocationOnScreen(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
		return location;
	}

	public static void showFullWindow(Activity activity, Boolean full) {
		Window window = activity.getWindow();
		if (full) {
			window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase(Locale.US);
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 获取等同于keytool -exportcert -alias openapi -keypass 654321 -keystore
	 * ./test.keystore -storepass 123456 > out.txt 然后md5
	 * out.txt，通过keystore来判断此用户是否有效
	 * 
	 * @param context
	 * @return
	 */
	public static String getSignature(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			byte[] certificate = pi.signatures[0].toByteArray();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] fingerprint = md.digest(certificate);
			String hexFingerprint = bytesToHexString(fingerprint);
			return hexFingerprint;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String flurryKey;

	public static String getFlurryKey(Context context) {
		if (!TextUtils.isEmpty(flurryKey)) {
			showLog("channel", "flurryKey: " + flurryKey);
			return flurryKey;
		}
		String ret = null;
		// 不再用flurry渠道，所以不用再去读取channel.jsons数据
		// try {
		// String dataJSONString = FileUtils.getFromAssets("channel.json",
		// context);
		// if (!TextUtils.isEmpty(dataJSONString)) {
		// showLog("channel", "发现channel.json文件: "
		// + dataJSONString);
		// JSONObject jsonObject = new JSONObject(dataJSONString);
		// String code = jsonObject.getString("flurry_key");
		// showLog("channel", "FLURRY_APPKEY: " + code);
		// ret = code;
		// }
		// } catch (Exception e) {
		// showErrorLog(e);
		// }
		if (TextUtils.isEmpty(ret)) {
			ret = getMetaData(context, "FLURRY_APPKEY");
			showLog("channel", "读取manifest里的FLURRY_APPKEY: " + ret);
		}
		flurryKey = ret;
		return ret;
	}

	private static String channelName;

	public static String getChannelName(Context context, String metaName) {
		if (!TextUtils.isEmpty(channelName)) {
			showLog("channel", "channelName: " + channelName);
			return channelName;
		}
		String ret = getMetaData(context, metaName);
		showLog("渠道：" + ret);
		channelName = ret;
		return ret;
	}

	public static boolean isAlipayInstalled(Context context) {
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);
		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);
			if (pI.packageName.equalsIgnoreCase("com.alipay.android.app"))
				return true;
		}

		return false;
	}

	public static TextView getTextView(Activity activity, int id) {
		return (TextView) getView(activity, id);
	}

	public static TextView getTextView(View view, int id) {
		return (TextView) getView(view, id);
	}

	public static EditText getEditTextView(Activity activity, int id) {
		return (EditText) getView(activity, id);
	}

	public static View getView(Activity activity, int id) {
		return activity.findViewById(id);
	}

	public static View getView(View view, int id) {
		return view.findViewById(id);
	}
	//计算字宽
	public static float GetTextWidth(String text, float Size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }	

	public static boolean isSinaInstalled(Context context) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("sinaweibo://splash"));
		return isIntentAvailable(context, intent);
	}
	
	public static String getMobileUUID(Context context) {
		String uuid = "";
		// 先获取mac
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		/* 获取mac地址 */
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null && info.getMacAddress() != null) {
				uuid = info.getMacAddress().replace(":", "");
			}
		}
		// 再加上imei
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		uuid = uuid + imei;
		if (uuid != null && uuid.length() > 64) {
			uuid = uuid.substring(0, 64);
		}
		return uuid;
	}	
}
