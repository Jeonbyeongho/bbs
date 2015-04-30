<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <script src="/bbs/resources/ckeditor/ckeditor.js"></script>   <!-- ck에디터 -->
  <title>게시판 작성부</title>
  </head>
  <body>
  
  <form id="form" method="post" action="./write_ok">
  <input type="hidden" name="idx" id="idx" value="${object.idx}" />
  <div>
  <span>제목</span>
  <input type="text" id="subject" name="subject" value="${object.subject}" />
  </div>
  <div>
  <span>작성자</span>
  <input type="text" id="user_name" name="user_name" value="${object.user_name}" />
  </div>
  <div>
  <span>내용</span>
  <textarea id="content" name="content" rows="10" cols="20">${object.content}</textarea>
  </div>

  <div>
  <button id="save" type="button" onclick="form_save('#form');">저장</button>
  <a href="./">목록</a>
  </div>
  </form>
  
  <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
  <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script src="./resources/jstemp/jstemp.js"></script>
  </body>
</html>
