package org.ktc2.cokaen.wouldyouin.member.api.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class MemberCreateRequestBase {

    protected String nickname;
    protected String email;
}
