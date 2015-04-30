function search() {
            var sch_value = jQuery('#form_search #sch_value').val();
            if (sch_value == '') { alert('검색어를 입력하세요.'); }
            else {
                 jQuery('#form_search').submit();
            }
       }
       jQuery('#form_search #sch_type value').val('${mapSearch.sch_type}');
  
       
function del(idx) {
	jQuery.ajax({
		type : 'POST' , 
		url : './delete' ,
		data : 'idx=' + idx
		}).done(function(data) {
			var message = jQuery(data).find("message").text();
			var error = jQuery(data).find("error").text();
			alert(message);
			if (error == 'false') $(location).attr('href','./');  //페이지 이동
			});
	}


//ck에디터 세팅
var ckeditor_config = {
        resize_enabled : false, // 에디터 크기를 조절하지 않음
        enterMode : CKEDITOR.ENTER_BR , // 엔터키를 <br> 로 적용함.
        shiftEnterMode : CKEDITOR.ENTER_P ,  // 쉬프트 +  엔터를 <p> 로 적용함.
        toolbarCanCollapse : true , 
        removePlugins : "elementspath", // DOM 출력하지 않음
        filebrowserUploadUrl: '/bbs/file_upload', // 파일 업로드를 처리 할 경로 설정.

//에디터에 사용할 기능 정의
        toolbar : [
                   [ 'Source', '-' , 'NewPage', 'Preview' ],
                   [ 'Cut', 'Copy', 'Paste', 'PasteText', '-', 'Undo', 'Redo' ],
                   [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript'],
                   [ 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ],
                   '/',
                   [ 'Styles', 'Format', 'Font', 'FontSize' ],
                   [ 'TextColor', 'BGColor' ],
                   [ 'Image', 'Flash', 'Table' , 'SpecialChar' , 'Link', 'Unlink']

                 ]

               };

var editor = null;
jQuery(function() {
	editor = CKEDITOR.replace( "content" , ckeditor_config );   //ck에디터를 적용할 엘리먼트 설정.
});

// 전송을 위한 체크 함수
function form_save(form) {
	editor.updateElement();     //게시물이 저장되기 전 에디터에 입력된 값 가져옴.
	
    var is = filter([
    { target : '#subject' , filter : 'empty' , title : '제목' },
    { target : '#content' , filter : 'empty' , title : '내용' } , 
    { target : '#user_name' , filter : 'empty' , title : '작성자' }
    ]);

    if(is==true){
    	jQuery.ajax({
    		type : 'POST' , 
    		url : './write_ok' ,
    		data : jQuery('#form :input').serialize()
    		//위의 코드가 form태그 안에 있는 모든 input,textarea,select등 데이터 정보를 수집한다.
    		}).done(function(data) {
    			var message = jQuery(data).find("message").text();
    			var error = jQuery(data).find("error").text();
    			alert(message);
    			if (error == 'false') location.href = './';
    			});
    }
}
// 입력 항목의 체크 함수
function filter(options) {
    var is = true;

    jQuery(options).each(function() {
        var item = this;

        switch (item.filter) {
            case 'empty' :
                var val = jQuery(item.target).val();
                if (val == '') {
                    alert(item.title + '을(를) 입력하세요.');
                    jQuery(item.target).focus();
                    is = false;
                }
            break;
        
        }
    });
    return is;
}