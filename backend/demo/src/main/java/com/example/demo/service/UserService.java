package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProfileCreateRequest;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.QualificationRequest;
import com.example.demo.dto.QualificationResponse;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.Profile;
import com.example.demo.entity.Qualification;
import com.example.demo.entity.User;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidPageException;
import com.example.demo.exception.InvalidQualificationException;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.QualificationMasterRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ProfileRepository profileRepository;
	private final QualificationMasterRepository qualificationMasterRepository;

	// 問題１
	public UserResponse create(UserCreateRequest userCreateRequest) {
		if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
			throw new EmailAlreadyExistsException();
		}

		User user = new User(
				userCreateRequest.getName(), 
				userCreateRequest.getEmail(), 
				passwordEncoder.encode(userCreateRequest.getPassword()));

		if (userCreateRequest.getProfile() != null && hasProfileData(userCreateRequest.getProfile())) {
			// ProfileのリクエストDTOをEntityに保持
			ProfileCreateRequest profileCreateRequest = userCreateRequest.getProfile();
			Profile profile = new Profile();

			profile.setNickname(profileCreateRequest.getNickname());
			profile.setBirthday(profileCreateRequest.getBirthday());

			profile.setUser(user);
			user.setProfile(profile);
		}

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
			List<Qualification> qualifications = qualificationRequests.stream().map(
					qualificationRequest -> {
						Qualification qualification =  new Qualification();

						qualification.setQualificationMaster(
								qualificationMasterRepository.findById(qualificationRequest.getQualificationId())
								.orElseThrow(() -> new InvalidQualificationException("対象の資格で登録または変更できません")));
						qualification.setAcquisitionDate(qualificationRequest.getAcquisitionDate());
						qualification.setUser(user);

						return qualification;
					}).toList();

			user.setQualifications(qualifications);
		}

		User savedUser = userRepository.save(user);

		return createUserResponse(savedUser,"登録完了");
	}

	// 問題２
	public UserResponse update(UserUpdateRequest userUpdateRequest, Integer id) {
		User user = getById(id);

		if (isUpdatable(userUpdateRequest.getEmail())) {
			if (userRepository.existsByEmailAndIdNot(userUpdateRequest.getEmail(), id)) {
				throw new EmailAlreadyExistsException();
			}
			user.setEmail(userUpdateRequest.getEmail());
		}

		if (isUpdatable(userUpdateRequest.getName())) {
			user.setName(userUpdateRequest.getName());
		}

		if (isUpdatable(userUpdateRequest.getPassword())) {
			user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));;
		}

		userRepository.save(user);

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), "更新完了");
	}

	// 問題３
	public UserResponse delete(Integer id) {
		User user = getById(id);

		userRepository.deleteById(user.getId());

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), "削除完了");
	}

	// 問題４
	public UserResponse findById(Integer id) {
		User findedUser = getById(id);

		return createUserResponse(findedUser, "取得完了");
	}

	public User getById(Integer id) {
		User user = userRepository.findById(id).orElseThrow(() -> 
		new RuntimeException("対象のユーザーが見つかりません"));

		return user;
	}

	// 問題５
	public Page<UserResponse> findAll(Pageable pageable) {
		if (pageable.getPageSize() > 20) {
			throw new InvalidPageException("size", "20以下を指定してください");
		}

		Page<User> users = userRepository.findAll(pageable);

		Page<UserResponse> userResponsePage = users.map(user -> {
			return createUserResponse(user, "");
		});
		return userResponsePage;
	}

	// 問題６
	public List<UserResponse> findByName(String name) {
		List<User> users = userRepository.findByNameContaining(name);

		List<UserResponse> userResponses = users.stream().map(
				user -> {return createUserResponse(user, "");})
				.toList();

		return userResponses;
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
		if(profile == null) {
			return null;
		}

		ProfileResponse profileResponse = new ProfileResponse();

		profileResponse.setId(profile.getId());
		profileResponse.setUserId(profile.getUser().getId());
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
		if(qualifications == null || qualifications.isEmpty()) {
			return List.of();
		}

		List<QualificationResponse> qualificationResponses = qualifications.stream().map(
				qualification -> {
					QualificationResponse qualificationResponse = new QualificationResponse();

					qualificationResponse.setQualificationId(qualification.
							getQualificationMaster().
							getId());
					qualificationResponse.setQualificationName(qualification.
							getQualificationMaster().
							getQualificationName());
					qualificationResponse.setAcquisitionDate(qualification.getAcquisitionDate());

					return qualificationResponse;
				}).toList();

		return qualificationResponses;
	}

	private UserResponse createUserResponse(User user, String message) {
		ProfileResponse profileResponse = createProfileResponse(user.getProfile());

		List<Qualification> savedQualifications = user.getQualifications();

		List<QualificationResponse> qualificationResponses = 
				createQualificationResponses(savedQualifications);

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

}
