package org.mql.services;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import org.mql.dao.StreamingRepository;
import org.mql.models.Member;
import org.mql.models.Module;
import org.mql.models.Role;
import org.mql.models.Streaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamingServiceImpl implements StreamingService{
	
	@Autowired
	StreamingRepository streamingRepository;
	
	@Autowired
	RoleService roleService;
	
	@Override
	public Streaming findById(int id) {
		Optional<Streaming> streaming = streamingRepository.findById(id); 
		if(streaming.isPresent()) {
			return streaming.get();
		}
		return null;
	}
	
	@Override
	public boolean isAllowed(Streaming streaming, Member member) {
		if(streaming.getModule().getFormation().getMembers().contains(member) || streaming.getModule().getTeacher().equals(member)) 
			return true;
		Role admin = roleService.findRoleByName(RoleService.ADMIN);
		Role responsable = roleService.findRoleByName(RoleService.RESPONSABLE);
		if(member.getRoles().contains(admin) || member.getRoles().contains(responsable))
			return true;
		return false;
	}

	@Override
	public Streaming save(Streaming s) {
		return streamingRepository.save(s);
	}

}
