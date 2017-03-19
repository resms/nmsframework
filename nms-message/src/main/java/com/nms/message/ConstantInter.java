package com.nms.message;

import com.nms.util.JsonMapper;

public abstract class ConstantInter extends JsonMapper
{
	public final static String DEFAULT_NAMESPACE = "http://www.resms.net";
	public final static String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 定义消息标识字段常量，代码里解析Json时使用该常量作为key值判断
	 * 在后期代码重构时，此常量应与本类中的mssageName和ver保持一致
	 * 例如：
	 *      后期mssageName重构成no,那么MSSAGE_NAME常量值应该同步成no
	 */
	public static final String FIELD_MSSAGE_NAME = "messageName";
	public static final String FIELD_MESSAGE_VERSION = "ver";
	public static final String FIELD_MESSAGE_GUID = "guid";
	public static final String FIELD_MESSAGE_ADAPTOR_ID = "adaptorId";
	public static final String FIELD_MESSAGE_PROXY_ID = "proxyId";
	public static final String FIELD_MESSAGE_DATA = "data";
    /**
     * 编码规则，0x10000001,第1位表示系统，第2，3位表示子系统,第4,5,6位表示模块，第7,8位表示功能
     * 此消息定义为全局消息定义，后面有新消息接入也必须遵循此编码规则追加
     * 0x1=netWatch
     * 0x110=losweb
     * 0x111=losportal
     * 0x112=losproxy
     * 0x112101=losproxy common
     * 0x112102=losproxy log
     * 0x112103=losproxy pnm
     * 0x112104=losproxy fbc
     * 0x112105=losproxy hfc
     *
     * 0x120=nmse-pon portal
     * 0x120101=nmse-pon common
     * 0x120102=
     * 0x121=nmse-pon adaptor
     */

    public static final String NETWATCH_SYSTEM_MESSAGE_HEARTBEAT = "HEARTBEAT";

    //0x112101=losproxy common
    public static final String NETWATCH_SYSTEM_MESSAGE_PORTAL_SESSION_CONNECTED = "PortalSessionConnected";
    public static final String NETWATCH_SYSTEM_MESSAGE_PORTAL_SESSION_DISCONNECTED = "PortalSessionDisconnected";
    public static final String NETWATCH_SYSTEM_MESSAGE_PORTAL_SESSION_RECONNECTED = "PortalSessionReconnected";

    public static final String NETWATCH_SYSTEM_MESSAGE_ADAPTOR_SESSION_CONNECTED = "AdaptorSessionConnected";
    public static final String NETWATCH_SYSTEM_MESSAGE_ADAPTOR_SESSION_DISCONNECTED = "AdaptorSessionDisconnected";
    public static final String NETWATCH_SYSTEM_MESSAGE_ADAPTOR_SESSION_RECONNECTED = "AdaptorSessionReconnected";

    public static final String NETWATCH_SYSTEM_MESSAGE_DIRECT_FORWARD_TO_ADAPTOR = "DirectForwardToAdaptor";
    public static final String NETWATCH_SYSTEM_MESSAGE_DIRECT_FORWARD_TO_PORTAL = "DirectForwardToPortal";


    //0x112102=losproxy log
//    public static final String NETWATCH_SYSTEM_MESSAGE_SET_PROXY_LOG = "setProxyLog";

    //0x112105=losproxy hfc
//    public static final String NETWATCH_HFC_MESSAGE_GET_HFC_INFO_REQUEST = "getHfcInfoRequest";
//    public static final String NETWATCH_HFC_MESSAGE_GET_HFC_INFO_RESPONSE = "getHfcInfoResponse";
//    public static final String NETWATCH_HFC_MESSAGE_GET_HFC_INFO_PROXY_REQUEST = "getHfcInfoProxyRequest";
//    public static final String NETWATCH_HFC_MESSAGE_GET_HFC_INFO_PROXY_RESPONSE = "getHfcInfoProxyResponse";
//
//    public static final String NETWATCH_HFC_MESSAGE_GET_BRANCH_INFO_REQUEST = "getBranchHfcInfoRequest";
//    public static final String NETWATCH_HFC_MESSAGE_GET_BRANCH_INFO_RESPONSE = "getBranchHfcInfoResponse";
//
//    public static final String NETWATCH_HFC_MESSAGE_GET_PROVINCE_INFO_REQUEST = "getProvinceHfcInfoRequest";
//    public static final String NETWATCH_HFC_MESSAGE_GET_PROVINCE_INFO_RESPONSE = "getProvinceHfcInfoResponse";
//
//    public static final String NETWATCH_HFC_MESSAGE_GET_BRANCH_INFO_PROXY_REQUEST = "getBranchHfcInfoProxyRequest";
//    public static final String NETWATCH_HFC_MESSAGE_GET_BRANCH_INFO_PROXY_RESPONSE = "getBranchHfcInfoProxyResponse";
//
//    public static final String NETWATCH_HFC_MESSAGE_GET_PROVINCE_INFO_PROXY_REQUEST = "getProvinceHfcInfoProxyRequest";
//    public static final String NETWATCH_HFC_MESSAGE_GET_PROVINCE_INFO_PROXY_RESPONSE = "getProvinceHfcInfoProxyResponse";

    //0x120101=nmse-pon common
//    public static final String NMS_PON_MESSAGE_USER_REALTIME_POLLING = "URTPollingMessage";
//    public static final String NMS_PON_MESSAGE_ADAPTOR_TIMER_POLLING = "TimerPollingMessage";

      

	// public enum OpCode implements ConstantInter{
	// Success ("1"),
	// FAILURE ("0");
	//
	// public String code;
	//
	// private OpCode(String code){
	// this.code = code;
	// }
	// }
}
