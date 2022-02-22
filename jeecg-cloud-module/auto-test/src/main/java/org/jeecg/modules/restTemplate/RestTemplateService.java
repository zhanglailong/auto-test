package org.jeecg.modules.restTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 接口转发
 *
 * @author zlf
 * @version V1.0
 * @date 2021/4/14
 */
@Service
@Slf4j
public class RestTemplateService {
    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取Headers
     *
     * @param type 0为token 1为其他
     * @return HttpHeaders 头部信息
     */
    public HttpHeaders getHeaders(int type) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(mediaType);
        //0为token 1为其他
        if (CommonConstant.DATA_INT_0 == type) {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        }
        headers.add("accept", "application/json");
        return headers;
    }


    /**
     * 发送数据
     *
     * @param url     请求接口地址
     * @param headers 请求头
     * @param t       请求体参数
     * @param type    请求方式
     * @return 返回值
     */
    public String postHeadersT(String url, HttpHeaders headers, Object t, HttpMethod type) {
        try {
            log.info("restTemplate 接口,url:" + url);
            HttpEntity<Object> entity;
            if (t != null) {
                entity = new HttpEntity<>(t, headers);
            } else {
                entity = new HttpEntity<>(headers);
            }
            ResponseEntity<String> responseEntity;
            //将请求头部和参数合成一个请求
            responseEntity = restTemplate.exchange(url, type, entity, String.class);
            //执行HTTP请求，将返回的结构使用ResultVO类格式化
            String resultData = responseEntity.getBody();
            log.info("返回数据：" + JSON.toJSONString(resultData));
            if (StringUtils.isNotBlank(resultData)) {
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty()) {
                    if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                            || CommonConstant.DATA_STR_200.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())){
                        return resultData;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("发送数据异常,url为" + url + "异常原因为:" + e);
            return null;
        }
    }
}
