function checkPasswords() {
    var pass1 = document.getElementById("newPassword").value;
    var pass2 = document.getElementById("checkNewPassword").value;
    console.info("pass1:"+pass1);
    console.info("pass2:"+pass2);
    if (pass1!= pass2){
    	alert("两次新密码输入不一致,请重新输入!");
    	//设置新密码，验证新密码为空
    	$("#newPassword").val("");
    	$("#checkNewPassword").val("");
    }else{
    	pass1.setCustomValidity("");
    } 
}
