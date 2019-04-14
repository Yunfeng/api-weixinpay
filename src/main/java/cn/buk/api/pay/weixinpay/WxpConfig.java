package cn.buk.api.pay.weixinpay;

/**
 * 微信支付需要的参数
 */
public class WxpConfig {

  private String wxpKey;

  private String appId;

  private String mchId;

  private String certFilename;

  private String certPassword;

  private String wxpNotifyUrl;

  public String getWxpKey() {
    return wxpKey;
  }

  public void setWxpKey(String wxpKey) {
    this.wxpKey = wxpKey;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getMchId() {
    return mchId;
  }

  public void setMchId(String mchId) {
    this.mchId = mchId;
  }

  public String getCertFilename() {
    return certFilename;
  }

  public void setCertFilename(String certFilename) {
    this.certFilename = certFilename;
  }

  public String getCertPassword() {
    return certPassword;
  }

  public void setCertPassword(String certPassword) {
    this.certPassword = certPassword;
  }

  public String getWxpNotifyUrl() {
    return wxpNotifyUrl;
  }

  public void setWxpNotifyUrl(String wxpNotifyUrl) {
    this.wxpNotifyUrl = wxpNotifyUrl;
  }
}
