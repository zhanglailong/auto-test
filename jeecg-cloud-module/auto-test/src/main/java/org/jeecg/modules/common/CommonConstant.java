package org.jeecg.modules.common;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/5/6
 */
public interface CommonConstant {
    /**
     * socket注册状态
     */
    Integer SOCKET_REGISTER_CODE_201 = 201;
    /**
     * socket客户端发送数据状态
     */
    Integer SOCKET_REGISTER_CODE_202 = 202;
    Integer SOCKET_REGISTER_CODE_203 = 203;

    /**
     * 数据状态0
     */
    String DATA_STATE_0 = "0";
    Integer DATA_INT_STATE_0 = 0;
    Integer DATA_INT_STATE_1 = 1;
    String DATA_STR_0 = "0";
    String DATA_STR_1 = "1";
    String DATA_STR_2 = "2";
    String DATA_STR_3 = "3";
    String DATA_STR_4 = "4";
    String DATA_STR_5 = "5";
    String DATA_STR_6 = "6";
    String DATA_STR_7 = "7";
    String DATA_STR_8 = "8";
    String DATA_STR_9 = "9";
    String DATA_STR_10 = "10";
    String DATA_STR_16 = "16";
    String DATA_STR_32 = "32";
    String DATA_STR_64 = "64";
    String DATA_STR_100 = "100";
    String DATA_STR_10000 = "10000";
    Integer DATA_INT_0 = 0;
    Integer DATA_INT_1 = 1;
    Integer DATA_INT_2 = 2;
    Integer DATA_INT_3 = 3;
    Integer DATA_INT_4 = 4;
    Integer DATA_INT_5 = 5;
    Integer DATA_INT_6 = 6;
    Integer DATA_INT_7 = 7;
    Integer DATA_INT_8 = 8;
    Integer DATA_INT_9 = 9;
    Integer DATA_INT_10 = 10;
    Integer DATA_INT_12 = 12;
    Integer DATA_INT_16 = 16;
    Integer DATA_INT_22 = 22;
    Integer DATA_INT_32 = 32;
    Integer DATA_INT_50 = 50;
    Integer DATA_INT_64 = 64;
    Integer DATA_INT_100 = 100;
    Long DATA_LONG_86400000 = 86400000L;
    Integer DATA_INT_200 = 200;
    String DATA_UNDEFINED= "undefined";
    /**
     * 虚拟删除0
     */
    Integer DATA_INT_IDEL_0 = 0;
    /**
     * 虚拟删除1 删除
     */
    Integer DATA_INT_IDEL_1 = 1;
    /**
     * 强制虚拟删除2 删除
     */
    Integer DATA_INT_IDEL_2 = 2;

    /**
     * 规划-网络type值
     */
    String PLAN_TYPE_NET = "net";
    /**
     * 规划-子网type值
     */
    String PLAN_TYPE_CHILDNET = "childNet";
    /**
     * 规划-虚拟机type值
     */
    String PLAN_TYPE_VIRTUAL= "virtual";
    /**
     * 规划-路由type值
     */
    String PLAN_TYPE_ROUTE= "route";

    /**
     * resttemplate 请求方式
     */
    String REST_TEMPLATE_GET= "get";
    String REST_TEMPLATE_POST= "post";
    String REST_TEMPLATE_ACCESS_TOKEN= "access_token";
    String REST_TEMPLATE_EXPIRES_AT= "expires_at";
    String REST_TEMPLATE_RESULT_CODE= "code";
    String REST_TEMPLATE_RESULT_MESSAGE= "message";

    /**
     *字符串
     */
    String DATA_STRING_IDEL= "idel";
    String DATA_STRING_SCRIPT_NAME= "script_name";
    String DATA_STRING_DEL_FLAG= "del_flag";
    String DATA_STRING_CONTENT= "content";
    String DATA_STRING_WEIGHT= "weight";
    String DATA_STRING_SORT= "sort";
    String DATA_START_TIME= "start_time";
    String DATA_STRING_NODEID= "record_node_id";
    String DATA_STRING_TEST_CASE_ID= "test_case_id";
    String DATA_STRING_TEST_CASE_NAME= "test_case_name";
    String DATA_STRING_PLANID= "plan_id";
    String DATA_STRING_OLDSCRIPTID= "old_script_id";
    String DATA_STRING_TEST_TASK_ID= "test_task_id";
    String DATA_STRING_TEST_ITEM_ID= "test_item_id";
    String DATA_STRING_TEST_ITEM_NAME= "test_item_name";
    String DATA_STRING_SCRIPT_ID= "script_id";
    String DATA_STRING_PROJECT_ID= "project_id";
    String DATA_STRING_PROJECT_NAME= "project_name";
    String DATA_STRING_PLAN_RESULT_ID= "plan_result_id";
    String ORDER_COLUMN = "column";
    String DATA_STRING_ID= "id";
    String DATA_STRING_CREATE_BY= "create_by";
    String DATA_STRING_UPDATE_TIME= "update_time";
    String DATA_STRING_IDS= "ids";
    String DATA_STRING_STATE= "state";
    String DATA_STRING_DATA= "data";
    String DATA_STRING_URL="url";
    String DATA_STRING_STACK= "stack";
    String DATA_STRING_STACK_STATUS= "stack_status";
    String DATA_STRING_STACK_STATUS_REASON= "stack_status_reason";
    String DATA_STRING_STACK_STACK_ID= "{stack_id}";
    String DATA_STRING_STACK_SERVER_ID= "{server_id}";
    String DATA_STRING_STACK_PLAN_ID= "plan_id";
    String DATA_STRING_OS_NOVA_SERVER= "OS::Nova::Server";
    String DATA_STRING_VIR_SERVER= "Server-";
    String DATA_STRING_SERVER_FIXED = "fixed";
    String DATA_STRING_SERVER_FLOATING = "floating";
    String DATA_STRING_SERVERS_SERVER= "server";
    String DATA_STRING_ADDRESS= "addresses";
    String DATA_STRING_SERVER_IP= "private_v4";
    String DATA_STRING_SERVER_STATUS= "status";
    String DATA_STRING_STACK_NAME_S= "s-";
    String DATA_STRING_ORDER_ID= "order_id";
    String DATA_STRING_ORDER_TYPE= "order_type";
    String DATA_STRING_ORDER_TYPES= "orderTypes";
    String DATA_STRING_PRO_NAME= "资源申请";
    String DATA_STRING_ADMIN= "admin";
    String DATA_STRING_NUMBER= "number";
    Integer MEMORY_CAPACITY = 1024;
    String DATA_STRING_SERVER_NAME = "name";
    String DATA_STRING_IMAGE = "image";
    String DATA_STRING_FLAVOR = "flavor";
    String DATA_STRING_ORIGINAL_NAME = "original_name";
    String OS_NAME = "os.name";
    String Y_M_D = "yyyy-MM-dd";
    String WINDOWS = "windows";
    String SYMBOL = "\\";
    String APP_SCAN_PATH = "appscan";
    String HOME = "/home/";
    String CMD_EXE_START = "cmd.exe /c start ";
    String APP_SCAN = "appscancmd e /su ";
    String D_D = " /d ";
    String SCAN = "\\test.scan";
    String REPORT_FILE = " /rt pdf /report_file ";
    String PDF_SCAN = "\\test.pdf";
    String UNDER_STAND = "underStand";
    String UND_CREATE = "und create -languages c++ ";
    String MYDB_UDB = "myDb.udb";
    String UND_ADD = "und add ";
    String UND_ANALYZE = "und analyze -all ";
    String UND_REPORT = "und report ";
    String JMX = ".jmx";
    String JTL = ".jtl";
    String REPLAYLOGPATH = "";
    String C_PATH = "c:/";
    String JMETER_N_T_S = "\\jmeter -n -t ";
    String JMETER__L = " -l ";
    String JMETER_N_T = "\\jmeter.bat -n -t ";
    String JMETER_E_O = " -e -o ";
    String REST_TEMPLATE_RESULT_MSG = "msg";
    String DATA_STRING_STACK_ID= "{stack_id}";
    String DATA_STRING_SNAPSHOT_ID = "{snapshot_id}";
    String SEPARATOR = "_";
    String REGULAR_EXPRESSION= "[a-zA-Z]";
    String MIRROR_SPECIFICATION= "specification";
    String MIRROR_VIRTUAL_MACHINE_CODE= "virtual_machine_code";
    String MIRROR_C_= "c_";
    String MIRROR_G_= "g_";
    String MIRROR_100G= "100G";
    String SCRIPT_ID="script_id";
    String SCRIPT_RECORD_ID="script_record_id";
    String AUTO_SCRIPT_ID="auto_script_id";
    String FAIL="失败";
    String IS_SUCCESS="成功";

    /**
     * 支付宝字符串
     */
    String ALIPAY_TRADE_STATUS= "trade_status";
    String ALIPAY_TRADE_SUCCESS= "TRADE_SUCCESS";
    String ALIPAY_OUT_TARDE_NO = "out_trade_no";
    String ALIPAY_SUCCESS= "SUCCESS";
    String ALIPAY_RETRURN_CODE= "return_code";

    /**
     * 微信字符串
     */
    String WXPAY_ERR_CODE= "err_code";
    String WXPAY_ORDERPAID= "ORDERPAID";
    String WXPAY_FAIL = "FAIL";
    String WXPAY_SUCCESS= "SUCCESS";
    String WXPAY_RETRURN_CODE= "return_code";
    String WXPAY_TRADE_STATE= "trade_state";
    String WXPAY_TRANSACTION_ID= "transaction_id";
    public final static String SCHEDULED_STACK_LOCK= "scheduled_stack_lock";
    public final static String SCHEDULED_CREATE_VM_LOCK= "scheduled_create_vm_lock";
    public final static String SCHEDULED_DELETE_VM_LOCK= "scheduled_delete_vm_Lock";


    /**
     * 上传下载
     */
    String DOWNLOAD_WINDOWS= "windows";
    String DOWNLOAD_ADDRESS_Home= "/home";

    /**
     * 在线用户
     */
    public static final String ONLINE_NUMBER = "onlineNumber";
    public final static String DATA_TOOL_TYPE_TB = "XY";


    String NODE_SUCCESS= "success";


    String DATA_STR_200 = "200";
    String STRING_SCRIPT_ID ="script_id" ;
    String STRING_PARAMETER_KEY ="parameter_key" ;
    //String IPFS_QUERY_URL ="http://192.168.1.9:8080/ipfs/" ;
    String IPFS_QUERY_URL ="http://192.168.10.101:9044/ipfs/" ;
    String DATA_STR_7Z =".7z" ;
    String DATA_STR_ZIP =".zip";
    String DATA_STR_RAR =".rar";
    String DATA_FORMAT_ZIP ="zip";
    String DATA_FORMAT_7Z ="7z";
    String DATA_FORMAT_RAR ="rar";
}
