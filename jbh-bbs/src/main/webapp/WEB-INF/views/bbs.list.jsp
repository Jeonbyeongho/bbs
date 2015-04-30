<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="./resources/bootstrap/css/bootstrap.min.css">
    <title>게시판 목록부</title>
  </head>
  <body>
  <h1>${message}</h1>
  <div class="container">
      <h2>게시판</h2>
      <div class="table-responsive">          
      <table class="table table-striped">
  <thead>
    <tr>
      <th scope="col">번호</th>
      <th scope="col">제목</th>
      <th scope="col">작성자</th>
      <th scope="col">등록일</th>
      <th scope="col">조회수</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="item" items="${list}" varStatus="status"> 
    <tr>
        <td>${item.idx}</td>
        <td><a href="./${item.idx}">${item.subject}</a></td>
        <td>${item.user_name}</td>
        <td>${item.reg_datetime}</td>
        <td>${item.read_count}</td>
    </tr>
    </c:forEach>

    </tbody>

  </table>
   
  <form id="form_search" method="get" action="./"> 
    <button type="button" class="btn btn-default"  onclick="location.href='./write'">글쓰기</button>
    <span style="cursor: pointer;float: right;">  
    <select id="sch_type" name="sch_type">
       <option value="subject" selected="selected">제목</option>
       <option value="content">내용</option>
       <option value="user_name">작성자</option>
    </select>
    <input type="text" id="sch_value" name="sch_value" value="${mapSearch.sch_value}" />
    <button type="button" class="btn btn-default"  onclick="search();">검색</button>
    </span>
  </form>
  
  </div>
  </div>
  <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
  <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script src="./resources/jstemp/jstemp.js"></script>
  <script src="./resources/bootstrap/css/bootstrap.min.css"></script>
  
  </body>
</html>
