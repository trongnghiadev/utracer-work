package com.utrace.model;

import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.utils.MD5;
import com.utrace.utils.RandomNumber;
import com.utrace.utils.SendMail;
import java.io.UnsupportedEncodingException;
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

        if (value == null
                || value.email == null || value.email.length() <= 0
                || value.fullname == null || value.fullname.length() <= 0) {
            return null;
        }

        value.email = ConvertUtil.toString(value.email).trim();
        value.fullname = ConvertUtil.toString(value.fullname).trim();
        value.phone = ConvertUtil.toString(value.phone).trim();

        return value;
    }

    public static boolean insert(UserEnt item) {
        long startTime = System.currentTimeMillis();
        boolean result = false;
        item = valid(item);

        if (item == null) {
            return result;
        }

        try {
            item.createdAt = System.currentTimeMillis();
            item.updatedAt = item.createdAt;
            item.status = true;

            if (!UserDA.insert(item)) {
                return result;
            }

            if (!UserCA.set(item)) {
                return result;
            }

            result = UserCA.setMap(item.email, item.id);

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static boolean changeOTP(String email) {
        boolean result = false;
        try {
            UserEnt userEnt = getByEmail(email);
            if (userEnt == null) {
                return result;
            }
            String randomOtp = RandomNumber.getRandomNumberString();

            String msgContent = randomOtp + " là mã xác nhận tài khoản";
            try {
                SendMail.Send(email, "Mã xác nhận", msgContent);
            } catch (MessagingException e) {
                e.printStackTrace();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return result;
            }
            return UserCA.UpdateOTP(email, randomOtp);

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static boolean update(UserEnt item) {
        long startTime = System.currentTimeMillis();

        boolean result = false;

        item = valid(item);

        if (item.id <= 0) {
            return result;
        }

        try {
            UserEnt user = UserCA.get(item.id);
            if (user == null) {
                return result;
            }

            item.email = user.email;
            item.createdAt = user.createdAt;
            item.updatedAt = System.currentTimeMillis();

            if (!UserCA.set(item)) {
                return result;
            }

            if (!UserDA.update(item)) {
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

    public static boolean updateSatus(String email) {
        long startTime = System.currentTimeMillis();

        boolean result = false;

        try {
            UserEnt user = getByEmail(email);
            if (user == null) {
                return result;
            }

            user.emailVerified = true;
            user.updatedAt = System.currentTimeMillis();

            if (!UserCA.set(user)) {
                return result;
            }

            if (!UserDA.update(user)) {
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

    public static UserEnt get(int id) {
        long startTime = System.currentTimeMillis();

        UserEnt result = null;

        try {

            if (id <= 0) {
                return result;
            }

            result = UserCA.get(id);

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

            if (ConvertUtil.toString(email).length() <= 0) {
                return result;
            }

            int id = UserCA.getMap(email.trim());
            result = get(id);

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static UserEnt login(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            return null;
        }

        try {
            UserEnt user = getByEmail(email);

            if (!(user.status && user.emailVerified)) {
                return null;
            }

            if (!user.password.equals(pass)) {
                return null;
            }

            return user;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
            return null;
        }
    }

    public static UserEnt forgotPass(String email) {
        long startTime = System.currentTimeMillis();

        UserEnt result = null;

        try {
            if (ConvertUtil.toString(email).length() <= 0) {
                return result;
            }

            // Check if user with email exists
            result = getByEmail(email);

            if (result != null) {
                sendVerityEmail(email);
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static boolean checkOtp(String email, String otp) {
        boolean result = false;

        try {
            if (!UserCA.verifyOtp(email, otp)) {
                return result;
            }

            return updateSatus(email);

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean register(String email) {
        UserEnt item = new UserEnt();
        boolean result = false;
        if (getByEmail(email) != null) {
            return result;
        }
        try {
            item.email = email;
            item.createdAt = System.currentTimeMillis();
            item.updatedAt = item.createdAt;
            item.status = true;

            if (!UserDA.insert(item)) {
                return result;
            }

            if (!UserCA.set(item)) {
                return result;
            }

            result = UserCA.setMap(item.email, item.id);
            if (result) {
                sendVerityEmail(item.email);
            }

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;

    }

    public static boolean setNewPassword(String email, String pass) {
        long startTime = System.currentTimeMillis();

        boolean result = false;
        if (email == null || email.length() <= 0
                || pass == null || pass.length() <= 0) {
            return result;
        }

        try {
            UserEnt user = getByEmail(email);

            if (user == null) {
                return result;
            }
            if (user.password.length() > 0) {
                return result;
            }
            if (!(user.status && user.emailVerified)) {
                return result;
            }

            user.password = pass;
            user.updatedAt = System.currentTimeMillis();

            if (!UserCA.set(user)) {
                return result;
            }

            if (!UserDA.update(user)) {
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

    private static void sendVerityEmail(String email) throws MessagingException, UnsupportedEncodingException {
        String randomOtp = RandomNumber.getRandomNumberString();
        UserCA.setOTP(email, randomOtp);
        String msgContent = randomOtp + " là mã xác nhận tài khoản";
        try {
            SendMail.Send(email, "Mã xác nhận", msgContent);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static UserEnt changePassword(String email, String oldPassword, String newPassword) {
        // Kiểm tra tính hợp lệ của email, mật khẩu cũ và mật khẩu mới
        if (email == null || email.isEmpty() || oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return null;
        }

        // Kiểm tra xem người dùng có tồn tại và mật khẩu cũ chính xác hay không
        UserEnt userEntity = UserBC.login(email, MD5.md5(oldPassword));
        if (userEntity == null) {
            return null;
        }

        try {
            UserEnt item = getByEmail(email);
            item.updatedAt = System.currentTimeMillis();
            item.password = MD5.md5(newPassword);

            if (!UserDA.update(item)) {
                return null;
            }

            if (!UserCA.set(item)) {
                return null;
            }

            // Trả về đối tượng UserEnt sau khi cập nhật mật khẩu thành công
            return item;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
            return null;
        }
    }

}
