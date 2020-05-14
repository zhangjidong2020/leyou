package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String secret;//ly@Login(Auth}*^31)&hei%
    private String pubKeyPath;//D:/rsa/rsa.pub
    private String priKeyPath;//D:/rsa/rsa.pri
    private Integer expire;//30

    private PublicKey publicKey;
    private PrivateKey privateKey;



    @PostConstruct//构造方法执行之后，PostConstruct
    public void init() throws Exception {

        try{

            File file = new File(pubKeyPath);
            File file1 = new File(priKeyPath);
            if(!file.exists()||!file1.exists()){
                //如果没有公钥 私钥，产生
                RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
            }
            publicKey=RsaUtils.getPublicKey(pubKeyPath);

            privateKey=RsaUtils.getPrivateKey(priKeyPath);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("公钥私钥初始化失败");

        }



    }


}
