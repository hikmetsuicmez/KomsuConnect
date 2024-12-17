package com.hikmetsuicmez.komsu_connect.response;

import com.hikmetsuicmez.komsu_connect.enums.FavoriteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private Long favoriteTargetId;
    private FavoriteType favoriteType;
    private LocalDateTime createdAt;


}
