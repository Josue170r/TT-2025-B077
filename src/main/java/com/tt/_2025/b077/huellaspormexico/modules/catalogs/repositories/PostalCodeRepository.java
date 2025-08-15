package com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostalCodeRepository extends JpaRepository<PostalCodeCatalog, Long>, JpaSpecificationExecutor<PostalCodeCatalog> {
}
