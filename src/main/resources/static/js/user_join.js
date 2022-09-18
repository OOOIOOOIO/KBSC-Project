/*<![CDATA[*/

function sample6_execDaumPostcode() {
	new daum.Postcode(
			{
				oncomplete : function(data) {
					// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

					// 각 주소의 노출 규칙에 따라 주소를 조합한다.
					// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
					var addr = ''; // 주소 변수
					var extraAddr = ''; // 참고항목 변수

					// 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
					if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을
															// 경우
						addr = data.roadAddress;
					} else { // 사용자가 지번 주소를 선택했을 경우(J)
						addr = data.jibunAddress;
					}

					// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
					if (data.userSelectedType === 'R') {
						// 법정동명이 있을 경우 추가한다. (법정리는 제외)
						// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
						if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
							extraAddr += data.bname;
						}
						// 건물명이 있고, 공동주택일 경우 추가한다.
						if (data.buildingName !== '' && data.apartment === 'Y') {
							extraAddr += (extraAddr !== '' ? ', '
									+ data.buildingName : data.buildingName);
						}
						// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
						if (extraAddr !== '') {
							extraAddr = ' (' + extraAddr + ')';
						}
						// 조합된 참고항목을 해당 필드에 넣는다.
						document.getElementById("sample6_extraAddress").value = extraAddr;

					} else {
						document.getElementById("sample6_extraAddress").value = '';
					}

					// 우편번호와 주소 정보를 해당 필드에 넣는다.
					document.getElementById('sample6_postcode').value = data.zonecode;
					document.getElementById("sample6_address").value = addr;
					// 커서를 상세주소 필드로 이동한다.
					document.getElementById("sample6_detailAddress").focus();
				}
			}).open();
}

/*]]>*/


/*

var joinForm = document.joinForm;
var user_id = joinForm.user_id;
var user_pw = joinForm.user_pw;
var user_name = joinForm.user_name;
var user_phone = joinForm.user_phone;
var user_email = joinForm.user_email;
var addr_ete = joinForm.addr_ete;


// id
function check_id(){
	var user_id_val =user_id.value;
	var idCheck = /^[a-zA-Z0-9]{4,12}$/; //id와 pwassword 유효성 검사 정규식
	
	
	// 공백
	if(user_id_val == "" || user_id_val == null){
		alert("아이디를 입력해주세요!")
	}
	// 유효성
	else if(!(idCheck.test(user_id_val))){
		alert("영어+숫자 4자리 이상 12자리 이하로 입력해주세요!!")
	}
	// 중복
	else{
		$.ajax(
			{
				type:"POST",
				url:"/user/checkId",
//				data:JSON.stringify(user_id_val), 이렇게 하면 controller에서 ""가 붙어서 넘어간다.
				data:user_id_val,
				dataType:"json", // "text"로 해도 contentType이 json으로 날아간다.
				contentType:"application/json; charset=utf-8",
				success:function(result){
					if(result > 0){
						alert("중복된 아이디입니다!")
					}
					else{
						alert("사용 가능한 아이디입니다!")
					}
				},
				error:function(error){
					alert("에러에러!")
				}
					
			}
		);
	}
	
}

// pw
function check_pw(){
	
	var user_pw_val = user_pw.value;
	
	//비밀번호 영문자+숫자+특수조합(8~15자리 입력) 정규식
	var pwCheck = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
  
	// 공백
	if(user_pw_val == "" || user_pw_val == null){
		alert("비밀번호를 입력해주세요!!")
	}
	// 유효성
	else if(!(pwCheck.test(user_pw_val))){
		alert("비밀번호는 영문자+숫자+특수문자 조합으로 8~15자리 사용해야 합니다!!!")
	}
	else{
		alert("사용 가능한 비밀번호입니다!!")
	}
	
}

// eamil
function check_email(){
	var user_email_val = user_email.value;
	var emailCheck = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
	
	// 공백일 경우
	if(user_email_val == "" || user_email_val == null){
		alert("이메일을 입력해주세요!")
	}
	// 유효성
	else if(!(emailCheck.test(user_email_val))){
		alert("이메일 형식에 맞춰 기입해주세요!!")
	}
	// 중복
	else{
		$.ajax(
			{
				type:"POST",
				url:"/user/checkEmail",
				data:user_email_val,
				dataType:"json",
				contentType:"application/json; charset:utf-8",
				success:function(result){
					if(result > 0){
						alert("중복된 이메일입니다!!")
					}
					else{
						alert("사용 가능한 이메일입니다!!")
					}
				},
				error:function(error){
					alert("에러에러!!!!")
				}
				
				
			}	
		);
	}
	
}

function check_phone(){
	var regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
	var user_phone_val = user_phone.value;
	
	if(user_phone_val == ""){
		alert("핸드폰 번호를 입력해주세요")
	}
	else if(!regPhone.test(user_phone_val)){
		alert("핸드폰 번호를 확인해주세요")
	}
	else{
		$.ajax(
			{
				type:"POST",
				url:"/user/checkPhone",
				data:user_phone_val,
				dataType:"json",
				contentType:"application/json; charset:utf-8",
				success:function(result){
					if(result > 0){
						alert("중복된 번호입니다.!!")
					}
					else{
						alert("사용 가능한 번호입니다!!")
						
					}
				},
				error:function(error){
					alert("에러에러!!!!")
				}
				
				
			}	
		);
	}
}



// 서브밋하기
function sendit(){
	if(!id_flag){
		alert("아이디 확인을 해주세요!")
	}
	if(!pw_flag){
		alert("비밀번호 확인을 해주세요!")
	}
	if(!email_flag){
		alert("이메일 확인을 해주세요!")
	}
	
	// 주소 비어있는지 확인
	
	let zipcodeTag = joinForm.zipcode;
	if(zipcodeTag.value == ""){
		alert("우편번호를 찾아주세요!");
		sample6_execDaumPostcode();
	}
	let addr_detailTag = joinForm.addr_detail;
	if(addr_detailTag.value == ""){
		alert("상세주소를 입력해주세요!!");
		addrdetailTag.focus();
	}
	
	let addr_eteTag = joinForm.addr_ete;
	if(addr_eteTag.value == ""){
		alert("참고항목을 입력해주세요!")
	}
	
	let addr = joinForm.addr;
	
	// 회원가입하기
	$.ajax(
		{
			type:"POST",
			url:"/user/doJoin",
			dataType:"json",
			data:JSON.stringify({
				"user_name" : user_name.value,
				"user_id" : user_id.value,
				"user_pw" : user_pw.value,
				"user_email" : user_email.value,
				"user_phone" : user_phone.value,
				"zipcode" : zipcodeTag.value,
				"addr" : addr.value,
				"addr_detail" : addr_detailTag.value,
				"addr_ete" : addr_eteTag.value
			}),
			contentType:"application/json; charset=utf-8",
			success:function(result){
				if(result > 0){
					alert("회원가입에 성공하였습니다")
						joinForm.action = "/user/login";
						joinForm.method = "POST";
						joinForm.submit();
				}
				else{
					alert("회원가입에 실패하였습니다")
				}
			},
			error:function(error){
				alert("에러에러!!")
			}
		}
	)
}


*/














