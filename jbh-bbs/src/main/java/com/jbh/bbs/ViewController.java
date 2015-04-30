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

//@RequestParam 요청된 파라미터에 값을 찾아 변수에 담는 역할
//RedirectAttributes 작업을 처리 후 리다이렉트 된 화면에 변수 값을 넘겨주는 역할
@Controller(value = "viewController")
public class ViewController {
    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    //BbsDao.java에서도 선언한 Resource 어노테이션을 이용하여 BbsDao 선언.
    //@Resource 어노테이션을 사용 하려면 name 속성에 자동으로 연결할 빈 객체의 이름을 입력 하면된다
    @Resource(name = "bbsDao")
    private BbsDao bbsDao;
    
    @Resource(name="messageSource")
	private MessageSource messageSource;

    //리퀘스트메소드: HTTP request 메소드값을 맵핑 조건으로 부여, 요청 메소드 값이 일치해야 맵핑이 이루어 짐.
    //@RequestMapping:요청에 대한 어떤 컨트롤러, 어떤 메소드가 처리할지를 맵핑해주는 annotation.
    // 게시판 목록
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dispBbsList(HttpServletRequest request, Model model) {
        logger.info("display view BBS list");
        
     // 검색을 위한
        String sch_type = request.getParameter("sch_type");
        String sch_value = request.getParameter("sch_value");
        Map mapSearch = new HashMap();
        mapSearch.put("sch_type", sch_type);
        mapSearch.put("sch_value", sch_value);
        // 검색 값을 뷰에 넘겨줌
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

         // 저장된 쿠키 불러오기 (저장할때는 response 불러올때는 request)
         Cookie cookies[] = request.getCookies();
         Map mapCookie = new HashMap();
        if(request.getCookies() != null){
          for (int i = 0; i < cookies.length; i++) {
            Cookie obj = cookies[i];
            mapCookie.put(obj.getName(),obj.getValue());
          }
        }

        // 저장된 쿠키중에 read_count 만 불러오기
         String cookie_read_count = (String) mapCookie.get("read_count");
         // 저장될 새로운 쿠키값 생성
        String new_cookie_read_count = "|" + idx;

        // 저장된 쿠키에 새로운 쿠키값이 존재하는 지 검사
         if ( StringUtils.indexOfIgnoreCase(cookie_read_count, new_cookie_read_count) == -1 ) {
              // 없을 경우 쿠키 생성
              Cookie cookie = new Cookie("read_count", cookie_read_count + new_cookie_read_count);
              //cookie.setMaxAge(1000); // 초단위
              response.addCookie(cookie);

              // 조회수 업데이트
              this.bbsDao.updateReadCount(idx);
         }

         BbsVo object = this.bbsDao.getSelectOne(idx);

         model.addAttribute("object", object);
         return "bbs.view";
    }

    //RequestMapping 에 {변수} 조건을 지정하고   메서드 상수에 @PathVariable 어노테이션을 정의해서 받아올 수 있다
    //URL(/)뒤에 {}로 컨트롤러 메소드의 파라미터로 받을 값의 변수명을 입력해주면 변수를 받는다.
    //@리퀘스트파람(파라미터): HTTP request 파라미터를 맵핑 조건으로 부여
    // 게시판 쓰기
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String dispBbsWrite(@RequestParam(value="idx", defaultValue="0") int idx, Model model) {
        logger.info("display view BBS write");    //defaultValue는 값이 empty인 경우 치환하는 작업을 한다.
                                                  //defaultValue를 지정하지 않을 경우 파라메터 값은 필수조건이기 때문에 예외 발생
        if (idx > 0) {
            BbsVo object = this.bbsDao.getSelectOne(idx);
            model.addAttribute("object", object);
        }

        return "bbs.write";
    }

    //@ModelAttribute는 파라메터 값을 Vo에 자동으로 맵핑하는 작업을 한다.
	//bbsVo를 유효성검사 대상으로 지정, 검사 결과를 result변수로 받을 수 있다.
    @RequestMapping(value = "/write_ok", method = RequestMethod.POST)
    public View procBbsWrite(@ModelAttribute("bbsVo") @Valid BbsVo bbsVo, BindingResult result,Model model) {
        XStream xst = xstreamMarshaller.getXStream();
        xst.alias("result", XmlResult.class);

        XmlResult xml = new XmlResult();
        
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yy-MM-dd HH:mm");
        bbsVo.setReg_datetime(new DateTime().toString(fmt));
        
        if(result.hasErrors()) {
        	  String message = messageSource.getMessage(result.getFieldError(), Locale.getDefault());
        	  
        	  List<FieldError> errors = result.getFieldErrors();    //result객체의 오류 필드를 목록 타입으로 받아 메세지 추출
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
                 xml.setMessage("추가되었습니다.");
                 xml.setError(false);
            } else {
                 this.bbsDao.update(bbsVo);
                 xml.setMessage("수정되었습니다.");
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
            xml.setMessage("삭제되었습니다.");
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
         String root_path = session.getServletContext().getRealPath("/"); // 웹서비스 root 경로
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