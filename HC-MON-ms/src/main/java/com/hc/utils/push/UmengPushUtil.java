package com.hc.utils.push;


import com.hc.utils.DateUtil;
import com.hc.utils.JsonUtil;
import com.hc.utils.push.android.*;
import com.hc.utils.push.ios.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class UmengPushUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(UmengPushUtil.class);
	private String appkey = null;
	private String appMasterSecret = null;

	private static String android_appkey = "5ab9a786f29d983a19000191";
	private static String android_appMasterSecret = "nawbhlnitcde7fq7snvu1bvcxnof1cvv";
	private static String iso_appkey = "5ab861de8f4a9d595f00002f";
	private static String iso_appMasterSecret = "ecosqe5a6xthv99itpzglocrmxqznjqj";

	private String timestamp = null;
	private PushClient client = new PushClient();

	public UmengPushUtil(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndroidBroadcast() throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
		broadcast.setTicker( "Android broadcast ticker");
		broadcast.setTitle(  "中文的title");
		broadcast.setText(   "Android broadcast text");
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		broadcast.setProductionMode();
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		client.send(broadcast);
	}

	public void sendAndroidUnicast(Map<String, Object> map) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast(android_appkey,android_appMasterSecret);

		// TODO Set your device token
		//unicast.setDeviceToken( "ArqGU2I7_qt1MZ_2CUNqcv_vLNIfTSlP9PXXTcOfMrjZ");  调试使用
		unicast.setDeviceToken( map.get("DeviceToken").toString());
		unicast.setTicker(map.get("title").toString());
		unicast.setTitle(map.get("title").toString());
		unicast.setText(map.get("content").toString());
		unicast.goAppAfterOpen();
		unicast.setAfterOpenAction(AndroidNotification.AfterOpenAction.go_custom);
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();

		// Set customized fields
		unicast.setExtraField("equipmentTypeNo", map.get("equipmentTypeNo").toString());//0001
		unicast.setExtraField("equipmentTypeName", map.get("equipmentTypeName").toString());//培养箱

		unicast.setExtraField("equipmentName", map.get("equipmentName").toString());//1号培养箱
		String inputDateTime = map.get("inputDateTime")!=null ? DateUtil.formatToString((Date)(map.get("inputDateTime")), DateUtil.DATE_HH_mm_FORMAT) : "";
		unicast.setExtraField("inputDateTime", inputDateTime);//1474537178000

		unicast.setExtraField("equipmentNo", map.get("equipmentNo").toString());//E1001
		unicast.setExtraField("instrumentNo", map.get("instrumentNo").toString());//S2001
		client.send(unicast);
	}

	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupcast groupcast = new AndroidGroupcast(appkey,appMasterSecret);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where":
		 *	{
    	 *		"and":
    	 *		[
      	 *			{"tag":"test"},
      	 *			{"tag":"Test"}
    	 *		]
		 *	}
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();
		testTag.put("tag", "test");
		TestTag.put("tag", "Test");
		tagArray.put(testTag);
		tagArray.put(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		groupcast.setFilter(filterJson);
		groupcast.setTicker( "Android groupcast ticker");
		groupcast.setTitle(  "中文的title");
		groupcast.setText(   "Android groupcast text");
		groupcast.goAppAfterOpen();
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		groupcast.setProductionMode();
		client.send(groupcast);
	}

	public void sendAndroidCustomizedcast() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setTicker( "Android customizedcast ticker");
		customizedcast.setTitle(  "中文的title");
		customizedcast.setText(   "Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	public void sendAndroidCustomizedcastFile() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then
		// use file_id to send customized notification.
		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb"+"\n"+"alias");
		customizedcast.setFileId(fileId, "alias_type");
		customizedcast.setTicker( "Android customizedcast ticker");
		customizedcast.setTitle(  "中文的title");
		customizedcast.setText(   "Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	public void sendAndroidFilecast() throws Exception {
		AndroidFilecast filecast = new AndroidFilecast(appkey,appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb");
		filecast.setFileId( fileId);
		filecast.setTicker( "Android filecast ticker");
		filecast.setTitle(  "中文的title");
		filecast.setText(   "Android filecast text");
		filecast.goAppAfterOpen();
		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		client.send(filecast);
	}

	public void sendIOSBroadcast() throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(appkey,appMasterSecret);

		broadcast.setAlert("IOS 广播测试");
		broadcast.setBadge( 0);
		broadcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		broadcast.setTestMode();
		// Set customized fields
		broadcast.setCustomizedField("test", "helloworld");
		client.send(broadcast);
	}

	public void sendIOSUnicast(Map<String, Object> map) throws Exception {
		IOSUnicast unicast = new IOSUnicast(iso_appkey, iso_appMasterSecret);
		// TODO Set your device token
		unicast.setDeviceToken( map.get("DeviceToken").toString());
		//unicast.setDeviceToken( "cbe485547ed1c22fcb8c6101c0f864508b4434926a10537ee1737efffa87500c"); 调试使用
		unicast.setAlert(map.get("title").toString());
		unicast.setBadge(1);
		unicast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		unicast.setProductionMode(true);
//		unicast.setTestMode();
		// Set customized fields

		unicast.setCustomizedField("title", map.get("title").toString());//0001
		unicast.setCustomizedField("content", map.get("content").toString());//0001

		unicast.setCustomizedField("equipmentTypeNo", map.get("equipmentTypeNo").toString());//0001
		unicast.setCustomizedField("equipmentTypeName", map.get("equipmentTypeName").toString());//培养箱

		unicast.setCustomizedField("equipmentName", map.get("equipmentName").toString());//1号培养箱
		String inputDateTime = map.get("inputDateTime")!=null ? DateUtil.formatToString((Date)(map.get("inputDateTime")), DateUtil.DATE_HH_mm_FORMAT) : "";
		unicast.setCustomizedField("inputDateTime", inputDateTime);//1474537178000

		unicast.setCustomizedField("equipmentNo", map.get("equipmentNo").toString());//E1001
		unicast.setCustomizedField("instrumentNo", map.get("instrumentNo").toString());//S2001
		LOGGER.info("苹果推送信息："+ JsonUtil.toJson(unicast));

		client.send(unicast);
	}

	public void sendIOSGroupcast() throws Exception {
		IOSGroupcast groupcast = new IOSGroupcast(appkey,appMasterSecret);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where":
		 *	{
    	 *		"and":
    	 *		[
      	 *			{"tag":"iostest"}
    	 *		]
		 *	}
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put("tag", "iostest");
		tagArray.put(testTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		// Set filter condition into rootJson
		groupcast.setFilter(filterJson);
		groupcast.setAlert("IOS 组播测试");
		groupcast.setBadge( 0);
		groupcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		groupcast.setTestMode();
		client.send(groupcast);
	}

	public void sendIOSCustomizedcast() throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey,appMasterSecret);
		// TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setAlert("IOS 个性化测试");
		customizedcast.setBadge( 0);
		customizedcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		customizedcast.setTestMode();
		client.send(customizedcast);
	}

	public void sendIOSFilecast() throws Exception {
		IOSFilecast filecast = new IOSFilecast(appkey,appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb");
		filecast.setFileId( fileId);
		filecast.setAlert("IOS 文件播测试");
		filecast.setBadge( 0);
		filecast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		filecast.setTestMode();
		client.send(filecast);
	}

	public static boolean sendUnicastMessage(String deviceType, Map<String, Object> map){
		if(deviceType!=null) {
			try {
				if(deviceType.equals("android")){
					UmengPushUtil umengPushUtil = new UmengPushUtil(android_appkey, android_appMasterSecret);
					umengPushUtil.sendAndroidUnicast(map);
					return true;
				}else if(deviceType.equals("iOS")){
					UmengPushUtil umengPushUtil = new UmengPushUtil(iso_appkey, iso_appMasterSecret);
					umengPushUtil.sendIOSUnicast(map);
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return false;
	}
}
