package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ProfileDao;
import com.example.demo.dao.QualificationDao;
import com.example.demo.dao.QualificationMasterDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.ProfileCreateRequest;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.QualificationRequest;
import com.example.demo.dto.QualificationResponse;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserDetailDto;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.Profile;
import com.example.demo.entity.Qualification;
import com.example.demo.entity.User;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidPageException;
import com.example.demo.exception.InvalidQualificationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	//	private final UserRepository userRepository;
	//	private final ProfileRepository profileRepository;
	//	private final QualificationMasterRepository qualificationMasterRepository;

	private final UserDao userDao;
	private final ProfileDao profileDao;
	private final QualificationDao qualificationDao;
	private final QualificationMasterDao qualificationMasterDao;

	// 問題１
	@Transactional
	public UserResponse create(UserCreateRequest userCreateRequest) {
		//		if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
		//			throw new EmailAlreadyExistsException();
		//		}
		//
		//		User user = new User(
		//				userCreateRequest.getName(), 
		//				userCreateRequest.getEmail(), 
		//				passwordEncoder.encode(userCreateRequest.getPassword()));
		//
		//		if (userCreateRequest.getProfile() != null && hasProfileData(userCreateRequest.getProfile())) {
		//			// ProfileのリクエストDTOをEntityに保持
		//			ProfileCreateRequest profileCreateRequest = userCreateRequest.getProfile();
		//			Profile profile = new Profile();
		//
		//			profile.setNickname(profileCreateRequest.getNickname());
		//			profile.setBirthday(profileCreateRequest.getBirthday());
		//
		//			profile.setUser(user);
		//			user.setProfile(profile);
		//		}
		//
		//		if (userCreateRequest.getQualifications() != null && 
		//				!userCreateRequest.getQualifications().isEmpty()) {
		//			List<QualificationRequest> qualificationRequests = userCreateRequest.getQualifications();
		//
		//			Set<Integer> qualificationIds = new HashSet<>();
		//
		//			// 資格の重複チェック
		//			for (QualificationRequest request : qualificationRequests) {
		//				if (!qualificationIds.add(request.getQualificationId())) {
		//					throw new InvalidQualificationException(
		//							"同じ資格を複数登録できません");
		//				}
		//			}
		//
		//			// 資格リクエストDTOをEntityに保持
		//			List<Qualification> qualifications = qualificationRequests.stream().map(
		//					qualificationRequest -> {
		//						Qualification qualification =  new Qualification();
		//
		//						qualification.setQualificationMaster(
		//								qualificationMasterRepository.findById(qualificationRequest.getQualificationId())
		//								.orElseThrow(() -> new InvalidQualificationException("対象の資格で登録または変更できません")));
		//						qualification.setAcquisitionDate(qualificationRequest.getAcquisitionDate());
		//						qualification.setUser(user);
		//
		//						return qualification;
		//					}).toList();
		//
		//			user.setQualifications(qualifications);
		//		}
		//
		//		User savedUser = userRepository.save(user);
		//
		//		return createUserResponse(savedUser,"登録完了");

		User existUser = userDao.findByEmail(userCreateRequest.getEmail()).orElse(null);

		if (existUser != null) {
			throw new EmailAlreadyExistsException();
		}

		User user = new User();
		user.setName(userCreateRequest.getName());
		user.setEmail(userCreateRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));

		userDao.create(user);

		Profile profile = new Profile();

		if (userCreateRequest.getProfile() != null && hasProfileData(userCreateRequest.getProfile())) {
			// ProfileのリクエストDTOをEntityに保持
			ProfileCreateRequest profileCreateRequest = userCreateRequest.getProfile();

			profile.setId(user.getId());
			profile.setNickname(profileCreateRequest.getNickname());
			profile.setBirthday(profileCreateRequest.getBirthday());

			profileDao.create(profile);
		}

		List<Qualification> qualifications = new ArrayList<>();

		if (userCreateRequest.getQualifications() != null && 
				!userCreateRequest.getQualifications().isEmpty()) {
			List<QualificationRequest> qualificationRequests = userCreateRequest.getQualifications();

			Set<Integer> qualificationIds = new HashSet<>();

			// 資格の重複チェック
			for (QualificationRequest request : qualificationRequests) {
				if (!qualificationIds.add(request.getQualificationId())) {
					throw new InvalidQualificationException(
							"同じ資格を複数登録できません");
				}
			}

			// 資格リクエストDTOをEntityに保持
			qualifications = qualificationRequests.stream().map(
					qualificationRequest -> {
						qualificationMasterDao.findById(qualificationRequest.getQualificationId())
						.orElseThrow(() -> new InvalidQualificationException("対象の資格で登録または変更できません"));

						Qualification qualification =  new Qualification();

						qualification.setUserId(user.getId());
						qualification.setQualificationId(qualificationRequest.getQualificationId());
						qualification.setAcquisitionDate(qualificationRequest.getAcquisitionDate());

						qualificationDao.create(qualification);

						return qualification;
					}).toList();
		}

		return createUserResponse(user, profile, qualifications, "登録完了");
	}

	// 問題２
	@Transactional
	public UserResponse update(UserUpdateRequest userUpdateRequest, Integer id) {
		//		User user = getById(id);
		//
		//		if (userRepository.existsByEmailAndIdNot(userUpdateRequest.getEmail(), id)) {
		//			throw new EmailAlreadyExistsException();
		//		}
		//		user.setEmail(userUpdateRequest.getEmail());
		//		user.setName(userUpdateRequest.getName());
		//
		//
		//		// リクエストDTOにパスワードがあればリクエストのパスワード、なければ元のパスワードをセット
		//		user.setPassword(userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isBlank()
		//				? passwordEncoder.encode(userUpdateRequest.getPassword())
		//				: user.getPassword());
		//
		//		if (userUpdateRequest.getProfile() != null && hasProfileData(userUpdateRequest.getProfile())) {
		//			// ProfileのリクエストDTOを保持
		//			ProfileCreateRequest profileCreateRequest = userUpdateRequest.getProfile();
		//			
		//			// もとのProfileデータがあれば使用し、なければ新しくProfileデータを作成
		//			Profile profile = user.getProfile() != null ? user.getProfile() : new Profile();
		//
		//			profile.setNickname(profileCreateRequest.getNickname());
		//			profile.setBirthday(profileCreateRequest.getBirthday());
		//
		//			profile.setUser(user);
		//			user.setProfile(profile);
		//			
		//		// プロフィールのリクエストがなければEntityをnullに設定
		//		} else {
		//			user.setProfile(null);
		//		}
		//
		//		if (userUpdateRequest.getQualifications() != null && 
		//				!userUpdateRequest.getQualifications().isEmpty()) {
		//			List<QualificationRequest> qualificationRequests = userUpdateRequest.getQualifications();
		//
		//			Set<Integer> qualificationIds = new HashSet<>();
		//
		//			// 資格の重複チェック
		//			for (QualificationRequest request : qualificationRequests) {
		//				if (!qualificationIds.add(request.getQualificationId())) {
		//					throw new InvalidQualificationException(
		//							"同じ資格を複数登録できません");
		//				}
		//			}
		//			
		//			// 資格リクエストDTOをEntityに保持
		//			List<Qualification> qualifications = qualificationRequests.stream().map(
		//					qualificationRequest -> {
		//						Qualification qualification =  new Qualification();
		//
		//						qualification.setQualificationMaster(
		//								qualificationMasterRepository.findById(qualificationRequest.getQualificationId())
		//								.orElseThrow(() -> new InvalidQualificationException("対象の資格で登録または変更できません")));
		//						qualification.setAcquisitionDate(qualificationRequest.getAcquisitionDate());
		//						qualification.setUser(user);
		//
		//						return qualification;
		//					}).collect(Collectors.toList());
		//			
		//			// 全て一致している場合は破棄しない
		//			// 対象のユーザーが持っている既存の資格情報を破棄
		//			user.getQualifications().clear();
		//			// 新しい資格情報を対象のユーザーの資格情報に結びつける
		//			user.getQualifications().addAll(qualifications);
		//
		//		// 資格のリクエストがなければエンティティの配列を空にする
		//		} else {
		//			user.getQualifications().clear();
		//		}
		//
		//		User savedUser = userRepository.save(user);
		//
		//		return createUserResponse(savedUser,"更新完了");

		User user = getById(id);

		User existUser = userDao.findByEmailAndNotId(userUpdateRequest.getEmail(), id).orElse(null);
		if (existUser != null) {
			throw new EmailAlreadyExistsException();
		}

		user.setName(userUpdateRequest.getName());
		user.setEmail(userUpdateRequest.getEmail());

		user.setPassword(userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isBlank()
				? passwordEncoder.encode(userUpdateRequest.getPassword())
						: user.getPassword());

		userDao.update(user);

		// もとのProfileデータがあれば使用し、なければ新しくProfileデータを作成
		Profile profile = profileDao.findByUserId(id).orElse(null);

		if (profile == null) {
			profile = new Profile();
		}

		if (userUpdateRequest.getProfile() != null && hasProfileData(userUpdateRequest.getProfile())) {
			// ProfileのリクエストDTOを保持
			ProfileCreateRequest profileCreateRequest = userUpdateRequest.getProfile();

			profile.setUserId(id);
			profile.setNickname(profileCreateRequest.getNickname());
			profile.setBirthday(profileCreateRequest.getBirthday());

			if (profile.getId() != null) {
				profileDao.update(profile);

			} else {
				profileDao.create(profile);
			}
			// プロフィールのリクエストがなければEntityをnullに設定
		} else {
			if (profile.getId() != null) {
				profileDao.delete(profile);
			}
		}

		List<Qualification> qualifications = new ArrayList<>();

		List<Qualification> preQualifications = qualificationDao.findByUserId(id);

		if (userUpdateRequest.getQualifications() != null && 
				!userUpdateRequest.getQualifications().isEmpty()) {
			List<QualificationRequest> qualificationRequests = userUpdateRequest.getQualifications();

			Set<Integer> qualificationIds = new HashSet<>();

			// 資格の重複チェック
			for (QualificationRequest request : qualificationRequests) {
				if (!qualificationIds.add(request.getQualificationId())) {
					throw new InvalidQualificationException(
							"同じ資格を複数登録できません");
				}
			}

			// 資格リクエストDTOをEntityに保持
			qualifications = qualificationRequests.stream().map(
					qualificationRequest -> {
						Qualification qualification =  new Qualification();

						qualificationMasterDao.findById(qualificationRequest.getQualificationId())
						.orElseThrow(() -> new InvalidQualificationException("対象の資格で登録または変更できません"));

						qualification.setUserId(user.getId());
						qualification.setQualificationId(qualificationRequest.getQualificationId());
						qualification.setAcquisitionDate(qualificationRequest.getAcquisitionDate());

						qualificationDao.create(qualification);

						return qualification;
					}).collect(Collectors.toList());

			for (Qualification preQualification : preQualifications) {
				qualificationDao.delete(preQualification);
			}

			// 資格のリクエストがなければエンティティの配列を空にする
		} else {
			for (Qualification preQualification : preQualifications) {
				qualificationDao.delete(preQualification);
			}
		}

		return createUserResponse(user, profile, qualifications, "更新完了");
	}

	// 問題３
	public UserResponse delete(Integer id) {
		User user = getById(id);

		userDao.delete(user);

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), "削除完了");
	}

	// 問題４
	public UserResponse findById(Integer id) {
		List<UserDetailDto> userDetails = userDao.findByIdDetailDto(id);
		
		User user = new User();
		user.setId(id);
		user.setName(userDetails.get(0).getName());
		user.setEmail(userDetails.get(0).getEmail());
		
		Profile profile = new Profile();
		profile.setNickname(userDetails.get(0).getNickname());
		profile.setBirthday(userDetails.get(0).getBirthday());
		
		List<Qualification> qualifications = userDetails.stream().map(
				userDetail -> {
					Qualification qualification = new Qualification();
					qualification.setQualificationId(userDetail.getQualificationId());
					qualification.setAcquisitionDate(userDetail.getAcquisitionDate());
					return qualification;
				}
				).toList();
		
		return createUserResponse(user, profile, qualifications, "取得完了");
		
//		User findedUser = getById(id);
//		Profile profile = profileDao.findByUserId(id).orElse(null);
//		List<Qualification> qualifications = qualificationDao.findByUserId(id);
//
//		return createUserResponse(findedUser, profile, qualifications, "取得完了");
	}

	public User getById(Integer id) {
		User user = userDao.findById(id).orElseThrow(() -> 
		new RuntimeException("対象のユーザーが見つかりません"));

		return user;
	}

	// 問題５
	public Page<UserResponse> findAll(Pageable pageable) {
		if (pageable.getPageSize() > 20) {
			throw new InvalidPageException("size", "20以下を指定してください");
		}

		Sort.Order order =
				pageable.getSort().iterator().next();

		String sortColumn = order.getProperty();
		String direction = order.getDirection().name().toLowerCase();
		
		String safeSortColumnString = normalizeSortColumn(sortColumn);
		String safeDeirection = normalizeDirection(direction);

		SelectOptions options = SelectOptions.get()
				.offset((int) pageable.getOffset())
				.limit(pageable.getPageSize())
				.count();

		List<User> users;
		users = userDao.findByName("", safeSortColumnString, safeDeirection, options);

//		if ("name".equals(sortColumn)) {
//
//			if ("desc".equals(direction)) {
//				users = userDao.findAllOrderByNameDesc(options);
//			} else {
//				users = userDao.findAllOrderByNameAsc(options);
//			}
//
//		} else {
//
//			if ("desc".equals(direction)) {
//				users = userDao.findAllOrderByIdDesc(options);
//			} else {
//				users = userDao.findAllOrderByIdAsc(options);
//			}
//		}

		Page<UserResponse> userResponsePage = new PageImpl<UserResponse>(
				users.stream().map(user -> {
					Profile profile = profileDao.findByUserId(user.getId()).orElse(null);
					List<Qualification> qualifications = qualificationDao.findByUserId(user.getId());

					return createUserResponse(user, profile, qualifications, "");
				}).toList()
				, pageable, options.getCount());

		//		Page<UserResponse> userResponsePage = users.map(user -> {
		//			return createUserResponse(user, "");
		//		});
		return userResponsePage;
	}

	// 問題６
	public Page<UserResponse> findByName(String name, Pageable pageable) {
		if (pageable.getPageSize() > 20) {
			throw new InvalidPageException("size", "20以下を指定してください");
		}

		//		Page<User> users = userRepository.findByNameContaining(name, pageable);
		//
		//		Page<UserResponse> userResponses = users.map(
		//				user -> {return createUserResponse(user, "");});
		//
		//		return userResponses;

		Sort.Order order =
				pageable.getSort().iterator().next();

		String sortColumn = order.getProperty();
		String direction = order.getDirection().name().toLowerCase();
		
		String safeSortColumnString = normalizeSortColumn(sortColumn);
		String safeDeirection = normalizeDirection(direction);

		SelectOptions options = SelectOptions.get()
				.offset((int) pageable.getOffset())
				.limit(pageable.getPageSize())
				.count();

		List<User> users;
		users = userDao.findByName(name, safeSortColumnString, safeDeirection, options);

//		if ("name".equals(sortColumn)) {
//
//			if ("desc".equals(direction)) {
//				users = userDao.findByNameOrderByNameDesc(name, options);
//			} else {
//				users = userDao.findByNameOrderByIdAsc(name, options);
//			}
//
//		} else {
//
//			if ("desc".equals(direction)) {
//				users = userDao.findByNameOrderByIdDesc(name, options);
//			} else {
//				users = userDao.findByNameOrderByIdAsc(name, options);
//			}
//		}
		
		Page<UserResponse> userResponsePage = new PageImpl<UserResponse>(
				users.stream().map(user -> {
					Profile profile = profileDao.findByUserId(user.getId()).orElse(null);
					List<Qualification> qualifications = qualificationDao.findByUserId(user.getId());

					return createUserResponse(user, profile, qualifications, "");
				}).toList()
				, pageable, options.getCount());

		return userResponsePage;
	}

	private boolean isUpdatable(String value) {
		return value != null && !value.isBlank();
	}

	/**
	 * ProfileのレスポンスDTOを作成
	 * @param profile PfoifleのEntity
	 * @return
	 */
	private ProfileResponse createProfileResponse(Profile profile) {
		//		if(profile == null) {
		//			return null;
		//		}
		//
		//		ProfileResponse profileResponse = new ProfileResponse();
		//
		//		profileResponse.setId(profile.getId());
		//		profileResponse.setUserId(profile.getUser().getId());
		//		profileResponse.setNickname(profile.getNickname());
		//		profileResponse.setBirthday(profile.getBirthday());
		//
		//		return profileResponse;

		if(profile == null) {
			return null;
		}

		ProfileResponse profileResponse = new ProfileResponse();

		profileResponse.setId(profile.getId());
		profileResponse.setUserId(profile.getUserId());
		profileResponse.setNickname(profile.getNickname());
		profileResponse.setBirthday(profile.getBirthday());

		return profileResponse;
	}

	/**
	 * QualificationのレスポンスDTOを作成
	 * @param qualifications
	 * @return
	 */
	private List<QualificationResponse> createQualificationResponses(List<Qualification> qualifications) {
		//		if(qualifications == null || qualifications.isEmpty()) {
		//			return List.of();
		//		}
		//
		//		List<QualificationResponse> qualificationResponses = qualifications.stream().map(
		//				qualification -> {
		//					QualificationResponse qualificationResponse = new QualificationResponse();
		//
		//					qualificationResponse.setQualificationId(qualification.
		//							getQualificationMaster().
		//							getId());
		//					qualificationResponse.setQualificationName(qualification.
		//							getQualificationMaster().
		//							getQualificationName());
		//					qualificationResponse.setAcquisitionDate(qualification.getAcquisitionDate());
		//
		//					return qualificationResponse;
		//				}).toList();
		//
		//		return qualificationResponses;

		if(qualifications == null || qualifications.isEmpty()) {
			return List.of();
		}

		List<QualificationResponse> qualificationResponses = qualifications.stream().map(
				qualification -> {
					QualificationResponse qualificationResponse = new QualificationResponse();

					qualificationResponse.setQualificationId(qualification.getQualificationId());
					qualificationResponse.setQualificationName(
							qualificationMasterDao.findById(qualification.getQualificationId())
							.orElse(null)
							.getQualificationName()
							);
					qualificationResponse.setAcquisitionDate(qualification.getAcquisitionDate());

					return qualificationResponse;
				}).toList();

		return qualificationResponses;
	}

	//	private UserResponse createUserResponse(User user, String message) {
	//		ProfileResponse profileResponse = createProfileResponse(user.getProfile());
	//
	//		List<Qualification> savedQualifications = user.getQualifications();
	//
	//		List<QualificationResponse> qualificationResponses = 
	//				createQualificationResponses(savedQualifications);
	//
	//		return new UserResponse(
	//				user.getId(), 
	//				user.getName(), 
	//				user.getEmail(), 
	//				profileResponse, 
	//				qualificationResponses, 
	//				message);
	//	}

	private UserResponse createUserResponse(
			User user, 
			Profile profile, 
			List<Qualification> qualifications,
			String message) {
		ProfileResponse profileResponse = createProfileResponse(profile);

		List<QualificationResponse> qualificationResponses = 
				createQualificationResponses(qualifications);

		return new UserResponse(
				user.getId(), 
				user.getName(), 
				user.getEmail(), 
				profileResponse, 
				qualificationResponses, 
				message);
	}

	private boolean hasProfileData(ProfileCreateRequest request) {
		return (request.getNickname() != null
				&& !request.getNickname().isBlank())
				|| request.getBirthday() != null;
	}

	public Integer findIdByEmail(String email) {
		User user = userDao.findByEmail(email).orElseThrow();

		return user.getId();
	}
	
	private String normalizeSortColumn(String sortColumn) {

	    Set<String> allowedColumns =
	            Set.of("id", "name");

	    if (!allowedColumns.contains(sortColumn)) {
	        return "id";
	    }

	    return sortColumn;
	}

	private String normalizeDirection(String direction) {

	    return "desc".equals(direction)
	            ? "desc"
	            : "asc";
	}

}
