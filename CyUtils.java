package weaver.interfaces.workflow.action;

import com.ec.spring.test.APIHttpClient;
import com.ibm.icu.text.SimpleDateFormat;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.workflow.webservices.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.*;
import weaver.hrm.User;

public class CyUtils {

    public static void main(String[] args){
        System.out.println(rzdsqAction.xc("2017-03-17"));

    }


    public static void createLog(String mainid,String czr,String cznr,boolean msap){
        Date date=new Date();
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String nowday = day.format(date);
        String nowtime = time.format(date);

        String mscs = null;
        RecordSet rs = new RecordSet();
        String sql = "";
        if(msap) {
            sql = "select count(*) from uf_jl_dt1 where mscs is not null and mainid = " + mainid;
            rs.execute(sql);
            rs.next();
            mscs = rs.getString("mscs");
            int cs = Integer.parseInt(mscs)+1;
            mscs = String.valueOf(cs);
        }
        sql = "insert into uf_jl_dt1 (mainid,czr,czrq,czsj,cznr,mscs) values ("+mainid+","+czr+",'"+nowday+"','"+nowtime+"','"+cznr+"',"+mscs+")";
        rs.executeUpdate(sql);



    }


    /** 发起提醒流程——提醒人会签
     * @param txnr  提醒内容
     * @param name  流程标题
     * @param btxr  被提醒人
     * @throws Exception
     */
    public static void createRequest_hq(String txnr,String name,String btxr) throws Exception {
        //主字段
        WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[4]; //字段信息
        wrti[0] = new WorkflowRequestTableField();
        wrti[0].setFieldName("fqr");//发起人
        wrti[0].setFieldValue("1");//被留言人字段的值，111为被留言人id
        wrti[0].setView(true);//字段是否可见
        wrti[0].setEdit(true);//字段是否可编辑

        wrti[1] = new WorkflowRequestTableField();
        wrti[1].setFieldName("txnr");//提醒内容
        wrti[1].setFieldValue(txnr);
        wrti[1].setView(true);
        wrti[1].setEdit(true);

        wrti[2] = new WorkflowRequestTableField();
        wrti[2].setFieldName("btxr");//提醒内容
        wrti[2].setFieldValue(btxr);
        wrti[2].setView(true);
        wrti[2].setEdit(true);



        WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据
        wrtri[0] = new WorkflowRequestTableRecord();
        wrtri[0].setWorkflowRequestTableFields(wrti);
        WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
        wmi.setRequestRecords(wrtri);




        WorkflowBaseInfo wbi = new WorkflowBaseInfo();

        WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息
        wri.setRequestName(name);//流程标题
        wbi.setWorkflowId("132");//workflowid 132代表提醒——提醒人会签
        wri.setCreatorId("1");//创建人id
        wri.setRequestLevel("1");//0 正常，1重要，2紧急
        wri.setWorkflowMainTableInfo(wmi);//添加主字段数据
        wri.setWorkflowBaseInfo(wbi);

//执行创建流程接口
        WorkflowServiceImpl WorkflowServiceImpl = new WorkflowServiceImpl();
        String requestid = WorkflowServiceImpl.doCreateWorkflowRequest(wri,1);
        if(Util.getIntValue(requestid,0)>0){
            System.out.println("流程触发成功,流程id为"+requestid);
        }else{
            System.out.println( "流程触发失败,"+requestid);
        }

    }

    /** 发起提醒流程——提醒人非会签
     * @param txnr  提醒内容
     * @param name  流程标题
     * @param btxr  被提醒人
     * @throws Exception
     */
    public static void createRequest_fhq(String txnr,String name,String btxr) throws Exception {
        //主字段
        WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[4]; //字段信息
        wrti[0] = new WorkflowRequestTableField();
        wrti[0].setFieldName("fqr");//发起人
        wrti[0].setFieldValue("1");//被留言人字段的值，111为被留言人id
        wrti[0].setView(true);//字段是否可见
        wrti[0].setEdit(true);//字段是否可编辑

        wrti[1] = new WorkflowRequestTableField();
        wrti[1].setFieldName("txnr");//提醒内容
        wrti[1].setFieldValue(txnr);
        wrti[1].setView(true);
        wrti[1].setEdit(true);

        wrti[2] = new WorkflowRequestTableField();
        wrti[2].setFieldName("btxr");//提醒内容
        wrti[2].setFieldValue(btxr);
        wrti[2].setView(true);
        wrti[2].setEdit(true);



        WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据
        wrtri[0] = new WorkflowRequestTableRecord();
        wrtri[0].setWorkflowRequestTableFields(wrti);
        WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
        wmi.setRequestRecords(wrtri);




        WorkflowBaseInfo wbi = new WorkflowBaseInfo();

        WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息
        wri.setRequestName(name);//流程标题
        wbi.setWorkflowId("165");//workflowid 149代表测试库提醒——提醒人非会签 165代表测试库提醒——提醒人非会签
        wri.setCreatorId("1");//创建人id
        wri.setRequestLevel("1");//0 正常，1重要，2紧急
        wri.setWorkflowMainTableInfo(wmi);//添加主字段数据
        wri.setWorkflowBaseInfo(wbi);

//执行创建流程接口
        WorkflowServiceImpl WorkflowServiceImpl = new WorkflowServiceImpl();
        String requestid = WorkflowServiceImpl.doCreateWorkflowRequest(wri,1);
        if(Util.getIntValue(requestid,0)>0){
            System.out.println("流程触发成功,流程id为"+requestid);
        }else{
            System.out.println( "流程触发失败,"+requestid);
        }

    }


    /** 发起提醒流程——面试安排转日程
     * @param name  流程标题
     * @param msrq  面试日期
     * @param ksmssj  开始面试时间
     * @param jsmssj  结束面试时间
     * @param msdd  面试地点
     * @param hxr  候选人
     * @param msry  面试人员
     * @throws Exception
     */
    public static void createRequest_rc(String name,String msrq,String ksmssj,String jsmssj,String msdd,String hxr,String msry,String msfs) throws Exception {
        //主字段
        WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[8]; //字段信息
        wrti[0] = new WorkflowRequestTableField();
        wrti[0].setFieldName("fqr");//发起人
        wrti[0].setFieldValue("1");//被留言人字段的值，111为被留言人id
        wrti[0].setView(true);//字段是否可见
        wrti[0].setEdit(true);//字段是否可编辑

        wrti[1] = new WorkflowRequestTableField();
        wrti[1].setFieldName("msrq");//提醒内容
        wrti[1].setFieldValue(msrq);
        wrti[1].setView(true);
        wrti[1].setEdit(true);

        wrti[2] = new WorkflowRequestTableField();
        wrti[2].setFieldName("ksmssj");//提醒内容
        wrti[2].setFieldValue(ksmssj);
        wrti[2].setView(true);
        wrti[2].setEdit(true);

        wrti[3] = new WorkflowRequestTableField();
        wrti[3].setFieldName("jsmssj");//提醒内容
        wrti[3].setFieldValue(jsmssj);
        wrti[3].setView(true);
        wrti[3].setEdit(true);

        wrti[4] = new WorkflowRequestTableField();
        wrti[4].setFieldName("msdd");//提醒内容
        wrti[4].setFieldValue(msdd);
        wrti[4].setView(true);
        wrti[4].setEdit(true);

        wrti[5] = new WorkflowRequestTableField();
        wrti[5].setFieldName("hxr");//提醒内容
        wrti[5].setFieldValue(hxr);
        wrti[5].setView(true);
        wrti[5].setEdit(true);

        wrti[6] = new WorkflowRequestTableField();
        wrti[6].setFieldName("msry");//提醒内容
        wrti[6].setFieldValue(msry);
        wrti[6].setView(true);
        wrti[6].setEdit(true);

        wrti[7] = new WorkflowRequestTableField();
        wrti[7].setFieldName("msfs");//提醒内容
        wrti[7].setFieldValue(msfs);
        wrti[7].setView(true);
        wrti[7].setEdit(true);

        WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据
        wrtri[0] = new WorkflowRequestTableRecord();
        wrtri[0].setWorkflowRequestTableFields(wrti);
        WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
        wmi.setRequestRecords(wrtri);




        WorkflowBaseInfo wbi = new WorkflowBaseInfo();

        WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息
        wri.setRequestName(name);//流程标题
        wbi.setWorkflowId("168");//workflowid 148代表测试库面试安排转日程 168代表测试库面试安排转日程
        wri.setCreatorId("1");//创建人id
        wri.setRequestLevel("1");//0 正常，1重要，2紧急
        wri.setWorkflowMainTableInfo(wmi);//添加主字段数据
        wri.setWorkflowBaseInfo(wbi);

        //执行创建流程接口
        WorkflowServiceImpl WorkflowServiceImpl = new WorkflowServiceImpl();
        String requestid = WorkflowServiceImpl.doCreateWorkflowRequest(wri,1);
        if(Util.getIntValue(requestid,0)>0){
            System.out.println("流程触发成功,流程id为"+requestid);
        }else{
            System.out.println( "流程触发失败,"+requestid);
        }

    }






    /** 发送短信
     * @param phoneNumber  手机号码
     * @param content      短信内容
     * @throws Exception
     */

    public boolean SMS(String phoneNumber,String content){
        try {
            String smskey = "1h6RQ1kZXWSDpOkJ";
            String smsdxuserName = "chengying";
            String smsuserPassword = "v869qoWH";
            String dxurl = "http://apis.hzfacaiyu.com/sms/openCard";

            if ("".equals(phoneNumber) || "null".equals(phoneNumber)) {
                phoneNumber = "11111111111";
            }
            APIHttpClient ac = new APIHttpClient(dxurl);
            JSONObject params = new JSONObject();
            String s = (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
            s = s + (int)(Math.random() * 900.0D + 100.0D);
            params.put("userPassword", smsuserPassword);
            Map parameters = new HashMap();
            parameters.put("tradeNo", s);
            parameters.put("userName", smsdxuserName);
            parameters.put("userPassword", smsuserPassword);
            parameters.put("phones", phoneNumber);
            parameters.put("content",content);
            parameters.put("etnumber", "");
            JSONObject jsonObject = JSONObject.fromObject(parameters);
            String sign = encrypt(jsonObject.toString(), smskey);
            parameters.put("sign", sign);
            parameters.put("userPassword", MD5(smsuserPassword));
            params.put("tradeNo", s);
            params.put("userPassword", MD5(smsuserPassword));
            params.put("userName", smsdxuserName);
            params.put("etnumber", "");
            params.put("phones", phoneNumber);
            params.put("content",content);
            params.put("sign", sign);
            System.out.println(ac.post(params.toString()));
        } catch (Exception var35) {
            var35.printStackTrace();
        }
        return true;
    }


    public static String MD5(String sourceStr) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (Exception var7) {
            System.out.println(var7);
        }

        return result;
    }


    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength += blockSize - plaintextLength % blockSize;
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(password.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(password.getBytes());
            cipher.init(1, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return parseByte2HexStr(encrypted);
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }


    public static long xc(String myString) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date now1 = new Date();
        String now = sdf.format(now1);
        Date date = null;

        try {
            now1 = sdf.parse(now);
            date = sdf.parse(myString);
        } catch (ParseException var13) {
            var13.printStackTrace();
        }

        long l = now1.getTime() - date.getTime();
        long day = l / 86400000L;
        long mon = day / 30L;
        long year = mon / 12L;
        return day;
    }


    public static String js(int i, int j, String pj) {
        int pd = i + j;
        String month = "";
        if (pd > 12) {
            pd -= 12;
        }

        if (pd < 10) {
            month = "0" + pd + "-" + pj;
        }

        if (pd >= 10) {
            month = pd + "-" + pj;
        }

        return month;
    }


}
