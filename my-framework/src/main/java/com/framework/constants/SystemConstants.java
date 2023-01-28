package com.framework.constants;

/***
 * 项目中最好使用字面量，而非单纯的数字0/1/2
 * 提高可读性和可维护性
 */
public class SystemConstants {

    //文章为草稿
    public static final int ARTICLE_STATUS_DRAFT = 1;

    //文章为正常发布状态
    public static final int ARTICLE_STATUS_NORMAL = 0;

    //章节为正常状态
    public static final String STATUS_NORMAL = "0";

    //友链审核通过
    public static final String LINK_STATUS_NORMAL = "0";

    //评论类型为文章评论
    public static final String ARTICLE_COMMENT = "0";

    //评论类型为友链评论
    public static final String LINK_COMMENT = "1";
}
