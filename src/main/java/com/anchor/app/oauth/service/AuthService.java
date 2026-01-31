package com.anchor.app.oauth.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.oauth.dto.AuthReq;
import com.anchor.app.oauth.enums.PermissionType;
import com.anchor.app.users.enums.UserRoleType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.oauth.model.User;
import com.anchor.app.users.enums.UserPrivacyType;
import com.anchor.app.msg.repository.UserContactRepository;
import com.anchor.app.msg.enums.ContactType;

@Service
public class AuthService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IAuthenticationFacade authfacade;

	@Autowired
	private UserContactRepository userContactRepository;

	public boolean hasPersmission(AuthReq authReq) throws AuthServiceException {
		boolean result = false;
		try {
			// If reqUserID (requesting user) is null, get it from authenticated user
			if (null == authReq.getReqUserID()) {
				populateReqUser(authReq);
				populateReqUserRoles(authReq);
			}

			if (
			// ( null == authReq.getUserID())
			// || (null == authReq.getChnlID())
			(null == authReq.getReqPermission())
			// || (null == authReq.getReqUserRoles())
			// || (authReq.getReqUserRoles().isEmpty())
			) {
				throw new AuthServiceException("Invalid AuthRequest");
			}

			// Perform Authorization
			result = performAuthorization(authReq);

		} catch (Exception e) {
			throw new AuthServiceException(e.getMessage(), e);
		}

		return result;
	}

	private void populateReqUser(AuthReq authReq) throws AuthServiceException {
		try {
			if (authReq.getReqUserID() == null) {
				User authenticatedUser = authfacade.getApiAuthenticationDetails();
				if (authenticatedUser == null) {
					throw new AuthServiceException("User is not authenticated");
				}
				authReq.setReqUserID(authenticatedUser.getId());
				authReq.setReqUser(authenticatedUser);
				authReq.getReqUserRoles().addAll(authenticatedUser.getUserRoles());
				logger.debug("Populated reqUserID from authenticated user: {}", authenticatedUser.getId());
			}
		} catch (Exception e) {
			throw new AuthServiceException("Exception While populating AuthReq reqUser with error:" + e.getMessage(),
					e);
		}

	}

	private void populateReqUserRoles(AuthReq authReq) throws AuthServiceException {
		try {

			if (authReq.getReqPermission().equals(PermissionType.UsrView)) {
				if (!authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin)
						&& !authReq.getReqUserRoles().contains(UserRoleType.Admin)) {
					// If Req and Actual User are not same then add Dybamic Authrization Role
					if (!authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID())) {

						populateRolsForPermission(authReq);
					}
				}

			}
		} catch (Exception e) {
			throw new AuthServiceException(
					"Exception While populating AuthReq reqUserRoles with error:" + e.getMessage(),
					e);
		}

	}

	private void populateRolsForPermission(AuthReq authReq) throws AuthServiceException {
		try {

			User targetUser = authfacade.getUserById(authReq.getUserID());
			if (null == targetUser) {
				throw new AuthServiceException("AuthReq User not found with Id:" + authReq.getUserID());
			}
			authReq.setTargetUser(targetUser);

			if (targetUser.getProfileType().equals(VisibilityType.Public)) {
				authReq.getReqUserRoles().add(UserRoleType.Viewer);
				// If Profile can be seen by Contantcs
				if ((targetUser.getPrivacySettings().getProfileVisiblity().equals(UserPrivacyType.Contacts))
						|| (targetUser.getPrivacySettings().getProfileVisiblity().equals(UserPrivacyType.NoBody))) {

					authReq.setPartialAccess(true);

				}

			} else {

				boolean isBlocked = isBlocked(authReq.getReqUserID(), authReq.getUserID());
				if (!isBlocked) {
					authReq.getReqUserRoles().add(UserRoleType.Viewer);
					authReq.setPartialAccess(true);
				}

			}

		} catch (Exception e) {
			throw new AuthServiceException(
					"Exception While populating AuthReq reqUserRoles with error:" + e.getMessage(),
					e);
		}
	}

	public boolean isBlocked(String reqUserId, String targetUserId) {
		try {
			// Check if reqUserId blocked targetUserId
			boolean blockedByReq = userContactRepository.existsByUserIdAndContactIdAndContactType(reqUserId,
					targetUserId, ContactType.BLOCKED);
			if (blockedByReq) {
				return true;
			}

			// Check if targetUserId blocked reqUserId
			boolean blockedByTarget = userContactRepository.existsByUserIdAndContactIdAndContactType(targetUserId,
					reqUserId, ContactType.BLOCKED);
			if (blockedByTarget) {
				return true;
			}

		} catch (Exception e) {
			logger.error("Error checking block status between {} and {}", reqUserId, targetUserId, e);
		}
		return false;
	}

	private boolean performAuthorization(AuthReq authReq) {
		boolean result = false;
		try {

			switch (authReq.getReqPermission()) {
				case CnAdd:
					if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))) {

						result = true;
					}

					break;

				case CnEdit:
					if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))) {

						result = true;
					}

					break;

				case CnDelete:
					if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))) {

						result = true;
					}

					break;

				case CnDeleteSelf:
					if (authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID())) {

						result = true;
					}

					break;

				case CnView:
					if (authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID())) {
						result = true;
					} else if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Viewer))) {

						result = true;
					}

					break;

				case UsrAdd:
					if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Author))) {

						result = true;
					}

					break;

				case UsrEdit:
					if (authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID())) {
						result = true;
					} else if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))) {

						result = true;
					}

					break;

				case UsrDelete:
					if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))) {

						result = true;
					}

					break;

				case UsrDeleteSelf:
					if (authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID())) {

						result = true;
					}

					break;

				case UsrView:
					if (authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID())) {
						result = true;
					} else if ((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
							|| (authReq.getReqUserRoles().contains(UserRoleType.Viewer))) {

						result = true;
					}

					break;

				default:
					throw new AuthServiceException(
							"Implementation not available for Permission:" + authReq.getReqPermission().name());

			}
		} catch (Exception e) {
			logger.error("Invalid Auth Request for User:" + authReq.getReqUserID() + " , Req Permission:"
					+ authReq.getReqPermission());

		}

		return result;
	}

	public Map<String, Object> decodeToken(String token) throws AuthServiceException {

		/*
		 * try {
		 * Jwt jwt = JwtHelper.decodeAndVerify(token, new MacSigner("123"));
		 * String claimsStr = jwt.getClaims();
		 * Map<String, Object> claims = JsonParserFactory.create().parseMap(claimsStr);
		 * 
		 * if(claims.containsKey("exp"))
		 * {
		 * Integer expireAt = (Integer) claims.get("exp");
		 * long expireMills = expireAt.longValue()*1000l;
		 * 
		 * Date expiryDate = new Date(expireMills);
		 * 
		 * Date now = new Date();
		 * 
		 * if(expiryDate.before(now))
		 * {
		 * claims.put("tokenExpire", true);
		 * 
		 * logger.info("Token is Expire as Expire date is :"
		 * +expiryDate+" , and Current Date is:"+now);
		 * }
		 * else {
		 * claims.put("tokenExpire", false);
		 * }
		 * 
		 * }
		 * 
		 * return claims;
		 * } catch (Exception e) {
		 * e.printStackTrace();
		 * return null;
		 * }
		 */

		return null;
	}
}
