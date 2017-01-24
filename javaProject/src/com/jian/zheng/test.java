package com.jian.zheng;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class test {

	public static void main(String[] args){
		String sheetType="057";
		String serviceType="8";
		String serialNo="000201604200803061308";
		String serSupplier="ZhiZhen";
		String serCaller="JT_EOMS";

		String callerPwd="123321";
		String callTime="";
		String opTimeStamp="";

		String attachRef="";
		String opPerson="quweizheng03061308";
		String opCorp="北京公司";
		String opDepart="ydjtywb";

		String opContact="15012341234";
		String opTime="2016-05-26 15:18:28";
		String opDetail="<opDetail><fieldInfo><fieldChName>操作说明</fieldChName><fieldEnName>opDesc</fieldEnName><fieldContent><opDesc><fieldInfo><fieldChName>操作描述</fieldChName><fieldEnName>descOutline</fieldEnName><fieldContent>集团客户部派发</fieldContent></fieldInfo><recordInfo><fieldInfo><fieldChName>处理人</fieldChName><fieldEnName>name</fieldEnName><fieldContent>承载传输T1组</fieldContent></fieldInfo><fieldInfo><fieldChName>单位及部门</fieldChName><fieldEnName>department</fieldEnName><fieldContent>集团公司</fieldContent></fieldInfo><fieldInfo><fieldChName>联系电话</fieldChName><fieldEnName>contactPhone</fieldEnName><fieldContent>13910651105</fieldContent></fieldInfo></recordInfo></opDesc></fieldContent></fieldInfo></opDetail>";		
//		String opDetail="<opDetail><fieldInfo><fieldChName>操作说明</fieldChName><fieldEnName>opDesc</fieldEnName><fieldContent><opDesc><fieldInfo><fieldChName>操作描述</fieldChName><fieldEnName>descOutline</fieldEnName><fieldContent>集团客户部派发</fieldContent></fieldInfo><recordInfo><fieldInfo><fieldChName>单位及部门</fieldChName><fieldEnName>department</fieldEnName><fieldContent>集团公司</fieldContent></fieldInfo><fieldInfo><fieldChName>联系电话</fieldChName><fieldEnName>contactPhone</fieldEnName><fieldContent>13910651105</fieldContent></fieldInfo></recordInfo></opDesc></fieldContent></fieldInfo></opDetail>";		

		chuanshuNotifyWorkSheet( sheetType,  serviceType,
				 serialNo,  serSupplier,  serCaller,
				 callerPwd,  callTime,  opTimeStamp,
				 attachRef,  opPerson,  opCorp,  opDepart,
				 opContact,  opTime,  opDetail);
	}
	
	//工单阶段回复服务接口（notifyWorkSheet） 传输专线投诉工单
	public static String  chuanshuNotifyWorkSheet(String sheetType, String serviceType,
			String serialNo, String serSupplier, String serCaller,
			String callerPwd, String callTime, String opTimeStamp,
			String attachRef, String opPerson, String opCorp, String opDepart,
			String opContact, String opTime, String opDetail){
		
	          SAXReader reader = new SAXReader();
			  Document document = null;
			  try{
					document = reader.read(new StringReader(opDetail));
				}catch (Exception e) {
					String error = "解析opDetail出错:"+e;
					return "opDetail详细信息格式错误";
				}
			  Element root = document.getRootElement();		  
			  System.out.println("qqqq:"+root.getStringValue());
	          String opdesc = null;
	          String fieldContent = null;
			    if(root.selectSingleNode("fieldInfo[fieldEnName='opDesc']/fieldContent")!=null){
			    	opdesc = root.selectSingleNode("fieldInfo[fieldEnName='opDesc']/fieldContent").getText();
			    	fieldContent = root.selectSingleNode("fieldInfo[fieldEnName='opDesc']/fieldContent").getStringValue();
			    	System.out.println(fieldContent);
			    }
				
	            
                String name ="";
				if(root.selectSingleNode("fieldInfo[fieldEnName='name']/fieldContent")!=null){
					name = root.selectSingleNode("fieldInfo[fieldEnName='name']/fieldContent").getText();
					System.out.println(" 下一环节:"+name);
				}	  
				String contactPhone = "";
				if(root.selectSingleNode("fieldInfo[fieldEnName='contactPhone']/fieldContent")!=null){
					contactPhone = root.selectSingleNode("fieldInfo[fieldEnName='contactPhone']/fieldContent").getText();
					System.out.println(" 电话:"+contactPhone);
				}	  
				String department = "";
				if(root.selectSingleNode("fieldInfo[fieldEnName='department']/fieldContent")!=null){
					opdesc = root.selectSingleNode("fieldInfo[fieldEnName='opDesc']/fieldContent").getText();
					System.out.println(" 单位:"+department);
				}	  
				String opDesc = "";
				opDesc = " 当前处理人:"+opPerson+" 电话:"+opContact+" 单位:"+opCorp+" 部门:"+opDepart+"<br>";
				if (!name.equals("")){
					opDesc +=" 下一环节:"+name;
				}
				if (!contactPhone.equals("")){
					opDesc +=" 电话:"+contactPhone;
				}
				if (!department.equals("")){
					opDesc +=" 单位:"+department;
				}
				System.out.println("办理内容："+opDesc);
                
				return "0";
				
	       }

}