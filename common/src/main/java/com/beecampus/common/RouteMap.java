package com.beecampus.common;

/*******************************************************************
 * RouteMap.java  2018/12/13
 * <P>
 * 路由表<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface RouteMap {


    interface User {
        /**
         * 登录页面
         */
        String LoginPage = "/user/LoginPage";

        /**
         * 短信登录页面
         */
        String LoginWithMessagePage = "/user/LoginWithMessagePage";

        /**
         * 注册页面
         */
        String RegisterPage = "/user/RegisterPage";

        /**
         * 重置密码
         */
        String ResetPasswordPage = "/user/ResetPasswordPage";

        /**
         * 编辑个人信息页面
         */
        String EditUserInfoPage = "/user/EditUserInfoPage";
    }

    interface Info {
        /**
         * 搜索历史记录
         */
        String SearchHistoryPage = "/info/SearchHistoryPage";

        /**
         * 跳蚤市场主页
         */
        String FleaMarketPage = "/info/FleaMarketPage";

        /**
         * 出租首页
         */
        String RentHomePage = "/info/RentHomePage";

        /**
         * 房屋首页
         */
        String HouseHomePage = "/info/HouseHomePage";

        /**
         * 信息主页
         */
        String InfoHomePage = "/info/InfoHomePage";

        /**
         * 信息主页
         */
        String InfoDetailPage = "/info/InfoDetailPage";
    }
}
