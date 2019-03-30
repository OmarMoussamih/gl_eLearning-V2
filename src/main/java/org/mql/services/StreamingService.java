package org.mql.services;

import org.mql.models.Member;
import org.mql.models.Module;
import org.mql.models.Streaming;

import java.util.List;

public interface StreamingService {
	Streaming findById(int id);
	boolean isAllowed(Streaming streaming,Member member);
	Streaming save(Streaming s);
}
