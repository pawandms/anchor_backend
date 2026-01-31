package com.anchor.app.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.anchor.app.oauth.model.User;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

        Optional<User> findByEmail(String email);

        Optional<User> findByUserName(String userName);

        Optional<User> findByMobile(String mobile);

        boolean existsByEmail(String email);

        boolean existsByUserName(String userName);

        boolean existsByMobile(String mobile);

        /**
         * Update profileImageMediaId for a specific user
         * 
         * @param userId  User ID
         * @param mediaId Media ID to set
         * @param modUser User who modified
         * @param modDate Modification date
         */
        @Query("{ '_id': ?0 }")
        @Update("{ '$set': { 'profileImageMediaId': ?1, 'modUser': ?2, 'modDate': ?3 } }")
        void updateProfileImageMediaId(String userId, String mediaId, String modUser, Date modDate);

        /**
         * Update privacy settings for a specific user
         * 
         * @param userId          User ID
         * @param privacySettings Privacy settings object
         * @param modUser         User who modified
         * @param modDate         Modification date
         */
        @Query("{ '_id': ?0 }")
        @Update("{ '$set': { 'privacySettings': ?1, 'modUser': ?2, 'modDate': ?3 } }")
        void updatePrivacySettings(String userId, com.anchor.app.users.dto.UserPrivacy privacySettings, String modUser,
                        Date modDate);

        /**
         * Update notification settings for a specific user
         * 
         * @param userId               User ID
         * @param notificationSettings Notification settings object
         * @param modUser              User who modified
         * @param modDate              Modification date
         */
        @Query("{ '_id': ?0 }")
        @Update("{ '$set': { 'notificationSettings': ?1, 'modUser': ?2, 'modDate': ?3 } }")
        void updateNotificationSettings(String userId, com.anchor.app.users.dto.UserNotification notificationSettings,
                        String modUser, Date modDate);

        /**
         * Update nickName for a specific user
         * 
         * @param userId   User ID
         * @param nickName Nick name to set
         * @param modUser  User who modified
         * @param modDate  Modification date
         */
        @Query("{ '_id': ?0 }")
        @Update("{ '$set': { 'nickName': ?1, 'modUser': ?2, 'modDate': ?3 } }")
        void updateNickName(String userId, String nickName, String modUser, Date modDate);

        /**
         * Update mobile for a specific user
         * 
         * @param userId         User ID
         * @param mobile         Mobile number to set
         * @param mobileBindTime Mobile bind timestamp
         * @param modUser        User who modified
         * @param modDate        Modification date
         */
        @Query("{ '_id': ?0 }")
        @Update("{ '$set': { 'mobile': ?1, 'mobileBindTime': ?2, 'modUser': ?3, 'modDate': ?4 } }")
        void updateMobile(String userId, String mobile, Long mobileBindTime, String modUser, Date modDate);

        /**
         * Update user info fields (firstName, lastName, nickName, mobile, email,
         * userName)
         * This method uses dynamic update based on which fields are provided
         * Note: When email is updated, userName is also updated with the same value
         * 
         * @param userId         User ID
         * @param firstName      First name
         * @param lastName       Last name
         * @param nickName       Nick name
         * @param mobile         Mobile number
         * @param mobileBindTime Mobile bind timestamp
         * @param email          Email address
         * @param userName       User name (same as email when email is updated)
         * @param emailBindTime  Email bind timestamp
         * @param modUser        User who modified
         * @param modDate        Modification date
         */
        @Query("{ '_id': ?0 }")
        @Update("{ '$set': { 'firstName': ?1, 'lastName': ?2, 'nickName': ?3, 'mobile': ?4, 'mobileBindTime': ?5, 'email': ?6, 'userName': ?7, 'emailBindTime': ?8, 'userLanguage': ?9, 'gender': ?10, 'profileType': ?11, 'dob': ?12, 'location': ?13, 'modUser': ?14, 'modDate': ?15 } }")
        void updateUserInfo(String userId, String firstName, String lastName, String nickName,
                        String mobile, Long mobileBindTime, String email, String userName,
                        Long emailBindTime, com.anchor.app.users.enums.UserLanguageType userLanguage,
                        com.anchor.app.oauth.enums.GenderType gender,
                        com.anchor.app.oauth.enums.VisibilityType profileType,
                        Date dob,
                        double[] location,
                        String modUser, Date modDate);
}
