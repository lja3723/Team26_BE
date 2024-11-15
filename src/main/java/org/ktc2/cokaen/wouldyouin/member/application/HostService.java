package org.ktc2.cokaen.wouldyouin.member.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ktc2.cokaen.wouldyouin._common.exception.EntityNotFoundException;
import org.ktc2.cokaen.wouldyouin._common.exception.UnauthorizedException;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.auth.api.dto.LocalLoginRequest;
import org.ktc2.cokaen.wouldyouin.image.application.MemberImageService;
import org.ktc2.cokaen.wouldyouin.image.persist.MemberImage;
import org.ktc2.cokaen.wouldyouin.member.api.dto.MemberResponse;
import org.ktc2.cokaen.wouldyouin.member.api.dto.request.create.HostCreateRequest;
import org.ktc2.cokaen.wouldyouin.member.api.dto.request.edit.HostEditRequest;
import org.ktc2.cokaen.wouldyouin.member.exception.LoginFailedException;
import org.ktc2.cokaen.wouldyouin.member.persist.Host;
import org.ktc2.cokaen.wouldyouin.member.persist.HostRepository;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostService implements MemberServiceCommonBehavior, LikeableMemberService<Host> {

    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberImageService memberImageService;

    @Transactional
    public MemberResponse createHost(HostCreateRequest request) {
        MemberImage profileImage = memberImageService.getById(request.getProfileImageId());
        Optional.ofNullable(profileImage.getBaseMember()).ifPresent( x -> {
            throw new UnauthorizedException("해당 프로필 이미지에 접근할 권한이 없습니다.");
        });
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String profileImageThumbnailUrl = memberImageService.createThumbnail(profileImage.getName());
        Host host = hostRepository.save(request.toEntity(hashedPassword, profileImage, profileImageThumbnailUrl));
        memberImageService.setBaseMember(profileImage, host);
        return MemberResponse.from(host, host.getProfileImage().getId(), memberImageService.getImageUrl(profileImage));
    }

    @Transactional
    public MemberResponse updateHost(MemberIdentifier identifier, HostEditRequest request) {
        Host host = getByIdOrThrow(identifier.id());
        MemberImage profileImage = host.getProfileImage();
        String profileImageThumbnailUrl = host.getProfileImageThumbnailUrl();

        if (Optional.ofNullable(request.getProfileImageId()).isPresent() && !request.getProfileImageId().equals(host.getProfileImage().getId())) {
            MemberImage newProfileImage = memberImageService.getById(request.getProfileImageId());
            Optional.ofNullable(newProfileImage.getBaseMember()).ifPresent( x ->
                memberImageService.validateMemberId(identifier, newProfileImage)
            );

            MemberImage toDelete = profileImage;
            host.setProfileImage(null);
            memberImageService.deleteImage(identifier, toDelete.getId());
            profileImage = memberImageService.getById(request.getProfileImageId());
            profileImageThumbnailUrl = memberImageService.createThumbnail(profileImage.getName());
            memberImageService.setBaseMember(profileImage, host);
        }
            
        host.updateFrom(request, profileImage, profileImageThumbnailUrl);
        return MemberResponse.from(host, host.getProfileImage().getId(), memberImageService.getImageUrl(host.getProfileImage()));
    }

    @Override
    @Transactional
    public void deleteByMemberIdentifier(MemberIdentifier identifier) {
        memberImageService.deleteImage(identifier, getByIdOrThrow(identifier.id()).getProfileImage().getId());
        hostRepository.delete(getByIdOrThrow(identifier.id()));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseById(Long id) {
        Host host = getByIdOrThrow(id);
        return MemberResponse.from(host, host.getProfileImage().getId(), memberImageService.getImageUrl(host.getProfileImage()));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseBy(LocalLoginRequest loginRequest) {
        Host host = hostRepository.findByEmail(loginRequest.email())
            .orElseThrow(() -> new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(loginRequest.password(), host.getHashedPassword())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return MemberResponse.from(host, host.getProfileImage().getId(), memberImageService.getImageUrl(host.getProfileImage()));
    }

    @Transactional(readOnly = true)
    public Host getByIdOrThrow(Long id) {
        return hostRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("사용자가 주최자가 아니거나 없습니다.")
        );
    }

    @Override
    public MemberType getTargetMemberType() {
        return MemberType.host;
    }

    @Override
    public LikeableMemberService<Host> getLikeableMemberService() {
        return this;
    }
}