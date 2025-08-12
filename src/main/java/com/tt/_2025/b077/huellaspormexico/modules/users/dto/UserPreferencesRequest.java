package com.tt._2025.b077.huellaspormexico.modules.users.dto;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferencesRequest {
    @NotEmpty(message = "Debe especificar al menos una")
    private List<CategoryPlacesCatalog>  categories;
}
