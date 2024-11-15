package org.ktc2.cokaen.wouldyouin.member.application;

import org.ktc2.cokaen.wouldyouin.auth.MemberIdentifier;
import org.ktc2.cokaen.wouldyouin.member.api.dto.MemberResponse;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.springframework.transaction.annotation.Transactional;

public interface MemberServiceCommonBehavior {

    @Transactional
    void deleteByMemberIdentifier(MemberIdentifier identifier);

    @Transactional(readOnly = true)
    MemberResponse getMemberResponseById(Long id);

    MemberType getTargetMemberType();
}
