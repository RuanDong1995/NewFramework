package com.beecampus.user.vo;

import com.beecampus.common.Constant;
import com.beecampus.model.dto.user.QueryPersonalInfoDTO;
import com.beecampus.model.vo.School;

/*******************************************************************
 * UserInfo.java  2018/12/17
 * <P>
 * 用户信息<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class UserInfo {

    /**
     * 用户ID
     */
    public long userId;

    /**
     * 手机号
     */
    public String phone;

    /**
     * 真实姓名
     */
    public String realName;

    /**
     * 昵称
     */
    public String nickName;

    /**
     * 是否实名
     */
    public boolean isCertificate;

    /**
     * 是否注销
     */
    public boolean isWriteOff;

    /**
     * 是否被冻结
     */
    public boolean isBlack;

    /**
     * 头像地址
     */
    public String headUrl;

    /**
     * 学校id
     */
    public long schoolId;

    /**
     * 校区ID
     */
    public long campusId;

    /**
     * 学校名称
     */
    public String schoolName;

    /**
     * 校区名称
     */
    public String campusName;

    public UserInfo(long userId, QueryPersonalInfoDTO.Response response) {
        this.userId = userId;
        this.realName = response.real_name;
        this.nickName = response.nick_name;
        this.isCertificate = response.is_certificate == Constant.TRUE;
        this.isWriteOff = response.is_write_off == Constant.TRUE;
        this.isBlack = response.is_black == Constant.TRUE;
        this.headUrl = response.head_pic;
        if (response.school != null) {
            this.schoolId = response.school.school_id;
            this.schoolName = response.school.school;
            this.campusId = response.school.campus_id;
            this.campusName = response.school.campus_name;
        }
    }

}
