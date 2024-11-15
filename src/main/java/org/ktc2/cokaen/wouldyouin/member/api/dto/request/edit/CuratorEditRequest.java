package org.ktc2.cokaen.wouldyouin.member.api.dto.request.edit;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin._common.vo.Area;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CuratorEditRequest extends MemberEditRequest {

    @Nullable private String intro;
    @Nullable private List<String> hashtags;

    @Builder(builderMethodName = "curatorEditRequestBuilder")
    public CuratorEditRequest(String nickname, String phoneNumber, Long profileImageId, Area area, @Nullable String intro, @Nullable List<String> hashtags) {
        super(nickname, phoneNumber, profileImageId, area);
        this.intro = intro;
        this.hashtags = hashtags;
    }
}
