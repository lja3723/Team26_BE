package org.ktc2.cokaen.wouldyouin.member.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin._common.vo.Area;
import org.ktc2.cokaen.wouldyouin.member.persist.AccountType;
import org.ktc2.cokaen.wouldyouin.member.persist.BaseMember;
import org.ktc2.cokaen.wouldyouin.member.persist.Curator;
import org.ktc2.cokaen.wouldyouin.member.persist.Gender;
import org.ktc2.cokaen.wouldyouin.member.persist.Host;
import org.ktc2.cokaen.wouldyouin.member.persist.Member;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;

@Getter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Builder
public class MemberResponse {

    private Long memberId;
    private AccountType accountType;
    private MemberType memberType;
    private String email;
    private String nickname;
    private String phoneNumber;
    private Long profileImageId;
    private String profileUrl;
    private String profileThumbnailUrl;

    private Area area;
    private Gender gender;

    private String intro;
    private Integer likes;

    private List<String> hashtag;

    private static MemberResponseBuilder responseBase(BaseMember baseMember, Long profileImageId, String profileUrl) {
        return MemberResponse.builder()
            .memberId(baseMember.getId())
            .accountType(baseMember.getAccountType())
            .memberType(baseMember.getMemberType())
            .email(baseMember.getEmail())
            .nickname(baseMember.getNickname())
            .phoneNumber(baseMember.getPhone())
            .profileImageId(profileImageId)
            .profileUrl(profileUrl)
            .profileThumbnailUrl(baseMember.getProfileImageThumbnailUrl());
    }

    public static MemberResponse from(final Member member, Long profileImageId,  String profileUrl) {
        return responseBase(member, profileImageId, profileUrl)
            .area(member.getArea())
            .gender(member.getGender())
            .build();
    }

    public static MemberResponse from(final Host host, Long profileImageId, String profileUrl) {
        return responseBase(host, profileImageId, profileUrl)
            .intro(host.getIntro())
            .likes(host.getLikes())
            .hashtag(host.getHashtags())
            .build();
    }

    public static MemberResponse from(final Curator curator, Long profileImageId, String profileUrl) {
        return responseBase(curator, profileImageId, profileUrl)
            .area(curator.getArea())
            .gender(curator.getGender())
            .intro(curator.getIntro())
            .likes(curator.getLikes())
            .hashtag(curator.getHashtags())
            .build();
    }
}
