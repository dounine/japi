package com.dounine.japi.auth;

import com.dounine.japi.entity.User;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huanghuanlai on 2017/2/27.
 */
public class UserUtils {
    private static final Map<String, UserAuth> ONLINES = new ConcurrentHashMap<>();
    private static final List<UserAuth> SYSTEM_USERS = new ArrayList<>();

    static {
        String userProperties = UserUtils.class.getResource("/users.properties").getFile();
        String configProperties = UserUtils.class.getResource("/config.properties").getFile();
        File userFile = new File(userProperties);
        File configFile = new File(configProperties);
        Integer sessionTime = 60 * 60;
        if (userFile.exists()) {
            try {
                Collection<String> lines = FileUtils.readLines(userFile, Charset.forName("utf-8"));
                if (lines.size() == 0) {
                    throw new JapiException("japi 没有用户,无法用于登录授权.");
                }
                for (String line : lines) {
                    UserAuth userAuth = new UserAuth();
                    userAuth.setUsername(line.split("=")[0]);
                    String[] passwordAndRole = line.split("=")[1].split(" ");
                    if(passwordAndRole.length==2&&"admin".equals(passwordAndRole[1])){
                        userAuth.setAdmin(true);
                        userAuth.setPassword(passwordAndRole[0]);
                    }else{
                        userAuth.setPassword(line.split("=")[1]);
                    }
                    SYSTEM_USERS.add(userAuth);
                }
                if (configFile.exists()) {
                    Collection<String> configLines = FileUtils.readLines(configFile, Charset.forName("utf-8"));
                    for (String line : configLines) {
                        if (line.split("=")[0].equals("sessionTime")) {
                            sessionTime = Integer.parseInt(line.split("=")[1]);
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final Integer _sessionTime = sessionTime;
        new Thread(new Runnable() {
            @Override
            public void run() {
                sessionManager(_sessionTime);
            }
        }).start();

    }

    public static UserAuth getUserAuth(String token){
        return ONLINES.get(token);
    }

    public static Map<String, UserAuth> getOnlines(){
        return ONLINES;
    }

    private static void sessionManager(Integer sessionTime) {
        while (true) {
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Iterator<UserAuth> userAuthIterator = ONLINES.values().iterator();
            while(userAuthIterator.hasNext()){
                UserAuth userAuth = userAuthIterator.next();
                if(!userAuth.isAdmin()){
                    if (userAuth.getLiveTime().plusSeconds(sessionTime).isBefore(LocalDateTime.now())) {
                        userAuthIterator.remove();
                    }
                }
            }
        }
    }

    public static boolean isAuth(String token) {
        if (StringUtils.isBlank(token)) {
            throw new JapiException("token 不能为空.");
        }
        for (String to : ONLINES.keySet()) {
            if (to.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private static boolean userIsAuth(UserAuth userAuth) {
        boolean isAuth = false;
        for (UserAuth ua : SYSTEM_USERS) {
            if (ua.getUsername().equals(userAuth.getUsername()) && DigestUtils.md5Hex(ua.getPassword()).equals(userAuth.getPassword())) {
                isAuth = true;
                break;
            }
        }
        return isAuth;
    }

    public static boolean updateLiveTime(String token) {
        boolean isUpdate = false;
        for (String mToken : ONLINES.keySet()) {
            if (mToken.equals(token)) {
                ONLINES.get(mToken).setLiveTime(LocalDateTime.now());
                isUpdate = true;
                break;
            }
        }
        return isUpdate;
    }

    public static String login(UserAuth userAuth) {
        if (userIsAuth(userAuth)) {
            for (String token : ONLINES.keySet()) {
                if (ONLINES.get(token).getUsername().equals(userAuth.getUsername())) {
                    if(!ONLINES.get(token).isAdmin()){
                        ONLINES.remove(token);
                        break;
                    }else{
                        userAuth.setLiveTime(LocalDateTime.now());
                        return token;
                    }
                }
            }
            String token = TokenUtils.createToken();
            userAuth.setLiveTime(LocalDateTime.now());
            ONLINES.put(token, userAuth);
            return token;
        }
        return null;
    }

    public static boolean logout(String token) {
        for (String to : ONLINES.keySet()) {
            if (to.equals(token)) {
                ONLINES.remove(token);
                return true;
            }
        }
        return false;
    }

}
