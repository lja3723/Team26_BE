package org.ktc2.cokaen.wouldyouin.advertisement.api.dto;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.ktc2.cokaen.wouldyouin.advertisement.persist.Advertisement;
import org.ktc2.cokaen.wouldyouin.image.persist.Image;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class AdvertisementResponse {

    private Long id;
    private String title;
    private String imageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static AdvertisementResponse from(Advertisement advertisement, String imageUrl) {
        return AdvertisementResponse.builder()
            .id(advertisement.getId())
            .title(advertisement.getTitle())
            .imageUrl(imageUrl)
            .startTime(advertisement.getStartTime())
            .endTime(advertisement.getEndTime())
            .build();
    }
}