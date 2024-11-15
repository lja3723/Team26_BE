package org.ktc2.cokaen.wouldyouin.member.api.dto.request.edit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HostEditRequest extends MemberEditRequestBase {

    private String intro;
    private List<String> hashtags;

    @Builder
    public HostEditRequest(String nickname, String phoneNumber, Long profileImageId, String intro, List<String> hashtags) {
        super(nickname, phoneNumber, profileImageId);
        this.intro = intro;
        this.hashtags = hashtags;
    }
}
