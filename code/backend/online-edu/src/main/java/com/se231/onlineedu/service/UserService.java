package com.se231.onlineedu.service;

import java.io.IOException;
import java.util.List;

import com.se231.onlineedu.message.request.SignInUserForm;
import com.se231.onlineedu.message.response.PersonalInfo;
import com.se231.onlineedu.message.response.SignInWithState;
import com.se231.onlineedu.message.response.UserAvatar;
import com.se231.onlineedu.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * User Service Interface
 *
 * interface of main service related to user.
 *
 * @author Zhe Li
 *
 * @date 2019/07/08
 */
public interface UserService {
    /**
     * this service allow user to get his own information
     * @param userId the id of the getting user
     * @return  user information form
     *
     */
    User getUserInfo(Long userId);

    /**
     * this service allow admin to manage users' personal information or a user to modify his personal information.
     * @param id    the id of managed user
     * @param personalInfo  the form admin submit
     * @return  user information form after changing.
     *
     */
    User manageUserInfo(Long id,PersonalInfo personalInfo);

    /**
     * this service allow admin to get a list of all user
     * @return list of all users
     */
    List<User> getAllUser();

    /**
     * check if there is same username
     * @param username pending username
     * @return true if exists same username, false if doesn't exist.
     */
    boolean checkSameUsername(String username);

    /**
     * check if there is same email
     * @param email pending email
     * @return true if exists same email, false if doesn't exist.
     */
    boolean checkSameEmail(String email);

    /**
     * check if there is same telephone number
     * @param tel pending telephone number
     * @return true if exists same telephone number, false if doesn't exist.
     */
    boolean checkSameTel(String tel);

    /**
     * this service allows admin or super admin to buck import users' information.
     * @param excel the excel used to bulk import user information
     * @return  String to return hints
     * @IOException save failed
     */
    String bulkImportUser(MultipartFile excel) throws IOException;

    /**
     * this service allows user to update his personal avatar
     * @param avatarUrl url of upload image
     * @param id    id of requesting user
     * @return  user info
     *
     */
     String updateUserAvatar(String avatarUrl, Long id) ;


    /**
     * this service confirm to change password
     * @param id  user id request to verify
     * @param password new password
     * @return  user info
     *
     */
    User updateUserPasswordConfirm(Long id, String password) ;

    /**
     * this service confirm to change password
     * @param id  user id request to verify
     * @param email new email
     * @return  user info
     *
     */
    User updateUserEmailConfirm(Long id, String email) ;


    /**
     * save user sign in
     * @param id user id requested to sign in
     * @param signInUserForm sign in form
     * @return
     */
    User saveUserSignIn(Long id, SignInUserForm signInUserForm);

    /**
     * check if new is same as old
     * @param id user id
     * @param password user password
     * @return true if new password is the same as the old
     */
    boolean checkIfSameAsOldPassword(Long id, String password);

    /**
     * check if new is same as old
     * @param id user id
     * @param tel user telephone number
     * @return true if tel is the same as old
     */
    boolean checkIfSameAsOldTel(Long id, Long tel);

    /**
     * check if new is same as old
     * @param id
     * @param email
     * @return true if email is the same as old
     */
    boolean checkIfSameAsOldEmail(Long id, String email);

    /**
     * @param userId
     * @return
     */
    UserAvatar getUserAvatar(Long userId);

    /**
     * @param courseId
     * @param userId
     * @return
     */
    List<SignInWithState> getUserSignIns(Long courseId, Long userId);
}
