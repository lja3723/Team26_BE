package org.ktc2.cokaen.wouldyouin.member.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ktc2.cokaen.wouldyouin._common.exception.EntityNotFoundException;
import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.image.application.MemberImageService;
import org.ktc2.cokaen.wouldyouin.image.persist.MemberImage;
import org.ktc2.cokaen.wouldyouin.member.api.dto.MemberResponse;
import org.ktc2.cokaen.wouldyouin.member.api.dto.request.MemberAdditionalInfoRequest;
import org.ktc2.cokaen.wouldyouin.member.api.dto.request.create.MemberCreateRequest;
import org.ktc2.cokaen.wouldyouin.member.api.dto.request.edit.MemberEditRequest;
import org.ktc2.cokaen.wouldyouin.member.exception.AdditionalInfoIllegalAccessException;
import org.ktc2.cokaen.wouldyouin.member.persist.Member;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberRepository;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberServiceCommonBehavior {

    private final MemberRepository memberRepository;
    private final MemberImageService memberImageService;

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        MemberImage profileImage = memberImageService.convert(request.getProfileImageUrl());
        String thumbnailImageUrl = memberImageService.createThumbnail(profileImage.getName());
        Member member = memberRepository.save(request.toEntity(profileImage, thumbnailImageUrl));
        memberImageService.setBaseMember(profileImage, member);
        return MemberResponse.from(member, member.getProfileImage().getId(), memberImageService.getImageUrl(profileImage));
    }

    @Transactional
    public MemberResponse updateMember(MemberIdentifier identifier, MemberEditRequest request) {
        Member member = getByIdOrThrow(identifier.id());
        MemberImage profileImage = member.getProfileImage();
        String profileImageThumbnailUrl = member.getProfileImageThumbnailUrl();

        if (Optional.ofNullable(request.getProfileImageId()).isPresent() && !request.getProfileImageId().equals(member.getProfileImage().getId())) {
            MemberImage newProfileImage = memberImageService.getById(request.getProfileImageId());
            Optional.ofNullable(newProfileImage.getBaseMember()).ifPresent(x -> {
                memberImageService.validateMemberId(identifier, newProfileImage);
            });

            MemberImage toDelete = profileImage;
            member.setProfileImage(null);
            memberImageService.deleteImage(identifier, toDelete.getId());
            profileImage = memberImageService.getById(request.getProfileImageId());
            profileImageThumbnailUrl = memberImageService.createThumbnail(profileImage.getName());
            memberImageService.setBaseMember(profileImage, member);
        }

        member.updateFrom(request, profileImage, profileImageThumbnailUrl);
        return MemberResponse.from(member, member.getProfileImage().getId(), memberImageService.getImageUrl(member.getProfileImage()));
    }

    @Transactional
    public MemberResponse updateWelcomeMember(Long welcomeMemberId, MemberAdditionalInfoRequest additionalInfoRequest) {
        Member member = getByIdOrThrow(welcomeMemberId);
        if (member.getMemberType() != MemberType.welcome) {
            throw new AdditionalInfoIllegalAccessException("최초 소셜로그인 후 추가정보를 기입하지 않은 사용자만 접근 가능합니다.");
        }
        member.updateFrom(additionalInfoRequest);
        return MemberResponse.from(member, member.getProfileImage().getId(), memberImageService.getImageUrl(member.getProfileImage()));
    }

    @Override
    @Transactional
    public void deleteByMemberIdentifier(MemberIdentifier identifier) {
        memberImageService.deleteImage(identifier, getByIdOrThrow(identifier.id()).getProfileImage().getId());
        memberRepository.delete(getByIdOrThrow(identifier.id()));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseById(Long id) {
        Member member = getByIdOrThrow(id);
        return MemberResponse.from(member, member.getProfileImage().getId(), memberImageService.getImageUrl(member.getProfileImage()));
    }

    @Transactional(readOnly = true)
    public Member getByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Optional<MemberIdentifier> getMemberIdentifierBySocialId(String socialId) {
        return memberRepository.findBySocialId(socialId).map(m -> new MemberIdentifier(m.getId(), m.getMemberType()));
    }

    @Override
    public MemberType getTargetMemberType() {
        return MemberType.normal;
    }
}