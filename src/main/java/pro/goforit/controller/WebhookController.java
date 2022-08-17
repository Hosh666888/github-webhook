package pro.goforit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pro.goforit.domain.MainInfo;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;

/**
 * @author: Double>J
 * @email: zjj20001031@foxmail.com
 * @editTime: 2022/8/16 16:54
 * @desc:
 **/
@RestController
@Slf4j
public class WebhookController {

    @Resource
    private RestTemplate restTemplate;

    @PostMapping("/webhook")
    public void webhook(@RequestBody String payload){

        MainInfo mainInfo = JSON.parseObject(payload, MainInfo.class);

        log.info(mainInfo.toString());

        send2FeiShu(mainInfo);

    }


    private void send2FeiShu(MainInfo content){

        String url = "https://open.feishu.cn/open-apis/bot/v2/hook/cd0baccb-596f-45af-b07a-be614840e43c";


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg_type","text");
        jsonObject.put("content",JSON.toJSONString(content));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<JSONObject> entity = new HttpEntity<>(jsonObject,headers);

        ResponseEntity<Void> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, void.class);
        }catch (Exception e){
            log.error("发送飞书失败,"+e.getMessage(),e);
            throw new RuntimeException("发送飞书失败");
        }

        responseEntity.getBody();


        log.info("成果发送飞书");

    }






}
