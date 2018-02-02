package com.personal.common.jwt;

import com.alibaba.fastjson.JSONObject;
import com.personal.common.util.DeployInfoUtil;
import com.personal.common.util.crypt.CryptTool;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTHelper {

    /**
     * 生成基于 jwt 的token
     * @param subject 加密的用户信息
     * @param salt 秘钥
     * @param exprie 有效时间
     * @return 加密的token
     */
    public static String createJwt(Map<String,Object> subject, String salt, Long exprie){

        try {
            /** 生成token 所用的加密方式 **/
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(getHeader().get("alg").toString());
            JwtBuilder builder = Jwts.builder()
                    .setHeader(getHeader())
                    .setClaims(subject)
                    .setExpiration(new Date(exprie))
                    .signWith(signatureAlgorithm, salt);

            return builder.compact();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析token
     * @param token token
     * @param salt 秘钥
     * @return claims
     */
    public static Claims parseToken(String token, String salt){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(token)){
            String header = token.split("\\.")[0];
            String payload = token.split("\\.")[1];
            String sign = token.split("\\.")[2];
            JwsHeader jwsHeader = Jwts.parser().setSigningKey(salt).parseClaimsJws(token).getHeader();
            Claims claims = Jwts.parser().setSigningKey(salt).parseClaimsJws(token).getBody();
            return  claims;
        }else{
            return null;
        }
    }

    /**
     * 获取签名头
     * @return
     */
    public static Map<String,Object> getHeader(){
        Map<String,Object> header = new HashMap<String,Object>();
        header.put("typ","JWT");
        header.put("alg", DeployInfoUtil.getTokenCrypt());
        return header;
    }

    public static void main(String[] args) {
        String str = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6ImFkbWluIiwiZXhwIjoxNTE3NDIxNzE1LCJ1c2VySWQiOjF9.S91bKlzoGJ4MYzCJnLgELQfFiP3z6l2q3WxuFFyAtbA";
        String header = str.split("\\.")[0];
        String payload = str.split("\\.")[1];
        String sign = str.split("\\.")[2];
        System.out.println(CryptTool.base64(2,header));
        System.out.println(CryptTool.base64(2,payload));
        System.out.println(sign);
        Claims claims = parseToken(str,"QijWLS");
        System.out.println(claims.getSubject());
        System.out.println("userId==" + claims.get("userId"));
    }
}
