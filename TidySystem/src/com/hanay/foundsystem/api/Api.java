package com.hanay.foundsystem.api;

import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.imageutils.Bimp;
import com.hanay.foundsystem.imageutils.FileUtils;
import com.hanay.foundsystem.imageutils.ImageItem;
import com.hanay.foundsystem.util.ValueUtil;
import com.hanay.foundsystem.volley.VolleyCallBack;
import com.hanay.foundsystem.volley.VolleyUtils;


/**
 * @author 李海红
 * @version 创建时间：2015-4-1
 * @description 接口调用
 */

public class Api {
	/** 当前的 Token */
	public static String token = "yb2f5L6qgpRZwQNNsiZYde8U9K3bqy4P";
	/**
	 * UserRegister
	 *  @param $token
	 * @param $userInfo
	 *{"username":"hong","phone":"13651999158","password":"123456","repassword":"123456"}
	 * @param apiCallBack
	 */

	public static void UserRegister(String username, String phone,String password,String repassword, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		HashMap<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("username", username);
		userInfo.put("phone", phone);
		userInfo.put("password", password);
		userInfo.put("repassword", repassword);
		parms.put("userInfo", new JSONObject(userInfo).toString());

		System.out.println("===phone============================"+phone);
		VolleyUtils.postVolley(ApiClient.hostName + "/UserRegister", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}

	/**
	 * 用户登录验证
	 * @param userName   用户登录账号
	 * @param userPwd  用户登录密码
	 * @param apiCallBack
	 */

	public static void UserLogin(String username, String password, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		HashMap<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("username", username);
		userInfo.put("password", password);
		parms.put("userInfo", new JSONObject(userInfo).toString());
		VolleyUtils.postVolley(ApiClient.hostName + "/UserLogin", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}



	/**
	 *  首页
	 * 获取空巷广告滑动页
	 * @param limit   显示数量 不传则默认为3
	 * @param apiCallBack
	 */
	public static void GetAlley(final ApiCallBack apiCallBack){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		VolleyUtils.postVolley(ApiClient.hostName + "/GetAlley", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}

	/**
	 *  话题
	 * 获取所有或某个话题下的记忆列表数据  
	 * @param string $type 'list' or '123' list为某个topic下或者某个用户的记忆列表，'数字'为某个具体记忆的ID
	 * @param string $topicId 可选参数，当type为list时传
	 *  @param string $page 可选参数，当type为list时传，从0开始计算
	 * @param apiCallBack
	 */
	public static void GetTopicMemory(String type, String topicId,int page ,final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("type", type);
		parms.put("topicId",topicId);
		parms.put("page", String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/GetMemory", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 *  我的
	 * 获取某个用户发布的、收藏的记忆列表数据  
	 * @param string $type 'list' or '123' list为某个topic下或者某个用户的记忆列表，'数字'为某个具体记忆的ID
	 *  @param string $userId 可选参数，当type为list时传
	 *  @param string $page 可选参数，当type为list时传，从0开始计算
	 * @param apiCallBack
	 */
	public static void GetMyMemory(String type, String userId,String userType,int page, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("type", type);
		parms.put("userId", userId);
		parms.put("userType", userType);
		parms.put("page", String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/GetMemory", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 *  详情
	 * 获取某个状态的详情 
	 * @param string $type '数字'为某个具体记忆的ID
	 *  @param string $page 可选参数，当type为list时传，从0开始计算
	 * @param apiCallBack
	 */
	public static void GetInfoMemory(String type,int page, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("type", type);
		parms.put("page", String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/GetMemory", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}


	/**
	 * 详情
	 * 根据发表的状态memoryId获取该发表下的评论。
	 * @param token
	 *  @param $memoryId 记忆ID 具体某个记忆下的评论
	 *  @param $page 分页 从0开始
	 * @param apiCallBack
	 */
	public static void GetMemoryReply(String memoryId,int page,final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("memoryId", memoryId);
		parms.put("page", String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/GetReply", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 * 根据userId获取该用户的所有评论。
	 * @param token
	 *  @param $userId 用户ID 我的评论
	 *  @param $page 分页 从0开始
	 * @param apiCallBack
	 */
	public static void GetMyReply(String userId,int page, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userId", userId);
		parms.put("page", String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/GetReply", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}


	/**
	 * 对一条发表进行评论。
	 * @param $userId 用户ID
	 *  @param $memoryId 记忆ID
	 *@param $content 回复的内容，内容不超过140个汉字。
	 *@return 回复成功返回该回复的ID
	 */
	public static void AddReply(String userId,String pid,String memoryId,String content, final ApiCallBack apiCallBack) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("userId", userId);
		if(ValueUtil.isStrNotEmpty(pid)){
			params.put("pid",pid);
		}else{
			params.put("pid","0");
		}
		params.put("memoryId",memoryId);
		params.put("content",content);
		
		VolleyUtils.postVolley(ApiClient.hostName + "/AddReply", params, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
				
				
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 * 收藏记忆
	 * @param $token
	 * @param $userId 用户ID
	 * @param $memoryId 记忆ID
	 * @param $type 1为收藏 0为取消收藏
	 * @return 操作成功or失败
	 */
	public static void FavoriteMemory(String userId,String memoryId,String type,final ApiCallBack apiCallBack){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userId", userId);
		parms.put("memoryId", memoryId);
		parms.put("type", type);
		VolleyUtils.postVolley(ApiClient.hostName + "/FavoriteMemory", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 * 消息
	 * 获取私聊列表
	 * @param userId      当前账号ID
	 * @param type
	 * @param apiCallBack
	 */
	public static void getChatList(String userId,  int page, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userId", userId);
		parms.put("page", Integer.toString(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/GetChatList", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 * 个人主页
	 * @param $token
	 * @param $userId
	 * @return 个人信息
	 */
	public static void HomePage(String userId, final ApiCallBack apiCallBack){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userId",userId);
		VolleyUtils.postVolley(ApiClient.hostName + "/HomePage", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 * 话题
	 * 获取最新、最热话题、我的话题列表
	 * @param string $type 'list' or '123' 
	 * ----传'list'获取Topic列表，传'数字'则获取ID为这一数字的某个具体topic
	 *  @param string $listType 'new' 'hot' '43'  最新 最热 我的
	 *   ----可选参数，当type为list时传值，传'new'获取最新列表，传'hot'获取热门列表，
	 *   传'数字'则获取用户userId为这一数字的某个具体用户所发布的topic
	 *    @param string $page 列表分页
	 *    ----可选参数，当当type为list时传值，从0开始计数
	 * @param apiCallBack
	 */
	public static void getTopic(String type, String listType, int page, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("type", type);//传'list'获取Topic列表，传'数字'则获取ID为这一数字的某个具体topic
		parms.put("listType", listType);// 传'new'获取最新列表，传'hot'获取热门列表传'数字'则获取用户userId为这一数字的某个具体用户所发布的topic
		parms.put("page",String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/getTopic", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**话题
	 * @param $token
	 * @param $search 搜索内容
	 * @param $page 分页 从0开始计数
	 * @return 搜索结果集
	 */
	public static void SearchTopic(String searchContent,int page,final ApiCallBack apiCallBack){

		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("search", searchContent);
		parms.put("page",String.valueOf(page));
		VolleyUtils.postVolley(ApiClient.hostName + "/SearchTopic", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});

	};
	/**
	 * 设置
	 * 修改密码
	 * @param userId            用户账号ID
	 * @param oldPassword   老密码
	 * @param newPassword   新密码
	 * @param rePassword    重复新密码
	 * @param apiCallBack
	 */
	public static void NewPassword(String userId, String oldPassword, String newPassword, String rePassword, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userId", userId);
		parms.put("oldPassword", oldPassword);
		parms.put("newPassword", newPassword);
		parms.put("rePassword", rePassword);
		VolleyUtils.postVolley(ApiClient.hostName + "/NewPassword", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}
	/**
	 * 修改头像
	 * @param $token
	 * @param $userId 用户ID
	 * @param $uploadImg 上传用户头像
	 */
	public static void NewAvatar(String userId,String FileName,final ApiCallBack apiCallBack){
		final HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token",Api.token);
		parms.put("userId", userId);
		final HashMap<String, File> files = new HashMap<String, File>();
		File file =new File(FileUtils.SDPATH +FileName ) ;
		files.put("uploadImg" , file);
		VolleyUtils.post(ApiClient.hostName + "/NewAvatar", parms, files,new VolleyCallBack(){


			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}

		});
	}


	/**
	 * 新建话题
	 * @param $token
	 * @param $userId 用户ID
	 * @param $topic 话题标题
	 * @return 成功返回话题ID
	 */

	public static void AddTopic(String userId,String topic,final ApiCallBack apiCallBack){

		final HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token",Api.token);
		parms.put("userId", userId);
		parms.put("topic", topic);
		VolleyUtils.postVolley(ApiClient.hostName + "/AddTopic", parms,new VolleyCallBack(){


			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}

		});

	}
	/**
	 * 新建记忆
	 * @param $token
	 * @param $userId 用户ID
	 * @param $topicId 话题ID
	 * @param $memory 记忆内容
	 * {"title":"记忆标题","content":"记忆内容"}
	 * @param $uploadImg 话题图片
	 * @return 成功返回话题ID
	 */
	public static void AddMemory(String userId,String topicId,String memory,final ApiCallBack apiCallBack){

		final HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token",Api.token);
		parms.put("userId", userId);
		parms.put("topicId", topicId);
		parms.put("memory", memory);
		/*	final HashMap<String, File> files = new HashMap<String, File>();
		File file =new File(FileUtils.SDPATH +FileName ) ;
		files.put("uploadImg" , file);*/
	
		//for (ImageItem c : Bimp.tempSelectBitmap) {
		
		if(Bimp.tempSelectBitmap.size()!=0){
			File file = null;
			ImageItem c=Bimp.tempSelectBitmap.get(0);
			file = new File(FileUtils.SDPATH + c.getName());

			final HashMap<String, File> files = new HashMap<String, File>();
			System.out.println("FileUtils.SDPATH + c.getName()================="+FileUtils.SDPATH + c.getName());
			files.put("uploadImg", file);
			file = null;
			//}
			VolleyUtils.post(ApiClient.hostName + "/AddMemory", parms, files,new VolleyCallBack(){


				@Override
				public void httpSucceed(Result result) {
					apiCallBack.succeed(result);
				}

				@Override
				public void httpLoading() {
					apiCallBack.loading();
				}

				@Override
				public void httpFail(String error) {
					apiCallBack.fail(error);
				}

			});

		
		}else{
      System.out.println("================userId"+userId+"topicId"+topicId+"memory"+memory);
			VolleyUtils.postVolley(ApiClient.hostName + "/AddMemory", parms,new VolleyCallBack(){


				@Override
				public void httpSucceed(Result result) {
					apiCallBack.succeed(result);
				}

				@Override
				public void httpLoading() {
					apiCallBack.loading();
				}

				@Override
				public void httpFail(String error) {
					apiCallBack.fail(error);
				}

			});
		
		}
	
	}

	/**
	 * 获取推送消息
	 * @param userId   当前账号ID
	 * @param roleId  当前账号角色ID
	 * @param apiCallBack
	 */
	public static void PushMsg(String userId, final ApiCallBack apiCallBack) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userId", userId);
		VolleyUtils.postVolley(ApiClient.hostName + "/PushMsg", parms, new VolleyCallBack() {

			@Override
			public void httpSucceed(Result result) {
				apiCallBack.succeed(result);
			}

			@Override
			public void httpLoading() {
				apiCallBack.loading();
			}

			@Override
			public void httpFail(String error) {
				apiCallBack.fail(error);
			}
		});
	}

}
