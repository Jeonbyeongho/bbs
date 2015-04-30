package com.jbh.bbs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.jbh.bbs.dao.*;
import com.thoughtworks.xstream.XStream;

//@RequestParam ��û�� �Ķ���Ϳ� ���� ã�� ������ ��� ����
//RedirectAttributes �۾��� ó�� �� �����̷�Ʈ �� ȭ�鿡 ���� ���� �Ѱ��ִ� ����
@Controller(value = "viewController")
public class ViewController {
    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    //BbsDao.java������ ������ Resource ������̼��� �̿��Ͽ� BbsDao ����.
    //@Resource ������̼��� ��� �Ϸ��� name �Ӽ��� �ڵ����� ������ �� ��ü�� �̸��� �Է� �ϸ�ȴ�
    @Resource(name = "bbsDao")
    private BbsDao bbsDao;
    
    @Resource(name="messageSource")
	private MessageSource messageSource;

    //������Ʈ�޼ҵ�: HTTP request �޼ҵ尪�� ���� �������� �ο�, ��û �޼ҵ� ���� ��ġ�ؾ� ������ �̷�� ��.
    //@RequestMapping:��û�� ���� � ��Ʈ�ѷ�, � �޼ҵ尡 ó�������� �������ִ� annotation.
    // �Խ��� ���
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dispBbsList(HttpServletRequest request, Model model) {
        logger.info("display view BBS list");
        
     // �˻��� ����
        String sch_type = request.getParameter("sch_type");
        String sch_value = request.getParameter("sch_value");
        Map mapSearch = new HashMap();
        mapSearch.put("sch_type", sch_type);
        mapSearch.put("sch_value", sch_value);
        // �˻� ���� �信 �Ѱ���
        model.addAttribute("mapSearch", mapSearch);

        List<BbsVo> list = this.bbsDao.getSelect(mapSearch);
        
        //List<BbsVo> list = this.bbsDao.getSelect();
        model.addAttribute("list", list);

        logger.info("totcal count" + list.size() );

        return "bbs.list";
    }
    @RequestMapping("/{idx}")
    public String dispBbsView(HttpServletResponse response, HttpServletRequest request, @PathVariable int idx, Model model) {
         logger.info("display view BBS view idx = {}", idx);

         // ����� ��Ű �ҷ����� (�����Ҷ��� response �ҷ��ö��� request)
         Cookie cookies[] = request.getCookies();
         Map mapCookie = new HashMap();
        if(request.getCookies() != null){
          for (int i = 0; i < cookies.length; i++) {
            Cookie obj = cookies[i];
            mapCookie.put(obj.getName(),obj.getValue());
          }
        }

        // ����� ��Ű�߿� read_count �� �ҷ�����
         String cookie_read_count = (String) mapCookie.get("read_count");
         // ����� ���ο� ��Ű�� ����
        String new_cookie_read_count = "|" + idx;

        // ����� ��Ű�� ���ο� ��Ű���� �����ϴ� �� �˻�
         if ( StringUtils.indexOfIgnoreCase(cookie_read_count, new_cookie_read_count) == -1 ) {
              // ���� ��� ��Ű ����
              Cookie cookie = new Cookie("read_count", cookie_read_count + new_cookie_read_count);
              //cookie.setMaxAge(1000); // �ʴ���
              response.addCookie(cookie);

              // ��ȸ�� ������Ʈ
              this.bbsDao.updateReadCount(idx);
         }

         BbsVo object = this.bbsDao.getSelectOne(idx);

         model.addAttribute("object", object);
         return "bbs.view";
    }

    //RequestMapping �� {����} ������ �����ϰ�   �޼��� ����� @PathVariable ������̼��� �����ؼ� �޾ƿ� �� �ִ�
    //URL(/)�ڿ� {}�� ��Ʈ�ѷ� �޼ҵ��� �Ķ���ͷ� ���� ���� �������� �Է����ָ� ������ �޴´�.
    //@������Ʈ�Ķ�(�Ķ����): HTTP request �Ķ���͸� ���� �������� �ο�
    // �Խ��� ����
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String dispBbsWrite(@RequestParam(value="idx", defaultValue="0") int idx, Model model) {
        logger.info("display view BBS write");    //defaultValue�� ���� empty�� ��� ġȯ�ϴ� �۾��� �Ѵ�.
                                                  //defaultValue�� �������� ���� ��� �Ķ���� ���� �ʼ������̱� ������ ���� �߻�
        if (idx > 0) {
            BbsVo object = this.bbsDao.getSelectOne(idx);
            model.addAttribute("object", object);
        }

        return "bbs.write";
    }

    //@ModelAttribute�� �Ķ���� ���� Vo�� �ڵ����� �����ϴ� �۾��� �Ѵ�.
	//bbsVo�� ��ȿ���˻� ������� ����, �˻� ����� result������ ���� �� �ִ�.
    @RequestMapping(value = "/write_ok", method = RequestMethod.POST)
    public View procBbsWrite(@ModelAttribute("bbsVo") @Valid BbsVo bbsVo, BindingResult result,Model model) {
        XStream xst = xstreamMarshaller.getXStream();
        xst.alias("result", XmlResult.class);

        XmlResult xml = new XmlResult();
        
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yy-MM-dd HH:mm");
        bbsVo.setReg_datetime(new DateTime().toString(fmt));
        
        if(result.hasErrors()) {
        	  String message = messageSource.getMessage(result.getFieldError(), Locale.getDefault());
        	  
        	  List<FieldError> errors = result.getFieldErrors();    //result��ü�� ���� �ʵ带 ��� Ÿ������ �޾� �޼��� ����
        	  for(FieldError error : errors){
        		  message = error.getObjectName() + " - " + error.getDefaultMessage();
        	  }
        	  
        	  xml.setMessage(message);
        	  xml.setError(true);
        	  model.addAttribute("xmlData", xml);
        	  return xmlView;
          }
        
        Integer idx = bbsVo.getIdx();
                
        try {
            if (idx == null || idx == 0) {
                 this.bbsDao.insert(bbsVo);
                 xml.setMessage("�߰��Ǿ����ϴ�.");
                 xml.setError(false);
            } else {
                 this.bbsDao.update(bbsVo);
                 xml.setMessage("�����Ǿ����ϴ�.");
                 xml.setError(false);
            }
       } catch(Exception e) {
            xml.setMessage(e.getMessage());
            xml.setError(true);
       }

        model.addAttribute("xmlData", xml);
        return xmlView;
    }

    @Resource(name = "xstreamMarshaller")
    private XStreamMarshaller xstreamMarshaller;

    @Resource(name = "xmlView")
    private View xmlView;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public View procBbsDelete(@RequestParam(value = "idx", required = true) int idx, Model model) {
    	XStream xst = xstreamMarshaller.getXStream();
    	xst.alias("result", XmlResult.class);
    	
        XmlResult xml = new XmlResult();
        
        try {
            this.bbsDao.delete(idx);
            xml.setMessage("�����Ǿ����ϴ�.");
            xml.setError(false);
       } catch (Exception e) {
            xml.setMessage(e.toString());
            xml.setError(true);
       }

        model.addAttribute("xmlData", xml);
        return xmlView;
    }
    
    @RequestMapping(value = "/file_upload", method = RequestMethod.POST)
    public String procFileUpload(FileBean fileBean,HttpServletRequest request, Model model) {

         HttpSession session = request.getSession();
         String root_path = session.getServletContext().getRealPath("/"); // ������ root ���
         String attach_path = "resources/files/attach/";

        MultipartFile upload = fileBean.getUpload();
        String filename = "";
        String CKEditorFuncNum = "";
        if (upload != null) {
            filename = upload.getOriginalFilename();
            fileBean.setFilename(filename);
            CKEditorFuncNum = fileBean.getCKEditorFuncNum();
            try {
                File file = new File(root_path + attach_path + filename);
                logger.info(root_path + attach_path + filename);
                upload.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String file_path = "/bbs/" + attach_path + filename;
        model.addAttribute("file_path", file_path);
        model.addAttribute("CKEditorFuncNum", CKEditorFuncNum);

         return "bbs.fileupload";
    }
    
}

class XmlResult {

    private String message;
    private boolean error = false;

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }

    public void setError(boolean error) {
        this.error = error;
    }
    public boolean getError() {
        return this.error;
    }
}