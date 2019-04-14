package cn.buk.api.pay.weixinpay.service;

import cn.buk.api.pay.weixinpay.JsApiParam;
import cn.buk.api.pay.weixinpay.UnifiedOrderResData;
import cn.buk.api.pay.weixinpay.WxpConfig;
import cn.buk.api.pay.weixinpay.WxpData;
import com.tencent.WXPay;
import com.tencent.common.RandomStringGenerator;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeixinPayServiceImpl implements WeixinPayService {

  private static final Logger logger = Logger.getLogger(WeixinPayServiceImpl.class);

  @Override
  public JsApiParam generateWxpJsApiParam(WxpConfig config, String openid, String tradeNo, final int totalFee, String memo, String remoteAddr) {

    String localPath = this.getClass().getResource("/").getPath();
    String certLocalPath = localPath + config.getCertFilename();

    WxpData request = new WxpData();

    String appid0;

      WXPay.initSDKConfiguration(config.getWxpKey(), config.getAppId(), config.getMchId(), "", certLocalPath, config.getCertPassword());
      appid0 = config.getAppId();
    request.setMap("appid", appid0);


    request.setMap("device_info", "WEB");
    request.setMap("body", memo);
    request.setMap("attach", "BUK");

    request.setMap("total_fee", totalFee);


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

//    Date startTime = DateUtil.getCurDateTime();
    LocalDateTime startTime = LocalDateTime.now();
//    String temp = DateUtil.formatDate(startTime, "yyyyMMddHHmmss");
    request.setMap("time_start", startTime.format(dateTimeFormatter));

    request.setMap("out_trade_no", tradeNo);

    LocalDateTime expireTime = startTime.plusMinutes(10);
//    Date expireTime = DateUtil.addMinutes(startTime, 10);
    request.setMap("time_expire", expireTime.format(dateTimeFormatter));

    //request.setGoods_tag("test");
    request.setMap("trade_type", "JSAPI");
    request.setMap("openid", openid);

    request.setMap("notify_url", config.getWxpNotifyUrl());

    request.setMap("mch_id", config.getMchId());
    request.setMap("spbill_create_ip", remoteAddr);
    request.setMap("nonce_str", RandomStringGenerator.getRandomStringByLength(32));

    String sign = request.makeSign(config.getWxpKey());
    request.setMap("sign", sign);

    String xml = null;
    try {
      logger.info("Unified Order Request:");
      xml = WXPay.requestUnifiedOrderService(request);
    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.info("Unified Order Response:");
    logger.info(xml);


    ///////////
    // 生成JS需要的签名
    JsApiParam params = new JsApiParam();
    WxpData result = new WxpData();
    try {
      UnifiedOrderResData res = UnifiedOrderResData.fromXml(xml);
      if (res.getReturn_code().equalsIgnoreCase("SUCCESS")) {
//                result.setMap("appId", appId);
        result.setMap("appId", appid0);

//        LocalDateTime.now().

//        Instant.now().getEpochSecond()

//        final String timeStamp = "" + DateUtil.getCurDateTime().getTime() / 1000;
        final String timeStamp = "" + Instant.now().getEpochSecond();
        result.setMap("timeStamp", timeStamp);

        final String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
        result.setMap("nonceStr", nonceStr);

        final String packageA = "prepay_id=" + res.getPrepay_id();
        result.setMap("package", packageA);

        final String signType = "MD5";
        result.setMap("signType", "MD5");

        final String sign1 = result.makeSign(config.getWxpKey()); //使用微信支付密码 签名
        result.setMap("paySign", sign1);

        logger.info("JsApi paras:");
        logger.info(result.toXml());

        params.setAppId(appid0);
        params.setTimeStamp(timeStamp);
        params.setNonceStr(nonceStr);
        params.setPackageA(packageA);
        params.setSignType(signType);
        params.setPaySign(sign1);
      }
    } catch (IOException | SAXException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    return params;
  }
}
