package com.lccf.base.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Function:(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2017/10/19 18:24
 * @see
 */
@RestController
public class DownloadController  extends BaseController{
  @RequestMapping(value = "/download", method = RequestMethod.GET)
  @ApiOperation(value = "文件下載", httpMethod = "GET", response = byte.class)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "fileName", value = "文件名称", paramType = "query"),
      @ApiImplicitParam(name = "fileType", value = "文件类型", paramType = "query")
  })
  public ResponseEntity<InputStreamResource> downloadFile(String fileName, String fileType)
      throws IOException {
    String filePath = fileName+"."+fileType;
    ClassPathResource file = new ClassPathResource("down/"+filePath);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");
    return ResponseEntity
        .ok()
        .headers(headers)
        .contentLength(file.contentLength())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(new InputStreamResource(file.getInputStream()));
  }
}
