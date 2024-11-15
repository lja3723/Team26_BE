package org.ktc2.cokaen.wouldyouin.member.api.dto.request.edit;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class MemberEditRequestBase {

    private String nickname;
    private String phoneNumber;
    private Long profileImageId;
}
