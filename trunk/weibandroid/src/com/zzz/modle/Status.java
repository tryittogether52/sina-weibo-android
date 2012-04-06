package com.zzz.modle;

import java.util.Date;


public class Status implements java.io.Serializable  {
	private static final long serialVersionUID = -8795691786466526420L;

	private User user = null;                            //作者信息
	private Date createdAt;                              //status创建时间
	private String id;                                   //status id
	private String mid;                                  //微博MID
	private long idstr;                                  //保留字段，请勿使用                     
	private String text;                                 //微博内容
	private Source source;                               //微博来源
	private boolean favorited;                           //是否已收藏
	private boolean truncated;
	private long inReplyToStatusId;                      //回复ID
	private long inReplyToUserId;                        //回复人ID
	private String inReplyToScreenName;                  //回复人昵称
	private String thumbnailPic;                         //微博内容中的图片的缩略地址
	private String bmiddlePic;                           //中型图片
	private String originalPic;                          //原始图片
	private Status retweetedStatus = null;               //转发的博文，内容为status，如果不是转发，则没有此字段
	private String geo;                                  //地理信息，保存经纬度，没有时不返回此字段
	private double latitude = -1;                        //纬度
	private double longitude = -1;                       //经度
	private int repostsCount;                            //转发数
	private int commentsCount;                           //评论数
	private String annotations;                          //元数据，没有时不返回此字段
	private int mlevel;

}
