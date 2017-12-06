package com.lccf.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 17:30
 * @see
 */
public class StringUtil {

  public static String transContent(String content,String args){
     if(StringUtils.isEmpty(content))return "";
     if(StringUtils.isEmpty(args))return content;
     String[] arg = args.split(",");
     for (int i=0 ;i<arg.length;i++){
       content = content.replace("@"+(i+1)+"@",arg[i]);
     }
     return  content;
  }
}
