package org.mql.controllers.dashboard;

import java.security.Principal;
import java.util.List;
import java.util.Vector;

import javax.validation.Valid;

import org.mql.dao.FormationRepository;
import org.mql.dao.MemberRepository;
import org.mql.dao.ModuleRepository;
import org.mql.models.Formation;
import org.mql.models.Member;
import org.mql.models.Module;
import org.mql.models.Streaming;
import org.mql.services.FormationService;
import org.mql.services.MemberService;
import org.mql.services.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
public class DashFormationController {

	@Autowired
	FormationRepository formationRepository;
	
	@Autowired
	ModuleRepository moduleRepository;
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	FormationService formationService;

	@Autowired
	StreamingService streamingService;
	
	@GetMapping("/")
	public String mainPage(Model model, Principal principal) {
		Member member = memberService.findByEmail(principal.getName());
		model.addAttribute("member", member);
		return "dashboard/index";
	}
	
	@GetMapping("/formation")
	public String getFormations(Model model,Principal principal) {
		Member member = memberService.findByEmail(principal.getName());
		List<Formation> formations = formationService.findByResponsable(member);
		model.addAttribute("formations", formations);
		return "dashboard/formations";
	}
	

	//CODE HAJAR : ajouter une formation via une formulaire**************************************************
	@GetMapping(value="formation/add")
	public String FormulaireFormation(Model model)
	{
		model.addAttribute("formation",new Formation());
		model.addAttribute("member",new Member());
		return "dashboard/addFormation" ;
	}
	
	//affichage de la formation ajoutee**********************************************************************
	@PostMapping(value="/saveFormation")
	public String save(Model model,@Valid Formation formation,Member member, BindingResult bindingResult,Principal principal)
	{
		if(bindingResult.hasErrors())
		{
			return "/dashboard/formation/add" ;
		}
		member=memberService.findByEmail(principal.getName());
		formation.setCreator(member);
		formationRepository.save(formation);
		return "redirect:/dashboard/formation/";
	}
	
	
	
	@GetMapping("/formation/{id}/add")
	public String ModuleForm(@PathVariable int id,Model model) {
		Formation formation = formationRepository.findById(id).get();
		List<Member> members = memberService.findTeachers();
		System.out.println(members);
		model.addAttribute("members", members);
		model.addAttribute("formation", formation);
		model.addAttribute("module",new Module());
		return "/dashboard/addmodule";
	}
	
	@PostMapping("addModule")
	public String addModule(@ModelAttribute Module module,@RequestParam("formation_id") int id,@RequestParam("member_id") int memberId,Model model) {
		Formation formation = formationRepository.findById(id).get();
		Member member = memberRepository.findById(memberId).get();
		module.setTeacher(member);
		formation.add(module);
		formationRepository.save(formation);
		return "redirect:/dashboard/formation/";
	}
	
	// Get the authetificated student followed Formations
	@GetMapping("/followedFormation")
	public String getFollowedFormations(Model model,Principal principal) {
		Member member = memberService.findByEmail(principal.getName());
		List<Formation> formations = formationService.findByFollower(member);
		model.addAttribute("formations", formations);
		return "dashboard/followedFormations";
	}

	// Get the authetificated student Streamed Courses
	@GetMapping("/StreamedCourses")
	public String getStreamedCourses(Model model,Principal principal) {
		Member member = memberService.findByEmail(principal.getName());
		List<Formation> formations = formationService.findByFollower(member);
		List<Module> modules = new Vector<>();
		List<Streaming> streamings = new Vector<>();
		for (Formation formation:formations ) {
			modules.addAll(formation.getModules());
		}
		for (Module module: modules ) {
			streamings.addAll(module.getStreams());
		}
		model.addAttribute("streams", streamings);
		return "dashboard/StreamedCourses";
	}

//	@GetMapping("/StreamedCourses")
//	public @ResponseBody
//	int getStreamedCourses(Model model, Principal principal) {
//		Member member = memberService.findByEmail(principal.getName());
//		List<Formation> formations = formationService.findByFollower(member);
//		List<Module> modules = new Vector<>();
//		List<Streaming> streamings = new Vector<>();
////		Get Followed Modules
//		for (Formation formation : formations) {
//			modules.addAll(formation.getModules());
//		}
////		Get Streams From Modules
//		for (Module module : modules) {
////			if (!streamingService.findByModule(module).isEmpty())
//				streamings.addAll(module.getStreams());
//		}
//		return streamings.size();
//	}
}
