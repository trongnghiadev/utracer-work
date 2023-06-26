package com.utrace.example.model;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import static com.utrace.example.model.UserDA.updateVerifyStatus;
import com.utrace.example.utils.MD5;
import com.utrace.example.utils.RandomNumber;
import com.utrace.example.utils.SendMail;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import javax.mail.MessagingException;
import org.apache.logging.log4j.Logger;

// User Bus
/**
 *
 * @author superman
 */
public class UserBC {
    private static final Logger logger = LogUtil.getLogger(UserBC.class);
    
    private static UserEnt valid(UserEnt value) {
        if (value == null || value.getEmail() == null || value.getEmail().isEmpty() ||
                value.getFullname() == null || value.getFullname().isEmpty()) {
            return null;
        }

        value.setEmail(value.getEmail().trim());
        value.setFullname(value.getFullname().trim());
        value.setPhone(value.getPhone() != null ? value.getPhone().trim() : null);

        return value;
    }
    
    public static boolean insert(UserEnt item){
        long startTime = System.currentTimeMillis();
        
        boolean result = false;
        item = valid(item);
        
        if(item == null){
            return result;
        }
        
        try {
            item.setCreatedAt((Timestamp) new Date());
            item.setUpdatedAt(item.getCreatedAt());
            item.setStatus(true);
            
            if(!UserDA.insert(item)) {
                return result;
            }
            
            if(!UserCA.set(item)){
                return result;
            }
            
            result = UserCA.setMap(item.getEmail(), item.getId());
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static boolean update(UserEnt item){
        long startTime = System.currentTimeMillis();
        
        boolean result = false;
        
        item = valid(item);
        
        if(item.getId() <= 0){
            return result;
        }
        
        try {
            UserEnt user = UserCA.get(item.getId());
            if(user == null) {
                return result;
            }
            
            item.setEmail(user.getEmail());
            item.setCreatedAt(user.getCreatedAt());
            item.setUpdatedAt((Timestamp) new Date());
            
            if(!UserCA.set(item)){
                return result;
            }
            
            if(!UserDA.update(item)){
                UserCA.set(user);             
                return result;
            }
            
            result = true;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static UserEnt getById(int id){
        long startTime = System.currentTimeMillis();
        
        UserEnt result = null;
        
        try {
            if(id <= 0){
                return result;
            }
            
            result = UserCA.get(id);
            
            if (result == null)
                result = UserDA.getById(id);
            
            if (result != null)
                UserCA.set(result);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static UserEnt getByEmail(String email) {
        long startTime = System.currentTimeMillis();
        
        UserEnt result = null;
        
        try {
            if(ConvertUtil.toString(email).length() <= 0) {
                return result;
            }
            result = UserDA.getByEmail(email);
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

   public static UserEnt login(String email, String md5) {
    if (email.isEmpty() || md5.isEmpty()) {
        return null;
    }

    try {
        return UserDA.login(email, md5);
    } catch (Exception e) {
        logger.error(LogUtil.stackTrace(e));
    }

    return null;
}

    
    public static UserEnt forgotPass(String email) {
        long startTime = System.currentTimeMillis();
        
        UserEnt result = null;
        
        try {
            if(ConvertUtil.toString(email).length() <= 0) {
                return result;
            }
            
            // Check if user with email exists
            result = UserDA.forgotPass(email);
            
            if (result != null)
            {
                sendVerityEmail(email);
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static UserEnt checkOtp(String email, String otp) {
        UserDA userDA = new UserDA();
        Boolean verifySuccess = userDA.verifyOtp(email, otp);
        if (verifySuccess == true) {
            updateVerifyStatus(email);
            return getByEmail(email);
        }
        return null;
    }

    public static UserEnt register(String email) {
        UserEnt userEnt = UserDA.getByEmail(email);
        if (userEnt != null)
            return null;
        userEnt = UserDA.register(email);
        
        if (userEnt != null) {
            try {
                sendVerityEmail(userEnt.email);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return userEnt;
        }
        
        return null;
    }

    public static UserEnt setNewPassword(String email, String pass, String repass) {
        UserEnt userEnt = UserDA.getByEmail(email);
        if (userEnt.status && userEnt.emailVerified != true)
            return null;
            
        if (userEnt == null)
            return null;
        
        if (!pass.equals(repass)) {
            return null;
        }
        
        userEnt = UserDA.setNewPassword(email, pass);
        return userEnt;
    }

    private static void sendVerityEmail(String email) throws MessagingException, UnsupportedEncodingException {
        String randomOtp = RandomNumber.getRandomNumberString();
        UserDA userDA = new UserDA();
        userDA.updateVerifyOtp(email, randomOtp);
        String msgContent = randomOtp + " là mã xác nhận tài khoản";
        try {
        SendMail.Send(email, "Mã xác nhận", msgContent);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    
    public static boolean changePassword(String email, String oldPassword, String newPassword) {
        // Kiểm tra tính hợp lệ của email, mật khẩu cũ và mật khẩu mới
        if (email == null || email.isEmpty() || oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return false;
        }

        // Kiểm tra xem người dùng có tồn tại và mật khẩu cũ chính xác hay không
        UserEnt userEntity = UserDA.login(email, MD5.md5(oldPassword));
        if (userEntity == null) {
            return false;
        }
        
        // Thực hiện cập nhật mật khẩu mới
        UserEnt updatedUser = UserDA.setNewPassword(email, newPassword);
        if (updatedUser == null) {
            return false;
        }

        // Trả về true nếu cập nhật mật khẩu thành công
        return true;
    }


}
