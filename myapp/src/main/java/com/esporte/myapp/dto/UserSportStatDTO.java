// com.esporte.myapp.dto.UserSportStatDTO
package com.esporte.myapp.dto;

public record UserSportStatDTO(
        String sport,
        Integer totalSkill,
        Integer totalReceivedEvaluations,
        Double averageSkill // totalSkill / totalReceivedEvaluations
) {}
