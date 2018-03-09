package com.hssy.hssy.config;

import java.util.ArrayList;

/**
 *
 * 登录到期时间 2018-01-18 需重新登录
 * .bkt.clouddn.com为新创建存储空间后系统默认为用户生成的测试域名，此类测试域名，限总流量，限单 IP 访问频率，限速，仅供测试使用。
 * 单IP每秒限制请求次数10次，大于10次403禁止5秒。
 * 单url限速8Mbps，下载到10MB之后，限速1Mbps。
 *
 *
 */

public class ConstantsImageUrl {
    // 电影栏头部的图片
    public static final String ONE_URL_01 = "https://jsnt2018.com/examples/hunsha/psb1.jpg";

    // 头像
    public static final String IC_AVATAR = "https://jsnt2018.com/examples/hunsha/psb2.jpg";

    // 过渡图的图片链接
    private static final String TRANSITION_URL_01 = "https://jsnt2018.com/examples/hunsha/psb2.jpg";
    private static final String TRANSITION_URL_02 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String TRANSITION_URL_03 = "https://jsnt2018.com/examples/hunsha/psb6.jpg";
    private static final String TRANSITION_URL_04 = "https://jsnt2018.com/examples/hunsha/psb9.jpg";
    private static final String TRANSITION_URL_05 = "https://jsnt2018.com/examples/hunsha/psb11.jpg";
    private static final String TRANSITION_URL_06 = "https://jsnt2018.com/examples/hunsha/psb15.jpg";

    public static final String[] TRANSITION_URLS = new String[]{
            TRANSITION_URL_01, TRANSITION_URL_02, TRANSITION_URL_03
            , TRANSITION_URL_04, TRANSITION_URL_05, TRANSITION_URL_06
    };

    // 2张图的随机图
    private static final String HOME_TWO_01 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_02 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_03 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_04 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_05 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_06 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_07 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_08 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static final String HOME_TWO_09 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    public static final String[] HOME_TWO_URLS = new String[]{
            HOME_TWO_01, HOME_TWO_02, HOME_TWO_03, HOME_TWO_04
            , HOME_TWO_05, HOME_TWO_06, HOME_TWO_07, HOME_TWO_08
            , HOME_TWO_09
    };

    /**
     * 一张图的随机图
     */
    private static final String HOME_ONE_1 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";

    private static ArrayList<String> oneList;

    private static ArrayList<String> getOneUrl() {
//        DebugUtil.error("oneList == null:   " + (oneList == null));
        if (oneList == null) {
            synchronized (ArrayList.class) {
                if (oneList == null) {
                    oneList = new ArrayList<>();
                    for (int i = 1; i < 13; i++) {
                        oneList.add("https://jsnt2018.com/examples/hunsha/psb" + i + ".jpg");
                    }
                    return oneList;
                }
            }
        }
        return oneList;
    }

    // 一张图的随机图
    public static final String[] HOME_ONE_URLS = new String[]{
            getOneUrl().get(0), getOneUrl().get(1), getOneUrl().get(2), getOneUrl().get(3)
            , getOneUrl().get(4), getOneUrl().get(5), getOneUrl().get(6), getOneUrl().get(7)
            , getOneUrl().get(8), getOneUrl().get(9), getOneUrl().get(10), getOneUrl().get(11)
    };


    //-----------------------------------------------------------------------------
    // 1 -- 23
    private static final String HOME_SIX_1 = "https://jsnt2018.com/examples/hunsha/psb4.jpg";
    private static ArrayList<String> sixList;
//    https://jsnt2018.com/examples/hunsha/psb
    private static ArrayList<String> getSixUrl() {
//        DebugUtil.error("sixList == null:   " + (sixList == null));
        if (sixList == null) {
            synchronized (ArrayList.class) {
                if (sixList == null) {
                    sixList = new ArrayList<>();
                    for (int i = 1; i < 24; i++) {
                        sixList.add("https://jsnt2018.com/examples/hunsha/psb" + i + ".jpg");
                    }
                    return sixList;
                }
            }
        }
        return sixList;
    }

    // 六图的随机图
    public static final String[] HOME_SIX_URLS = new String[]{
            getSixUrl().get(0), getSixUrl().get(1), getSixUrl().get(2), getSixUrl().get(3)
            , getSixUrl().get(4), getSixUrl().get(5), getSixUrl().get(6), getSixUrl().get(7)
            , getSixUrl().get(8), getSixUrl().get(9), getSixUrl().get(10), getSixUrl().get(11)
            , getSixUrl().get(12), getSixUrl().get(13), getSixUrl().get(14), getSixUrl().get(15)
            , getSixUrl().get(16), getSixUrl().get(17), getSixUrl().get(18), getSixUrl().get(19)
            , getSixUrl().get(20), getSixUrl().get(21), getSixUrl().get(22)
    };
}
