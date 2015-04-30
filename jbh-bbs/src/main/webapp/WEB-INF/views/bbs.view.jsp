<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="./resources/bootstrap/css/bootstrap.min.css">
    <title>게시판 내용부</title>
  </head>
  <body>
    
    <form id="form" name="form" method="post" action="./delete">
      <input type="hidden" id="idx" name="idx" value="${object.idx}" />
    </form>
  
    <div>
    <p>제목 : ${object.subject}</p>
    <p>내용 : ${object.content}</p>
    <p>작성자 : ${object.user_name}</p>
    <p>등록일 : ${object.reg_datetime}</p>
    <p>조회수 : ${object.read_count}</p>
    </div>
    
    <div>
      <button type="button" class="btn btn-default" onclick="del(${object.idx})">삭제</button>
      <button type="button" class="btn btn-default"  onclick="location.href='./write?idx=${object.idx}'">수정</button> 
      <button type="button" class="btn btn-default"  onclick="location.href='./'">목록</button>
    </div>
    
    <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
    <script src="./resources/jstemp/jstemp.js"></script>
  </body>
</html>