package com.beecampus.model.remote;

import com.beecampus.model.dto.GetAllSchoolDTO;
import com.beecampus.model.dto.UploadImageDTO;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*******************************************************************
 * ConfigApi.java  2018/12/10
 * <P>
 * 配置类api<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface ConfigApi {

    /**
     * 查询所有学校
     *
     * @param params 参数
     * @return
     */
    @POST("runningman/get_all_schools")
    Single<ApiResponse<GetAllSchoolDTO.Response>> getAllSchools(@Body ApiRequest params);

    /**
     * 上传图片
     *
     * @param params 参数
     * @return
     */
    @POST("http://47.106.102.174:19002/common/upload_img")
    Single<ApiResponse<UploadImageDTO.Response>> uploadImage(@Body ApiRequest params);
}
