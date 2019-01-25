package com.beecampus.model.remote.http;

/*******************************************************************
 * ReturnCode.java 2017/8/9
 * <P>
 * 返回码表<br/>
 * <br/>
 * </p>
 * Copyright2017 by GNNT CompanyVO. All Rights Reserved.
 *
 * @author:Zhoupeng
 ******************************************************************/
public interface ReturnCode {

    /**
     * 无效的用户
     */
    int USER_INVALID = -403;

    /**
     * Token 过期
     */
    int TOKEN_INVALID = -10003;
}
