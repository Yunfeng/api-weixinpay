package cn.buk.api.pay.weixinpay.service;

import cn.buk.api.pay.weixinpay.JsApiParam;
import cn.buk.api.pay.weixinpay.WxpConfig;

public interface WeixinPayService {

  /**
   * 生成微信支付需要的JsApi参数
   * @param openid
   * @param tradeNo
   * @param totalFee 支付的金额，单位分
   * @param remoteAddr
   * @return
   */
  JsApiParam generateWxpJsApiParam(WxpConfig config, final String openid, final String tradeNo, final int totalFee, String memo, final String remoteAddr);
}
