package org.jeecg.modules.nodeserve.service;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;

/**
 * 接口转发
 * @author yeyl
 * @version V1.0
 * @date 2021/8/17
 */
@Service
@Slf4j
public class NodeService {
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private INodeLogService iNodeLogService;



    /**
     * 获取Headers
     * @return HttpHeaders 头部信息
     */
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(mediaType);
        headers.add("accept","application/json");
        return headers;
    }



    /**
     * 发送数据
     * @param url 请求接口地址
     * @param headers 请求头
     * @param t 请求体参数
     * @param type  请求方式
     * @return 返回值
     */
    public String postHeadersT(String url, HttpHeaders headers, Object t, HttpMethod type) {
        try {
            log.info("restTemplate接口,url:"+url);
            HttpEntity<Object> entity ;
            if (t != null){
                entity = new HttpEntity<>(t,headers);
            }else {
                entity = new HttpEntity<>( headers);
            }
            ResponseEntity<String> responseEntity;
            //将请求头部和参数合成一个请求
            responseEntity =restTemplate.exchange(url,type,entity, String.class);
            //执行HTTP请求，将返回的结构使用ResultVO类格式化
            log.info("返回数据："+ JSON.toJSONString(responseEntity.getBody()));
            return responseEntity.getBody();
        }catch (Exception e){
            log.error("发送数据异常,url为"+url+"异常原因为:"+e);
            return null;
        }finally {
            iNodeLogService.saveOne(url,JSON.toJSONString(t),type.toString());
        }
    }
}
