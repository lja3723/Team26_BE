package org.ktc2.cokaen.wouldyouin.member.api.dto.request.edit;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
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
public class MemberEditRequest extends MemberEditRequestBase {

    @Nullable private Area area;

    @Builder
    public MemberEditRequest(@Nullable String nickname, @Nullable String phoneNumber, @Nullable Long profileImageId, @Nullable Area area) {
        super(nickname, phoneNumber, profileImageId);
        this.area = area;
    }
}
