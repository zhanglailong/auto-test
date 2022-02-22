/*
package org.jeecg.modules.task.dispatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.cloud.entity.RunningCloudKlocCase;
import org.jeecg.modules.cloud.entity.RunningCloudKlocQue;
import org.jeecg.modules.cloud.service.IRunningCloudKlocCaseService;
import org.jeecg.modules.cloud.service.IRunningCloudKlocQueService;
import org.jeecg.modules.cloudtools.CaseType;
import org.jeecg.modules.cloudtools.Cases;
import org.jeecg.modules.cloudtools.runex.entity.RunCase;
import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;
import org.jeecg.modules.cloudtools.runex.entity.RunQue;
import org.jeecg.modules.cloudtools.runex.service.IRunCaseService;
import org.jeecg.modules.cloudtools.runex.service.IRunParamsSetService;
import org.jeecg.modules.cloudtools.runex.service.IRunQueService;
import org.jeecg.modules.message.smartsocket.SocketHandler;
import org.jeecg.modules.message.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
@Service("taskDispatcher")
public class TaskDispatcher {
	@Autowired
	private IRunningCloudKlocCaseService runningCloudKlocCaseService;
	
	@Autowired
	private IRunningCloudKlocQueService runningCloudKlocQueService;
	
	@Autowired
	private IRunQueService runQueService;
	
	@Autowired
	private IRunCaseService runCaseService;
	
	@Autowired
	private IRunParamsSetService runParamSetService;
	
	private final String SIGN="200";
	private final String CODE="code";
	
	
	//kloc删除project
	public boolean delKlocProject(String ip,String name){
		
		List<String> list = null;
		if(SocketHandler.getPortMap()!=null&& SocketHandler.getPortMap().containsKey(ip)) {
			byte[] body = HttpUtil.getBytes(String.format("http://%s:%s/get/kloc/delproj?name=%s", ip,SocketHandler.getPortMap().get(ip),name));
			JSONObject json = JSONObject.parseObject(body,JSONObject.class);
			
			if(SIGN.equals(json.getString(CODE))) {
				if(SocketHandler.getKlocNameMap().containsKey(ip)&&
						SocketHandler.getKlocNameMap().get(ip).contains(name)) {
					SocketHandler.getKlocNameMap().get(ip).remove(name);
				}
				return true;
			}
		}
		
		
		return false;
	}
	
	public String getKlocLog(String ip,String id) {
		
		byte[] body = HttpUtil.getBytes(String.format("http://%s:%s/get/log?filePath=kloc/%s",
				ip,SocketHandler.getPortMap().get(ip),id));
		return new String(body);
	}
	
	//kloc获取project列表
	@Async
	public List<String> getKlocProjectNames(String ip){
		
		List<String> list = null;
		
		byte[] body = HttpUtil.getBytes(String.format("http://%s:%s/get/kloc/proname", ip,SocketHandler.getPortMap().get(ip)));
		JSONObject json = JSONObject.parseObject(body,JSONObject.class);
		
		if(SIGN.equals(json.getString(CODE))) {
			list = new ArrayList<>(Arrays.asList(json.getString("names").split("\\$_\\$")));
			
			SocketHandler.getKlocNameMap().put(ip, list);
		}
		
		return list;
	}
	
	//kloc修改project名
	public boolean changeKlocProjectName(String ip,String name,String newName){
		
		byte[] body = HttpUtil.getBytes(String.format("http://%s:%s/get/kloc/changename?name=%s&newName=%s",ip,SocketHandler.getPortMap().get(ip),name,newName));
		
		JSONObject json = JSONObject.parseObject(body,JSONObject.class);
		
		if(SIGN.equals(json.getString(CODE))) {
			SocketHandler.getKlocNameMap().get(ip).remove(name);
			SocketHandler.getKlocNameMap().get(ip).add(newName);
			return true;
		}
		return false;
		
	}
	
	public void stop(String ip,CaseType caseType){
		JSONObject json = new JSONObject();
		json.put("type", caseType);
		json.put("commond", "stop");
		switch(caseType) {
		case Xcelium:
			
			break;
		case Kloc:
			//发送停止命令
			SocketHandler.write(ip, json.toJSONString());
			break;
		case TestBed:
			//发送停止命令
			SocketHandler.write(ip, json.toJSONString());
			break;
		default:
			break;
		}
	}
	
	public void forceStop(String ip,CaseType caseType){
		JSONObject json = new JSONObject();
		json.put("type", caseType);
		json.put("commond", "forcestop");
		switch(caseType) {
		case Xcelium:
			
			break;
		case Kloc:
			//发送停止命令
			SocketHandler.write(ip, json.toJSONString());
			break;
		case TestBed:
			break;
		default:
			break;
		}
	}
	
	private void nextKloc(String ip,CaseType caseType) {
		//从队列获取任务
		QueryWrapper<RunningCloudKlocQue> wrapper = new QueryWrapper<RunningCloudKlocQue>();
		wrapper.eq("client_ip", ip);
		wrapper.orderByDesc("priority_level").orderByDesc("create_time").last("limit 1");
		RunningCloudKlocQue que = runningCloudKlocQueService.getOne(wrapper);
		if(que == null) {
			SocketHandler.getSocketLock().get(ip).put(caseType, 0);
		}else {
			SocketHandler.getSocketLock().get(ip).put(caseType, 1);
			RunningCloudKlocCase cas = runningCloudKlocCaseService.getById(que.getKlocCaseId());
			startKlocTask(cas.getId(), cas, caseType, ip);
		}
	}
	
	//获取下一任务
	public void goNext(String ip,CaseType caseType) {
		
		if(caseType.equals(CaseType.Kloc)) {
			nextKloc(ip, caseType);
		}else {
			//从队列获取任务
			QueryWrapper<RunQue> wrapper = new QueryWrapper();
			wrapper.eq("client_ip", ip).eq("case_type", caseType.toString());
			wrapper.orderByDesc("priority_level").orderByDesc("create_time").last("limit 1");
			RunQue que = runQueService.getOne(wrapper);
			if(que == null) {
				SocketHandler.getSocketLock().get(ip).put(caseType, 0);
			}else {
				//SocketHandler.getSocketLock().get(ip).put(caseType, 1);
				RunCase cas = runCaseService.getById(que.getCaseId());
				startTask(cas.getId(), cas, caseType, ip);
			}
		}
		
	}
	
	private String getJsonStr(Object obj) {
		
		String str = JSONObject.toJSONString(obj);
		
		return str;
	}
	
	private JSONObject getJsonObj(Object obj) {
		
		return JSONObject.parseObject(getJsonStr(obj));
	}

	//获取选项以及子集的集合
	private List<RunParamsSet> orderList(List<RunParamsSet> list) {

		return list.stream().flatMap(item->{

			List<RunParamsSet> runList = null;

			if(item.getChildren()!=null){
				runList = orderList(item.getChildren());
			}else{
				runList = new ArrayList<>();
			}
			item.setChildren(null);
			runList.add(0,item);
			return runList.stream();
		}).collect(Collectors.toList());

	}
	
	//开始任务
	public boolean startTask(String taskId,Cases cs,CaseType type,String ip) {
		
		QueryWrapper<RunParamsSet> paramWrapper = new QueryWrapper<>();
		List<RunParamsSet> list = runParamSetService.getParamsList(paramWrapper.orderByAsc("`order`"));
		list = orderList(list);

		JSONObject json = getJsonObj(cs);
		JSONObject jsonParam = JSONObject.parseObject(json.getString("params"));
		StringBuilder cmd = new StringBuilder();
		
		list.stream().filter(item->jsonParam.containsKey(item.getId())&&filters(item.getType(),jsonParam.getString(item.getId())))
		.forEach(item->{
			if(type == CaseType.TestBed) {
				cmd.append(item.getParam());
			}else {
				//（1勾选、2选择、3手填,4:文件，5多选）
				switch(item.getType()) {
					case 1:
						cmd.append(item.getParam()).append(" ");
					break;
					case 2:
						
					case 3:
						if(!StringUtils.isEmpty(item.getParam())) {
							cmd.append(item.getParam());
						}
						if("2".equals(item.getColc())){
							cmd.append(" ");
						}
						cmd.append(jsonParam.getString(item.getId())).append(" ");
						break;
					case 5:
						if(!StringUtils.isEmpty(item.getParam())) {
							cmd.append(item.getParam());
						}
						if("2".equals(item.getColc())){
							cmd.append(" ");
						}
						cmd.append(jsonParam.getString(item.getId()).replace(",", "")).append(" ");
						break;
					case 4:
						json.put(item.getParam(), jsonParam.getString(item.getId()));
						break;
					
					default:
						break;
				}
			}
			
		});
		
		if(type == CaseType.TestBed) {
			cmd.append("q");
		}
		
		json.put("params", cmd);
		json.put("type", type);
		json.put("taskId", taskId);
		json.put("commond", "start");
		SocketHandler.write(ip, json.toJSONString());
		
		return true;
	}
	
	//（1勾选、2选择、3手填,4:文件，5多选）
	private Boolean filters(Integer type,String value) {
		
		switch(type) {
		case 1:
			return "1".equals(value);
		case 2:
		case 3:
		case 4:
		case 5:
			return !StringUtils.isEmpty(value);
		default:
			return false;
		}
	}
	
	//开始任务
	public boolean startKlocTask(String taskId,Cases cs,CaseType type,String ip) {
		JSONObject json = getJsonObj(cs);
		json.put("type", type);
		json.put("taskId", taskId);
		json.put("commond", "start");
		SocketHandler.write(ip, json.toJSONString());
		
		return true;
	}
	
	
}
*/
