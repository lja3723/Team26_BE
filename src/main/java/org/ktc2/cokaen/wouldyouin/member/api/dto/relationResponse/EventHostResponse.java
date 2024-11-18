package org.ktc2.cokaen.wouldyouin.member.api.dto.relationResponse;

import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin.image.persist.Image;
import org.ktc2.cokaen.wouldyouin.member.api.dto.MemberResponse;
import org.ktc2.cokaen.wouldyouin.member.persist.BaseMember;
import org.ktc2.cokaen.wouldyouin.member.persist.Host;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class EventHostResponse {

    private Long hostId;
    private String nickname;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String intro;
    private Integer likes;
    private List<String> hashtags;

    public static EventHostResponse from(MemberResponse host, String hostProfileImageUrl) {
        return EventHostResponse.builder()
            .hostId(host.getMemberId())
            .nickname(host.getNickname())
            .email(host.getEmail())
            .phone(host.getPhoneNumber())
            .profileImageUrl(hostProfileImageUrl
            )
            .intro(host.getIntro())
            .likes(host.getLikes())
            .hashtags(host.getHashtag())
            .build();
    }
}