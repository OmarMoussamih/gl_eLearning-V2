package org.mql.dao;

import org.mql.models.Module;
import org.mql.models.Streaming;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StreamingRepository extends JpaRepository<Streaming, Integer> {

	Streaming findFirstByOrderByIdDesc();

}
