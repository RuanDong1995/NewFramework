package com.beecampus.model.dto;

/*******************************************************************
 * UploadImageDTO.java  2018/12/14
 * <P>
 * 上传图片<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface UploadImageDTO {

    class Request{
        /**
         * 图像base64数据
         */
        public String img_data;

        /**
         * 图片类型
         */
        public String img_type;
    }

    class Response{
        /**
         * 图片路径
         */
        public String img_path;
    }
}
