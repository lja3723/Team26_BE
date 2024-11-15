package org.ktc2.cokaen.wouldyouin.member.api.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin.image.persist.MemberImage;
import org.ktc2.cokaen.wouldyouin.member.persist.Host;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HostCreateRequest extends MemberCreateRequestBase {

    protected String phone;
    protected String password;
    protected Long profileImageId;

    public HostCreateRequest(String nickname, String email, String phone, String password, Long profileImageId) {
        super(nickname, email);
        this.phone = phone;
        this.password = password;
        this.profileImageId = profileImageId;
    }

    public Host toEntity(String hashedPassword, MemberImage profileImage, String thumbnailImageUrl) {

        return Host.builder()
            .nickname(this.nickname)
            .profileImage(profileImage)
            .email(this.email)
            .phone(this.phone)
            .hashedPassword(hashedPassword)
            .profileImageThumbnailUrl(thumbnailImageUrl)
            .build();
    }
}
