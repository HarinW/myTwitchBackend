package com.laioffer.twitch.db.entity;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("favorite_records")
public record FavoriteRecordEntity(
    @Id Long id,
    Long userId,
    Long itemId,
    Instant createdAt
) {
}
