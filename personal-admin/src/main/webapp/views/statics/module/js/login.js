layui.use(['form','layer','jquery'], function(){
      var form = layui.form,
      layer = layui.layer,
      $ = layui.$;
      $("#myLogin").click(function(){
          var userName = $("#userName").val();
          var password = $("#password").val();
          password = hex_md5(password);
          if(userName == null || userName == ""){
            layer.tips('请输入用户名！', '#userName');
            return;
          }
          if(password == null || password == ""){
            layer.tips('请输入密码！', '#password');
            return;
          }
          $.ajax({
            url:'/api/user/login.do',
            type:'POST', //GET
            async:true,    //或false,是否异步
            data:{
                userName:$("#userName").val(),
                password:password
            },
            timeout:5000,    //超时时间
            dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
            success:function(data,textStatus,jqXHR){
                if(data.result != 0){
                    layer.msg(data.message);
                }else{
                    if (typeof(Storage) !== "undefined") {
                        // 针对 localStorage/sessionStorage 的代码
                        window.sessionStorage.setItem("token",data.data);
                    } else {
                        // 抱歉！不支持 Web Storage ..
                        layer.msg("请更换其他浏览器");
                    }
                    console.log(sessionStorage.getItem("token"));
                }
            }
          });
      })
});
if (window != top)
top.location.href = location.href;