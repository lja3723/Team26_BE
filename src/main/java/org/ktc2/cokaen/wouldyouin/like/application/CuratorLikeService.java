package org.ktc2.cokaen.wouldyouin.like.application;


import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.ktc2.cokaen.wouldyouin._common.api.EntityGettable;
import org.ktc2.cokaen.wouldyouin.like.persist.CuratorLike;
import org.ktc2.cokaen.wouldyouin.like.persist.CuratorLikeRepository;
import org.ktc2.cokaen.wouldyouin.like.persist.LikeRepository;
import org.ktc2.cokaen.wouldyouin.member.persist.Curator;
import org.ktc2.cokaen.wouldyouin.member.persist.LikeableMember;
import org.ktc2.cokaen.wouldyouin.member.persist.Member;
import org.ktc2.cokaen.wouldyouin.member.persist.MemberType;
import org.springframework.stereotype.Service;

@Service
public class CuratorLikeService extends LikeService<CuratorLike> {

    private final CuratorLikeRepository likeRepository;

    public CuratorLikeService(
        Map<String, EntityGettable<? extends LikeableMember>> likeableMemberGetter,
        EntityGettable<Member> memberGetter,
        CuratorLikeRepository likeRepository) {
        super(likeableMemberGetter, memberGetter);
        this.likeRepository = likeRepository;
    }

    @Override
    protected LikeRepository<CuratorLike> getLikeRepository() {
        return likeRepository;
    }

    @Override
    protected Function<CuratorLike, LikeResponse> getLikeToResponseMapper() {
        return like -> LikeResponse.builder()
            .memberId(like.getLikeableMember().getId())
            .nickname(like.getLikeableMember().getNickname())
            .intro(like.getLikeableMember().getIntro())
            .hashtags(List.of()) // TODO: 큐레이터에 해시태그 추가시 변경 필요
            .profileImageUrl(like.getLikeableMember().getProfileImageUrl())
            .build();
    }

    @Override
    protected CuratorLike toEntity(Member member, LikeableMember targetLikableMember) {
        return CuratorLike.builder()
            .targetMember((Curator)targetLikableMember)
            .member(member)
            .build();
    }

    @Override
    public MemberType getTargetLikeableMemberType() {
        return MemberType.curator;
    }
}
