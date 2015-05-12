package com.auction.config;



import java.util.HashMap;
import java.util.Map;

/**
 * 错误代码索引表
 * 
 * @author linyg
 * 
 */
public class ErrorCode {

	private static Map<Integer, String> map = new HashMap<Integer, String>();
	/** 没有可用的网络，请设置 */
	public static final int E_100 = 100;
	/** 网络连接失败 */
	public static final int E_101 = 101;
	/** 发送数据失败 */
	public static final int E_102 = 102;
	/** 连接超时 */
	public static final int E_103 = 103;
	/** 请求失败 **/
	public static final int E_104 = 104;
	/** room服务连接中断 */
	public static final int E_105 = 105;
	/** session服务连接中断 */
	public static final int E_106 = 106;

	/** 请求数据失败 **/
	public static final int E_107 = 107;

	/** 未知的系统异常(如：数据库连接中断) */
	public static final int E_5000 = 5000;
	/** 密等性校验失败（对链接的加密校验） */
	public static final int E_1000 = 1000;
	/** 客户端被重新发布 **/
	public static final int E_2000 = 2000;
	/** 金币不足 **/
	public static final int E_4000 = 4000;
	/** 请求参数为空 */
	public static final int E_4001 = 4001;
	/** 请求参数非法 */
	public static final int E_4002 = 4002;
	/** 请求参数的内容小于或者超过规定长 */
	public static final int E_4003 = 4003;
	/** 无上传头像权限 */
	public static final int E_4004 = 4004;
	/** 因服务接口升级 **/
	public static final int E_8000 = 8000;
	/** 用户当前为“未登录”状态 */
	public static final int E_4100 = 4100;
	/** 用户名或密码不正确 */
	public static final int E_4101 = 4101;
	/** 账号被管理员锁定 */
	public static final int E_4102 = 4102;
	/** 服务器超过负载 */
	public static final int E_4103 = 4103;
	/** 当前用户不存在 */
	public static final int E_4104 = 4104;
	/** 当前用户的隐私数据不存在 */
	public static final int E_4105 = 4105;
	/** 登陆token信息不存在 **/
	public static final int E_4106 = 4106;

	/** 该账号已注册 */
	public static final int E_4201 = 4201;
	/** 用户生日大于当前日期 **/
	public static final int E_4202 = 4202;
	/** 账号注册不成功 **/
	public static final int E_4203 = 4203;
	/** 邮件格式不正确 **/
	public static final int E_4204 = 4204;
	/** 邮件已经被注册 */
	public static final int E_4205 = 4205;
	/** 用户名不是邮件地址或遇见号 */
	public static final int E_4206 = 4206;
	/** 帐号登录失败 **/
	public static final int E_4207 = 4207;
	/** 无法登录 **/
	public static final int E_4208 = 4208;
	/** 帐号不存在 */
	public static final int E_4209 = 4209;
	/** 找回密码邮件发送失败 */
	public static final int E_4210 = 4210;
	/** 密码错误 */
	public static final int E_4211 = 4211;
	/** 已经绑定过微博 */
	public static final int E_4212 = 4212;
	/** 绑定的遇见账号不存在 */
	public static final int E_4213 = 4213;
	/** 手机号已注册过 */
	public static final int E_4215 = 4215;

	/** 用户已经被添加为好友 */
	public static final int E_4301 = 4301;
	/** 用户在黑名单中存在 */
	public static final int E_4302 = 4302;
	/** 用户不能将自己添加为好友 */
	public static final int E_4303 = 4303;
	/** 好友关系不存在 */
	public static final int E_4304 = 4304;
	/** 自己在用户黑名单中，不能请求加好友 **/
	public static final int E_4305 = 4305;
	/** 自己在用户黑名单中，不能向用户打招呼 **/
	public static final int E_4306 = 4306;
	/** 自己在用户黑名单中，不能向用户发私信 **/
	public static final int E_4307 = 4307;
	/** 好友数量已经达到系统上限500个,不能再添加 **/
	public static final int E_4308 = 4308;
	/** 对方好友数量已经达到系统上限500个,不能再添加 **/
	public static final int E_4309 = 4309;

	/** 添加的教育信息为空 */
	public static final int E_4401 = 4401;
	/** 教育信息所属用户不存在 */
	public static final int E_4402 = 4402;
	/** 找不到指定ID的教育信息 */
	public static final int E_4403 = 4403;
	/** 删除教育信息出错 */
	public static final int E_4404 = 4404;

	/** 添加的职业信息为空 */
	public static final int E_4501 = 4501;
	/** 职业信息所属用户不存在 */
	public static final int E_4502 = 4502;
	/** 找不到指定ID的职业信息 */
	public static final int E_4503 = 4503;
	/** 删除职业信息出错 */
	public static final int E_4504 = 4504;

	/** 添加的兴趣爱好信息为空 */
	public static final int E_4601 = 4601;
	/** 找不到指定ID的兴趣爱好 */
	public static final int E_4602 = 4602;

	/** 添加的黑名单信息为空 **/
	public static final int E_4701 = 4701;
	/** 找不到黑名单所属用户 */
	public static final int E_4702 = 4702;
	/** 黑名单已存在 */
	public static final int E_4703 = 4703;
	/** 删除黑名单出错 */
	public static final int E_4704 = 4704;

	/** 添加的微博信息为空 */
	public static final int E_4801 = 4801;
	/** 找不到所属微博 */
	public static final int E_4802 = 4802;
	/** 删除微博绑定出错 */
	public static final int E_4803 = 4803;

	/** 通知不存在 */
	public static final int E_4901 = 4901;
	/** 私信不存在 */
	public static final int E_4902 = 4902;

	/** 相片所属用户不存在 */
	public static final int E_5101 = 5101;
	/** 删除相片出错 **/
	public static final int E_5102 = 5102;
	/** 相片标签不存在 */
	public static final int E_5103 = 5103;
	/** 用户已称赞过相片 **/
	public static final int E_5104 = 5104;
	/** 相片不存在 **/
	public static final int E_5105 = 5105;
	/** 当前查看相片的用户不存在 **/
	public static final int E_5106 = 5106;
	/** 当前用户没称赞过此相片 **/
	public static final int E_5107 = 5107;
	/** 当前上传照片已达最大数量 **/
	public static final int E_5108 = 5108;
	/** 相片不存在 **/
	public static final int E_5201 = 5201;
	/** 评论不存在 **/
	public static final int E_5202 = 5202;
	/** 评论不存在 **/
	public static final int E_5203 = 5203;
	/** 评论已经被删除 **/
	public static final int E_5204 = 5204;
	/** 评论时间间隔小于15秒，不能进行再次评论 **/
	public static final int E_5205 = 5205;
	/** 已被列入黑名单，不能做评论 */
	public static final int E_5206 = 5206;
	/** 把对方加入黑名单，无法发表评论 */
	public static final int E_5207 = 5207;
	/** 举报所属用户不存在 */
	public static final int E_5401 = 5401;
	/** 举报的目标不存在 */
	public static final int E_5402 = 5402;
	/** 举报的目标已被他人举报并做了相关处理 */
	public static final int E_5403 = 5403;
	/** 当前用户已经举报过该目标 */
	public static final int E_5404 = 5404;
	/** 系统帐号不能被举报 */
	public static final int E_5405 = 5405;
	/** 封停用户不能被举报 */
	public static final int E_5406 = 5406;

	/** 找不到隐私所属用户 */
	public static final int E_5501 = 5501;
	/** 找不到邀请所属用户 */
	public static final int E_5601 = 5601;
	/** 该号码被重复邀请 */
	public static final int E_5602 = 5602;
	/** 验证码不正确 */
	public static final int E_5603 = 5603;
	/** 验证码发送失败 **/
	public static final int E_5604 = 5604;
	/** 该号码被重复绑定 **/
	public static final int E_5605 = 5605;
	/** 用户绑定超过当天次数限制 **/
	public static final int E_5606 = 5606;
	/** 抱歉，暂未开通该功能 **/
	public static final int E_5607 = 5607;

	/** 手机验证码获取过于频繁 */
	public static final int E_5610 = 5610;
	/** 错误的手机号 */
	public static final int E_5611 = 5611;
	/** 一个月只能更换绑定手机号一次 */
	public static final int E_5612 = 5612;
	/** 12小时内手机验证码获取次数超过10次 */
	public static final int E_5613 = 5613;
	/** 5分钟过内手机验证码获取次数超过5次 */
	public static final int E_5614 = 5614;

	/** 该用户已经被关注 **/
	public static final int E_5701 = 5701;
	/** 该用户未被关注 **/
	public static final int E_5702 = 5702;
	/** 您的关注用户数量已经达到系统上限，不能再继续关注 */
	public static final int E_5703 = 5703;

	/** 礼物不存在 **/
	public static final int E_5801 = 5801;
	/** 未设置礼物价格 **/
	public static final int E_5802 = 5802;
	/** 不能给自己送礼物 **/
	public static final int E_5803 = 5803;
	/** 赠送礼物失败 **/
	public static final int E_5804 = 5804;
	/** 该类别没有礼物 **/
	public static final int E_5805 = 5805;
	/** 该用户暂无礼物 **/
	public static final int E_5806 = 5806;
	/** 不能查看别人赠送的礼物清单 */
	public static final int E_5807 = 5807;
	/** 非VIP无法赠送VIP礼物 */
	public static final int E_5808 = 5808;
	/** 场景不存在 */
	public static final int E_5809 = 5809;
	/** 该场景已被购买过 */
	public static final int E_5810 = 5810;

	/** 订单编号产生失败 **/
	public static final int E_5901 = 5901;
	/** 订单不存在 **/
	public static final int E_5902 = 5902;
	/** 当前用户不是订单中指定的用户 **/
	public static final int E_5903 = 5903;
	/** 订单收据验证不通过 **/
	public static final int E_5904 = 5904;
	/** 支付通道无效 **/
	public static final int E_5905 = 5905;
	/** 购买的商品不存在 **/
	public static final int E_5906 = 5906;
	/** 已经购买了该商品 **/
	public static final int E_5907 = 5907;
	/** 该订单被重复提交处理 **/
	public static final int E_5908 = 5908;
	/** 该订单已经支付并处理成功 **/
	public static final int E_5909 = 5909;
	/** 金币领取失败 **/
	public static final int E_5910 = 5910;
	/** 钻石不足 */
	public static final int E_5930 = 5930;
	/** 已是VIP **/
	public static final int E_5950 = 5950;

	/** 用户不在指定的群组里 **/
	public static final int E_6001 = 6001;
	/** 群组不存在 */
	public static final int E_6002 = 6002;
	/** 群组创建者不能退出该群组 */
	public static final int E_6003 = 6003;
	/** 非群组创建者不能解散该群组 */
	public static final int E_6004 = 6004;
	/** 创建群组的金币数不够 */
	public static final int E_6005 = 6005;
	/** 群组人数达到上限 */
	public static final int E_6006 = 6006;
	/** 用户加入的群组数达到上限 */
	public static final int E_6007 = 6007;
	/** 被踢出群组后，24小时内不能再申请加入 */
	public static final int E_6008 = 6008;
	/** 用户创建群的数量达到限制 */
	public static final int E_6009 = 6009;
	/** 群的限制加入距离不正确 */
	public static final int E_6010 = 6010;
	/** 不能把圈主升级为管理员 */
	public static final int E_6011 = 6011;
	/** 该圈成员已经是圈管理员 */
	public static final int E_6012 = 6012;
	/** 该圈管理员已经达到上限 */
	public static final int E_6013 = 6013;
	/** 该用户退出群时长还不到两小时，不准加入 **/
	public static final int E_6014 = 6014;
	/** 用户被踢出群后，24小时内不得加入 */
	public static final int E_6015 = 6015;
	/** 不能对圈主禁言 **/
	public static final int E_6016 = 6016;
	/** 话题不存在 **/
	public static final int E_6017 = 6017;
	/** 话题已经称赞过，不能再称赞咯 */
	public static final int E_6018 = 6018;
	/** 该话题没有被称赞过，不能取消称赞 **/
	public static final int E_6019 = 6019;
	/** 该话题评论不存在 **/
	public static final int E_6020 = 6020;
	/** 该用户在圈子已经被禁言，不能再禁言 */
	public static final int E_6021 = 6021;
	/** 该用户没有被禁言，不能取消禁言 */
	public static final int E_6022 = 6022;
	/** 非圈主管理员不能禁言 */
	public static final int E_6023 = 6023;
	/** 非圈主管理员不能解禁 */
	public static final int E_6024 = 6024;
	/** 非圈主管理员不能置顶话题 */
	public static final int E_6025 = 6025;
	/** 非圈主管理员不能取消置顶话题 */
	public static final int E_6026 = 6026;
	/** 非圈主管理员不能删除话题 */
	public static final int E_6027 = 6027;
	/** 不能对圈主管理员禁言 */
	public static final int E_6028 = 6028;
	/** 被禁言用户不能发表话题 */
	public static final int E_6029 = 6029;
	/** 被禁言用户不能发表评论 */
	public static final int E_6030 = 6030;
	/** 加入过的圈子，不能再次加入 */
	public static final int E_6031 = 6031;

	/** 你加入圈子数量已超过上限 */
	public static final int E_6032 = 6032;
	/** 你已加入该圈，请勿重复加入 */
	public static final int E_6033 = 6033;
	/** 该用户加入圈子数量已达上限 */
	public static final int E_6034 = 6034;
	/** 你不是该圈管理员，无法处理申请 */
	public static final int E_6035 = 6035;
	/** 该申请已被其他管理处理 -5.2弃用 */
	public static final int E_6036 = 6036;
	/** 该请求已经被其他管理员同意 */
	public static final int E_6046 = 6046;
	/** 该请求已经被其他管理员拒绝 */
	public static final int E_6047 = 6047;
	/** 该消息已失效 */
	public static final int E_6037 = 6037;
	/** 该圈子成员数量已达上限 */
	public static final int E_6038 = 6038;

	/** 非圈主无法进行操作 */
	public static final int E_6039 = 6039;

	/** 非圈主无法修改圈地标 */
	public static final int E_6040 = 6040;

	/** 本周已申请加入过该圈子 */
	public static final int E_6043 = 6043;

	/** 推荐用户已达上限 */
	public static final int E_6201 = 6201;
	/** 搭讪人数不正确 */
	public static final int E_6101 = 6101;
	/** 无该系统搭讪编号 */
	public static final int E_6102 = 6102;
	public static final int E_6301 = 6301;

	/** 退出圈子 48小时内不能再次加入 **/
	public static final int E_6048 = 6048;

	/** 请打开地理位置定位功能 */
	public static final int E_6400 = 6400;

	/** 当天绑定次数超过上限 */
	public static final int E_5608 = 5608;
	/** 对应的绑定手机号不正确 */
	public static final int E_5609 = 5609;
	public static final int E_7101 = 7101;
	public static final int E_7001 = 7001;
	public static final int E_7002 = 7002;
	public static final int E_7003 = 7003;
	public static final int E_7004 = 7004;
	/**** 搭讪内容不正确 ***********/
	public static final int E_6103 = 6103;

	
}
