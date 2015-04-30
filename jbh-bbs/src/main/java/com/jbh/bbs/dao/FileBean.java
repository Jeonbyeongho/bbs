package com.jbh.bbs.dao;

import org.springframework.web.multipart.MultipartFile;
//파일정보를 담을 파일 빈
public class FileBean {
     private String attach_path;
     private MultipartFile upload;
     private String filename;
     private String CKEditorFuncNum;

     public String getAttach_path() {
          return this.attach_path;
     }

     public void setAttach_path(String attach_path) {
          this.attach_path = attach_path;
     }

     public MultipartFile getUpload() {
         return upload;
     }

     public void setUpload(MultipartFile upload) {
         this.upload = upload;
     }

     public String getFilename() {
          return this.filename;
     }
     public void setFilename(String filename) {
          this.filename = filename;
     }

     //CKEditorFuncNum은 CKEditor 대상을 기억하기 위한 값을 저장.
     public String getCKEditorFuncNum() {
          return this.CKEditorFuncNum;
     }
     public void setCKEditorFuncNum(String CKEditorFuncNum) {
          this.CKEditorFuncNum = CKEditorFuncNum;
     }
}
